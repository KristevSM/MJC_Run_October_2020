package com.epam.esm.dto;

import com.epam.esm.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class OrderDto extends RepresentationModel<Order> implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;
    private BigDecimal purchaseCost;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private ZonedDateTime purchaseTime;
}
