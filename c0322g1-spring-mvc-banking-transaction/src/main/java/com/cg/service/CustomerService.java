package com.cg.service;

import com.cg.model.Customer;

import java.util.List;

public interface CustomerService extends IGeneralService<Customer> {

    List<Customer> findByIdIsNot(Long id);
}