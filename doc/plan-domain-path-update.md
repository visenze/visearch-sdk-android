# Plan: Domain and API Path Update (Backwards Compatible)

## Context

The existing `ProductSearch` client talks to `https://multimodal.search.rezolve.com/v1/` (base URL includes `/v1/`) with paths prefixed by `product/`.
Two new cloud-specific domains are being introduced with a different path structure:

| Domain | Cloud |
|--------|-------|
| `https://multisearch-aw.rezolve.com` | AWS |
| `https://multisearch-az.rezolve.com` | Azure |

New path mapping (relative to the new base URL):

| Old path (under `/v1/`) | New path (under `/`) |
|-------------------------|----------------------|
| `product/search_by_image` | `v1/visearch/search_by_image` |
| `product/recommendations/{id}` | `v1/visearch/recommendations/{id}` |
| `product/multisearch` | `v1/search` |
| `product/multisearch/complementary` | `v1/search/complementary` |
| `product/multisearch/outfit-recommendations` | `v1/search/outfit-recommendations` |
| `product/multisearch/autocomplete` | `v1/autocomplete` |

**Constraint:** Existing callers using the old domain must continue to work unchanged.

---

## Problem: Paths are baked into the Retrofit interface

Retrofit resolves a URL as `baseUrl + @GET/@POST path`. Because the old and new domains use different path structures, a single Retrofit interface cannot serve both correctly.

---

## Design

### 1. Normalise `ProductSearch.SEARCH_URL` — strip `/v1/`, move it into `APIProductService`

Currently `/v1/` lives in the base URL constant, which means it is invisible in the interface and inconsistent with `APIProductServiceV2` (where `v1/` is part of the path).

Change `ProductSearch.SEARCH_URL` to end without `/v1/`:

```java
// ProductSearch.java — before
private static final String SEARCH_URL = "https://multimodal.search.rezolve.com/v1/";

// ProductSearch.java — after
private static final String SEARCH_URL = "https://multimodal.search.rezolve.com/";
```

Update all paths in `APIProductService` to include the `v1/` prefix explicitly:

```java
// APIProductService.java — after
@GET("v1/product/recommendations/{product_id}")
@POST("v1/product/search_by_image")
@POST("v1/product/multisearch")
@POST("v1/product/multisearch/complementary")
@POST("v1/product/multisearch/outfit-recommendations")
@POST("v1/product/multisearch/autocomplete")
```

Both `APIProductService` (legacy) and `APIProductServiceV2` (new) now follow the same convention: base URL is the bare domain with a trailing slash, paths start with `v1/`.

---

### 2. Add a `Cloud` enum — `ProductSearch.Cloud`

```java
// inside ProductSearch.java
public enum Cloud {
    AWS("https://multisearch-aw.rezolve.com/"),
    AZURE("https://multisearch-az.rezolve.com/");

    public final String endPoint;
    Cloud(String endPoint) { this.endPoint = endPoint; }
}
```

Callers using a named cloud get the correct base URL automatically.
Callers who supply a custom `URL` via `setApiEndPoint()` are unaffected.

---

### 3. New Retrofit interface — `APIProductServiceV2`

Create `network/APIProductServiceV2.java` with the new path structure.
The interface is otherwise structurally identical to `APIProductService`.

```java
public interface APIProductServiceV2 {

    @Retry @GET("v1/visearch/recommendations/{product_id}")
    Call<ProductResponse> searchById(...);

    @Retry @POST("v1/visearch/search_by_image")
    Call<ProductResponse> searchByImage(...);

    @Retry @Multipart @POST("v1/visearch/search_by_image")
    Call<ProductResponse> searchByImage(@Part MultipartBody.Part image, ...);

    @Retry @POST("v1/search")
    Call<ProductResponse> multisearch(...);

    @Retry @Multipart @POST("v1/search")
    Call<ProductResponse> multisearch(@Part MultipartBody.Part image, ...);

    @Retry @POST("v1/autocomplete")
    Call<AutoCompleteResponse> multisearchAutocomplete(...);

    @Retry @Multipart @POST("v1/autocomplete")
    Call<AutoCompleteResponse> multisearchAutocomplete(@Part MultipartBody.Part image, ...);

    @Retry @POST("v1/search/complementary")
    Call<ProductResponse> multisearchComplementary(...);

    @Retry @Multipart @POST("v1/search/complementary")
    Call<ProductResponse> multisearchComplementary(@Part MultipartBody.Part image, ...);

    @Retry @POST("v1/search/outfit-recommendations")
    Call<ProductResponse> multisearchOutfitRec(...);

    @Retry @Multipart @POST("v1/search/outfit-recommendations")
    Call<ProductResponse> multisearchOutfitRec(@Part MultipartBody.Part image, ...);
}
```

---

### 4. Update `ProductSearchService` to select the right interface

Add a helper that detects whether a given endpoint string is one of the new domains, then instantiate `APIProductServiceV2` for those and the existing `APIProductService` for everything else.

