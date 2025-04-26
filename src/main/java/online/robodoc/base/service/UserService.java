package online.robodoc.base.service;

import online.robodoc.base.domain.User;
import online.robodoc.base.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User save(User user)
    {
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
