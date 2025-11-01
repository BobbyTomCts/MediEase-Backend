package org.example.restapi.services;

import io.restassured.response.Response;
import org.example.restapi.Test.BaseTest;
import org.example.restapi.models.User;

public class UserService extends BaseTest {
    public Response getRequest(String endpoint)
    {
        return doGet(endpoint);
    }
    public Response postRequest(String endpoint,Object body)
    {
        return doPost(endpoint,body);
    }
    public Response postRequest1(String endpoint,User body)
    {
        return doPost1(endpoint,body);
    }
    public Response getRequest(String endpoint, String token)
    {
        return doGet(endpoint,token);
    }
    public Response postRequest(String endpoint, Object body, String token)
    {
        return doPost(endpoint,body,token);
    }
}
