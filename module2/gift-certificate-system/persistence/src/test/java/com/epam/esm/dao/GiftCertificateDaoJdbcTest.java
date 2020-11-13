package com.epam.esm.dao;

import com.epam.esm.dao.jdbc.TagDaoJdbc;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


class GiftCertificateDaoJdbcTest {

    private DataSource dataSource;
    private EmbeddedDatabase db;
    private GiftCertificateDao certificateCrudDAO;
    private TagDaoJdbc tagDaoJdbc;

//    @BeforeEach
//    void setUp() {
//
//        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                .generateUniqueName(true)
//                .addScript("classpath:schema.sql")
//                .addScript("classpath:create_table.sql")
//                .addScript("classpath:data_script.sql")
//                .build();
//        certificateCrudDAO = new GiftCertificateDaoJdbc(dataSource);
//        tagDaoJdbc = new TagDaoJdbc(dataSource);
//    }
//
//    //Hibernate
//    @Test
//    public void shouldSaveCertificateWithTags() {
//        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
//        session.beginTransaction();
//
//        GiftCertificate certificate = GiftCertificate.builder()
//                .name("New certificate")
//                .description("Some description")
//                .price(BigDecimal.valueOf(100D))
//                .createDate(ZonedDateTime.now())
//                .lastUpdateDate(ZonedDateTime.now())
//                .duration(6)
//                .tags(new ArrayList<>())
//                .build();
//
//        Tag tag = Tag.builder()
//                .name("Tag 4")
//                .build();
//
//        certificate.getTags().add(tag);
//
//        session.persist(certificate);
//
//        session.getTransaction().commit();
//        session.close();
//
//        session = HibernateAnnotationUtil.getSessionFactory().openSession();
//        String sql = "SELECT * FROM GIFT_CERTIFICATE";
//        Query query = session.createNativeQuery(sql).addEntity(GiftCertificate.class);
//        List<GiftCertificate> certificates = query.list();
//
//        String sql2 = "SELECT * FROM TAG";
//        Query query2 = session.createNativeQuery(sql2).addEntity(Tag.class);
//        List<Tag> tags = query2.list();
//
//        session.close();
//    }
//
//    @Test
//    void shouldFindAllCertificates() {
//        List<GiftCertificate> giftCertificates = certificateCrudDAO.findAll();
//        assertFalse(giftCertificates.isEmpty());
//        System.out.println(giftCertificates.size());
//        System.out.println(giftCertificates);
//
//    }
//
//    @Test
//    void shouldFindCertificateById() {
//        Optional<GiftCertificate> giftCertificate = certificateCrudDAO.find(1L);
//        System.out.println(giftCertificate);
//        assertTrue(giftCertificate.isPresent());
//
//    }
//
//    @Test
//    void shouldUpdateCertificate() {
//        GiftCertificate certificate = GiftCertificate.builder()
//                .id(1L)
//                .name("Updated certificate")
//                .description("Some description")
//                .price(BigDecimal.valueOf(120L))
//                .createDate(ZonedDateTime.now())
//                .lastUpdateDate(ZonedDateTime.now())
//                .duration(4)
//                .build();
//
//        certificateCrudDAO.update(certificate);
//
//        Optional<GiftCertificate> updatedCertificate = certificateCrudDAO.find(1L);
//        System.out.println(updatedCertificate);
//
//    }
//
//    @Test
//    void shouldSaveAndDeleteCertificate() {
//        GiftCertificate certificate = GiftCertificate.builder()
//                .name("New certificate")
//                .description("Some description")
//                .price(BigDecimal.valueOf(100D))
//                .createDate(ZonedDateTime.now())
//                .lastUpdateDate(ZonedDateTime.now())
//                .duration(6)
//                .tags(new ArrayList<>())
//                .build();
//
//        Long id = certificateCrudDAO.save(certificate);
//        Tag tag = Tag.builder()
//                .id(2L)
//                .name("Tag 4")
//                .build();
//
//        Long tagId = tagDaoJdbc.save(tag);
//        certificateCrudDAO.addTagToCertificate(id, tagId);
//        System.out.println("Current id: " + id);
//        assertTrue(certificateCrudDAO.find(id).isPresent());
//
//        certificateCrudDAO.removeTagFromCertificate(id, tagId);
//        certificateCrudDAO.delete(id);
//        tagDaoJdbc.delete(tagId);
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            certificateCrudDAO.find(id);
//        });
//    }
//
//    @Test
//    void shouldFindCertificateByTagName() {
//        CertificateSearchQuery query = new CertificateSearchQuery();
//        query.setTagName("Tag 3");
//        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificates(query);
//        assertEquals(giftCertificates.size(), 1);
//        System.out.println(giftCertificates);
//    }
//
//    @Test
//    void shouldFindCertificateByPartOfDescription() {
//        CertificateSearchQuery query = new CertificateSearchQuery();
//        query.setPartOfDescription("ficate 2");
//        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificates(query);
//        assertEquals(giftCertificates.size(), 1);
//        System.out.println(giftCertificates);
//    }
//
//    @Test
//    void shouldFindCertificateByPartOfName() {
//        CertificateSearchQuery query = new CertificateSearchQuery();
//        query.setPartOfName("2");
//        List<GiftCertificate> giftCertificates = certificateCrudDAO.getCertificates(query);
//        assertEquals(giftCertificates.size(), 1);
//        System.out.println(giftCertificates);
//        query.setPartOfName("2f");
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            certificateCrudDAO.getCertificates(query);
//        });
//    }

}