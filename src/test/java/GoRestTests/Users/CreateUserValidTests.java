package GoRestTests.Users;

import GoRestMain.POJO.Deserialization.Users.CreateUserResponse;
import GoRestMain.POJO.Serialization.Users.CreateUserRequest;
import GoRestMain.POJO.Serialization.Users.PatchUserRequest;
import GoRestMain.Reusables.Reusables;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("GoRest API Testing")
@Feature("User Management")
@Story("User CRUD Operations")
public class CreateUserValidTests extends Reusables {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserValidTests.class);
    private int userId;

    // Creating a user before running the tests
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
        logger.info("User created successfully with ID: {}", userId);
    }

    // Updating the user status to active using PUT method
    @Test(dependsOnMethods = "createUserTest", description = "Update user status using PUT method")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Update user information using PUT method and verify response")
    @Step("Update user status to inactive")
    public void updateUserStatusPutTest() {

        logger.info("Starting updateUserStatusPutTest for user ID: {}", userId);

        CreateUserRequest createUserReqBody = new CreateUserRequest();
        createUserReqBody.setName("Pablo Robinson");
        createUserReqBody.setGender("male");
        createUserReqBody.setEmail("markRob12345678@gmail.com");
        createUserReqBody.setStatus("inactive");

        logger.info("Updating user with new name: {}, status: {}", 
                   createUserReqBody.getName(), createUserReqBody.getStatus());

        given().log()
                .all()
                .spec(Reusables.reqSpec)
                .body(createUserReqBody)
                .when()
                .log()
                .all()
                .put("/public/v2/users/" + userId)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body(containsString("id"))
                .body(containsString("name"))
                .body(containsString("email"))
                .body(containsString("gender"))
                .body(containsString("status"))
                .extract()
                .as(CreateUserResponse.class);
        
        logger.info("User updated successfully");
    }

    // Updating the username using PATCH method
    @Test(dependsOnMethods = "updateUserStatusPutTest", description = "Update user name using PATCH method")
    @Severity(SeverityLevel.NORMAL)
    @Description("Update user name using PATCH method and verify response")
    @Step("Update user name using PATCH")
    public void updateUserPatchTest() {

        logger.info("Starting updateUserPatchTest for user ID: {}", userId);

        PatchUserRequest patchUserReq = new PatchUserRequest();
        patchUserReq.setName("Mark Robinson");

        logger.info("Patching user name to: {}", "Mark Robinson");

        given().log()
                .all()
                .spec(Reusables.reqSpec)
                .body(patchUserReq)
                .when()
                .log()
                .all()
                .patch("/public/v2/users/" + userId)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body(containsString("id"))
                .body(containsString("name"))
                .body(containsString("email"))
                .body(containsString("gender"))
                .body(containsString("status"))
                .extract()
                .as(CreateUserResponse.class);

        logger.info("User patched successfully");
    }

    // Retrieving the user final details using GET method
    @Test(dependsOnMethods = "updateUserPatchTest", description = "Get user details by ID")
    @Severity(SeverityLevel.MINOR)
    @Description("Retrieve user details by ID and verify response")
    @Step("Get user details by ID")
    public void getUserTest() {

        logger.info("Starting getUserTest for user ID: {}", userId);

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
                .statusCode(200)
                .body(containsString("id"))
                .body(containsString("name"))
                .body(containsString("email"))
                .body(containsString("gender"))
                .body(containsString("status"));

        logger.info("User details retrieved successfully");
    }

    // Retrieving all users using GET method
    @Test(dependsOnMethods = "getUserTest", description = "Get all users with pagination")
    @Severity(SeverityLevel.MINOR)
    @Description("Retrieve all users with pagination and verify response")
    @Step("Get all users with page parameter")
    public void getAllUserTest() {

        logger.info("Starting getAllUserTest with pagination");

        given().log()
                .all()
                .spec(Reusables.reqSpec)
                .queryParam("page", 2)
                .when()
                .log()
                .all()
                .get("/public/v2/users")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body(containsString("id"))
                .body(containsString("name"))
                .body(containsString("email"))
                .body(containsString("gender"))
                .body(containsString("status"));
        
        logger.info("All users retrieved successfully");
    }

    // Deleting the user created in the beginning
    @Test(dependsOnMethods = "getAllUserTest", description = "Delete user by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete user by ID and verify successful deletion")
    @Step("Delete user by ID")
    public void deleteUserTest() {

        logger.info("Starting deleteUserTest for user ID: {}", userId);

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

        logger.info("User deleted successfully");
    }
}
