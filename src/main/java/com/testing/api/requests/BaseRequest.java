package com.testing.api.requests;

import com.testing.api.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

    private static final Logger logger = LogManager.getLogger(BaseRequest.class);


    /**
     * This is a function to read elements using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @return Response
     */
    protected Response requestGet(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .get(endpoint);
    }

    /**
     * This is a function to create a new element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     * @return Response
     */
    protected Response requestPost(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * This is a function to update an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     * @return Response
     */
    protected Response requestPut(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * This is a function to delete an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @return Response
     */
    protected Response requestDelete(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .delete(endpoint);
    }

    /**
     * This is a function to create a default header map object
     *
     * @return default headers
     */
    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }

    /**
     * Validate the response schema vs a json schema in a file
     *
     * @param response   the API response
     * @param schemaPath the schema file path in the system
     * @return true if the response schema matches the json schema, AssertionError if assertion fails
     */
    public boolean validateSchema(Response response, String schemaPath) {
       try {
           String responseBody = response.getBody().asString();
           logger.info("Json response body being validated: {}", responseBody);
           response.then()
                   .assertThat()
                   .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
           return true;
       }catch (AssertionError e){
           return false;
       }
    }
}
