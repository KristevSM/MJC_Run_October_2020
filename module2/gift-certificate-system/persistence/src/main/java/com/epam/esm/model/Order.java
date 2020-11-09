package com.epam.esm.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor()
@Builder
@Entity
@Table(name = "orders")
public class Order implements Serializable{
        private static final long serialVersionUID = -1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_id", unique = true, nullable = false)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable=false)
        private User user;

//        @ManyToOne
//        @JoinColumn(name = "giftCertificate_id")
//        private GiftCertificate giftCertificate;

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
