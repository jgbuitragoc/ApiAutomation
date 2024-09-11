package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.requests.ResourcesRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ResourcesSteps {

    private static final Logger logger = LogManager.getLogger(Resource.class);
    private final ResourcesRequest resourcesRequest = new ResourcesRequest();
    private Response response;
    private Resource resource;

    @Then("the resource response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info("Successfully validated response status {}", statusCode);
    }


    @Given("there are {int} active resources")
    public void thereAreActiveResources(int quantity) {
        resourcesRequest.createDefaultResources(quantity);
        logger.info("Dummy Resources created");
    }

    @When("i retrieve all the resources")
    public void iRetrieveAllTheResources() {
        response = resourcesRequest.getResources();
        logger.info("Resources retrieved");
    }

    @Then("at least {int} resources should be active")
    public void atLeastQuantityResourcesShouldBeActive(int quantity) {
        List<Resource> resourcesList = resourcesRequest.getResourcesEntity(response);
        int activeCount = 0;
        for (Resource res : resourcesList) {
            if (res.isActive()) {
                activeCount++;
            }
        }
        Assert.assertTrue(activeCount >= quantity);
        logger.info("At least {} resources were active", quantity);
    }

    @And("validates the response with resource list JSON schema")
    public void validatesTheResponseWithResourceListJSONSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourcesRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource List object");
    }


    @And("validates the response with resource JSON schema")
    public void validatesTheResponseWithResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourcesRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource object");
    }

    @And("the resources response should have the following details:")
    public void theResourcesResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        resource = resourcesRequest.getResourceEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps()
                .get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), resource.getName());
        Assert.assertEquals(expectedDataMap.get("Trademark"), resource.getTrademark());
        Assert.assertEquals(Integer.parseInt(expectedDataMap.get("Stock")), resource.getStock());
        Assert.assertEquals(Float.parseFloat(expectedDataMap.get("Price")), resource.getPrice(), 0);
        Assert.assertEquals(expectedDataMap.get("Description"), resource.getDescription());
        Assert.assertEquals(expectedDataMap.get("Tags"), resource.getTags());
        Assert.assertTrue(expectedDataMap.get("Active"), resource.isActive());
    }

    @And("i update the parameters of the last resource found")
    public void iUpdateTheParametersOfTheLastResourceFound(String requestBody) {
        List<Resource> resourceList = resourcesRequest.getResourcesEntity(response);
        Resource lastResource = resourceList.get(resourceList.size() - 1);
        logger.info("Last resource: {}", lastResource.getName());
        resource = resourcesRequest.getResourceEntity(requestBody);
        response = resourcesRequest.updateResource(resource, lastResource.getId());
        logger.info("updated client with id {} ", lastResource.getId());
    }

    @After("@resourcesActive")
    public void setResourcesToInactive() {
        logger.info("--------------DEACTIVATING RESOURCES----------------");
        response = resourcesRequest.getResources();
        List<Resource> resourceList = resourcesRequest.getResourcesEntity(response);
        for (Resource res : resourceList) {
            res.setActive(false);
            resourcesRequest.updateResource(res, res.getId());
        }
        logger.info("--------------{} RESOURCES DEACTIVATED----------------", resourceList.size());
    }

    @After("@resourcesPost")
    public void deleteCreatedClients() {
        logger.info("--------------DELETING RESOURCES----------------");
        response = resourcesRequest.getResources();
        List<Resource> resourcesList = resourcesRequest.getResourcesEntity(response);
        for (Resource res : resourcesList) {
            resourcesRequest.deleteResource(String.valueOf(res.getId()));
        }
        logger.info("-------------- {} CLIENTS DELETED----------------", resourcesList.size());
    }
}
