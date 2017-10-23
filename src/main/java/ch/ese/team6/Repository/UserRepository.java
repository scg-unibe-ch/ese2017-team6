package ch.ese.team6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
