package com.test.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.test.app.models.entity.Usuario;

public interface IUsuarioDAO extends CrudRepository<Usuario, Long> {

	public Usuario findByUsername(String username);
}
