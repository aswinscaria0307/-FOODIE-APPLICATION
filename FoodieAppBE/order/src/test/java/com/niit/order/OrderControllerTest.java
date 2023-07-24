package com.niit.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.order.controller.OrderController;
import com.niit.order.domain.Item;
import com.niit.order.domain.Order;
import com.niit.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    OrderService orderService;
    @InjectMocks
    OrderController orderController;
    @Autowired
    MockMvc mockMvc;
    private Item item;
    private Order order;

    @BeforeEach
    public void setUp(){
        item=new Item(1,"pizza",200,5.5f,"image1.jpg",1,"incart");
        order=new Order("1212","aswinscaria0307@gmail.com","Iritty", Arrays.asList(item));
        mockMvc= MockMvcBuilders.standaloneSetup(orderController).build();

    }
    @AfterEach
    public void tearDown(){
        item=null;
        order=null;
    }
    public static String convertToJson(final Object object)
    {
        String result="";
        ObjectMapper mapper=new ObjectMapper();
        try {
            result=mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  result;
    }
    @Test
    public void testInsertOrderSuccess() throws Exception {
        when(orderService.insertOrder(order)).thenReturn(order);
        mockMvc.perform(
                post("/api/v1/order/insertOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(order))
                        .requestAttr("attr1", order.getCustomerId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(1)).insertOrder(order);
    }
    @Test
    public void testInsertOrderFailure() throws Exception {
        mockMvc.perform(
                        post("/api/v1/order/insertOrder")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(order))
                                .requestAttr("attr1","sample@gmail.com"))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
            verify(orderService,times(0)).insertOrder(order);
    }
    @Test
    public void testGetItemsSuccess() throws Exception {
        when(orderService.getItems(item.getStatus(),order.getCustomerId())).thenReturn(Collections.singletonList(item));
        mockMvc.perform(get("/api/v1/order/getItems/incart/aswinscaria0307@gmail.com"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(orderService,times(2)).getItems(eq("incart"), eq("aswinscaria0307@gmail.com"));
    }
    @Test
    public void testGetItemsFailure() throws Exception {
        when(orderService.getItems(eq("incart"), eq("test@gmail.com"))).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/order/getItems/incart/test@gmail.com"))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());
        verify(orderService, times(2)).getItems(eq("incart"), eq("test@gmail.com"));
    }
    @Test
    public void testAddItemSuccess() throws Exception {
        when(orderService.addItem(order.getCustomerId(),item)).thenReturn(true);
        mockMvc.perform(post("/api/v1/order/addItem/aswinscaria0307@gmail.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(item))
                                .requestAttr("attr1", order.getCustomerId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(1)).addItem(order.getCustomerId(),item);
    }

    @Test
    public void testAddItemFailure() throws Exception {
        mockMvc.perform(post("/api/v1/order/addItem/aswinscaria0307@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(item))
                        .requestAttr("attr1", "sample@gmail.com"))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(0)).addItem(order.getCustomerId(),item);
    }
    @Test
    public void testRemoveItemSuccess() throws Exception {
        when(orderService.removeItem(order.getCustomerId(),item)).thenReturn(true);
        mockMvc.perform(post("/api/v1/order/removeItem/aswinscaria0307@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(item))
                        .requestAttr("attr1", order.getCustomerId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(1)).removeItem(order.getCustomerId(),item);
    }

    @Test
    public void testRemoveItemFailure() throws Exception {
        mockMvc.perform(post("/api/v1/order/removeItem/aswinscaria0307@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(item))
                        .requestAttr("attr1", "sample@gmail.com"))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(0)).removeItem(order.getCustomerId(),item);
    }
    @Test
    public void testCancelOrderSuccess()throws Exception{
        when(orderService.cancelOrder(order.getCustomerId(),Arrays.asList(item))).thenReturn(true);
        mockMvc.perform(delete("/api/v1/order/cancelOrder/aswinscaria0307@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(Arrays.asList(item)))
                .requestAttr("attr1","aswinscaria0307@gmail.com"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(1)).cancelOrder(order.getCustomerId(),Arrays.asList(item));
    }
    @Test
    public void testCancelOrderFailure()throws Exception{
        mockMvc.perform(delete("/api/v1/order/cancelOrder/aswinscaria0307@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(Arrays.asList(item)))
                        .requestAttr("attr1","sample@gmail.com"))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(orderService,times(0)).cancelOrder(order.getCustomerId(),Arrays.asList(item));
    }
    @Test
    public void testPlaceOrderSuccess()throws Exception{
        when(orderService.placeOrder("aswinscaria0307@gmail.com",Arrays.asList(item))).thenReturn(true);
        mockMvc.perform(post("/api/v1/order/placeOrder/aswinscaria0307@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(Arrays.asList(item)))
                        .requestAttr("attr1",order.getCustomerId()))
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print());
        verify(orderService,times(1)).placeOrder(order.getCustomerId(),Arrays.asList(item));
    }
    @Test
    public void testPlaceFailure()throws Exception{
        mockMvc.perform(post("/api/v1/order/placeOrder/aswinscaria0307@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(Arrays.asList(item)))
                        .requestAttr("attr1","sample@gmail.com"))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());
        verify(orderService,times(0)).placeOrder(order.getCustomerId(),Arrays.asList(item));
    }

}
