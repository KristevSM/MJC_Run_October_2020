package com.epam.esm.validator;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.equals(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {

        Tag tag = (Tag) target;
        if (tag.getName() == null || tag.getName().length() > 40 || tag.getName().length() < 1) {
            errors.rejectValue("name", "tag.size");
        }
    }
}
