package de.paradubsch.paradubschmanager.persistance.repository;

import de.paradubsch.paradubschmanager.persistance.model.PunishmentHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PunishmentHolderRepository extends JpaRepository<PunishmentHolder, String> {

}
