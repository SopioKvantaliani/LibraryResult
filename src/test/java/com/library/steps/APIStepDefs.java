package com.library.steps;


import com.library.pages.BookPage;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class APIStepDefs {

    RequestSpecification givenPart = given().log().uri();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;
    String actualPathValue;
    String endPoint1;
    BookPage bookPage = new BookPage();

    Map<String, Object> randomData;
    public static Map<String, Object> userResponseMap;

    String authToken;



    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String username) {
        String token = LibraryAPI_Util.getToken(username);
        givenPart.header("x-library-token",token );

    }


    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }


    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endPoint) {
        endPoint1 = endPoint;
        response = givenPart.when().get(endPoint);
    }


    @Then("status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);

    }


    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        jp = response.then().contentType(contentType).extract().jsonPath();
    }

    @Then("Each {string} field should not be null")
    public void each_field_should_not_be_null(String path) {

        List<String> list = jp.getList(path);

        for (String each : list) {

            Assert.assertNotNull(each);
        }

    }

    @Given("Path param {string} is {string}")
    public void path_param_is(String path, String pathParamValue) {
        actualPathValue = pathParamValue;
        givenPart.given().pathParams(path, pathParamValue);
    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String path) {
        String expectedId = jp.getString(path);
        Assert.assertEquals(expectedId, actualPathValue);

    }


    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List <String> dataTable) {
        for (String eachData : dataTable) {
           Assert.assertNotNull( jp.getString(eachData));
        }
    }

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentTypeHeader) {
        givenPart.given().contentType(contentTypeHeader);

    }

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String createData) {

        switch (createData){
            case "book":
                randomData = LibraryAPI_Util.getRandomBookMap();
                break;
            case "user":
                randomData = LibraryAPI_Util.getRandomUserMap();
                break;
            default:
                throw new RuntimeException("Invalid Data" + createData);
        }

        givenPart.given().formParams(randomData);

    }


    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = givenPart.when().post(endpoint).prettyPeek();
    }


    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String message, String messageText) {
        response.then().body(message, is(messageText));

    }


    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        Assert.assertNotNull(jp.get(path));

    }


    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {

        String bookId = jp.getString("book_id");

        //DB Data
        DB_Util.createConnection();
        //modified query
        String query = "select isbn, name, author, year from books where id="+bookId;
        DB_Util.runQuery(query);
        Map<String, Object> dbData = DB_Util.getRowMap(1);

        //full query
        String query2 = "select * from books where id="+bookId;
        DB_Util.runQuery(query2);
        Map<String, Object> dbData2 = DB_Util.getRowMap(1);

        Object bookName = dbData.get("name");

        //UI Data
        Map<String, String> createBookUi = bookPage.findSpecificBook(bookName.toString());
        String category = createBookUi.remove("category");
        System.out.println(createBookUi);
        Assert.assertEquals(dbData, createBookUi);
        createBookUi.put("category", category);

            //API Data
            jp =given()
                .accept(ContentType.JSON)
                .header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParams("id", bookId)
                .when()
                .get("/get_book_by_id/{id}")
                .then().statusCode(200).extract().jsonPath();

        Map<String, Object> responseMap = jp.getMap("$");

        Assert.assertEquals(dbData2, responseMap );

        DB_Util.destroy();
    }

    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        String userId = jp.getString("user_id");
        //API Data
        jp =given()
                .accept(ContentType.JSON)
                .header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParams("id", userId)
                .when()
                .get("/get_user_by_id/{id}")
                .then().statusCode(200).extract().jsonPath();

        userResponseMap = jp.getMap("$");
        System.out.println("API - "+userResponseMap);

        //DB Data
        DB_Util.createConnection();
        String userQuery = "select * from users where id= "+userId;
        DB_Util.runQuery(userQuery);
        Map<String, Object> rowMap = DB_Util.getRowMap(1);
        System.out.println("DB - "+rowMap);

        Assert.assertEquals(userResponseMap, rowMap);


    }

    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {

        authToken = LibraryAPI_Util.getToken(email, password);
        System.out.println("My TOKEN ---> "+authToken);

        if (authToken == null || authToken.isEmpty()) {
            throw new RuntimeException("Failed to retrieve authentication token.");
        }



    }


    @Given("I send {string} information as request body")
    public void i_send_information_as_request_body(String token) {
        givenPart.formParam(token,authToken);
    }
}

