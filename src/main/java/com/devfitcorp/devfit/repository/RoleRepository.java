package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Role;
import com.devfitcorp.devfit.model.UsuarioRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNome(UsuarioRole nome);
}
