package Reusables;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reusables {

    private static final Logger logger = LoggerFactory.getLogger(Reusables.class);
    private static final String token = "4f1412604a0c27e29fbc666445a9f125b8cb870f2876a935fcef0f9645d7d1fa";

    protected static RequestSpecification reqSpec = new RequestSpecBuilder()
            .setBaseUri("https://gorest.co.in")
            .addHeader("Authorization", "Bearer " + token)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build();

    protected static RequestSpecification reqSpecNoAuth = new RequestSpecBuilder()
            .setBaseUri("https://gorest.co.in")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build();

    static {
        logger.info("Initializing Reusables class");
        logger.info("Base URI: https://gorest.co.in");
        logger.info("Request specifications configured with headers");
    }
}
