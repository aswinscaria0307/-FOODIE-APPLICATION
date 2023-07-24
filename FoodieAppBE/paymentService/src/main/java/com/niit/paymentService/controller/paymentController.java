package com.niit.paymentService.controller;

import com.niit.paymentService.domain.PaymentOrder;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/payment")
public class paymentController {
    //creating order for payment
    @PostMapping("/createOrder")
    @ResponseBody
    public ResponseEntity createOrder(@RequestBody PaymentOrder paymentOrder){
        com.razorpay.Order order1;
        try {
            var client=new RazorpayClient("rzp_test_pVaEM3TS2Oh7JN","KuubExufG6n0XJJ70YNwv7yU");
            JSONObject orderRequest=new JSONObject();
            orderRequest.put("amount", paymentOrder.getOrderAmount()*100);
            orderRequest.put("currency","INR");
            orderRequest.put("receipt", "tnx_235425");
            System.out.println(orderRequest);
            //creating new order
             order1=client.orders.create(orderRequest);
            System.out.println(order1);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(order1.toString(), HttpStatus.CREATED);
    }
}
