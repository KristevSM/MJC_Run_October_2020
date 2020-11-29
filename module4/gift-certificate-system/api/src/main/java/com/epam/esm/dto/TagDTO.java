package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Builder
@Data
public class TagDTO extends RepresentationModel<TagDTO> implements Serializable {
    private static final long serialVersionUID = -1L;
    private Long id;
    private String name;
}
