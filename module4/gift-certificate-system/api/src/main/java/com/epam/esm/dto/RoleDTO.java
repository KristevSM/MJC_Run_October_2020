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
public class RoleDTO extends RepresentationModel<RoleDTO> implements Serializable {

    private static final long serialVersionUID = -1L;
    private String name;
}
