package com.test.app.auth.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.test.app.auth.service.JWTService;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTService jwtService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	// este metodo se ejecuta siempre, todas las peticiones
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization");

		boolean requiredAuth = requiresAuthentication(header);

		if (!requiredAuth) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;

		if (jwtService.validate(header)) {
			authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null,
					jwtService.getRoles(header));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}

	}

	protected boolean requiresAuthentication(String header) {

		if (header == null || !header.toLowerCase().startsWith("bearer ")) {
			return false;
		}
		return true;
	}

}
