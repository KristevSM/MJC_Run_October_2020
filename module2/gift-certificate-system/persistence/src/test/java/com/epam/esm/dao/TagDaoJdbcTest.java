package com.epam.esm.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagDaoJdbcTest {

//    private DataSource dataSource;
//    private TagDao tagCrudDAO;
//
//    @BeforeEach
//    void setUp() {
//
//        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                .generateUniqueName(true)
//                .addScript("classpath:schema.sql")
//                .addScript("classpath:create_table.sql")
//                .addScript("classpath:data_script.sql")
//                .build();
//
//        tagCrudDAO = new TagDaoJdbc(dataSource);
//    }
//
//
//    @Test
//    void shouldFindAllTags() {
//        List<Tag> tags = tagCrudDAO.findAll();
//        assertFalse(tags.isEmpty());
//        System.out.println(tags);
//
//    }
//
//    @Test
//    void shouldFindTagById() {
//        Optional<Tag> tag = tagCrudDAO.find(1L);
//        System.out.println(tag);
//        assertTrue(tag.isPresent());
//
//    }
//
//    @Test
//    void shouldSaveAndDeleteTag() {
//        Tag tag = Tag.builder()
//                .name("New tag")
//                .build();
//
//        Long id = tagCrudDAO.save(tag);
//        System.out.println("Current id: " + id);
//        assertTrue(tagCrudDAO.find(id).isPresent());
//
//        tagCrudDAO.delete(id);
//        assertTrue(!tagCrudDAO.find(id).isPresent());
//    }
//
//    @Test
//    void shouldUpdateTag() {
//        Tag tag = Tag.builder()
//                .id(1L)
//                .name("Updated name")
//                .build();
//
//        tagCrudDAO.update(tag);
//
//        Optional<Tag> updatedTag = tagCrudDAO.find(1L);
//        System.out.println(updatedTag);
//
//    }
//
//    @Test
//    void shouldFindTagName() {
//        Optional<Tag> tag = tagCrudDAO.findByTagName("Main");
//        System.out.println(tag);
//        assertTrue(tag.isPresent());
//
//    }

}