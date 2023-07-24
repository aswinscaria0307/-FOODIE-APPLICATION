package com.niit.order.controller;

import com.niit.order.domain.Item;
import com.niit.order.domain.Order;
import com.niit.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
//@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    @Autowired
    IOrderService iOrderService;
    @PostMapping("/insertOrder")
    public ResponseEntity insertOrder(@RequestBody Order order, HttpServletRequest httpServletRequest){
        if (order.getCustomerId().equals((String)httpServletRequest.getAttribute("attr1"))){
            return new ResponseEntity<>(iOrderService.insertOrder(order),HttpStatus.OK);
        }else
            return new ResponseEntity<>("Not Authorised to insert",HttpStatus.CONFLICT);
    }
    @GetMapping("/getItems/{status}/{email}")
    public ResponseEntity getItems(@PathVariable String status,@PathVariable String email){
        List<Item> items = iOrderService.getItems(status, email);
        if (items != null && !items.isEmpty()){
            return new ResponseEntity(iOrderService.getItems(status,email), HttpStatus.OK);
        }else
            return new ResponseEntity(iOrderService.getItems(status,email), HttpStatus.CONFLICT);    }
    @PostMapping("/addItem/{email}")
    public ResponseEntity addItem(@PathVariable String email, @RequestBody Item item,HttpServletRequest httpServletRequest){
        String attr1=(String)httpServletRequest.getAttribute("attr1");
        if (email.equals(attr1)) {
            return new ResponseEntity<>(iOrderService.addItem(email, item), HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Authorized to Add ",HttpStatus.CONFLICT);
    }
    @PostMapping("/removeItem/{email}")
    public ResponseEntity removeItem(@PathVariable String email, @RequestBody Item item,HttpServletRequest httpServletRequest){
        String attr1=(String)httpServletRequest.getAttribute("attr1");
        if (email.equals(attr1)) {
            System.out.println("yppppp");
            System.out.println(item);
            return new ResponseEntity<>(iOrderService.removeItem(email, item), HttpStatus.OK);
        }else
            return new ResponseEntity<>("Not Authorized to remove ",HttpStatus.CONFLICT);
    }
    @DeleteMapping("/cancelOrder/{email}")
    public ResponseEntity cancelOrder(@PathVariable String email,@RequestBody List<Item> items,HttpServletRequest httpServletRequest){
        if (email.equals((String)httpServletRequest.getAttribute("attr1"))) {
            return new ResponseEntity<>(iOrderService.cancelOrder(email, items), HttpStatus.OK);
        }else
        return new ResponseEntity<>("Not Authorized to cancel ",HttpStatus.CONFLICT);
    }
    @PostMapping("/placeOrder/{email}")
    public ResponseEntity placeOrder(@PathVariable String email,@RequestBody List<Item> items,HttpServletRequest httpServletRequest){
        if (email.equals((String)httpServletRequest.getAttribute("attr1"))){
            return new ResponseEntity<>(iOrderService.placeOrder(email,items),HttpStatus.OK);
        }else
        return new ResponseEntity<>("Not Authorized to place the order",HttpStatus.CONFLICT);
    }
}
