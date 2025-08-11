package GoRestTests.Comments;

import GoRestMain.POJO.Deserialization.Posts.CreatePostResponse;
import GoRestMain.POJO.Deserialization.Users.CreateUserResponse;
import GoRestMain.POJO.Serialization.Comment.CreateCommentRequest;
import GoRestMain.POJO.Serialization.Posts.CreatePostRequest;
import GoRestMain.POJO.Serialization.Users.CreateUserRequest;
import GoRestMain.Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;

@Epic("GoRest API Testing")
@Feature("Comment Management")
@Story("Comment Validation and Error Handling")
public class CreateCommentInvalidTests extends Reusables {

String userResource = "/public/v2/users";
String commentResource = "/public/v2/comments";
private int userId;
private int postId;
private String name;
private String email;

// Creating a user before running the tests
@BeforeClass
@Step("Setup: Create user for comment testing")
public void createUserTest() {

    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("Mark Robinson");
    createUserReqBody.setGender("male");
    createUserReqBody.setEmail("MarkRob12@gmail.com");
    createUserReqBody.setStatus("active");


    CreateUserResponse resp = given().spec(Reusables.reqSpec)
            .body(createUserReqBody)
            .when()
            .post(userResource)
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
    name = resp.getName();
    email = resp.getEmail();
}

// Creating a post after creating the user
@Test(description = "Create a post for comment testing")
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

// Creating a comment with invalid post ID
@Test(dependsOnMethods = "createPostTest", description = "Test creating comment with blank post ID")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a comment with blank post ID returns validation error")
@Step("Create comment with blank post ID")
public void createCommentWithBlankPostId() {

    CreateCommentRequest createCommentRequest = new CreateCommentRequest();
    createCommentRequest.setPost_id(null);
    createCommentRequest.setName(name);
    createCommentRequest.setEmail(email);
    createCommentRequest.setBody("This is a comment with invalid post ID");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createCommentRequest)
            .when()
            .post(commentResource)
            .then()
            .log()
            .all()
            .statusCode(422)
            .body("[0].field", equalTo("post"))
            .body("[0].message", equalTo("must exist"))
            .body("[1].field", equalTo("post_id"))
            .body("[1].message", equalTo("is not a number"));
}

@Test(dependsOnMethods = "createPostTest", description = "Test creating comment with blank name")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a comment with blank name returns validation error")
@Step("Create comment with blank name")
public void createCommentWithBlankName() {

    CreateCommentRequest createCommentRequest = new CreateCommentRequest();
    createCommentRequest.setPost_id(postId);
    createCommentRequest.setName("");
    createCommentRequest.setEmail(email);
    createCommentRequest.setBody("This is a comment in the created post");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createCommentRequest)
            .when()
            .post(commentResource)
            .then()
            .log()
            .all()
            .statusCode(422)
            .body("field", hasItem("name"))
            .body("message", hasItem("can't be blank"));
}

@Test(dependsOnMethods = "createPostTest", description = "Test creating comment with blank email")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a comment with blank email returns validation error")
@Step("Create comment with blank email")
public void createCommentWithBlankEmail() {

    CreateCommentRequest createCommentRequest = new CreateCommentRequest();
    createCommentRequest.setPost_id(postId);
    createCommentRequest.setName(name);
    createCommentRequest.setEmail("");
    createCommentRequest.setBody("This is a comment in the created post");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createCommentRequest)
            .when()
            .post(commentResource)
            .then()
            .log()
            .all()
            .statusCode(422)
            .body("field", hasItem("email"))
            .body("message", hasItem("can't be blank, is invalid"));
}

@Test(dependsOnMethods = "createPostTest", description = "Test creating comment with blank body")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a comment with blank body returns validation error")
@Step("Create comment with blank body")
public void createCommentWithBlankBody() {

    CreateCommentRequest createCommentRequest = new CreateCommentRequest();
    createCommentRequest.setPost_id(postId);
    createCommentRequest.setName(name);
    createCommentRequest.setEmail(email);
    createCommentRequest.setBody("");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createCommentRequest)
            .when()
            .post(commentResource)
            .then()
            .log()
            .all()
            .statusCode(422)
            .body("field", hasItem("body"))
            .body("message", hasItem("can't be blank"));
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
