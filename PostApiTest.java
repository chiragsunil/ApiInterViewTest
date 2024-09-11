package interview;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PostApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void testGetAllPost() {
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0);
    }

    @Test
    public void testGetPostById() {
        int postId = 1;
        Response response = given()
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract().response();
        Assert.assertNotNull(response);
        Assert.assertEquals(response.jsonPath().getInt("id"), postId);
    }

    @Test
    public void testCreatePost() {
        String requestBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201) // Note: The typical response code for POST is 201 (Created)
                .extract().response();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.jsonPath().getString("body"), "bar");
        Assert.assertEquals(response.jsonPath().getInt("userId"), 1);
    }

    @Test
    public void testUpdate() {
        int postId = 1;
        String requestBody = "{\"id\":" + postId + ",\"title\":\"updatedtitle\",\"body\":\"updated body\",\"userId\":1}";

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .put("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.jsonPath().getString("body"), "updated body");
        Assert.assertEquals(response.jsonPath().getInt("userId"), 1);
    }

    @Test
    public void testDeletePost() {
        int postId = 1;
        Response response = given()
                .when()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.asString().isEmpty());
    }
}
