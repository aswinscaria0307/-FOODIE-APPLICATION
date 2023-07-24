package com.niit.paymentService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class PaymentOrder {
    private int orderAmount;
    private String orderInfo;
}
