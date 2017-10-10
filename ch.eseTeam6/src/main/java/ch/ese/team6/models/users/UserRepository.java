package ch.ese.team6.models.users;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository  extends JpaRepository<Users, Long>{

	List<Users>findAll();
	
	Users findByUsername(String username);

	boolean existsByUsername(String username);
	
	
}
