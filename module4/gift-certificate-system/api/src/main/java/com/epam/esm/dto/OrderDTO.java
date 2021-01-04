package com.epam.esm.dto;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Builder
@Data
public class OrderDTO extends RepresentationModel<OrderDTO> implements Serializable {

    private static final long serialVersionUID = -1L;
    private Long id;
    private UserDTO userDTO;
    private GiftCertificateDTO giftCertificateDTO;
    private BigDecimal cost;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private ZonedDateTime orderDate;
}
