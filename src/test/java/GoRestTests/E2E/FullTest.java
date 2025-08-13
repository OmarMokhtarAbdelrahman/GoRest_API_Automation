package GoRestTests.E2E;

import GoRestMain.POJO.Deserialization.Posts.CreatePostResponse;
import GoRestMain.POJO.Deserialization.Todo.CreateTodoResponse;
import GoRestMain.POJO.Deserialization.Users.CreateUserResponse;
import GoRestMain.POJO.Serialization.Comment.CreateCommentRequest;
import GoRestMain.POJO.Serialization.Posts.CreatePostRequest;
import GoRestMain.POJO.Serialization.Todo.CreateTodoRequest;
import GoRestMain.POJO.Serialization.Users.CreateUserRequest;
import GoRestMain.Reusables.Reusables;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;


@Epic("GoRest API Testing")
@Feature("Full Website")
@Story("E2E Scenario")
public class FullTest extends Reusables{

    private static final Logger logger = LoggerFactory.getLogger(FullTest.class);
    private int userId;
    private int postId;
    private String name;
    private String email;
    private int commentId;
    private int todo_id;

    @Test(description = "Create a new user with valid data")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Create a new user and verify the response contains all required fields")
    @Step("Create user with valid data")
    public void createUserTest() {

        logger.info("Starting createUserTest");

        CreateUserRequest createUserReqBody = new CreateUserRequest();
        createUserReqBody.setName("Mark Robinson");
        createUserReqBody.setGender("male");
        createUserReqBody.setEmail("markRob12345678@gmail.com");
        createUserReqBody.setStatus("active");

        logger.info("Creating user with name: {}, email: {}, gender: {}, status: {}",
                createUserReqBody.getName(), createUserReqBody.getEmail(),
                createUserReqBody.getGender(), createUserReqBody.getStatus());

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

        logger.info("User created successfully with ID: {}", userId);
    }

    @Test(dependsOnMethods = "createUserTest", description = "Create a post for the user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a post for the user and verify response contains all required fields")
    @Step("Create post for user")
    public void createPostTest() {


        CreatePostRequest createPostReqBody = new CreatePostRequest();
        createPostReqBody.setTitle("This is a Post title");
        createPostReqBody.setBody("This body means post was created successfully");

        logger.info("Starting createPostTest");

       CreatePostResponse resp = given().log()
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

        postId = resp.getId();

        logger.info("Post created successfully with ID: {}", postId);
    }

    // Creating comments for the post created successfully
    @Test(dependsOnMethods = "createPostTest", description = "Create first comment for the post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create first comment for the post and verify response")
    @Step("Create first comment")
    public void createCommentTest() {

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

    @Test(dependsOnMethods = "createCommentTest", description = "Create first todo with completed status")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create first todo with completed status and verify response")
    @Step("Create first todo with completed status")
    public void createTodoTest() {

        CreateTodoRequest createTodoReqBody = new CreateTodoRequest();
        createTodoReqBody.setTitle("This todo must be completed");
        createTodoReqBody.setDue_on("2025-08-14");
        createTodoReqBody.setStatus("completed");

        CreateTodoResponse resp = given().log().all().spec(Reusables.reqSpec)
                .body(createTodoReqBody)
                .when().log().all()
                .post("/public/v2/users/"+userId+"/todos")
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body(containsString("id"))
                .body(containsString("user_id"))
                .body(containsString("title"))
                .body(containsString("due_on"))
                .body(containsString("status"))
                .extract()
                .as(CreateTodoResponse.class);

        todo_id = resp.getId();
        logger.info("Todo created successfully with ID: {}", todo_id);
    }

    @Test(dependsOnMethods = "createTodoTest", description = "Get all posts for the user")
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

    @Test(dependsOnMethods = "getUserPostsTest", description = "Get all comments for the post")
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
                .body("post_id", hasItems(postId))
                .body("name", hasItems(name))
                .body("email", hasItems(email))
                .body(containsString("body"))
                .body(containsString("id"));
    }

    @Test(dependsOnMethods = "getUserCommentsTest", description = "Get all todos for the user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Retrieve all todos for the user and verify response")
    @Step("Get user todos")
    public void getTodoTest(){

        given().log().all().spec(Reusables.reqSpec)
                .when().log().all()
                .get("/public/v2/users/"+userId+"/todos")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body(containsString("id"))
                .body(containsString("user_id"))
                .body(containsString("title"))
                .body(containsString("due_on"))
                .body(containsString("status"));
    }

    @Test(dependsOnMethods = "getTodoTest", description = "Delete the user")
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
