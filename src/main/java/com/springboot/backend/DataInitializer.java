package com.springboot.backend;

import com.springboot.backend.entity.Role;
import com.springboot.backend.entity.User;
import com.springboot.backend.repository.RoleRepository;
import com.springboot.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

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

                System.out.println("Admin mặc định đã được tạo: admin / admin123");
            }
        };
    }
}
