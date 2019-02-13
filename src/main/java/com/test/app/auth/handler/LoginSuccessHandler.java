package com.test.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/*
 * se decora como componente para saber que es un Beam de spring (igual que la anotacion de controllers)
 *  
 * */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		SessionFlashMapManager flasMapManager = new SessionFlashMapManager();

		FlashMap flashMap = new FlashMap();

		flashMap.put("success", "wena cabro! (" + authentication.getName() + ")");

		flasMapManager.saveOutputFlashMap(flashMap, request, response);
		
		if(authentication != null) {
			logger.info("El usuario " + authentication.getName() + " ha iniciado");
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
