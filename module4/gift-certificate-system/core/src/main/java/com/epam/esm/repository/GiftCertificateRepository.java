package com.epam.esm.repository;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
//    List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize);
    @Query(value = "SELECT c FROM GiftCertificate c LEFT JOIN c.tags t WHERE t.name IN :tagNames " +
            "GROUP BY c HAVING COUNT(t.name) = :tagNamesSize")
    List<GiftCertificate> getGiftCertificatesByTagsNames(@Param("tagNames") List<String> tagNames,
                                                         @Param("tagNamesSize") long size);
    Optional<GiftCertificate> getCertificateByName(String name);

}
