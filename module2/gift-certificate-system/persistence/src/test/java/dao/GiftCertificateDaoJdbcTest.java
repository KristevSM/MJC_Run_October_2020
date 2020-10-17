package dao;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateDaoJdbc;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class GiftCertificateDaoJdbcTest {

        private DataSource dataSource;
//    private DriverManagerDataSource dataSource;
    private EmbeddedDatabase db;
    private GiftCertificateDao certificateCrudDAO;
    private TagDaoJdbc tagDaoJdbc;

    @BeforeEach
    void setUp() {

        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema.sql")
                .addScript("classpath:create_table.sql")
                .addScript("classpath:data_script.sql")
                .build();
        certificateCrudDAO = new GiftCertificateDaoJdbc(dataSource);
        tagDaoJdbc = new TagDaoJdbc(dataSource);
    }



    @Test
    void shouldFindAllCertificates() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.findAll();
        assertFalse(giftCertificates.isEmpty());
        System.out.println(giftCertificates.size());
        System.out.println(giftCertificates);

    }

    @Test
    void shouldFindCertificateById() {
        Optional<GiftCertificate> giftCertificate = certificateCrudDAO.find(1L);
        System.out.println(giftCertificate);
        assertTrue(giftCertificate.isPresent());

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
                .tags(new ArrayList<>())
                .build();

        Long id = certificateCrudDAO.save(certificate);
        Tag tag = Tag.builder()
                .id(2L)
                .name("Tag 4")
                .build();

        Long tagId = tagDaoJdbc.save(tag);
        certificateCrudDAO.addTagToCertificate(id, tagId);
        System.out.println("Current id: " + id);
        assertTrue(certificateCrudDAO.find(id).isPresent());

        certificateCrudDAO.removeTagFromCertificate(id, tagId);
        certificateCrudDAO.delete(id);
        tagDaoJdbc.delete(tagId);
        assertThrows(NoSuchElementException.class, () -> {
            certificateCrudDAO.find(id);
        });
    }

    @Test
    void shouldFindCertificateByTagName() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificatesByTagName("Tag 3");
        assertEquals(giftCertificates.size(), 1);
        System.out.println(giftCertificates);
    }

    @Test
    void shouldFindCertificateByPartOfDescription() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificatesByPartOfDescription("certificate 2");
        assertEquals(giftCertificates.size(), 1);
        System.out.println(giftCertificates);
    }

    @Test
    void shouldFindCertificateByPartOfName() {
        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificatesByPartOfName("2");
        assertEquals(giftCertificates.size(), 1);
        System.out.println(giftCertificates);
        assertThrows(NoSuchElementException.class, () -> {
            certificateCrudDAO.getCertificatesByPartOfName("2f");
        });
    }

}