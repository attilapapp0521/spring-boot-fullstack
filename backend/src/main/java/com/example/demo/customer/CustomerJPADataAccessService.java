package com.example.demo.customer;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {
	private final CustomerRepository customerRepository;

	public CustomerJPADataAccessService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> selectAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> selectCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public void insertCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		return customerRepository.existsCustomerByEmail(email);
	}

	@Override
	public void deleteCustomer(Customer customer) {
		customerRepository.delete(customer);
	}

	@Override
	public boolean existsPersonWithId(Long customerId) {
		return customerRepository.existsCustomerById(customerId);
	}

	@Override
	public void deleteCustomerById(Long customerId) {
		customerRepository.deleteById(customerId);
	}

	@Override
	public void updateCustomer(Customer update) {
		customerRepository.save(update);
	}

}
