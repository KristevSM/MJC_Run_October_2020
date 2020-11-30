package com.epam.esm.repository;

import com.epam.esm.repository.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, JpaSpecificationExecutor<GiftCertificate> {
//    List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize);
    @Query(value = "SELECT c FROM GiftCertificate c LEFT JOIN c.tags t WHERE t.name IN :tagNames " +
            "GROUP BY c HAVING COUNT(t.name) = :tagNamesSize")
    Page<GiftCertificate> getGiftCertificatesByTagsNames(@Param("tagNames") List<String> tagNames,
                                                         @Param("tagNamesSize") long size, Pageable pageable);
    Optional<GiftCertificate> getCertificateByName(String name);
    Page<GiftCertificate> findAll(Specification<GiftCertificate> specification, Pageable pageable);

}
