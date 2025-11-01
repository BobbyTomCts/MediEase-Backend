package org.example.restapi.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.restapi.models.User;
import org.example.restapi.services.UserService;
import org.testng.Assert;

import java.util.Map;

public class UserStepDefinitions {

    private final UserService userService;
    private User user;
    private Response response;
    Map<String,String> map;
    private Long userId;
    private String token;
    private static final String REGISTER_ENDPOINT = "/api/users/register";
    private static final String LOGIN_ENDPOINT = "/api/users/login";
    public UserStepDefinitions()
    {
        this.userService = new UserService();
    }

    //register
    @Given("user is registering")
    public void userIsRegistering(DataTable data)
    {
        map = data.asMap(String.class,String.class);
        user = new User(map.get("name"),map.get("phone"),map.get("email"),map.get("password"));
        response = userService.postRequest(REGISTER_ENDPOINT,user);
        System.out.println("Register status: "+response.getStatusCode());
        System.out.println("Register body: "+response.asString());
        if (response.getStatusCode() == 200) {
            userId = response.jsonPath().getLong("id");
            System.out.println("Registered User ID: " + userId);
        }
    }
    @When("the user is successfully registered")
    public void theUserIsSuccessfullyRegistered()
    {
        Assert.assertEquals(response.getStatusCode(),200,"Registration failed with status: " + response.getStatusCode());
        Assert.assertEquals(response.getBody().path("name"),map.get("name"),"Name mismatch in registration response.");
    }
    public void performLoginAndExtractToken(String email, String password) {
        user = new User(email, password);
        response = userService.postRequest1(LOGIN_ENDPOINT, user);
        Assert.assertEquals(response.getStatusCode(), 200, "Login failed. Status: " + response.getStatusCode());
        token = response.jsonPath().getString("token");
        userId = response.jsonPath().getLong("id");
        Assert.assertTrue(token != null && !token.isEmpty(), "Token was not generated successfully");
        System.out.println("Token generated (ID: " + userId + ")");
    }
    @Then("the user can login")
    public void theUserCanLogin()
    {
        performLoginAndExtractToken(map.get("email"),map.get("password"));
    }

    //login
    @Given("the user can login with {string} and {string}")
    public void theUserCanLoginWith(String email,String password)
    {
        performLoginAndExtractToken(email,password);
    }
    @Then("token is generated successfully")
    public void tokenIsGeneratedSuccessfully()
    {
        Assert.assertTrue(token!=null && !token.isEmpty(),"Token is not generated successfully");
    }

    //validate api
    @When("the user calls the secure validate endpoint")
    public void theUserCallsTheSecureValidateEndpoint() {
        response = userService.getRequest("/api/users/validate", token);
        System.out.println("Validate Status: " + response.getStatusCode());
    }
    @Then("the response user details match the logged-in user")
    public void theResponseUserDetailsMatchTheLoggedInUser() {
        Assert.assertEquals(response.getStatusCode(), 200, "Validation failed with status: " + response.getStatusCode());

        Long validatedId = response.jsonPath().getLong("id");
        Assert.assertEquals(validatedId, userId, "Validated user ID does not match logged-in user ID.");

        String validatedEmail = response.jsonPath().getString("email");
        Assert.assertEquals(validatedEmail, user.getEmail(), "Validated user email does not match logged-in user email.");
    }

    //get user by id
    @When("the user calls the get user by ID endpoint")
    public void theUserCallsTheGetUserByIDEndpoint() {
        response = userService.getRequest("/api/users/" + userId, token);
        System.out.println("Get User Status: " + response.getStatusCode());
    }

    //isAdmin check
    @When("the user calls the isAdmin check endpoint")
    public void theUserCallsTheIsAdminCheckEndpoint() {
        response = userService.getRequest("/api/users/isAdmin/" + userId);
        System.out.println("Is Admin Status: " + response.getStatusCode());
    }

    @Then("the response confirms the user is {string}")
    public void theResponseConfirmsTheUserIs(String expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), 200, "Admin check failed with status: " + response.getStatusCode());
        boolean actualStatus = Boolean.parseBoolean(response.asString());
        boolean expectedBool = "admin".equalsIgnoreCase(expectedStatus);
        Assert.assertEquals(actualStatus, expectedBool, "Expected admin status to be " + expectedBool + " but got " + actualStatus);
    }

    @Then("the response status code is {int}")
    public void theResponseStatusCodeIs(int expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), expectedStatus, "Expected status code mismatch.");
    }



}
