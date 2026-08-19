package org.example.tools.network.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.example.tools.SystemConfig;
import org.example.tools.network.entity.Brand;
import org.example.tools.network.entity.Category;
import org.example.tools.network.entity.Product;
import org.example.tools.network.entity.ProductsPerPage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 500;

    private static final Type BRAND_LIST = TypeToken.getParameterized(List.class, Brand.class).getType();
    private static final Type CATEGORY_LIST = TypeToken.getParameterized(List.class, Category.class).getType();
    private static final Type PRODUCT_LIST = TypeToken.getParameterized(List.class, Product.class).getType();

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static final Gson GSON = new Gson();

    private final String baseUrl;

    public ApiClient() {
        this(SystemConfig.getApiBaseUrl());
    }

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
    }

    public List<Brand> getBrands() {
        return get(Endpoints.BRANDS, BRAND_LIST);
    }

    public List<Category> getCategoryTree() {
        return get(Endpoints.CATEGORIES_TREE, CATEGORY_LIST);
    }

    public ProductsPerPage getProducts() {
        return get(Endpoints.PRODUCTS, ProductsPerPage.class);
    }

    public Product getProduct(String productId) {
        return get(Endpoints.product(productId), Product.class);
    }

    public List<Product> getRelatedProducts(String productId) {
        return get(Endpoints.relatedProducts(productId), PRODUCT_LIST);
    }

    private <T> T get(String path, Type responseType) {
        String url = baseUrl + path;
        ApiException lastFailure = null;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                        .timeout(TIMEOUT)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new ApiException("GET " + url + " returned HTTP " + response.statusCode());
                }

                T parsed = GSON.fromJson(response.body(), responseType);
                if (parsed == null) {
                    throw new ApiException("GET " + url + " returned an empty body");
                }
                return parsed;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ApiException("Interrupted while calling " + url, e);
            } catch (IOException | JsonParseException e) {
                lastFailure = new ApiException("GET " + url + " failed", e);
            } catch (ApiException e) {
                lastFailure = e;
            }

            if (attempt < MAX_ATTEMPTS) {
                pauseBeforeRetry();
            }
        }
        throw lastFailure;
    }

    private static void pauseBeforeRetry() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static class ApiException extends RuntimeException {

        public ApiException(String message) {
            super(message);
        }

        public ApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
