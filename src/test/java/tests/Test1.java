package tests;

import methods.Requests;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.*;

public class Test1 extends Requests {

    @Test
    @Order(1)
    @DisplayName("Сценарий 1: Полная проверка книги")
    void fullBookLifecycleTest() {

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
        int bookId = createResponse.jsonPath().getInt("id");

        //Получение книги по ID
        Response getResponse = getBookById(bookId);
        assertEquals(200, getResponse.statusCode());
        assertEquals("Lifecycle Book", getResponse.jsonPath().getString("title"));

        //Обновление цены (PATCH)
        String patchJson = new JSONObject()
                .put("price", 750)
                .put("stock", 25)
                .toString();

        Response patchResponse = patchBook(bookId, patchJson);
        assertEquals(200, patchResponse.statusCode());
        assertEquals(750, patchResponse.jsonPath().getInt("price"));

        //Проверка наличия на складе
        Response stockResponse = getBookStock(bookId);
        assertEquals(200, stockResponse.statusCode());
        assertTrue(stockResponse.jsonPath().getBoolean("inStock"));
        int stockValue = stockResponse.jsonPath().getInt("stock");
        assertEquals(25, stockValue);

        //Удаление книги
        Response deleteResponse = deleteBook(bookId);
        assertEquals(204, deleteResponse.statusCode());

        //Проверка, что книга действительно удалена
        Response checkResponse = getBookById(bookId);
        assertEquals(404, checkResponse.statusCode());
    }
}