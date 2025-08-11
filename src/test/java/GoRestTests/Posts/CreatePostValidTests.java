package GoRestTests.Posts;

import GoRestMain.POJO.Deserialization.Posts.CreatePostResponse;
import GoRestMain.POJO.Deserialization.Users.CreateUserResponse;
import GoRestMain.POJO.Serialization.Posts.CreatePostRequest;
import GoRestMain.POJO.Serialization.Users.CreateUserRequest;
import GoRestMain.Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("GoRest API Testing")
@Feature("Post Management")
@Story("Post CRUD Operations")
public class CreatePostValidTests extends Reusables {

private int userId;

// Creating a user before running the tests
@Test(description = "Create a user for post testing")
@Severity(SeverityLevel.CRITICAL)
@Description("Create a user that will be used for creating posts")
@Step("Create user for post testing")
public void createUserTest() {

    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("Mark Robinson");
    createUserReqBody.setGender("male");
    createUserReqBody.setEmail("MarkRob1@gmail.com");
    createUserReqBody.setStatus("active");

    CreateUserResponse resp = given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createUserReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(201)
            .body(containsString("id"))
            .body(containsString("name"))
            .body(containsString("email"))
            .body(containsString("gender"))
            .body(containsString("status"))
            .extract()
            .as(CreateUserResponse.class);
    userId = resp.getId();
}

@Test(dependsOnMethods = "createUserTest", description = "Create a post for the user")
@Severity(SeverityLevel.CRITICAL)
@Description("Create a post for the user and verify response contains all required fields")
@Step("Create post for user")
public void createPostTest() {

    System.out.println(userId);

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("This is a Post title");
    createPostReqBody.setBody("This body means post was created successfully");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createPostReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users/" + userId + "/posts")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(201)
            .body(containsString("id"))
            .body(containsString("user_id"))
            .body(containsString("body"))
            .body(containsString("title"))
            .extract()
            .as(CreatePostResponse.class);
}

@Test(dependsOnMethods = "createPostTest", description = "Get all posts for the user")
@Severity(SeverityLevel.NORMAL)
@Description("Retrieve all posts for the user and verify response")
@Step("Get user posts")
public void getUserPostsTest() {

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .when()
            .log()
            .all()
            .get("/public/v2/users/" + userId + "/posts")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body(containsString("id"))
            .body(containsString("user_id"))
            .body(containsString("body"))
            .body(containsString("title"));
}

@Test(dependsOnMethods = "getUserPostsTest", description = "Delete the user")
@Severity(SeverityLevel.CRITICAL)
@Description("Delete the user and verify successful deletion")
@Step("Delete user")
public void deleteUserTest() {

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .when()
            .log()
            .all()
            .delete("/public/v2/users/" + userId)
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(204)
            .body(emptyString());
}

@Test(dependsOnMethods = "deleteUserTest", description = "Verify user and posts are not accessible after deletion")
@Severity(SeverityLevel.NORMAL)
@Description("Verify that user and posts are not accessible after user deletion")
@Step("Verify user and posts not accessible after deletion")
public void getUserPostsAfterDeleteTest() {

    //Assuring the user is deleted, the posts should not be retrievable
    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .when()
            .log()
            .all()
            .get("/public/v2/users/" + userId)
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(404)
            .body("message", equalTo("Resource not found"));

    //Assuring the posts are not retrievable after user deletion
    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .when()
            .log()
            .all()
            .get("/public/v2/users/" + userId + "/posts")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("", equalTo(Collections.emptyList()));
}
}
