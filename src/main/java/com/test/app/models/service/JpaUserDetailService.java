package com.test.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.app.models.dao.IUsuarioDAO;
import com.test.app.models.entity.Role;
import com.test.app.models.entity.Usuario;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService {

	@Autowired
	private IUsuarioDAO usuarioDAO;

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailService.class);

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = usuarioDAO.findByUsername(username);

		if (username == null) {
			logger.error("Usuario '" + username + "' no existe");
			throw new UsernameNotFoundException("Usuario no existe");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			logger.error("Usuario '" + username + "' no tiene roles asociados");
			throw new UsernameNotFoundException("Usuario sin roles");
		}

		return new User(username, user.getPassword(), user.getEnabled(), true, true, true, authorities);
	}

}
