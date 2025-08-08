package Tests.Comments;

import POJO.Deserialization.Posts.CreatePostResponse;
import POJO.Deserialization.Users.CreateUserResponse;
import POJO.Serialization.Comment.CreateCommentRequest;
import POJO.Serialization.Posts.CreatePostRequest;
import POJO.Serialization.Users.CreateUserRequest;
import Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("GoRest API Testing")
@Feature("Comment Management")
@Story("Comment CRUD Operations")
public class CreateCommentValidTests extends Reusables {

private int userId;
private int postId;
private String name;
private String email;

// Creating a user before running the tests
@Test(description = "Create a user for comment testing")
@Severity(SeverityLevel.CRITICAL)
@Description("Create a user that will be used for creating comments")
@Step("Create user for comment testing")
public void createUserTest() {

    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("Mark Robinson");
    createUserReqBody.setGender("male");
    createUserReqBody.setEmail("MarkRob12@gmail.com");
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
    name = resp.getName();
    email = resp.getEmail();
}

// Creating a post after creating the user
@Test(dependsOnMethods = "createUserTest", description = "Create a post for comment testing")
@Severity(SeverityLevel.CRITICAL)
@Description("Create a post that will be used for creating comments")
@Step("Create post for comment testing")
public void createPostTest() {

    CreatePostRequest createPostReqBody = new CreatePostRequest();
    createPostReqBody.setTitle("This is a Post title");
    createPostReqBody.setBody("This body means post was created successfully");

    CreatePostResponse resp = given().spec(Reusables.reqSpec)
            .body(createPostReqBody)
            .when()
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
            .response()
            .as(CreatePostResponse.class);

    postId = resp.getId();
}

// Validating the post creation
@Test(dependsOnMethods = "createPostTest", description = "Get user posts to validate creation")
@Severity(SeverityLevel.NORMAL)
@Description("Retrieve user posts to validate post creation")
@Step("Get user posts")
public void getUserPostsTest() {

    given().spec(Reusables.reqSpec)
            .when()
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

// Creating comments for the post created successfully
@Test(dependsOnMethods = "createPostTest", description = "Create first comment for the post")
@Severity(SeverityLevel.CRITICAL)
@Description("Create first comment for the post and verify response")
@Step("Create first comment")
public void create1stCommentTest() {

    CreateCommentRequest createCommentReq = new CreateCommentRequest();
    createCommentReq.setPost_id(postId);
    createCommentReq.setName(name);
    createCommentReq.setEmail(email);
    createCommentReq.setBody("This is 1st comment for the post created successfully");

    given().spec(reqSpec)
            .body(createCommentReq)
            .when()
            .post("/public/v2/comments")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(201)
            .body("post_id", equalTo(postId))
            .body("name", equalTo(name))
            .body("email", equalTo(email))
            .body(containsString("body"))
            .body(containsString("id"));
}

// Creating multiple comments for the post created successfully
@Test(dependsOnMethods = "createPostTest", description = "Create second comment for the post")
@Severity(SeverityLevel.CRITICAL)
@Description("Create second comment for the post and verify response")
@Step("Create second comment")
public void create2ndCommentTest() {

    CreateCommentRequest createCommentReq = new CreateCommentRequest();
    createCommentReq.setPost_id(postId);
    createCommentReq.setName(name);
    createCommentReq.setEmail(email);
    createCommentReq.setBody("This is 2nd comment for the post created successfully");

    given().spec(reqSpec)
            .body(createCommentReq)
            .when()
            .post("/public/v2/comments")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(201)
            .body("post_id", equalTo(postId))
            .body("name", equalTo(name))
            .body("email", equalTo(email))
            .body(containsString("body"))
            .body(containsString("id"));
}

// Creating 3rd comment for the post created successfully
@Test(dependsOnMethods = "createPostTest", description = "Create third comment for the post")
@Severity(SeverityLevel.CRITICAL)
@Description("Create third comment for the post and verify response")
@Step("Create third comment")
public void create3rdCommentTest() {

    CreateCommentRequest createCommentReq = new CreateCommentRequest();
    createCommentReq.setPost_id(postId);
    createCommentReq.setName(name);
    createCommentReq.setEmail(email);
    createCommentReq.setBody("This is 3rd comment for the post created successfully");

    given().spec(Reusables.reqSpec)
            .body(createCommentReq)
            .when()
            .post("/public/v2/comments")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(201)
            .body("post_id", equalTo(postId))
            .body("name", equalTo(name))
            .body("email", equalTo(email))
            .body(containsString("body"))
            .body(containsString("id"));
}

@Test(dependsOnMethods = "create3rdCommentTest", description = "Get all comments for the post")
@Severity(SeverityLevel.NORMAL)
@Description("Retrieve all comments for the post and verify response")
@Step("Get post comments")
public void getUserCommentsTest() {

    given().spec(Reusables.reqSpec)
            .when()
            .get("/public/v2/posts/" + postId + "/comments")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("post_id", hasItems(postId, postId, postId))
            .body("name", hasItems(name, name, name))
            .body("email", hasItems(email, email, email))
            .body(containsString("body"))
            .body(containsString("id"));
}

@Test(dependsOnMethods = "getUserPostsTest", description = "Delete the user")
@Severity(SeverityLevel.CRITICAL)
@Description("Delete the user and verify successful deletion")
@Step("Delete user")
public void deleteUserTest() {

    given().spec(Reusables.reqSpec)
            .when()
            .delete("/public/v2/users/" + userId)
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(204)
            .body(emptyString());
}

}
