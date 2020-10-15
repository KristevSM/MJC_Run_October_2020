package epam.com.esm.dao;

import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

class TagDaoJdbcTest {

    //    private DataSource dataSource;
    private DriverManagerDataSource dataSource;
    private EmbeddedDatabase db;
    private TagDao tagCrudDAO;

    @BeforeEach
    void setUp() {

//        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                .generateUniqueName(true)
//                .addScript("classpath:schema.sql")
//                .addScript("classpath:create_table.sql")
//                .addScript("classpath:data_script.sql")
//                .build();

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("gift_service_admin");
        dataSource.setPassword("gift");
        dataSource.setUrl("jdbc:mysql://localhost:3306/gift_certificate_service");

        tagCrudDAO = new TagDaoJdbc(dataSource);
    }


    @Test
    void shouldFindAllTags() {
        List<Tag> tags = tagCrudDAO.findAll();
        Assert.notEmpty(tags);
        System.out.println(tags);

    }

    @Test
    void shouldFindTagById() {
        Optional<Tag> tag = tagCrudDAO.find(1L);
        System.out.println(tag);
        Assert.isTrue(tag.isPresent());

    }

    @Test
    void shouldSaveAndDeleteTag() {
        Tag tag = Tag.builder()
                .name("New tag")
                .build();

        Long id = tagCrudDAO.save(tag);
        System.out.println("Current id: " + id);
        Assert.isTrue(tagCrudDAO.find(id).isPresent());

        tagCrudDAO.delete(id);
        Assert.isTrue(!tagCrudDAO.find(id).isPresent());
    }

    @Test
    void shouldUpdateTag() {
        Tag tag = Tag.builder()
                .id(1L)
                .name("Updated name")
                .build();

        tagCrudDAO.update(tag);

        Optional<Tag> updatedTag = tagCrudDAO.find(1L);
        System.out.println(updatedTag);

    }
//    @AfterEach
//    void tearDown() {
//        db.shutdown();
//    }
}