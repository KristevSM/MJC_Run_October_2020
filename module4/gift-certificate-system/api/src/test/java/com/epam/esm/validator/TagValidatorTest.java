package com.epam.esm.validator;

import com.epam.esm.model.Tag;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
class TagValidatorTest {

    private TagValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TagValidator();
    }

    @Test
    void shouldThrowErrorForLongName() {

        Tag validTag = Tag.builder()
                .name("Tag Name")
                .build();

        BindingResult result = new BeanPropertyBindingResult(validTag, "tag");
        validator.validate(validTag, result);
        System.out.println("Errors count: " + result.getAllErrors().size());

        Tag inValidTag = Tag.builder()
                .name("dlqbkmggdlgcekupeaoogaadudduwjdyxihdxzfpn")
                .build();

        validator.validate(inValidTag, result);
        System.out.println("Errors count: " + result.getAllErrors().size());
        result.getAllErrors().forEach(System.out::println);

        assertEquals(1, result.getAllErrors().size());
    }
}