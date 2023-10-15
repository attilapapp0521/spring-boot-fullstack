package com.example.demo.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
	private static final List<Customer> customers;

	static {
		customers = new ArrayList<>();
		Customer alex = new Customer(1L, "Alex", "alex@gmail.com", 21);
		customers.add(alex);
		Customer jamila = new Customer(2L, "Jamila", "jamila@gmail.com", 19);
		customers.add(jamila);
	}

	@Override
	public List<Customer> selectAllCustomers() {
		return customers;
	}

	@Override
	public Optional<Customer> selectCustomerById(Long id) {
		return customers.stream()
				.filter(c -> c.getId().equals(id))
				.findFirst();
	}

	@Override
	public void insertCustomer(Customer customer) {
		customer.setId(customers.size() + 1L);
		customers.add(customer);
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		return customers.stream()
				.anyMatch(c -> c.getEmail().equals(email));
	}

	@Override
	public void deleteCustomer(Customer customer) {
		customers.remove(customer);
	}

	@Override
	public boolean existsPersonWithId(Long customerId) {
		return customers.stream()
				.anyMatch(c -> c.getId().equals(customerId));
	}

	@Override
	public void deleteCustomerById(Long customerId) {
		customers.stream()
				.filter(c -> c.getId().equals(customerId))
				.findFirst()
				.ifPresent(customers::remove);
	}

	@Override
	public void updateCustomer(Customer customer) {
		customers.add(customer);
	}
}
