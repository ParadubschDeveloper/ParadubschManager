package de.paradubsch.paradubschmanager.persistance.repository;

import de.paradubsch.paradubschmanager.persistance.model.PunishmentUpdate;
import de.paradubsch.paradubschmanager.persistance.model.WarnPunishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PunishmentUpdateRepository extends JpaRepository<PunishmentUpdate, Long> {
    List<PunishmentUpdate> findByPunishmentRef (WarnPunishment punishmentRef);
}
