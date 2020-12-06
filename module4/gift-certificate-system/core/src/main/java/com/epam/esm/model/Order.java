package com.epam.esm.model;


import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends Auditable<String>  implements Serializable{
        private static final long serialVersionUID = -1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_id", unique = true, nullable = false)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable=false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "certificate_id")
        private GiftCertificate giftCertificate;

        @Column(name = "cost")
        private BigDecimal cost;

        @Column(name = "order_date")
        private ZonedDateTime orderDate;

        @Override
        public String toString() {
                return "Order{" +
                        "id=" + id +
                        ", orderDate=" + orderDate +
                        '}';
        }
}
