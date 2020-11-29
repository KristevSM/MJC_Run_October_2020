package esm.service;


import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

//    private TagService tagService;
//
//    private TagDao tagDao;
//    private TagValidator tagValidator;
//
//    @BeforeEach
//    void setUp() {
//        this.tagDao = mock(TagDaoImpl.class);
//        this.tagValidator = mock(TagValidator.class);
//        this.tagService = new TagServiceImpl(tagDao, tagValidator);
//
//    }
//
//    @Test
//    void shouldReturnAllTags() {
//
//        List<Tag> tags = mock(ArrayList.class);
//        when(tags.size()).thenReturn(10);
//        when(tagDao.findAll(1L, 20L)).thenReturn(tags);
//
//        assertEquals(10, tagService.findAllTags(1L, 20L).size());
//        Mockito.verify(tagDao, Mockito.times(1)).findAll(1L, 20L);
//    }
//
//
//    @Test
//    void shouldFindTagById() {
//
//        Tag tag = mock(Tag.class);
//        when(tagDao.find(1L)).thenReturn(Optional.of(tag));
//
//        assertEquals(tag, tagService.findTagById(1L));
//        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenTagByIdNotFound() {
//
//        Tag tag = new Tag();
//        tag.setId(1L);
//        when(tagDao.find(tag.getId())).thenThrow(TagNotFoundException.class);
//
//        assertThrows(TagNotFoundException.class, () -> {
//            tagService.findTagById(tag.getId());
//        });
//        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
//    }
//
//    @Test
//    void shouldSaveTag() {
//
//        Tag tag = mock(Tag.class);
//        when(tagDao.save(tag)).thenReturn(1L);
//        Long expectedId = 1L;
//        assertEquals(expectedId, tagService.saveTag(tag));
//        Mockito.verify(tagDao, Mockito.times(1)).save(tag);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenTagNotSaved() {
//
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("tag 1");
//        when(tagDao.save(tag)).thenThrow(IllegalArgumentException.class);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            tagService.saveTag(tag);
//        });
//        Mockito.verify(tagDao, Mockito.times(1)).findByTagName("tag 1");
//        Mockito.verify(tagDao, Mockito.times(1)).save(tag);
//    }
//
//    @Test
//    void shouldUpdateTag() {
//
//        Tag tag = mock(Tag.class);
//        tagService.updateTag(tag);
//        Mockito.verify(tagDao, Mockito.times(1)).update(tag);
//
//    }
//
//    @Test
//    void shouldDeleteTag() {
//
//        Tag tag = mock(Tag.class);
//        when(tagDao.find(1L)).thenReturn(Optional.ofNullable(tag));
//        tagService.deleteTag(1L);
//        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
//        Mockito.verify(tagDao, Mockito.times(1)).delete(1L);
//
//    }
//
//    @Test
//    void shouldThrowExceptionDeleteCertificate() {
//
//        when(tagDao.find(1L)).thenThrow(TagNotFoundException.class);
//        assertThrows(TagNotFoundException.class, () -> {
//            tagService.deleteTag(1L);
//        });
//        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
//        Mockito.verify(tagDao, Mockito.times(0)).delete(1L);
//    }
//
//    @Test
//    void shouldFindUsersMostWidelyUsedTag() {
//
//        Tag tag = mock(Tag.class);
//        when(tagDao.getUsersMostWidelyUsedTag()).thenReturn(Optional.of(tag));
//
//        assertEquals(tag, tagService.getUsersMostWidelyUsedTag());
//        Mockito.verify(tagDao, Mockito.times(1)).getUsersMostWidelyUsedTag();
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUsersMostWidelyUsedTagNotFound() {
//
//        when(tagDao.getUsersMostWidelyUsedTag()).thenThrow(TagNotFoundException.class);
//        assertThrows(TagNotFoundException.class, () -> {
//            tagService.getUsersMostWidelyUsedTag();
//        });
//        Mockito.verify(tagDao, Mockito.times(1)).getUsersMostWidelyUsedTag();
//    }
}