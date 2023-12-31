package com.example.demo.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
	List<Customer> selectAllCustomers();

	Optional<Customer> selectCustomerById(Long id);

	void insertCustomer(Customer customer);

	boolean existsPersonWithEmail(String email);

	void deleteCustomer(Customer customer);

	boolean existsPersonWithId(Long customerId);

	void deleteCustomerById(Long customerId);

	void updateCustomer(Customer customer);

}
