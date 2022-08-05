package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.persistance.model.*;
import de.paradubsch.paradubschmanager.persistance.repository.*;
import lombok.Cleanup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;


public class Hibernate {
    public static <T extends Repository<?, ?>> T getRepository(Class<T> clazz) {
        return ParadubschManager.getInstance().getCtx().getBean(clazz);
    }

    public static PlayerData getPlayerData(@NotNull Player p) {
        PlayerDataRepository repository = getRepository(PlayerDataRepository.class);
        PlayerData playerData = repository.findById(p.getUniqueId().toString()).orElse(null);
        if (playerData == null) {
            playerData = new PlayerData(p);
            repository.save(playerData);
            Bukkit.getLogger().log(Level.INFO, "!> Cached player data for " + p.getName());
        } else if (!playerData.getName().equals(p.getName())) {
            playerData.setName(p.getName());
            repository.save(playerData);
        }
        return playerData;
    }

    public static PlayerData getPlayerData(UUID uuid) {
        PlayerDataRepository repository = getRepository(PlayerDataRepository.class);
        return repository.findById(uuid.toString()).orElse(null);
    }

    public static List<Home> getHomes(Player p) {
        PlayerDataRepository repository = getRepository(PlayerDataRepository.class);
        PlayerData pd = repository.findById(p.getUniqueId().toString()).orElse(null);
        return pd == null ? new ArrayList<>() : pd.getHomes();
    }

    @Deprecated
    public static void delete(Object home) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.delete(home);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void save (Object o) {
        if (o == null) {
            return;
        }
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            session.saveOrUpdate(o);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static PlayerData getPlayerData(String playerName) {
       PlayerDataRepository repository = getRepository(PlayerDataRepository.class);
       return repository.findFirstByNameIgnoreCase(playerName);
    }

    public static List<PlayerData> getMoneyTop () {
        PlayerDataRepository repository = getRepository(PlayerDataRepository.class);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "money"));
        return repository.findAll(pageable).getContent();
    }

    public static SaveRequest getSaveRequest(Player p) {
        return getPlayerData(p).getOpenSaveRequest();
    }

    public static SaveRequest getSaveRequest(int id) {
        return getRepository(SaveRequestRepository.class).findById(id).orElse(null);
    }

    public static PunishmentHolder getPunishmentHolder(PlayerData pd) {
        PunishmentHolderRepository punishmentHolderRepository = getRepository(PunishmentHolderRepository.class);
        PunishmentHolder punishmentHolder = punishmentHolderRepository.findById(pd.getUuid()).orElse(null);
        if (punishmentHolder == null) {
            punishmentHolder = new PunishmentHolder();
            punishmentHolder.setPlayerRef(pd);
            punishmentHolder.setUuid(pd.getUuid());
        }
        return punishmentHolder;
    }
    public static PunishmentHolder getPunishmentHolder(Player player) {
        PunishmentHolderRepository punishmentHolderRepository = getRepository(PunishmentHolderRepository.class);
        PlayerDataRepository playerDataRepositoryRepository = getRepository(PlayerDataRepository.class);
        PunishmentHolder punishmentHolder = punishmentHolderRepository.findById(player.getUniqueId().toString()).orElse(null);
        if (punishmentHolder == null) {
            punishmentHolder = new PunishmentHolder();
            punishmentHolder.setPlayerRef(playerDataRepositoryRepository.findById(player.getUniqueId().toString()).orElse(null));
            punishmentHolder.setUuid(player.getUniqueId().toString());
        }
        return punishmentHolder;
    }

    public static List<PunishmentUpdate> getBanUpdates(BanPunishment ban) {
        PunishmentUpdateRepository repository = getRepository(PunishmentUpdateRepository.class);
        return repository.findByPunishmentRef(ban);
    }

    public static Warp getWarp(String name) {
        WarpRepository repository = getRepository(WarpRepository.class);
        return repository.findById(name).orElse(null);
    }

    public static List<Warp> getWarps() {
        WarpRepository repository = getRepository(WarpRepository.class);
        return repository.findAll();
    }

}
