import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.RedirectConfig;

import static io.restassured.config.RedirectConfig.*;
import static org.junit.Assert.*;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserStoriesIT {

    @Before
    public void setup() {
        reset();
        config = config().redirect(redirectConfig().followRedirects(false));
        port = 8080;
    }

    @Test
    public void user_logs_in() {
        Response response = given().log().all()
                .when().get("/user");

        response.then().statusCode(equalTo(302));
        response.then().cookie("JSESSIONID", notNullValue());
        response.then().header("Location", notNullValue());

        Cookie jSessionID = response.getDetailedCookie("JSESSIONID");

        response = given().log().all().cookie(jSessionID)
                .when().get(response.getHeader("Location"));  // redirection to /login

        String csrfToken = response.then().contentType(ContentType.HTML)
                .extract().htmlPath().getString("html.body.div.form.input.@value");

        response = given().log().all()
                .cookie(jSessionID)
                .formParam("username", "nelson.harvey@example.com")
                .formParam("password", "tinkerbe459")
                .formParam("remember-me", false)
                .formParam("_csrf", csrfToken)
                .when().post("/login");

        response.then().statusCode(equalTo(302));
        response.then().header("Location", notNullValue());
        jSessionID = response.getDetailedCookie("JSESSIONID");

        response =  given().log().all().cookie(jSessionID)
                .when().get(response.getHeader("Location")); // redirection to user

        response.then().statusCode(200);
        response.then().body(notNullValue());
        response.then().contentType(ContentType.JSON);

        assertEquals("nharvey", response.then().extract().jsonPath().getString("username"));
        assertEquals("nelson.harvey@example.com", response.then().extract().jsonPath().getString("mail"));
    }
}