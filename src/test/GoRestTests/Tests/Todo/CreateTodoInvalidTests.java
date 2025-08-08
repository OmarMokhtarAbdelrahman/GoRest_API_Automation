package Tests.Todo;

import POJO.Deserialization.Users.CreateUserResponse;
import POJO.Serialization.Todo.CreateTodoRequest;
import POJO.Serialization.Users.CreateUserRequest;
import Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("GoRest API Testing")
@Feature("Todo Management")
@Story("Todo Validation and Error Handling")
public class CreateTodoInvalidTests extends Reusables {

private int userId;

@BeforeClass
@Step("Setup: Create user for todo testing")
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
}

@Test(description = "Test creating todo with blank title")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a todo with blank title returns validation error")
@Step("Create todo with blank title")
public void createTodoWithBlankTitleTest() {

    CreateTodoRequest createTodoReqBody = new CreateTodoRequest();
    createTodoReqBody.setTitle("");
    createTodoReqBody.setDue_on("2025-08-14");
    createTodoReqBody.setStatus("completed");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createTodoReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users/" + userId + "/todos")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(422)
            .body("field",hasItem("title"))
            .body("message",hasItem("can't be blank"));

}

@Test(description = "Test creating todo with blank status")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a todo with blank status returns validation error")
@Step("Create todo with blank status")
public void createTodoWithBlankStatusTest() {

    CreateTodoRequest createTodoReqBody = new CreateTodoRequest();
    createTodoReqBody.setTitle("This todo must be completed");
    createTodoReqBody.setDue_on("2025-08-14");
    createTodoReqBody.setStatus("");

    given().log()
            .all()
            .spec(Reusables.reqSpec)
            .body(createTodoReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users/" + userId + "/todos")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(422)
            .body("field",hasItem("status"))
            .body("message",hasItem("can't be blank, can be pending or completed"));

}

@Test(description = "Test creating todo without authentication")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a todo without authentication returns 401 error")
@Step("Create todo without authentication")
public void createTodoWithNoAuthTest() {

    CreateTodoRequest createTodoReqBody = new CreateTodoRequest();
    createTodoReqBody.setTitle("This todo must be completed");
    createTodoReqBody.setDue_on("2025-08-14");
    createTodoReqBody.setStatus("completed");

    given().log()
            .all()
            .spec(Reusables.reqSpecNoAuth)
            .body(createTodoReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users/" + userId + "/todos")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(401)
            .body("message",equalTo("Authentication failed"));

}

@AfterClass
@Step("Cleanup: Delete test user")
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
