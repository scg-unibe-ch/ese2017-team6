package ch.ese.team6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Distance;

public interface DistanceRepository extends JpaRepository<Distance, Long>{
	List<Distance> findAll();
}
