package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tag")
public class Tag implements Serializable {
    private static final long serialVersionUID = -1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

//    @Builder.Default
//    private List<GiftCertificate> certificates = new ArrayList<>();
//
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "tag_has_gift_certificate",
//            //foreign key for Tag in tag_has_gift_certificate table
//            joinColumns = @JoinColumn(name = "tag_id"),
//            //foreign key for other side - TAg in tag_has_gift_certificate table
//            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
//    public List<GiftCertificate> getCertificates(){
//        return certificates;
//    }
//    public void setCertificates(List<GiftCertificate> certificates){
//        this.certificates = certificates;
//    }
}
