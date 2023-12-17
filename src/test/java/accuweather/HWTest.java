package accuweather;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import accuweather.weather.Weather;

public class HWTest extends AccuweatherAbstractTest {

    @Test
    void testGetResponseForecastsOneDays() {
        Weather weather = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when().get(getBaseUrl() + "/forecasts/v1/daily/5day/{locationKey}")
                .then().statusCode(200).time(lessThan(2000L))
                .extract().response().body().as(Weather.class);
        Assertions.assertEquals(5, weather.getDailyForecasts().size());
        System.out.println(weather);
    }

    @Test
    void testGetResponseForecastsTenDays() {
        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");
        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");
        Assertions.assertAll(() -> Assertions.assertEquals("Unauthorized", code),
                () -> Assertions.assertEquals("Api Authorization failed", message));
    }

    @Test
    void testGetResponseForecastsFifteenDays() {

        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");
        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");
        Assertions.assertAll(() -> Assertions.assertEquals("Unauthorized", code),
                () -> Assertions.assertEquals("Api Authorization failed", message));
    }

    @Test
    void testResponseDateAutocompleteSearch() {
        JsonPath response = given().queryParam("apikey", getApiKey()).queryParam("q", "Moscow")
                .when().request(Method.GET,getBaseUrl() + "/locations/v1/cities/autocomplete")
                .body().jsonPath();
        Assertions.assertAll(() -> Assertions.assertEquals("Moscow", response.get("[0].LocalizedName")),
                () -> Assertions.assertEquals("294021", response.get("[0].Key")));
    }
}
