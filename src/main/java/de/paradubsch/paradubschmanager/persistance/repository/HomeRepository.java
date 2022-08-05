package de.paradubsch.paradubschmanager.persistance.repository;

import de.paradubsch.paradubschmanager.persistance.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {

}
