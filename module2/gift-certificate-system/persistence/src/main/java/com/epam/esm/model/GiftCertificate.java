package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor()
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
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private BigDecimal price;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
    @Column(name = "create_date")
    private ZonedDateTime createDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
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

    @JsonIgnore
    @OneToMany(mappedBy = "giftCertificate")
    private List<Order> orders;
}
