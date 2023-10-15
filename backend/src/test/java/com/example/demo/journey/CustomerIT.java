package com.example.demo.journey;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRegistrationRequest;
import com.example.demo.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {

	private static final Random RANDOM = new Random();

	private static final String CUSTOMER_URI = "/api/v1/customers";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void canRegisterACustomer() {
		//create registration request
		Faker faker = new Faker();
		Name fakerName = faker.name();
		String name = fakerName.fullName();
		String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
		int age = RANDOM.nextInt(1, 100);

		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
				name, email, age
		);
		//send a post request
		WebTestClient.ResponseSpec ok = webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isOk();
		//get all customers
		List<Customer> allCustomers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<Customer>() {
				})
				.returnResult()
				.getResponseBody();

		//make sure that customer is present
		Customer expectedCustomer = new Customer(
				name, email, age
		);

		assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
				.contains(expectedCustomer);
		long id = allCustomers.stream()
				.filter(customer -> customer.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		expectedCustomer.setId(id);

		//get customer by id
		webTestClient.get()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(new ParameterizedTypeReference<Customer>() {
				})
				.isEqualTo(expectedCustomer);

	}

	@Test
	void canDeleteCustomer() {
		//create registration request
		Faker faker = new Faker();
		Name fakerName = faker.name();
		String name = fakerName.fullName();
		String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
		int age = RANDOM.nextInt(1, 100);

		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
				name, email, age
		);
		//send a post request
		WebTestClient.ResponseSpec ok = webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isOk();
		//get all customers
		List<Customer> allCustomers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<Customer>() {
				})
				.returnResult()
				.getResponseBody();

		//make sure that customer is present

		long id = allCustomers.stream()
				.filter(customer -> customer.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//delete customer

		webTestClient.delete()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();

		//get customer by id
		webTestClient.get()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNotFound();

	}

	@Test
	void canUpdateCustomer() {
		//create registration request
		Faker faker = new Faker();
		Name fakerName = faker.name();

		String name = fakerName.fullName();
		String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
		int age = RANDOM.nextInt(1, 100);

		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
				name, email, age
		);
		//send a post request
		webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isOk();
		//get all customers
		List<Customer> allCustomers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<Customer>() {
				})
				.returnResult()
				.getResponseBody();

		long id = allCustomers.stream()
				.filter(customer -> customer.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//update customer
		String newName ="Ali";
		CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
				newName, null, null
		);

		 webTestClient.put()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(updateRequest), CustomerUpdateRequest.class)
				.exchange()
				.expectStatus()
				.isOk();

		//get customer by id
		Customer updateCustomer = webTestClient.get()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Customer.class)
				.returnResult()
				.getResponseBody();

		Customer expected = new Customer(
				id, newName, email, age
		);

		assertThat(updateCustomer).isEqualTo(expected);
	}
}