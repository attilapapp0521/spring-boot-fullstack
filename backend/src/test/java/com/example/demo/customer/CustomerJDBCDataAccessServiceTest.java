package com.example.demo.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.demo.AbstractTestContainers;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

	private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
	private CustomerJDBCDataAccessService underTest;

	@BeforeEach
	void setUp() {
		underTest = new CustomerJDBCDataAccessService(
				getJdbcTemplate(),
				customerRowMapper
		);
	}

	@Test
	void selectAllCustomers() {
		//Given
		Customer customer = new Customer(
				FAKER.name().fullName(),
				FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
				20
		);
		underTest.insertCustomer(customer);

		//When
		List<Customer> actual = underTest.selectAllCustomers();

		//Then
		assertThat(actual).isNotEmpty();
	}

	@Test
	void selectCustomerById() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);

		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//When
		Optional<Customer> actual = underTest.selectCustomerById(id);

		//Then
		assertThat(actual).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}

	@Test
	void willReturnEmptyWhenSelectCustomerById() {
		//Given
		long id = -1;

		//When
		var actual = underTest.selectCustomerById(id);

		//Then
		assertThat(actual).isEmpty();
	}

	@Test
	void existsPersonWithEmail() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				30
		);
		underTest.insertCustomer(customer);
		//When
		boolean isReservedEmail = underTest.existsPersonWithEmail(email);

		//Then
		assertThat(isReservedEmail).isTrue();
	}

	@Test
	void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

		//When
		boolean actual = underTest.existsPersonWithEmail(email);

		//Then
		assertThat(actual).isFalse();
	}

	@Test
	void existsPersonWithId() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//When
		var actual = underTest.existsPersonWithId(id);

		//Then
		assertThat(actual).isTrue();
	}

	@Test
	void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
		//Given
		long id = -1;

		//When
		var actual = underTest.existsPersonWithId(id);

		//Then
		assertThat(actual).isFalse();
	}

	@Test
	void deleteCustomerWithId() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				30
		);
		underTest.insertCustomer(customer);

		long id = underTest.selectAllCustomers()
				.stream()
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//When
		underTest.deleteCustomerById(id);

		//Then
		Optional<Customer> actual = underTest.selectCustomerById(id);
		assertThat(actual).isNotPresent();
	}

	@Test
	void updateCustomerName() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		var newName = "foo";

		//When
		Customer update = new Customer();
		update.setId(id);
		update.setName(newName);

		underTest.updateCustomer(update);

		//Then
		Optional<Customer> actual = underTest.selectCustomerById(id);

		assertThat(actual).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(newName);
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}

	@Test
	void updateCustomerEmail() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

		//When email is changed
		Customer update = new Customer();
		update.setId(id);
		update.setEmail(newEmail);

		underTest.updateCustomer(update);

		//Then
		Optional<Customer> actual = underTest.selectCustomerById(id);

		assertThat(actual).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(newEmail);
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}

	@Test
	void updateCustomerAge() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		var newAge = 100;

		//When
		Customer update = new Customer();
		update.setId(id);
		update.setAge(newAge);

		underTest.updateCustomer(update);

		//Then
		Optional<Customer> actual = underTest.selectCustomerById(id);

		assertThat(actual).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(newAge);
		});
	}

	@Test
	void willUpdateAllPropertiesCustomer() {
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//When
		Customer update = new Customer();
		update.setId(id);
		update.setName("foo");
		update.setEmail(UUID.randomUUID().toString());
		update.setAge(88);

		underTest.updateCustomer(update);

		//Then
		Optional<Customer> actual = underTest.selectCustomerById(id);

		assertThat(actual).isPresent().hasValue(update);

	}

	@Test
	void willNotUpdateWhenNothingToUpdate() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20
		);
		underTest.insertCustomer(customer);
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		//When
		Customer update = new Customer();
		update.setId(id);

		underTest.updateCustomer(update);

		//Then
		Optional<Customer> actual = underTest.selectCustomerById(id);

		assertThat(actual).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}
}