package dev.pedro.tam;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class RetailAnalyticsTest {

    @Test
    void testInfoEndpoint() {
        given()
            .when().get("/api/v1/info")
            .then()
            .statusCode(200)
            .body("app", is("retail-analytics"));
    }

    @Test
    void testProductsEndpoint() {
        given()
            .when().get("/api/v1/products")
            .then()
            .statusCode(200)
            .body("size()", is(6));
    }

    @Test
    void testProductsByCategory() {
        given()
            .queryParam("category", "Electronics")
            .when().get("/api/v1/products")
            .then()
            .statusCode(200)
            .body("size()", is(3));
    }

    @Test
    void testProductNotFound() {
        given()
            .when().get("/api/v1/products/999")
            .then()
            .statusCode(404);
    }

    @Test
    void testAnalyticsSummary() {
        given()
            .when().get("/api/v1/analytics/summary")
            .then()
            .statusCode(200)
            .body("period", notNullValue())
            .body("totalRevenue", notNullValue());
    }

    @Test
    void testHealthLiveness() {
        given()
            .when().get("/q/health/live")
            .then()
            .statusCode(200);
    }

    @Test
    void testHealthReadiness() {
        given()
            .when().get("/q/health/ready")
            .then()
            .statusCode(200);
    }
}
