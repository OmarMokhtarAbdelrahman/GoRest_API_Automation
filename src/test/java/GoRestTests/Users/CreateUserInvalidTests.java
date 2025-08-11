package GoRestTests.Users;

import GoRestMain.POJO.Deserialization.Users.CreateUserResponse;
import GoRestMain.POJO.Serialization.Users.CreateUserRequest;
import GoRestMain.Reusables.Reusables;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@Epic("GoRest API Testing")
@Feature("User Management")
@Story("User Validation and Error Handling")
public class CreateUserInvalidTests extends Reusables {

private int userId;


// Creating a user with blank name field in the request body
@Test(description = "Test creating user with blank name field")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a user with blank name returns validation error")
@Step("Create user with blank name")
public void createUserWithBlankNameTest() {
    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("");
    createUserReqBody.setEmail("ahmed1234@gmail.com");
    createUserReqBody.setGender("male");
    createUserReqBody.setStatus("active");

    given().log()
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
            .statusCode(422)
            .body("field", hasItem("name"))
            .body("message", hasItem("can't be blank"));
}

// Creating a user with blank email field in the request body
@Test(description = "Test creating user with blank email field")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a user with blank email returns validation error")
@Step("Create user with blank email")
public void createUserWithBlankEmailTest() {
    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("mohamed khan");
    createUserReqBody.setEmail("");
    createUserReqBody.setGender("male");
    createUserReqBody.setStatus("active");

    given().log()
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
            .statusCode(422)
            .body("field", hasItem("email"))
            .body("message", hasItem("can't be blank"));
}

// Creating a user with blank gender field in the request body
@Test(description = "Test creating user with blank gender field")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a user with blank gender returns validation error")
@Step("Create user with blank gender")
public void createUserWithBlankGenderTest() {
    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("mohamed khan");
    createUserReqBody.setEmail("ahmed1234@gmail.com");
    createUserReqBody.setGender("");
    createUserReqBody.setStatus("active");

    given().log()
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
            .statusCode(422)
            .body("field", hasItem("gender"))
            .body("message", hasItem("can't be blank, can be male of female"));
}

// Creating a user with blank status field in the request body
@Test(description = "Test creating user with blank status field")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a user with blank status returns validation error")
@Step("Create user with blank status")
public void createUserWithBlankStatusTest() {
    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("mohamed khan");
    createUserReqBody.setEmail("ahmed1234@gmail.com");
    createUserReqBody.setGender("male");
    createUserReqBody.setStatus("");

    given().log()
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
            .statusCode(422)
            .body("field", hasItem("status"))
            .body("message", hasItem("can't be blank"));
}

// Creating a user with an email that is already used
@Test(description = "Test creating user with duplicate email")
@Severity(SeverityLevel.BLOCKER)
@Description("Verify that creating a user with duplicate email returns validation error")
@Step("Create user with duplicate email")
public void createUserWithUsedEmailTest() {
    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("mohamed khan");
    createUserReqBody.setEmail("ahmed1234@gmail.com");
    createUserReqBody.setGender("male");
    createUserReqBody.setStatus("active");

    // First create a user with the email
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
            .extract()
            .response()
            .as(CreateUserResponse.class);

    userId = resp.getId();

    // Attempt to create another user with the same email
    given().log()
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
            .statusCode(422)
            .body("field", hasItem("email"))
            .body("message", hasItem("has already been taken"));

    // Clean up by deleting the user created for this test
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
            .statusCode(204);
}

// Creating a user with no authentication
@Test(description = "Test creating user without authentication")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that creating a user without authentication returns 401 error")
@Step("Create user without authentication")
public void createUserWithNoAuthTest() {

    CreateUserRequest createUserReqBody = new CreateUserRequest();
    createUserReqBody.setName("mohamed khan");
    createUserReqBody.setEmail("ahmed1234@gmail.com");
    createUserReqBody.setGender("male");
    createUserReqBody.setStatus("active");

    given().log()
            .all()
            .spec(Reusables.reqSpecNoAuth)
            .body(createUserReqBody)
            .when()
            .log()
            .all()
            .post("/public/v2/users")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(401)
            .body("message", equalTo("Authentication failed"));
}
}
