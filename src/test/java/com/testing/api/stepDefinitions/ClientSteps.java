package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private Client oldClient;
    private Client client;

    @Given("I have a client with the following details:")
    public void iHaveAClientWithTheFollowingDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps()
                .get(0);
        client = Client.builder()
                .name(clientDataMap.get("Name"))
                .lastName(clientDataMap.get("LastName"))
                .country(clientDataMap.get("Country"))
                .city(clientDataMap.get("City"))
                .email(clientDataMap.get("Email"))
                .phone(clientDataMap.get("Phone"))
                .id(clientDataMap.get("Id"))
                .build();
        clientRequest.createClient(client);
        logger.info("Client mapped: {}", client);
    }

    @When("I send a DELETE request to delete the client with ID {string}")
    public void iSendADELETERequestToDeleteTheClientWithID(String clientId) {
        response = clientRequest.deleteClient(clientId);
        logger.info("Successfully Deleted client with id {}", clientId);
    }

    @When("I send a PUT request to update the client with ID {string}")
    public void iSendAPUTRequestToUpdateTheClientWithID(String clientId, String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateClient(client, clientId);
        logger.info("updated client with id {} ", clientId);
    }

    @Then("the client response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info("Successfully validated response status {}", statusCode);
    }

    @Then("the client response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        client = clientRequest.getClientEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps()
                .get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), client.getName());
        Assert.assertEquals(expectedDataMap.get("LastName"), client.getLastName());
        Assert.assertEquals(expectedDataMap.get("Country"), client.getCountry());
        Assert.assertEquals(expectedDataMap.get("City"), client.getCity());
        Assert.assertEquals(expectedDataMap.get("Email"), client.getEmail());
        Assert.assertEquals(expectedDataMap.get("Phone"), client.getPhone());
        Assert.assertEquals(expectedDataMap.get("Id"), client.getId());
    }

    @Then("the response should include the details of the created client")
    public void theResponseShouldIncludeTheDetailsOfTheCreatedClient() {
        Client newClient = clientRequest.getClientEntity(response);
        newClient.setId(null);
        Assert.assertEquals(client, newClient);
    }

    @Then("validates the response with client JSON schema")
    public void userValidatesResponseWithClientJSONSchema() {
        String path = "./schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @Then("validates the response with client list JSON schema")
    public void userValidatesResponseWithClientListJSONSchema() {
        String path = "./schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client List object");
    }

    @Given("there are registered {int} clients")
    public void thereAreRegisteredClients(int quantity) {
        clientRequest.createDefaultClients(quantity);
        logger.info("Dummy clients successfully created");
    }

    @Given("there is a client named {string} created")
    public void thereIsAClientNamedCreated(String name) {
        Faker faker = new Faker();
        String lastName = faker.name().lastName();
        String country = faker.country().name();
        String city = faker.country().capital();
        String email = faker.internet().emailAddress();
        String phone = faker.expression("#{numerify '+57 ###-###-##-##'}");
        client = new Client(name, lastName, country, city, email, phone, "1");
        response = clientRequest.createClient(client);
        logger.info("Client named {} created", name);
    }

    @When("i update the phone number of the first client with the name {string} to {string}")
    public void iUpdateThePhoneNumberOfTheFirstClientWithTheNameTo(String name, String phone) {
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        client = null;
        for (Client clientInList : clientList) {
            if (Objects.equals(clientInList.getName(), name)) {
                oldClient = new Client(clientInList.getName(),
                        clientInList.getLastName(), clientInList.getCountry(),
                        clientInList.getCity(), clientInList.getEmail(),
                        clientInList.getPhone(), clientInList.getId());
                client = clientInList;
                break;
            }
        }
        Assert.assertNotNull(client);
        client.setPhone(phone);
        response = clientRequest.updateClient(client, client.getId());
        logger.info("Phone number {} updated to client {}", phone, name);
    }

    @Then("the new phone number is different than the old phone number")
    public void theNewPhoneNumberIsDifferentThanTheOldPhoneNumber() {
        Assert.assertNotEquals(oldClient.getPhone(), client.getPhone());
        logger.info("Old number {} is not equal to the new phone {}", oldClient.getPhone(), client.getPhone());
    }

    @After("@clientsPost")
    public void deleteCreatedClients() {
        logger.info("--------------DELETING CLIENTS----------------");
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        for (Client clientListed : clientList) {
            clientRequest.deleteClient(String.valueOf(clientListed.getId()));
        }
        logger.info("-------------- {} CLIENTS DELETED----------------", clientList.size());
    }


}
