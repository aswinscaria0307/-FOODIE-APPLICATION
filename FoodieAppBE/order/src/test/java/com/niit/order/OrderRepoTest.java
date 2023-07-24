package com.niit.order;

import com.niit.order.domain.Item;
import com.niit.order.domain.Order;
import com.niit.order.repository.IOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class OrderRepoTest {
    @Autowired
    IOrderRepository iOrderRepository;
    private List<Item> item;
    private Order order;

    @BeforeEach
    public void setUp(){
    Item i=new Item(1,"pizza",200,5.5f,"http://...",1,"incart");
    item= new ArrayList<>();
    item.add(i);
    order=new Order("1212","a@gmail.com","Iritty",item);
    }
    @AfterEach
    public void tearDown(){
    item=null;
    order=null;
    iOrderRepository.deleteAll();
    }
    @Test
    public void getOrders(){
        List<Order> orderList=new ArrayList<>();
        orderList.add(order);
        iOrderRepository.insert(order);
        assertEquals(orderList,iOrderRepository.findByCustomerId(order.getCustomerId()));
    }
    @Test
    public void getOrdersFailure(){
        List<Order> orderList=new ArrayList<>();
        orderList.add(order);
        iOrderRepository.insert(order);
        order.setId("22");
        orderList.add(order);
        assertNotEquals(orderList,iOrderRepository.findByCustomerId(order.getCustomerId()));
    }
    @Test
    public void insertOrder(){
        Order theOrder=iOrderRepository.save(order);
        assertEquals(order.getCustomerId(),theOrder.getCustomerId());
        assertEquals(order.getItems(),theOrder.getItems());
    }
    @Test
    public void insertOrderFailure(){
        Order theOrder=iOrderRepository.save(order);
        assertNotEquals("aswin@gmail.com",theOrder.getCustomerId());
        assertNotEquals("Kannur",theOrder.getBillingAddress());
    }

}
