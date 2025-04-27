package online.robodoc.base.config;

import jakarta.annotation.PostConstruct;
import online.robodoc.base.domain.User;
import online.robodoc.base.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("Admin") == null) {
            User admin = new User();
            admin.setUsername("Admin");
            admin.setPassword("SuperSecretPassword");
            admin.setAdmin(true);
            userRepository.save(admin);
        }
    }
}