package Tests.Posts;

import POJO.Deserialization.Users.CreateUserResponse;
import POJO.Serialization.Posts.CreatePostRequest;
import POJO.Serialization.Users.CreateUserRequest;
import Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("GoRest API Testing")
@Feature("Post Management")
@Story("Post Validation and Error Handling")
public class CreatePostInvalidTests extends Reusables {

private int userId;

// Creating a user before running the tests
@BeforeClass
@Step("Setup: Create user for post testing")
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
            .response()
            .as(CreateUserResponse.class);
    userId = resp.getId();
}

@Test(description = "Test creating post with blank title")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a post with blank title returns validation error")
@Step("Create post with blank title")
public void createPostWithBlankTitleTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("");
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
            .statusCode(422)
            .body("field", hasItem("title"))
            .body("message", hasItem("can't be blank"));
}

@Test(description = "Test creating post with blank body")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a post with blank body returns validation error")
@Step("Create post with blank body")
public void createPostWithBlankBodyTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("This is a Post title");
    createPostReqBody.setBody("");

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
            .statusCode(422)
            .body("field", hasItem("body"))
            .body("message", hasItem("can't be blank"));
}

@Test(description = "Test creating post with missing body field")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a post with missing body field returns validation error")
@Step("Create post with missing body field")
public void createPostWithMissingBodyTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("This is a Post title");

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
            .statusCode(422)
            .body("field", hasItem("body"))
            .body("message", hasItem("can't be blank"));
}

@Test(description = "Test creating post with missing title field")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a post with missing title field returns validation error")
@Step("Create post with missing title field")
public void createPostWithMissingTitleTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
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
            .statusCode(422)
            .body("field", hasItem("title"))
            .body("message", hasItem("can't be blank"));
}

@Test(description = "Test creating post without authentication")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a post without authentication returns 401 error")
@Step("Create post without authentication")
public void createPostWithNoAuthTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("This is a Post title");
    createPostReqBody.setBody("This body means post was created successfully");

    given().log()
            .all()
            .spec(Reusables.reqSpecNoAuth)
            .body(createPostReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users/" + userId + "/posts")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(401)
            .body("message", equalTo("Authentication failed"));
}

@Test(description = "Test creating post for non-existent user")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a post for non-existent user returns validation error")
@Step("Create post for non-existent user")
public void createPostWithNoUserTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("This is a Post title");
    createPostReqBody.setBody("This body means post was created successfully");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createPostReqBody)
            .when()
            .post("/public/v2/users/" + 0 + "/posts")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(422)
            .body("field", hasItem("user"))
            .body("message", hasItem("must exist"));
}

@AfterClass
@Step("Cleanup: Delete test user")
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

}
