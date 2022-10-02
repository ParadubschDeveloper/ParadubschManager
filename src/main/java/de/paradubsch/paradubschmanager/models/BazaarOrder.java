package de.paradubsch.paradubschmanager.models;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.OrderType;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Material;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "bazaar_orders")
@SequenceGenerator(name="bazaarOrderSequence",sequenceName="bazaar_order_sequence", allocationSize = 1)
public class BazaarOrder extends BaseDatabaseEntity<BazaarOrder, Long>{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bazaarOrderSequence")
    @Column(name = "order_id")
    private long id;

    @Column(name = "holder", columnDefinition = "VARCHAR(36)", nullable = false)
    private String holderUuid;

    @Column(name = "order_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType = OrderType.BUY;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Material material;

    @Column(name = "price", nullable = false, columnDefinition = "int default 0")
    private Integer price = 0;

    @Column(name = "amount", nullable = false, columnDefinition = "BIGINT default 0")
    private Long amount = 0L;

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }

    private static BazaarOrder createByHolderOrderTypeItemTypePrice(@NotNull String holderUuid, @NotNull OrderType orderType, @NotNull Material material, @NotNull Integer price) {
        BazaarOrder order = new BazaarOrder();
        order.setHolderUuid(holderUuid);
        order.setOrderType(orderType);
        order.setMaterial(material);
        order.setPrice(price);
        order.setAmount(0L);
        return order;
    }

    public static @NotNull BazaarOrder getByHolderOrderTypeItemTypePrice(@NotNull String holderUuid, @NotNull OrderType orderType, @NotNull Material material, @NotNull Integer price) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            return session.createQuery("FROM BazaarOrder where holderUuid = :holderUuid and orderType = :orderType and material = :material and price = :price", BazaarOrder.class)
                    .setParameter("holderUuid", holderUuid)
                    .setParameter("orderType", orderType)
                    .setParameter("material", material)
                    .setParameter("price", price)
                    .getSingleResult();
        } catch (NoResultException e) {
            return createByHolderOrderTypeItemTypePrice(holderUuid, orderType, material, price);
        } catch (Exception e) {
            e.printStackTrace();
            return createByHolderOrderTypeItemTypePrice(holderUuid, orderType, material, price);
        }
    }

    public static @NotNull List<BazaarOrder> getOrdersByMaterial(@NotNull OrderType orderType, @NotNull Material material) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            return session.createQuery("FROM BazaarOrder where orderType = :orderType and material = :material", BazaarOrder.class)
                    .setParameter("orderType", orderType)
                    .setParameter("material", material)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
