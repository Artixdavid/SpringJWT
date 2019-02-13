package com.test.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model, Principal principal,
			RedirectAttributes flash) {

		// principal sirve para saber si ya ha iniciado sesion
		if (principal != null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesion");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error", "Usuario o contrasena incorrectos");
		}
		
		return "/login";
	}

}
