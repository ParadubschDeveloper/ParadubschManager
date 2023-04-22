package de.paradubsch.paradubschmanager.lifecycle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class TpaRequest {
    private Player requester;
    private Player target;
}
