package tests;

import methods.Requests;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.*;

public class BookStoreTest extends Requests {

    @Test
    @Order(1)
    @DisplayName("Сценарий 1: Полная проверка книги")
    void BookTest() {

        //Создание новой книги
        String uniqueIsbn = "978-" + System.currentTimeMillis();
        String createJson = new JSONObject()
                .put("isbn", uniqueIsbn)
                .put("title", "Lifecycle Book")
                .put("author", "Test Author")
                .put("genre", "Test Genre")
                .put("year", 2024)
                .put("price", 500)
                .put("stock", 10)
                .put("pages", 200)
                .toString();

        Response createResponse = createBook(createJson);
        assertEquals(201, createResponse.statusCode());
        int bookId = createResponse.jsonPath().get("id");

        //Получение книги по ID
        Response getResponse = getBookById(bookId);
        assertEquals(200, getResponse.statusCode());
        assertEquals("Lifecycle Book", getResponse.jsonPath().get("title"));

        //Обновление цены
        String patchJson = new JSONObject()
                .put("price", 750)
                .put("stock", 25)
                .toString();

        Response patchResponse = patchBook(bookId, patchJson);
        assertEquals(200, patchResponse.statusCode());
        assertEquals(750, patchResponse.jsonPath().get("price"));

        //Проверка наличия на складе
        Response stockResponse = getBookStock(bookId);
        assertEquals(200, stockResponse.statusCode());
        assertTrue(stockResponse.jsonPath().get("inStock"));
        assertEquals(25, stockResponse.jsonPath().get("stock"));

        //Удаление книги
        Response deleteResponse = deleteBook(bookId);
        assertEquals(204, deleteResponse.statusCode());

        //Проверка на удаление книги
        Response checkResponse = getBookById(bookId);
        assertEquals(404, checkResponse.statusCode());
    }

    @Test
    @Order(2)
    @DisplayName("Сценарий 2: Проверить наличие и добавить отзыв")
    void stockAndReviewTest() {
        int bookId = 1;

        //Проверка наличия книги
        Response stockResponse = getBookStock(bookId);
        assertEquals(200, stockResponse.statusCode());
        assertTrue(stockResponse.jsonPath().get("inStock"));
        int currentStock = stockResponse.jsonPath().get("stock");

        //Добавление отзыва
        String reviewJson = new JSONObject()
                .put("rating", 5)
                .put("comment", "Отличная книга!")
                .put("reviewerName", "Test User")
                .toString();

        Response reviewResponse = addReview(bookId, reviewJson);
        assertEquals(201, reviewResponse.statusCode());
        assertNotNull(reviewResponse.jsonPath().get("reviewId"));
    }

    @Test
    @Order(3)
    @DisplayName("Сценарий 3a: Фильтрация по жанру")
    void filterByGenreTest() {
        Response response = getBooksWithFilters("Classic", null, null, null, null, null);

        assertEquals(200, response.statusCode());

        //Проверка, что все книги в ответе имеют жанр "Classic"
        if (response.jsonPath().getList("books").size() > 0) {
            for (int i = 0; i < response.jsonPath().getList("books").size(); i++) {
                String genre = response.jsonPath().getString("books[" + i + "].genre");
                assertEquals("Classic", genre);
            }
        }
    }

    @Test
    @Order(4)
    @DisplayName("Сценарий 3b: Фильтрация по цене")
    void filterByPriceTest() {
        Response response = getBooksWithFilters(null, null, 400, 600, null, null);

        assertEquals(200, response.statusCode());

        // Проверка, что цены в диапазоне 400-600
        for (int i = 0; i < response.jsonPath().getList("books").size(); i++) {
            int price = response.jsonPath().getInt("books[" + i + "].price");
            assertTrue(price >= 400 && price <= 600);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Сценарий 3c: Пагинация")
    void paginationTest() {
        Response response = getBooksWithFilters(null, null, null, null, 0, 3);

        assertEquals(200, response.statusCode());
        assertEquals(0, response.jsonPath().getInt("page"));
        assertEquals(3, response.jsonPath().getInt("size"));
        assertTrue(response.jsonPath().getList("books").size() <= 3);
    }
}