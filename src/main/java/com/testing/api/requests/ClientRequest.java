package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Client;
import com.testing.api.utils.Constants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientRequest extends BaseRequest {
    private String endpoint;

    /**
     * Get Client list
     * @return rest-assured response
     */
    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get client by id
     * @param clientId string
     * @return rest-assured response
     */
    public Response getClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create client
     * @param client model
     * @return rest-assured response
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    /**
     * Update client by id
     * @param client model
     * @param clientId string
     * @return rest-assured response
     */
    public Response updateClient(Client client, String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestPut(endpoint, createBaseHeaders(), client);
    }

    /**
     * Delete client by id
     * @param clientId string
     * @return rest-assured response
     */
    public Response deleteClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    public Client getClientEntity(@NotNull Response response) {
        return response.as(Client.class);
    }

    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    public Client getClientEntity(String clientJson) {
        Gson gson = new Gson();
        return gson.fromJson(clientJson, Client.class);
    }

    /**
     * Create n quantity of clients with datafaker
     * @param quantity of clients to be created
     */
    public void createDefaultClients(int quantity){
        Faker faker = new Faker();
        Client client;
        int clientsCount = 0;
        int clientId = 1;
        while (clientsCount <= quantity) {
            String name = faker.name().firstName();
            String lastName = faker.name().lastName();
            String country = faker.country().name();
            String city = faker.country().capital();
            String email = faker.internet().emailAddress();
            String phone = faker.expression("#{numerify '+57 ###-###-##-##'}");
            client = new Client(name, lastName, country, city, email, phone, String.valueOf(clientId));
            this.createClient(client);
            clientsCount++;
            clientId++;
        }
    }
}
