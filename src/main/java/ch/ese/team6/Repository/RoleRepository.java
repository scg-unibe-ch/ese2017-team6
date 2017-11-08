package ch.ese.team6.Repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.User;

import java.util.List;

public interface RoleRepository  extends JpaRepository<Role, Long>{

	Set<Role> findByName(String name);
	List<User> findByName(String name);
	
}
