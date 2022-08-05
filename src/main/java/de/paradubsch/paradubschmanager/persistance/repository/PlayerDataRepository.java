package de.paradubsch.paradubschmanager.persistance.repository;

import de.paradubsch.paradubschmanager.persistance.model.PlayerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerDataRepository extends JpaRepository<PlayerData, String> {
    PlayerData findFirstByNameIgnoreCase(String name);
}

