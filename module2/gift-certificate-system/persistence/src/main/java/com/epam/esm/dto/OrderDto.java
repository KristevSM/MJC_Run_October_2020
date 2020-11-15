package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class OrderDto {
    private Long id;
    private BigDecimal purchaseCost;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private ZonedDateTime purchaseTime;
}
