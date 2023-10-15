package com.example.demo.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

	private CustomerJPADataAccessService underTest;
	private AutoCloseable autoCloseable;

	@Mock
	private CustomerRepository customerRepository;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new CustomerJPADataAccessService(customerRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void selectAllCustomers() {
		//When
		underTest.selectAllCustomers();

		//Then
		verify(customerRepository)
				.findAll();
	}

	@Test
	void selectCustomerById() {
		//Given
		long id = 1;

		//When
		underTest.selectCustomerById(id);

		//Then
		verify(customerRepository).findById(id);
	}

	@Test
	void insertCustomer() {
		//Given
		Customer customer = new Customer(
				1L, "Ali", "ali@gmail.com", 22
		);

		//When
		underTest.insertCustomer(customer);

		//Then
		verify(customerRepository).save(customer);
	}

	@Test
	void existsPersonWithEmail() {
		//Given
		var email = "foo@gmail.com";

		//When
		underTest.existsPersonWithEmail(email);

		//Then
		verify(customerRepository).existsCustomerByEmail(email);
	}

	@Test
	void existsPersonWithId() {
		//Given
		long id = 2;

		//When
		underTest.existsPersonWithId(id);

		//Then
		verify(customerRepository).existsCustomerById(id);
	}

	@Test
	void deleteCustomerById() {
		//Given
		long id = 2;

		//When
		underTest.deleteCustomerById(id);

		//Then
		verify(customerRepository).deleteById(id);
	}

	@Test
	void updateCustomer() {
		//Given
		Customer customer = new Customer(
				1L, "Ali", "ali@gmail.com", 22
		);

		//When
		underTest.updateCustomer(customer);

		//Then
		verify(customerRepository).save(customer);
	}
}