package com.test.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.test.app.models.entity.Factura;

public interface IFacturaDAO extends CrudRepository<Factura, Long>{

	
}
