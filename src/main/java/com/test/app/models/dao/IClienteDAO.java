package com.test.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.test.app.models.entity.Cliente;

public interface IClienteDAO extends PagingAndSortingRepository<Cliente, Long> {

}
