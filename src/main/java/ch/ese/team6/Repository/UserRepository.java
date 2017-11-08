package ch.ese.team6.Repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	List<User> findByRoles(Set<Role> roles);

}
