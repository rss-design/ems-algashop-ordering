package com.algaworks.algashop.ordering.contract.base;

import com.algaworks.algashop.ordering.contract.base.ShoppingCartBase;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@SuppressWarnings("rawtypes")
public class ShoppingCartTest extends ShoppingCartBase {

	@Test
	public void validate_addShoppingCartItemV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"productId\":\"a1b2c3d4-e5f6-7890-abcd-ef1234567890\",\"quantity\":2}");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17/items");

		// then:
			assertThat(response.statusCode()).isEqualTo(204);
	}

	@Test
	public void validate_createShoppingCartV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"customerId\":\"f5ab7a1e-37da-41e1-892b-a1d38275c2f2\"}");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/api/v1/shopping-carts");

		// then:
			assertThat(response.statusCode()).isEqualTo(201);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).field("['customerId']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).field("['totalItems']").isEqualTo(3);
			assertThatJson(parsedJson).field("['totalAmount']").isEqualTo(1250.00);
			assertThatJson(parsedJson).array("['items']").contains("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).array("['items']").contains("['productId']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).array("['items']").contains("['name']").isEqualTo("Notebook");
			assertThatJson(parsedJson).array("['items']").contains("['price']").isEqualTo(500.00);
			assertThatJson(parsedJson).array("['items']").contains("['quantity']").isEqualTo(2);
			assertThatJson(parsedJson).array("['items']").contains("['totalAmount']").isEqualTo(1000.00);
			assertThatJson(parsedJson).array("['items']").contains("['available']").matches("(true|false)");
			assertThatJson(parsedJson).array("['items']").contains("['name']").isEqualTo("Mouse pad");
			assertThatJson(parsedJson).array("['items']").contains("['price']").isEqualTo(250.00);
			assertThatJson(parsedJson).array("['items']").contains("['quantity']").isEqualTo(1);
			assertThatJson(parsedJson).array("['items']").contains("['totalAmount']").isEqualTo(250.00);
	}

	@Test
	public void validate_deleteAllShoppingCartItemsV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.delete("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17/items");

		// then:
			assertThat(response.statusCode()).isEqualTo(204);
	}

	@Test
	public void validate_deleteShoppingCartByIdV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.delete("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17");

		// then:
			assertThat(response.statusCode()).isEqualTo(204);
	}

	@Test
	public void validate_deleteShoppingCartItemByIdV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.delete("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17/items/a1b2c3d4-e5f6-7890-abcd-ef1234567890");

		// then:
			assertThat(response.statusCode()).isEqualTo(204);
	}

	@Test
	public void validate_findShoppingCartByIdV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.get("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").isEqualTo("ad265aa3-c77d-46e9-9782-b70c487c1e17");
			assertThatJson(parsedJson).field("['customerId']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).field("['totalItems']").isEqualTo(3);
			assertThatJson(parsedJson).field("['totalAmount']").isEqualTo(1250.00);
			assertThatJson(parsedJson).array("['items']").contains("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).array("['items']").contains("['productId']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).array("['items']").contains("['name']").isEqualTo("Notebook");
			assertThatJson(parsedJson).array("['items']").contains("['price']").isEqualTo(500.00);
			assertThatJson(parsedJson).array("['items']").contains("['quantity']").isEqualTo(2);
			assertThatJson(parsedJson).array("['items']").contains("['totalAmount']").isEqualTo(1000.00);
			assertThatJson(parsedJson).array("['items']").contains("['available']").matches("(true|false)");
			assertThatJson(parsedJson).array("['items']").contains("['name']").isEqualTo("Mouse pad");
			assertThatJson(parsedJson).array("['items']").contains("['price']").isEqualTo(250.00);
			assertThatJson(parsedJson).array("['items']").contains("['quantity']").isEqualTo(1);
			assertThatJson(parsedJson).array("['items']").contains("['totalAmount']").isEqualTo(250.00);
	}

	@Test
	public void validate_findShoppingCartByIdV1NotFound() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.get("/api/v1/shopping-carts/e2103964-5353-4910-81ee-212a40a2ca70");

		// then:
			assertThat(response.statusCode()).isEqualTo(404);
			assertThat(response.header("Content-Type")).matches("application/problem\\+json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['instance']").isEqualTo("/api/v1/shopping-carts/e2103964-5353-4910-81ee-212a40a2ca70");
			assertThatJson(parsedJson).field("['type']").isEqualTo("/errors/not-found");
			assertThatJson(parsedJson).field("['title']").isEqualTo("Not found");
	}

	@Test
	public void validate_listShoppingCartItemsByIdV1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.get("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17/items");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).array("['items']").contains("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).array("['items']").contains("['productId']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).array("['items']").contains("['name']").isEqualTo("Notebook");
			assertThatJson(parsedJson).array("['items']").contains("['price']").isEqualTo(500.00);
			assertThatJson(parsedJson).array("['items']").contains("['quantity']").isEqualTo(2);
			assertThatJson(parsedJson).array("['items']").contains("['totalAmount']").isEqualTo(1000.00);
			assertThatJson(parsedJson).array("['items']").contains("['available']").matches("(true|false)");
			assertThatJson(parsedJson).array("['items']").contains("['name']").isEqualTo("Mouse pad");
			assertThatJson(parsedJson).array("['items']").contains("['price']").isEqualTo(250.00);
			assertThatJson(parsedJson).array("['items']").contains("['quantity']").isEqualTo(1);
			assertThatJson(parsedJson).array("['items']").contains("['totalAmount']").isEqualTo(250.00);
	}

}
