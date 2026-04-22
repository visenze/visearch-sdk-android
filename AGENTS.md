## Overview

This is the **Rezolve Android SDK** (formerly ViSearch Android SDK) — a Java library for Android that wraps Rezolve's visual search APIs. It is distributed via JitPack as `com.github.visenze:visearch-sdk-android`.

Current version: **2.6.0** | Min SDK: 19 | Target SDK: 32

## Build Commands

```bash
# Build the library
./gradlew :visearch-android:assembleRelease

# Run all unit tests
./gradlew :visearch-android:test

# Run a single test class
./gradlew :visearch-android:test --tests "com.visenze.visearch.android.ProductSearchTest"

# Run a single test method
./gradlew :visearch-android:test --tests "com.visenze.visearch.android.ProductSearchTest.testBaseSearchParams_parsing"

# Build debug variant
./gradlew :visearch-android:assembleDebug
```

Tests use **Robolectric** (no emulator needed) and **Mockito**. They run on the JVM with `@Config(sdk = Build.VERSION_CODES.LOLLIPOP)`.

## Architecture

The SDK has two independent API clients:

### 1. `ViSearch` — Legacy visual search API
- Entry point: `ViSearch.Builder` → `ViSearch`
- Default endpoint: `https://visearch.visenze.com`
- Auth: access key + optional secret key (HMAC via `AuthGenerator`)
- Operations: `idSearch`, `colorSearch`, `uploadSearch`, `discoverSearch`, `recommendation`
- Results delivered via `ViSearch.ResultListener` (callback interface)
- Internal flow: `ViSearch` → `SearchOperations` (interface) → `SearchOperationsImp` → `SearchService` → `APIService` (Retrofit)

### 2. `ProductSearch` — Product Search / Multisearch API
- Entry point: `ProductSearch.Builder` → `ProductSearch`
- Default endpoint: `https://multimodal.search.rezolve.com/` (legacy); new cloud domains via `Builder.setCloud(ProductSearch.Cloud)`
- `ProductSearch.Cloud` enum: `AWS` (`https://multisearch-aw.rezolve.com/`), `AZURE` (`https://multisearch-az.rezolve.com/`)
- Auth: app key + placement ID (passed as query params)
- Operations: `searchByImage`, `searchById`, `multisearch`, `multisearchOutfitRec`, `multisearchComplementary`, `multisearchAutocomplete`
- Results delivered via `ProductSearch.ResultListener` or `ProductSearch.AutoCompleteResultListener`
- Internal flow: `ProductSearch` → `ProductSearchService` → `APIProductService` (legacy domain) or `APIProductServiceV2` (new cloud domains), selected at construction time based on the base URL
- API variants controlled by `ProductSearchApi` enum: `SBI`, `MS`, `MS_OUTFIT_REC`, `MS_COMPLEMENTARY`

### Shared Infrastructure
- **`Http`**: Singleton Retrofit instance factory
- **`RetrofitQueryMap`**: Custom map for serializing query params to Retrofit
- **`RetryCallAdapterFactory` / `@Retry`**: Custom Retrofit call adapter for automatic retries
- **Analytics**: Both clients inject session/device analytics params via `VisenzeAnalytics` (from the `visenze-tracking-android` dependency)

### Params Hierarchy
```
BaseSearchParams (analytics fields: uid, sid, appId, platform, os, etc.)
  └── SearchParams (ViSearch params: page, limit, fl, fq, etc.)
        ├── IdSearchParams
        ├── ColorSearchParams
        └── UploadSearchParams

BaseProductSearchParams (analytics fields)
  ├── ProductSearchByImageParams (image, im_url, im_id, q, detection, etc.)
  └── ProductSearchByIdParams (product_id, algorithms, etc.)
```

## Key Files

| File | Purpose |
|------|---------|
| `visearch-android/build.gradle` | Version number and publishing config |
| `ViSearch.java` | Legacy search client + Builder + ResultListener |
| `ProductSearch.java` | Product search client + Builder + ResultListeners |
| `network/Http.java` | Retrofit singleton factory |
| `network/APIProductService.java` | Retrofit interface for legacy ProductSearch endpoints (`v1/product/...`) |
| `network/APIProductServiceV2.java` | Retrofit interface for new cloud domain endpoints (`v1/visearch/...`, `v1/search/...`) |
| `network/APIService.java` | Retrofit interface for ViSearch endpoints |
| `model/ProductSearchApi.java` | Enum mapping search type → API endpoint path |
| `doc/ProductSearch.md` | Full ProductSearch API documentation |
| `doc/ViSearch.md` | Full ViSearch API documentation |

## Versioning & Publishing

To release a new version, update the three version variables at the top of `visearch-android/build.gradle`:
```groovy
def versionMajor = 2
def versionMinor = 6
def versionPatch = 0
```

The library is published to JitPack via the `maven-publish` plugin using the `release` publication.
