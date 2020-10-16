package dao;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateDaoJdbc;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


class GiftCertificateDaoJdbcTest {

        private DataSource dataSource;
//    private DriverManagerDataSource dataSource;
    private EmbeddedDatabase db;
    private GiftCertificateDao certificateCrudDAO;

    @BeforeEach
    void setUp() {

        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema.sql")
                .addScript("classpath:create_table.sql")
                .addScript("classpath:data_script.sql")
                .build();
        certificateCrudDAO = new GiftCertificateDaoJdbc(dataSource);
    }



    @Test
    void shouldFindAllCertificates() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.findAll();
        Assert.notEmpty(giftCertificates);
        System.out.println(giftCertificates);

    }

    @Test
    void shouldFindCertificateById() {
        Optional<GiftCertificate> giftCertificate = certificateCrudDAO.find(1L);
        System.out.println(giftCertificate);
        Assert.isTrue(giftCertificate.isPresent());

    }

    @Test
    void shouldUpdateCertificate() {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(1L)
                .name("Updated certificate")
                .description("Some description")
                .price(BigDecimal.valueOf(120L))
                .createDate(LocalDateTime.now())
                .duration(4)
                .build();

        certificateCrudDAO.update(certificate);

        Optional<GiftCertificate> updatedCertificate = certificateCrudDAO.find(1L);
        System.out.println(updatedCertificate);

    }

    @Test
    void shouldSaveAndDeleteCertificate() {
        GiftCertificate certificate = GiftCertificate.builder()
                .name("New certificate")
                .description("Some description")
                .price(BigDecimal.valueOf(100D))
                .createDate(LocalDateTime.now())
                .duration(6)
                .build();

        Long id = certificateCrudDAO.save(certificate);
        System.out.println("Current id: " + id);
        Assert.isTrue(certificateCrudDAO.find(id).isPresent());

        certificateCrudDAO.delete(id);
        Assert.isTrue(!certificateCrudDAO.find(id).isPresent());
    }

    @Test
    void shouldFindCertificateByTagName() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificatesByTagName("Tag 2");
        Assert.isTrue(giftCertificates.size() == 1);
        System.out.println(giftCertificates);
    }

    @Test
    void shouldFindCertificateByPartOfDescription() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificatesByPartOfDescription("certificate 2");
        Assert.isTrue(giftCertificates.size() == 1);
        System.out.println(giftCertificates);
    }

    @Test
    void shouldFindCertificateByPartOfName() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificatesByPartOfName("2");
        Assert.isTrue(giftCertificates.size() == 1);
        System.out.println(giftCertificates);
        assertThrows(NoSuchElementException.class, () -> {
            certificateCrudDAO.getCertificatesByPartOfName("2f");
        });
    }

//    @AfterEach
//    void tearDown() {
//        db.shutdown();
//    }

}