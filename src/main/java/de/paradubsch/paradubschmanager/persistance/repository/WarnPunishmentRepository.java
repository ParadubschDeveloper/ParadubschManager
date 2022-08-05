package de.paradubsch.paradubschmanager.persistance.repository;

import de.paradubsch.paradubschmanager.persistance.model.WarnPunishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarnPunishmentRepository extends JpaRepository<WarnPunishment, Long> {

}
