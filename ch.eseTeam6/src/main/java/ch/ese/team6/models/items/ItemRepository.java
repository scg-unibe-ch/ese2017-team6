package ch.ese.team6.models.items;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.models.items.Items;


public interface ItemRepository  extends JpaRepository<Items, Long>{

	List<Items>findAll();
	
	Items findByName(String Name);

	boolean existsByName(String Name);
}
