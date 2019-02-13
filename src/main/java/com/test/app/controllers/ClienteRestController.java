package com.test.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.app.models.entity.Cliente;
import com.test.app.models.service.IClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/listar")
	//@ResponseBody se quita el responsebody por que la clase esta anotada con restcontroller que anota a su vez con responsebody // se puede anotar ahi o despues del public
	@Secured("ROLE_ADMIN")
	public List<Cliente> listar() {
		return clienteService.findAll();
	}

}
