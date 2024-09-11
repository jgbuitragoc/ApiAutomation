package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Resource;
import com.testing.api.utils.Constants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourcesRequest extends BaseRequest {
    private String endpoint;

    /**
     * Get Resources list
     *
     * @return rest-assured response
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get resource by id
     *
     * @param resourceId string
     * @return rest-assured response
     */
    public Response getResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create resource
     *
     * @param resource model
     * @return rest-assured response
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Update resource by id
     *
     * @param resource   model
     * @param resourceId string
     * @return rest-assured response
     */
    public Response updateResource(Resource resource, String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Delete resource by id
     *
     * @param resourceId string
     * @return rest-assured response
     */
    public Response deleteResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    public Resource getResourceEntity(@NotNull Response response) {
        return response.as(Resource.class);
    }

    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    public Resource getResourceEntity(String resourceJson) {
        Gson gson = new Gson();
        return gson.fromJson(resourceJson, Resource.class);
    }

    public void createDefaultResources(int quantity) {
        Faker faker = new Faker();
        Resource resource;
        int resourcesCount = 0;
        int resourceId = 1;
        while (resourcesCount <= quantity) {
            String name = faker.commerce().productName();
            String trademark = faker.company().name();
            int stock = faker.number().numberBetween(0, 100000);
            float price = Float.parseFloat(faker.commerce().price());
            String description = faker.commerce().material();
            String tags = faker.verb().base();
            boolean active = true;
            resource = new Resource(
                    name, trademark, stock, price, description, tags, active, String.valueOf(resourceId)
            );
            this.createResource(resource);
            resourcesCount++;
            resourceId++;
        }
    }

}
