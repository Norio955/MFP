package sumdu.edu.ua.persistence.adapter;

import org.springframework.stereotype.Component;
import sumdu.edu.ua.core.domain.User;
import sumdu.edu.ua.core.port.UserRepositoryPort;
import sumdu.edu.ua.persistence.repo.UserJpaRepository;
import java.util.Optional;

@Component
public class JpaUserAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepo;

    public JpaUserAdapter(UserJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<User> findById(Long id) { return userRepo.findById(id); }

    @Override
    public Optional<User> findByUsername(String username) { return userRepo.findByUsername(username); }

    @Override
    public User save(User user) { return userRepo.save(user); }
}