package de.paradubsch.paradubschmanager.models;

import de.craftery.craftinglib.util.BaseDatabaseEntity;
import de.craftery.craftinglib.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Material;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "bazaar_collectables")
@SequenceGenerator(name="bazaarCollectableSequence",sequenceName="bazaar_collectable_sequence", allocationSize = 1)
public class BazaarCollectable extends BaseDatabaseEntity<BazaarCollectable, Long> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bazaarCollectableSequence")
    @Column(name = "collectable_id")
    private long id;

    @Column(name = "holder", columnDefinition = "VARCHAR(36)", nullable = false)
    private String holderUuid;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Material material;

    @Column(name = "amount", nullable = false, columnDefinition = "BIGINT default 0")
    private Long amount = 0L;

    @Override
    public Long getIdentifyingColumn() {
        return this.getId();
    }

    public static BazaarCollectable getByHolderItemType(String holderUuid, Material material) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            return session.createQuery("from BazaarCollectable where holderUuid = :holder and material = :material", BazaarCollectable.class)
                    .setParameter("holder", holderUuid)
                    .setParameter("material", material)
                    .getSingleResult();
        } catch (NoResultException e) {
            BazaarCollectable collectable = new BazaarCollectable();
            collectable.setHolderUuid(holderUuid);
            collectable.setAmount(0L);
            collectable.setMaterial(material);
            return collectable;
        }
    }

    public static List<BazaarCollectable> getByHolder(String holderUuid) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            return session.createQuery("from BazaarCollectable where holderUuid = :holder", BazaarCollectable.class)
                    .setParameter("holder", holderUuid)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public BazaarCollectable clone() {
        return (BazaarCollectable) super.clone();
    }
}
