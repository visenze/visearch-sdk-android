## Domain update

For Product Search API, the current domain is "https://search.visenze.com" or "https://multimodal.search.rezolve.com" for apps deployed in AWS/Azure respectively.

We now have 2 new cloud specific domains with updated paths (clearer and more intuitive):
- https://multisearch-aw.rezolve.com for apps deployed in AWS
- https://multisearch-az.rezolve.com for apps deployed in Azure

## API path changes

The API paths for the new domain has also changed.

| Current host path | New host path | Description
|:---|:---|:---|
| https://search.visenze.com/v1/product/search_by_image | https://multisearch-aw.rezolve.com/v1/visearch/search_by_image | Legacy image search API.
| https://search.visenze.com/v1/product/recommendations | https://multisearch-aw.rezolve.com/v1/visearch/recommendations | Legacy recommendations API.
| https://search.visenze.com/v1/product/search_by_id | https://multisearch-aw.rezolve.com/v1/visearch/search_by_id | Legacy recommendations API.
| https://search.visenze.com/v1/product/multisearch | https://multisearch-aw.rezolve.com/v1/search | Multi-search API.
| https://search.visenze.com/v1/product/multisearch/complementary | https://multisearch-aw.rezolve.com/v1/search/complementary | Multi-search complementary API.
| https://search.visenze.com/v1/product/multisearch/outfit-recommendations | https://multisearch-aw.rezolve.com/v1/search/outfit-recommendations | Multi-search outfit recommendations API.
| https://search.visenze.com/v1/product/multisearch/autocomplete | https://multisearch-aw.rezolve.com/v1/autocomplete | Multi-search autocomplete API.
