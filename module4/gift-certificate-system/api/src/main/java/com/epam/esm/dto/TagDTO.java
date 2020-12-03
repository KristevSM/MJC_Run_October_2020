package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TagDTO extends RepresentationModel<TagDTO> implements Serializable {
    private static final long serialVersionUID = -1L;
    private Long id;
    private String name;
}
