package ch.ese.team6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.User;

import java.util.List;

public interface RoleRepository  extends JpaRepository<Role, Long>{
	List<User> findByName(String name);
}
