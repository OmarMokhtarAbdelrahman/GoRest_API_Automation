package GoRestTests.Todo;

import GoRestMain.POJO.Deserialization.Todo.CreateTodoResponse;
import GoRestMain.POJO.Deserialization.Users.CreateUserResponse;
import GoRestMain.POJO.Serialization.Todo.CreateTodoRequest;
import GoRestMain.POJO.Serialization.Users.CreateUserRequest;
import GoRestMain.Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("GoRest API Testing")
@Feature("Todo Management")
@Story("Todo CRUD Operations")
public class CreateTodoValidTests extends Reusables {

private int userId;
private int postId;
private String name;
private String email;


@Test(description = "Create a user for todo testing")
@Severity(SeverityLevel.CRITICAL)
@Description("Create a user that will be used for creating todos")
@Step("Create user for todo testing")
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
            .response()
            .as(CreateUserResponse.class);

    userId = resp.getId();
    name = resp.getName();
    email = resp.getEmail();

    //8058297
}

@Test(dependsOnMethods = "createUserTest", description = "Create first todo with completed status")
@Severity(SeverityLevel.CRITICAL)
@Description("Create first todo with completed status and verify response")
@Step("Create first todo with completed status")
public void create1stTodoTest() {

    CreateTodoRequest createTodoReqBody = new CreateTodoRequest();
    createTodoReqBody.setTitle("This todo must be completed");
    createTodoReqBody.setDue_on("2025-08-14");
    createTodoReqBody.setStatus("completed");

    given().log().all().spec(Reusables.reqSpec)
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

}

@Test(dependsOnMethods = "create1stTodoTest", description = "Create second todo with pending status")
@Severity(SeverityLevel.CRITICAL)
@Description("Create second todo with pending status and verify response")
@Step("Create second todo with pending status")
public void create2ndTodoTest() {

    CreateTodoRequest createTodoReqBody = new CreateTodoRequest();
    createTodoReqBody.setTitle("This todo must be pending");
    createTodoReqBody.setDue_on("2025-08-24");
    createTodoReqBody.setStatus("pending");

    given().log().all().spec(Reusables.reqSpec)
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

}

@Test(dependsOnMethods = "create2ndTodoTest", description = "Get all todos for the user")
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

@Test(dependsOnMethods = "deleteUserTest", description = "Verify todos are not accessible after user deletion")
@Severity(SeverityLevel.NORMAL)
@Description("Verify that todos are not accessible after user deletion")
@Step("Verify todos not accessible after user deletion")
public void getTodoAfterDeleteUserTest(){

    given().log().all().spec(Reusables.reqSpec)
            .when().log().all()
            .get("/public/v2/users/"+userId+"/todos")
            .then().log().all()
            .assertThat()
            .statusCode(200)
            .body(containsString("[]"));
}

}
