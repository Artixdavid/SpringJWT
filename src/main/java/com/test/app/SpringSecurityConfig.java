package com.test.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.test.app.auth.filter.JWTAuthenticationFilter;
import com.test.app.auth.filter.JWTAuthorizationFilter;
import com.test.app.auth.service.JWTService;
import com.test.app.models.service.JpaUserDetailService;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true) // para que funciones las anotaciones de los roles en los controladores, prePostEnabled para los preauthorize 
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	 * @Autowired
	private LoginSuccessHandler successHandler;
	*/
	
	@Autowired
	private JpaUserDetailService userDetailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//listar** da permiso a todas las rutas que comiencen con listar
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/img/**", "/listar**").permitAll()
		//.antMatchers("/ver/**").hasAnyRole("USER")
		//.antMatchers("/uploads/**").hasAnyRole("USER")
		//.antMatchers("/form/**").hasAnyRole("ADMIN")
		//.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		//.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		/*
		 * Se desahbilita para evitar el formulario de login que es para una app web sin jwt
		 * .and()
		 
			.formLogin()
			.successHandler(successHandler)// anotacion del la clase LoginSuccessHandler para 
			.loginPage("/login") //mapeado al controlador
			.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403") // misma ruta del mvcConfig
		*/
		.and()
		
		.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService)) //registramos el filtro para solicitar persmio con JWT
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService)) //registra el filtro para validar al recibir el token
		
		.csrf().disable() //se deshabilita para evitar el csrf de los formularios de login para usar el JWT
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception{
		

		
		
		//build.userDetailsService(jpaUserDetailService)
		build.userDetailsService(userDetailService)
		.passwordEncoder(passwordEncoder);
		
		
		
		
		//build.inMemoryAuthentication()
		//.withUser(users.username("admin").password("123").roles("ADMIN","USER"))
		//.withUser(users.username("david").password("123").roles("USER"));
		
	}

}
