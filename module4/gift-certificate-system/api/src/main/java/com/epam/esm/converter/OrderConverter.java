package com.epam.esm.converter;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class OrderConverter {

    private final GiftCertificateConverter giftCertificateConverter;
    private final UserConverter userConverter;

    public OrderDTO convertFromEntity(Order order) {
        if (order == null) {
            return OrderDTO.builder().build();
        }
        OrderDTO orderDTO = OrderDTO.builder()
                .id(order.getId())
                .cost(order.getCost())
                .orderDate(order.getOrderDate())
                .userDTO(userConverter.convertFromEntity(order.getUser()))
                .build();
        if (order.getGiftCertificate() != null) {
            orderDTO.setGiftCertificateDTO(giftCertificateConverter.convertFromEntity(order.getGiftCertificate()));
        }
        return orderDTO;
    }

    public Order convertFromDTO(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return Order.builder().build();
        }
        Order order = Order.builder()
                .id(orderDTO.getId())
                .cost(orderDTO.getCost())
                .orderDate(orderDTO.getOrderDate())
                .user(userConverter.convertFromDTO(orderDTO.getUserDTO()))
                .build();
        if (orderDTO.getGiftCertificateDTO() != null) {
            order.setGiftCertificate(giftCertificateConverter.convertFromDTO(orderDTO.getGiftCertificateDTO()));
        }
        return order;
    }

}
