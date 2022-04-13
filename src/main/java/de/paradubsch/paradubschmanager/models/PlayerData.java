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

    @Getter
    @Setter
    @Id
    @Column(name = "uuid", length = 36)
    private String uuid;

    @Getter
    @Setter
    @Column(name = "name", length = 16)
    private String name;

    @Getter
    @Setter
    @Column(name = "language_preference", length = 16)
    private String language = "de";

    @Getter
    @Setter
    @Column(name = "chat_prefix", length = 256)
    private String chatPrefix = "&7Spieler";

    @Getter
    @Setter
    @Column(name = "name_color", length = 24)
    private String nameColor = "&7";

    @Getter
    @Setter
    @Column(name = "default_chat_color", length = 24)
    private String defaultChatColor = "&7";

    public PlayerData () {}

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
    }
}
