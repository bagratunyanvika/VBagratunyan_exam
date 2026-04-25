package methods;

import config.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Requests extends ApiConfig {

    public Response getAllBooks() {
        return given()
                .when()
                .get("/books")
                .then()
                .extract()
                .response();
    }

    public Response getBooksWithFilters(String genre, String author, Integer minPrice, Integer maxPrice, Integer page, Integer size) {
        return given()
                .queryParam("genre", genre)
                .queryParam("author", author)
                .queryParam("minPrice", minPrice)
                .queryParam("maxPrice", maxPrice)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/books")
                .then()
                .extract()
                .response();
    }

    public Response getBookById(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .get("/books/{id}")
                .then()
                .extract()
                .response();
    }

    public Response getBookByIsbn(String isbn) {
        return given()
                .pathParam("isbn", isbn)
                .when()
                .get("/books/isbn/{isbn}")
                .then()
                .extract()
                .response();
    }

    public Response getBookStock(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .get("/books/{id}/stock")
                .then()
                .extract()
                .response();
    }

    public Response getReviews(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .get("/books/{id}/reviews")
                .then()
                .extract()
                .response();
    }

    public Response createBook(String jsonBody) {
        return given()
                .header("X-API-Key", API_KEY)
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/books")
                .then()
                .extract()
                .response();
    }

    public Response createBookWithoutAuth(String jsonBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/books")
                .then()
                .extract()
                .response();
    }

    public Response addReview(int bookId, String jsonBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .pathParam("id", bookId)
                .when()
                .post("/books/{id}/reviews")
                .then()
                .extract()
                .response();
    }

    public Response updateBook(int id, String jsonBody) {
        return given()
                .header("X-API-Key", API_KEY)
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(jsonBody)
                .when()
                .put("/books/{id}")
                .then()
                .extract()
                .response();
    }

    public Response patchBook(int id, String jsonBody) {
        return given()
                .header("X-API-Key", API_KEY)
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(jsonBody)
                .when()
                .patch("/books/{id}")
                .then()
                .extract()
                .response();
    }

    public Response deleteBook(int id) {
        return given()
                .header("X-API-Key", API_KEY)
                .pathParam("id", id)
                .when()
                .delete("/books/{id}")
                .then()
                .extract()
                .response();
    }
}
