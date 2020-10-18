package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = SpringConfig.class)
class TagServiceImplTest {

    @Autowired
    TagService tagService;

    @Autowired
    private TagDao tagDao;

    @BeforeEach
    void setUp() {

    }


}