package com.test.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.app.models.entity.Cliente;
import com.test.app.models.entity.Factura;
import com.test.app.models.entity.ItemFactura;
import com.test.app.models.entity.Producto;
import com.test.app.models.service.IClienteService;

//todo lo que esta aqui solo lo puede ver el de ROL role admin
@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Factura factura = clienteService.findFacturaById(id);

		if (factura == null) {
			flash.addFlashAttribute("error", "Factura no encontrada");
			return "redirect:/listar";
		}

		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));

		return "factura/ver";
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable("clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {

		Cliente cliente = clienteService.FindById(clienteId);
		if (cliente == null) {
			flash.addFlashAttribute("error", "Cliente no existe");
			return "redirect:/listar ";
		}

		Factura factura = new Factura();

		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "Crear Factura");

		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}

		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "Error: La factura NO puede no tener líneas!");
			return "factura/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);

			ItemFactura item = new ItemFactura();
			item.setCantidad(cantidad[i]);
			item.setProducto(producto);

			factura.addItemFactura(item);
		}

		clienteService.saveFactura(factura);
		status.setComplete();

		flash.addFlashAttribute("success", "Factura creada");
		return "redirect:/ver/" + factura.getCliente().getId() + "/";

	}
	
	@GetMapping("eliminar/{id}")
	public String eliminar(
		@PathVariable(value="id") Long id,
		RedirectAttributes flash
		) {
		
		Factura factura = clienteService.findFacturaById(id);
		if(factura != null) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Factura eliminada exitosamente");
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error","Factura no encontrada");
		return"redirect:/listar";
	}

}
