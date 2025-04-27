package online.robodoc.base.service;

import online.robodoc.base.domain.User;
import online.robodoc.base.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User save(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> findAll()
    {
        return userRepository.findAll();
    }

    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
}
