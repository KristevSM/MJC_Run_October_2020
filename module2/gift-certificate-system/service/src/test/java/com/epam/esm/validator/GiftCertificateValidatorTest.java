package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GiftCertificateValidatorTest {

    private GiftCertificateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new GiftCertificateValidator();
    }

    @Test
    void shouldThrowErrorForInvalidCertificate() {
        GiftCertificate validCertificate = GiftCertificate.builder()
                .name("New certificate")
                .description("Some description")
                .price(BigDecimal.valueOf(100D))
                .createDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now())
                .duration(6)
                .tags(new ArrayList<>())
                .build();

        BindingResult result = new BeanPropertyBindingResult(validCertificate, "giftCertificate");
        validator.validate(validCertificate, result);
        System.out.println("Errors count: " + result.getAllErrors().size());
        assertFalse(result.hasErrors());

        GiftCertificate inValidCertificate = GiftCertificate.builder()
                .name("dlqbkmggdlgcekupeaoogaadudduwjdyxihdxzfpn")
                .description("wmrjkwqcrpsndahcsdshxrrhdcylygohzxlocbhqiiueobqllipiyxjyeldnsemdymdlwexbkqwgokyhzxnwcrosr" +
                        "smlkcmpongxzuuizdwqgnklglqobdrhpwtbudivcdjyzheefskryltblnqsleqsignrntnojzecnfuvcgjjyoahsbvgjbvo" +
                        "kfwviqrdqaknxwglxsyfmbxxxafskegeerxbejftfhdxsgyngbetifcvyvluxklbonj\n" +
                        "\n")
                .price(BigDecimal.valueOf(-1D))
                .createDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now().minusDays(1))
                .duration(-6)
                .build();

        validator.validate(inValidCertificate, result);
        System.out.println("Errors count: " + result.getAllErrors().size());
        result.getAllErrors().forEach(System.out::println);

        assertEquals(5, result.getAllErrors().size());

    }
}