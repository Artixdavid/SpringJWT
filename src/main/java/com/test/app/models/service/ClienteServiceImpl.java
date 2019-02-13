package com.test.app.models.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.test.app.models.dao.IClienteDAO;
import com.test.app.models.dao.IFacturaDAO;
import com.test.app.models.dao.IProductoDAO;
import com.test.app.models.entity.Cliente;
import com.test.app.models.entity.Factura;
import com.test.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDAO clienteDao;

	@Autowired
	private IProductoDAO productoDAO;
	
	@Autowired
	private IFacturaDAO facturaDAO;

	@Transactional
	@Override
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional
	@Override
	public void create(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Transactional
	@Override
	public Cliente FindById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> findByNombre(String term) {
		return productoDAO.findByNombreContainingIgnoreCase(term);
	}

	@Override
	public void saveFactura(Factura factura) {
		this.facturaDAO.save(factura);
		
	}

	@Transactional
	@Override
	public Producto findProductoById(Long id) {
		return productoDAO.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public Factura findFacturaById(Long id) {
		return facturaDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDAO.deleteById(id);
	}

}
