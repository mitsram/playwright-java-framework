package com.framework.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.framework.api.ApiResponseWrapper;
import com.framework.base.BaseAPITest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * API tests for the Swagger Petstore API (https://petstore.swagger.io/v2).
 * Demonstrates CRUD operations on the /pet endpoint.
 */
public class PetStoreApiTest extends BaseAPITest {

    private static final long TEST_PET_ID = 99887766L;

    // ── Pet CRUD ────────────────────────────────────────────────────

    @Test(description = "POST - Create a new pet", priority = 1)
    public void testCreatePet() {
        Map<String, Object> pet = Map.of(
                "id", TEST_PET_ID,
                "name", "Buddy",
                "status", "available",
                "category", Map.of("id", 1, "name", "Dogs"),
                "photoUrls", List.of("https://example.com/buddy.jpg"),
                "tags", List.of(Map.of("id", 1, "name", "friendly"))
        );

        ApiResponseWrapper response = apiClient.post("/pet", pet);

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode body = response.bodyAsJson();
        assertThat(body.get("id").asLong()).isEqualTo(TEST_PET_ID);
        assertThat(body.get("name").asText()).isEqualTo("Buddy");
        assertThat(body.get("status").asText()).isEqualTo("available");
    }

    @Test(description = "GET - Retrieve pet by ID", priority = 2, dependsOnMethods = "testCreatePet")
    public void testGetPetById() {
        ApiResponseWrapper response = apiClient.get("/pet/" + TEST_PET_ID);

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode body = response.bodyAsJson();
        assertThat(body.get("id").asLong()).isEqualTo(TEST_PET_ID);
        assertThat(body.get("name").asText()).isEqualTo("Buddy");
    }

    @Test(description = "PUT - Update an existing pet", priority = 3, dependsOnMethods = "testCreatePet")
    public void testUpdatePet() {
        Map<String, Object> updatedPet = Map.of(
                "id", TEST_PET_ID,
                "name", "Buddy Updated",
                "status", "sold",
                "category", Map.of("id", 1, "name", "Dogs"),
                "photoUrls", List.of("https://example.com/buddy-updated.jpg"),
                "tags", List.of(Map.of("id", 1, "name", "friendly"))
        );

        ApiResponseWrapper response = apiClient.put("/pet", updatedPet);

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode body = response.bodyAsJson();
        assertThat(body.get("name").asText()).isEqualTo("Buddy Updated");
        assertThat(body.get("status").asText()).isEqualTo("sold");
    }

    @Test(description = "GET - Verify pet was updated", priority = 4, dependsOnMethods = "testUpdatePet")
    public void testVerifyPetUpdated() {
        ApiResponseWrapper response = apiClient.get("/pet/" + TEST_PET_ID);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.bodyAsJson().get("name").asText()).isEqualTo("Buddy Updated");
        assertThat(response.bodyAsJson().get("status").asText()).isEqualTo("sold");
    }

    @Test(description = "DELETE - Remove a pet", priority = 5, dependsOnMethods = "testVerifyPetUpdated")
    public void testDeletePet() {
        ApiResponseWrapper response = apiClient.delete("/pet/" + TEST_PET_ID);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test(description = "GET - Verify pet was deleted returns 404", priority = 6, dependsOnMethods = "testDeletePet")
    public void testGetDeletedPetReturns404() {
        ApiResponseWrapper response = apiClient.get("/pet/" + TEST_PET_ID);

        assertThat(response.statusCode()).isEqualTo(404);
    }

    // ── Find by Status ──────────────────────────────────────────────

    @Test(description = "GET - Find pets by status 'available'")
    public void testFindPetsByStatusAvailable() {
        ApiResponseWrapper response = apiClient.get("/pet/findByStatus", Map.of("status", "available"));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.bodyAsJson().isArray()).isTrue();
        assertThat(response.bodyAsJson().size()).isGreaterThan(0);
    }

    @Test(description = "GET - Find pets by status 'sold'")
    public void testFindPetsByStatusSold() {
        ApiResponseWrapper response = apiClient.get("/pet/findByStatus", Map.of("status", "sold"));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.bodyAsJson().isArray()).isTrue();
    }

    @Test(description = "GET - Find pets by status 'pending'")
    public void testFindPetsByStatusPending() {
        ApiResponseWrapper response = apiClient.get("/pet/findByStatus", Map.of("status", "pending"));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.bodyAsJson().isArray()).isTrue();
    }

    // ── Store ───────────────────────────────────────────────────────

    @Test(description = "GET - Retrieve store inventory")
    public void testGetStoreInventory() {
        ApiResponseWrapper response = apiClient.get("/store/inventory");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.bodyAsJson().isObject()).isTrue();
    }

    // ── Negative Tests ──────────────────────────────────────────────

    @Test(description = "GET - Non-existent pet returns 404")
    public void testGetNonExistentPet() {
        ApiResponseWrapper response = apiClient.get("/pet/0");

        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test(description = "GET - Invalid pet ID returns error")
    public void testGetPetWithInvalidId() {
        ApiResponseWrapper response = apiClient.get("/pet/invalid");

        assertThat(response.statusCode()).isNotEqualTo(200);
    }
}
