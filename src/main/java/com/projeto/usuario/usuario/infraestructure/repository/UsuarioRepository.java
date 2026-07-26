package com.projeto.usuario.usuario.infraestructure.repository;

import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email); // Optional é uma classe que EVITA o retorno null
    Optional<Usuario> findById(Long id);
}
