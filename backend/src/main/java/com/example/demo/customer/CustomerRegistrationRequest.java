package com.example.demo.customer;

public record CustomerRegistrationRequest(
		String name,
		String email,
		Integer age
) {
}
