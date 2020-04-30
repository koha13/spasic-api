package music.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import music.server.entities.User;


public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}