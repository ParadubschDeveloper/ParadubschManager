package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import javax.persistence.*;

@Data
@Entity
@Table(name = "player_cache", indexes = @Index(name = "player_cache_index", columnList = "name"))
public class PlayerCache {

    @Getter
    @Setter
    @Id
    @Column(name = "uuid", length = 36)
    private String uuid;

    @Getter
    @Setter
    @Column(name = "name", length = 16)
    private String name;

    public PlayerCache(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
    }
}
