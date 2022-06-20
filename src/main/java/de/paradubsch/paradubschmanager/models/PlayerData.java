package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import org.bukkit.entity.Player;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.List;

import static org.hibernate.annotations.CascadeType.*;

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

    @Column(name ="money", columnDefinition = "BIGINT DEFAULT 150")
    private long money = 150L;

    @OneToMany(mappedBy = "playerRef", fetch = FetchType.LAZY)
    @Cascade(value = SAVE_UPDATE)
    private List<Home> homes;

    @Column(name = "max_homes", columnDefinition = "INT DEFAULT 2")
    private int maxHomes = 2;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "playerRef")
    @Cascade(value = SAVE_UPDATE)
    private SaveRequest openSaveRequest;

    public PlayerData () {}

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
    }
}
