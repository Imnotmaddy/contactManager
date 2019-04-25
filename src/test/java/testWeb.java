import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.equalTo;

import static io.restassured.RestAssured.get;

public class testWeb {

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void test1() {

        get("/contactManager?command=editContact&contactId=170")
                .then()
                .body("contactId", equalTo(170))
                .body("sex", equalTo("Male"));
    }

}
