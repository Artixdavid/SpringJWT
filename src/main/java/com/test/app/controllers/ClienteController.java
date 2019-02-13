package com.test.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.test.app.models.entity.Cliente;
import com.test.app.models.service.IClienteService;
import com.test.app.models.service.IUploadFileService;
import com.test.app.util.paginator.PageRender;

@Controller
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadService;

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = this.uploadService.laod(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(value = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {
		System.out.println("authentication: " + authentication);
		if (authentication != null) {
			System.out.println("Soy alta lacra: " + authentication.getName());
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			System.out.println("Soy alta lacra forma(SecurityContextHolder) 2: " + auth.getName());
		}

		if (this.hasRole("ROLE_ADMIN")) {
			System.out.println("Lacra tenes permiso: " + auth.getName());
		}

		// PARAMETRO UNO EL REQUEST DEL HTTP
		// segundo parametro un prefijo del rol, puede ser ROLE_ o vacio ""
		// en el if se valida completamente
		SecurityContextHolderAwareRequestWrapper securityContex = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		if (securityContex.isUserInRole("ADMIN")) {
			System.out.println(
					"Lacra tenes permiso forma(SecurityContextHolderAwareRequestWrapper) 2: " + auth.getName());
		} else {
			System.out.println("Lacra tas pegao " + auth.getName());
		}

		if (request.isUserInRole("ROLE_ADMIN")) {
			System.out.println("Lacra tenes permiso forma (HttpServletRequest) 2: " + auth.getName());
		} else {
			System.out.println("Lacra tas pegao " + auth.getName());
		}

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);

		model.addAttribute("titulo", "Welcome to ala Verga.com");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario de Clientes");
		model.put("cliente", cliente);
		model.put("textoBoton", "Crear");
		return "form";
	}

	// @PreAuthorize("hasRole('ROLE_USER')")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping(value = "/ver/{id}/")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.FindById(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "Cliente no valido");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle de cliente: " + cliente.getNombre());
		return "ver";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Registro");
			return "form";
		}

		if (!foto.isEmpty()) {
			// Path directorioRecurso = Paths.get("src//main//resources//static//files");
			// String rootPath =
			// "/Users/davidleones/archivos/uploads";//directorioRecurso.toFile().getAbsolutePath();

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				this.uploadService.delete(cliente.getFoto());

			}

			String uniFileName = null;

			try {

				uniFileName = this.uploadService.copy(foto);
				flash.addFlashAttribute("info", "Se ha subido la foto: " + uniFileName);
				cliente.setFoto(uniFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			cliente.setFoto("");
		}

		String mensajeFlash = cliente.getId() == null ? "Cliente ingresado" : "Cliente modificado";

		clienteService.create(cliente);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.FindById(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "Cliente no existe");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente invalido");
			return "redirect:/listar";
		}
		flash.addFlashAttribute("success", "Cliente modificado");
		model.put("cliente", cliente);
		model.put("titulo", "Editar");
		model.put("textoBoton", "Editar");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {

			Cliente cliente = clienteService.FindById(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente Eliminado");

			if (this.uploadService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto eliminarda");
			}

		} else {
			flash.addFlashAttribute("error", "Cliente invalido");
		}

		return "redirect:/listar";
	}

	private boolean hasRole(String rol) {
		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}
		// para cualquiere que herede de la clase GrantedAuthority
		// lista o coleccion de roles

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		/*
		 * for(GrantedAuthority authority : authorities) {
		 * if(rol.equals(authority.getAuthority())) { return true; } }
		 * 
		 * return false;
		 */
		Boolean retorno = authorities.contains(new SimpleGrantedAuthority(rol));
		System.out.println("Retonro: " + retorno);
		return retorno;
	}

}
