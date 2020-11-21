package com.epam.esm.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor()
@Builder
@Entity
@Table(name = "orders")
public class Order extends RepresentationModel<Order> implements Serializable{
        private static final long serialVersionUID = -1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_id", unique = true, nullable = false)
        private Long id;

        @JsonIgnoreProperties("orders")
        @ManyToOne
        @JoinColumn(name = "user_id", nullable=false)
        private User user;

        @JsonIgnoreProperties("orders")
        @ManyToOne
        @JoinColumn(name = "certificate_id")
        private GiftCertificate giftCertificate;

        @Column(name = "cost")
        private BigDecimal cost;

        @Column(name = "order_date")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
        private ZonedDateTime orderDate;

        @Override
        public String toString() {
                return "Order{" +
                        "id=" + id +
                        ", orderDate=" + orderDate +
                        '}';
        }
}
