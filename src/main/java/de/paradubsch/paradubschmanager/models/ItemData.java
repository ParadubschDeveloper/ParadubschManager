package de.paradubsch.paradubschmanager.models;

import de.paradubsch.paradubschmanager.models.converter.ItemStackConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "item_data")
public class ItemData {

    @Id
    @Column(name = "item_hash", nullable = false, columnDefinition = "VARCHAR(40)")
    private String itemHash;

    @Column(name = "data")
    @Convert(converter = ItemStackConverter.class)
    private ItemStack item;
}
