package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "player_data", indexes = @Index(name = "player_data_index", columnList = "name"))
public class PlayerData {

    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

    @Column(name = "name", columnDefinition = "VARCHAR(16)", nullable = false)
    private String name;

    @Column(name = "language_preference", columnDefinition = "VARCHAR(16) DEFAULT 'de'")
    private String language = "de";

    @Column(name = "chat_prefix", columnDefinition = "VARCHAR(256) DEFAULT '&7Spieler'")
    private String chatPrefix = "&7Spieler";

    @Column(name = "name_color", columnDefinition = "VARCHAR(24) DEFAULT '&7'")
    private String nameColor = "&7";

    @Column(name = "default_chat_color", columnDefinition = "VARCHAR(24) DEFAULT '&7'")
    private String defaultChatColor = "&7";

    @Column(name = "playtime", columnDefinition = "BIGINT DEFAULT 0")
    private long playtime = 0L;

    public PlayerData () {}

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
    }
}
