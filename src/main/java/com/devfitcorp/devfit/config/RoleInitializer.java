package com.devfitcorp.devfit.config;

import com.devfitcorp.devfit.model.Role;
import com.devfitcorp.devfit.model.UsuarioRole;
import com.devfitcorp.devfit.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (UsuarioRole role : UsuarioRole.values()) {
                roleRepository.findByNome(role)
                        .orElseGet(() -> roleRepository.save(new Role(null, role)));
            }
        };
    }
}
