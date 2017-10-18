package ch.ese.team6.models.users;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository  extends JpaRepository<Users, Long>{

	List<Users>findAll();
	List<Users>findByUserrole(int userrole);
	
	Users findByUsername(String username);

	boolean existsByUsername(String username);
	
	boolean existsByUserrole(int userrole);
	
	
}
