package ch.ese.team6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
