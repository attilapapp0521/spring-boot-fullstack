package com.example.demo.customer;

public record CustomerUpdateRequest(
		String name,
		String email,
		Integer age
) {
}
