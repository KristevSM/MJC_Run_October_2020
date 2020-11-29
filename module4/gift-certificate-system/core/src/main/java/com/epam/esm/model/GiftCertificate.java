package com.epam.esm.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;


@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
@Entity
@Table(name = "gift_certificate")
public class GiftCertificate implements Serializable {

    private static final long serialVersionUID = -1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id", nullable = false, insertable = true, updatable = true)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "create_date")
    private ZonedDateTime createDate;
    @Column(name = "last_update_date")
    private ZonedDateTime lastUpdateDate;
    @Column(name = "duration")
    private int duration;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag_has_gift_certificate",
            //foreign key for Certificate in tag_has_gift_certificate table
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            //foreign key for other side - Certificate in tag_has_gift_certificate table
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "giftCertificate")
    private List<Order> orders;
}
