package pl.edu.agh.cs.to.cinemamak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.cs.to.cinemamak.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