```java
// ProductSearchService.java
private static final Set<String> NEW_DOMAIN_PREFIXES = new HashSet<>(Arrays.asList(
    "https://multisearch-aw.rezolve.com",
    "https://multisearch-az.rezolve.com"
));

private final APIProductService     legacyApi;  // null when using new domain
private final APIProductServiceV2   newApi;     // null when using legacy domain

public ProductSearchService(String endPoint, String appKey, int placementId, String userAgent) {
    Retrofit retrofit = Http.getRetrofitInstance(endPoint);
    boolean isNewDomain = isNewDomain(endPoint);

    this.legacyApi = isNewDomain ? null : retrofit.create(APIProductService.class);
    this.newApi    = isNewDomain ? retrofit.create(APIProductServiceV2.class) : null;
    ...
}

private static boolean isNewDomain(String endPoint) {
    for (String prefix : NEW_DOMAIN_PREFIXES) {
        if (endPoint.startsWith(prefix)) return true;
    }
    return false;
}
```

All existing internal methods (`searchById`, `searchByImage`, `getProductResponseCall`, etc.) dispatch to `legacyApi` or `newApi` based on which is non-null. No logic change, just a routing switch.

---

### 5. Expose `Cloud` in `ProductSearch.Builder`

```java
// ProductSearch.Builder
public Builder setCloud(Cloud cloud) {
    this.searchApiEndPoint = cloud.endPoint;
    return this;
}
```

The existing `setApiEndPoint(URL)` method remains intact for custom endpoints.

---

## Migration path for callers

**Existing code — no change required:**
```java
// continues to work exactly as before
new ProductSearch.Builder(appKey, placementId).build(context);
```

**Opting into a new domain:**
```java
new ProductSearch.Builder(appKey, placementId)
    .setCloud(ProductSearch.Cloud.AWS)   // or Cloud.AZURE
    .build(context);
```

**Custom URL (unchanged):**
```java
new ProductSearch.Builder(appKey, placementId)
    .setApiEndPoint(new URL("https://custom.endpoint.com/"))
    .build(context);
```

---

### 6. Bump version to 2.6.0

In `visearch-android/build.gradle`:

```groovy
// before
def versionMajor = 2
def versionMinor = 5
def versionPatch = 2

// after
def versionMajor = 2
def versionMinor = 6
def versionPatch = 0
```

---

### 7. Update documentation

Update `AGENTS.md`:
- Change default endpoint note under `ProductSearch` from `https://multimodal.search.rezolve.com/v1/` to `https://multimodal.search.rezolve.com/`
- Add `Cloud` enum and `Builder.setCloud()` to the `ProductSearch` description
- Add `network/APIProductServiceV2.java` to the key files table
- Update the current version to `2.6.0`
- Remove the "watch for state leak" note on `Http.java` (no longer applies after the path normalisation makes it consistent)

Update `doc/ProductSearch.md`:
- Change the version badge to `2.6.0`
- Update the initialization section to document `setCloud()` alongside the existing `setApiEndPoint()` example:

  ```java
  // AWS cloud
  ProductSearch productSearch = new ProductSearch
      .Builder(appKey, placementId)
      .setCloud(ProductSearch.Cloud.AWS)
      .build(context);

  // Azure cloud
  ProductSearch productSearch = new ProductSearch
      .Builder(appKey, placementId)
      .setCloud(ProductSearch.Cloud.AZURE)
      .build(context);
  ```

- Update the API path references in sections 3.1–3.6 to show both old paths (`/v1/product/...`) and new paths (`/v1/visearch/...` or `/v1/search/...`) so callers understand which applies to which domain.

---

## Files to change

| File | Change |
|------|--------|
| `ProductSearch.java` | Strip `/v1/` from `SEARCH_URL`; add `Cloud` enum; add `Builder.setCloud()` |
| `network/APIProductService.java` | Add `v1/` prefix to all path annotations |
| `network/APIProductServiceV2.java` | **New file** — new-domain path interface |
| `network/ProductSearchService.java` | Accept both interfaces; route by domain |
| `ProductSearchTest.java` | Add tests covering new-domain routing and path selection |
| `visearch-android/build.gradle` | Bump version to `2.6.0` |
| `AGENTS.md` | Reflect new endpoint, `Cloud` enum, version, new interface file |
| `doc/ProductSearch.md` | Update version, add `setCloud()` usage, update API path references |

No changes to `ViSearch.java`, `APIService.java`, `SearchService.java`, `Http.java`, or any model/params classes.

---

## Test plan

1. **Existing tests pass unchanged** — old domain routing still works.
2. **New: `testNewDomainRouting`** — construct a `ProductSearchService` with `multisearch-aw.rezolve.com`, assert `newApi` is used and `legacyApi` is null (or vice versa for legacy endpoint).
3. **New: path assertions** — mock the Retrofit call layer to verify the URL each method resolves to for both old and new domains (or use OkHttp `MockWebServer` to capture the actual request path).
