package ch.ese.team6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Role;

public interface RoleRepository  extends JpaRepository<Role, Long>{
}
