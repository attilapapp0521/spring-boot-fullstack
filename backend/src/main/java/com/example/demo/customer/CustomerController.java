package com.example.demo.customer;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	//	@RequestMapping(path = "api/v1/customer", method = RequestMethod.GET)
	@GetMapping
	public List<Customer> getCustomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping("{customerId}")
	public Customer getCustomer(@PathVariable Long customerId) {
		return customerService.getCustomer(customerId);
	}

	@PostMapping()
	public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
		customerService.addCustomer(request);
	}

	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Long customerId){
		customerService.deleteCustomerById(customerId);
	}

	@PutMapping("{customerId}")
	public void updateCustomer(
			@PathVariable("customerId") Long customerId,
			@RequestBody CustomerUpdateRequest customerUpdateRequest
			){
		customerService.updateCustomer(customerId, customerUpdateRequest);
	}

}
