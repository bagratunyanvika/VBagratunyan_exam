package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class ApiConfig {

    protected static final String BASE_URL = "http://localhost:8085";
    protected static final String API_KEY = "bookstore-2026-secret";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    protected static String getApiKey() {
        return API_KEY;
    }
}