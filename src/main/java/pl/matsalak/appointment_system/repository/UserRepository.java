package pl.matsalak.appointment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.matsalak.appointment_system.domain.entity.User;
import pl.matsalak.appointment_system.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByActiveTrue();

    boolean existsByEmail(String email);
}
