package com.quotemedia.qaassignment.denhalych;

import com.quotemedia.qaassignment.denhalych.util.PropertyReader;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

class GetDataTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = String.format("%s%s", PropertyReader.getProperty("restassured.baseurl"), "/api/get-data");
    }

    @Test
    void givenUrlWithMandatoryParams_whenSuccessOnGetsResponse_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .statusCode(200);
    }

    @Test
    void givenUrlWithSymbol_whenResponseWithProvidedSymbol_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body("symbol", equalTo("ac"));
    }

    @Test
    void givenUrlWithSymbolAndDate_whenResponseWithProvidedSymbol_thenCorrect() {
        given().queryParam("symbol", "ad")
                .queryParam("date", "2023-08-15")
                .when().get().then()
                .assertThat().body("symbol", equalTo("ad"));
    }

    @Test
    void whenResponseContainsCorrectName_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body("name", equalTo("some name"));
    }

    @Test
    void whenResponseContainsCorrectLastPrice_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body("lastPrice", equalTo(125.15f));
    }

    @Test
    void whenResponseContainsCorrectChange_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body("change", equalTo(false));
    }

    @Test
    void whenResponseContainsCorrectBid_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body("bid", equalTo(1));
    }

    @Test
    void whenResponseContainsCorrectAsk_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body("ask", equalTo(2));
    }

    @Test
    void givenDateIsNotProvided_whenResponseWithCurrentDate_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .body("date", equalTo(LocalDate.now().toString()));
    }

    @Test
    void givenDateIsProvided_whenResponseWithProvidedDate_thenCorrect() {
        given().queryParam("symbol", "ad")
                .queryParam("date", "2023-08-15")
                .when().get().then()
                .assertThat().body("date", equalTo("2023-08-15"));
    }

    @Test
    void whenJsonResponseConformsToSchemaWithBidAndAsk_thenCorrect() {
        given().queryParam("symbol", "ac")
                .when().get().then()
                .assertThat().body(matchesJsonSchemaInClasspath("JSON_schemas/fullschema.json"));
    }

    @Test
    void whenJsonResponseConformsToSchemaWithNoBid_thenCorrect() {
        given().queryParam("symbol", "w")
                .when().get().then()
                .assertThat().body(matchesJsonSchemaInClasspath("JSON_schemas/nobidschema.json"));
    }

    @Test
    void whenJsonResponseConformsToSchemaWithNoAsk_thenCorrect() {
        given().queryParam("symbol", "l")
                .when().get().then()
                .assertThat().body(matchesJsonSchemaInClasspath("JSON_schemas/noaskschema.json"));
    }

    @Test
    void whenJsonResponseConformsToSchemaWithNoAskAndBid_thenCorrect() {
        given().queryParam("symbol", "ad")
                .queryParam("date", "2023-08-15")
                .when().get().then()
                .assertThat().body(matchesJsonSchemaInClasspath("JSON_schemas/nobidnoaskschema.json"));
    }

    @Test
    void givenNoSymbolParam_whenResponseWithError_thenCorrect() {
        when().get().then()
                .statusCode(400)
                .assertThat().body("Error", equalTo("Please provide mandatory parameter symbol"));
    }

    @Test
    void givenNotExistedSymbol_whenResponseWithError_thenCorrect() {
        given().queryParam("symbol", "ee")
                .when().get().then()
                .statusCode(404)
                .assertThat().body("Error", equalTo("Requested resource not found"));
    }

    @Test
    void givenIncorrectDate_whenResponseWithError_thenCorrect() {
        given().queryParam("symbol", "el")
                .queryParam("date", "2023-dec")
                .when().get().then()
                .statusCode(400)
                .assertThat().body("Error", equalTo("Please provide correct date parameter YYYY-MM-DD"));
    }

}
