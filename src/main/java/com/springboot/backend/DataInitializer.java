package com.springboot.backend;

import com.springboot.backend.entity.Role;
import com.springboot.backend.entity.User;
import com.springboot.backend.repository.RoleRepository;
import com.springboot.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Slf4j
public class DataInitializer {

    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository,
                                             RoleRepository roleRepository,
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {

                // Tạo role ADMIN nếu chưa có
                Role adminRole = roleRepository.findByCode("ADMIN").orElse(null);
                if (adminRole == null) {
                    adminRole = new Role();
                    adminRole.setCode("ADMIN");
                    adminRole.setName("Quản trị viên");
                    roleRepository.save(adminRole);
                }

                // Tạo user admin
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setStatus(1);
                admin.setRoleId(adminRole.getId());
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);

                logger.info("Admin mặc định đã được tạo: admin / admin123");
            }
        };
    }
}
