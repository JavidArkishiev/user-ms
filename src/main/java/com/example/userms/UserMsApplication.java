package com.example.userms;

import com.example.userms.entity.Role;
import com.example.userms.entity.User;
import com.example.userms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
@Slf4j
public class UserMsApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String adminEmail = "rufet@gmail.com";

    public static void main(String[] args) {
        SpringApplication.run(UserMsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        createAdminRoleIfNotExist();
    }

    private void createAdminRoleIfNotExist() {
        Optional<User> adminRole = userRepository.findByEmail(adminEmail);
        if (adminRole.isEmpty()) {
            User user = new User();
            user.setEmail(adminEmail);
            user.setFirstName("Rufet");
            user.setLastName("Mamedov");
            user.setCreatedDate(LocalDateTime.now());
            user.setCreatedBy(adminEmail);
            user.setPassword(passwordEncoder.encode("123456!Mr"));
            user.setOtp(null);
            user.setEnabled(true);
            user.setOtpGeneratedTime(null);
            user.setUuid(null);
            user.setUuidGeneratedTimme(null);
            user.setRoles(mapToRole());
            userRepository.save(user);
            log.info("{} -> Admin has created", adminEmail);

        } else {
            log.info("{}-> Admin already has been created", adminEmail);

        }
    }

    public List<Role> mapToRole() {
        List<Role> roleList = new ArrayList<>();
        Role roleAdmin = new Role();

        roleAdmin.setName("ADMIN");
        roleAdmin.setCreatedBy(adminEmail);
        roleAdmin.setCreatedDate(LocalDateTime.now());

        roleList.add(roleAdmin);

        return roleList;
    }
}
