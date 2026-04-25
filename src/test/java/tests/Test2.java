package tests;

import methods.Requests;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.*;

public class Test2 extends Requests {

    @Test
    @Order(2)
    @DisplayName("Сценарий 2: Проверить наличие и добавить отзыв")
    void stockAndReviewTest() {
        int bookId = 2;

        //Проверка наличия книги
        Response stockResponse = getBookStock(bookId);
        assertEquals(200, stockResponse.statusCode());
        assertTrue(stockResponse.jsonPath().getBoolean("inStock"));
        int currentStock = stockResponse.jsonPath().getInt("stock");

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
}