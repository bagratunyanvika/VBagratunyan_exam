package tests;

import methods.Requests;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.*;

public class Test3 extends Requests {

    @Test
    @Order(3)
    @DisplayName("Сценарий 3a: Фильтрация по жанру")
    void filterByGenreTest() {
        Response response = getBooksWithFilters("Classic", null, null, null, null, null);

        assertEquals(200, response.statusCode());

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

        int page = response.jsonPath().getInt("page");
        int size = response.jsonPath().getInt("size");

        assertEquals(0, page);
        assertEquals(3, size);
        assertTrue(response.jsonPath().getList("books").size() <= 3);
    }
}