package com.projeto.usuario.usuario.infraestructure.repository;

import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {}
