package org.example.restapi.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.restapi.models.User;

public class BaseTest {
    RequestSpecification requestSpecification;

    public BaseTest() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecification = requestSpecBuilder
                .setBaseUri("http://localhost:8082")
                .setContentType(ContentType.JSON)
                .build();
    }

    protected Response doGet(String endpoint) {
        return RestAssured
                .given(requestSpecification)
                .when()
                .get(endpoint)
                .then().extract().response();
    }

    protected Response doPost1(String endpoint, User body) {
        return RestAssured
                .given(requestSpecification)
                .queryParam("email", body.getEmail())
                .queryParam("password", body.getPassword())
                .auth().none()
                .when()
                .post(endpoint)
                .then().extract().response();
    }

    protected Response doPost(String endpoint, Object body) {
        return RestAssured
                .given(requestSpecification)
                .auth().none()
                .body(body)
                .when()
                .post(endpoint)
                .then().extract().response();
    }

    protected Response doGet(String endpoint, String token) {
        return RestAssured
                .given(requestSpecification)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint)
                .then().extract().response();
    }

    protected Response doPost(String endpoint, Object body, String token) {
        return RestAssured
                .given(requestSpecification)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(endpoint)
                .then().extract().response();
    }

    protected Response doDelete(String endpoint, String token) {
        return RestAssured
                .given(requestSpecification)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint)
                .then().extract().response();
    }

    protected Response doPut(String endpoint, Object body, String token) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .put(endpoint)
                .then().extract().response();
    }
}
