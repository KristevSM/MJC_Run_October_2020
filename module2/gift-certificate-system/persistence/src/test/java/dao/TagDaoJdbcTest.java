package dao;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

class TagDaoJdbcTest {

    private DataSource dataSource;
    private EmbeddedDatabase db;
    private TagDao tagCrudDAO;

    @BeforeEach
    void setUp() {

        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema.sql")
                .addScript("classpath:create_table.sql")
                .addScript("classpath:data_script.sql")
                .build();

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