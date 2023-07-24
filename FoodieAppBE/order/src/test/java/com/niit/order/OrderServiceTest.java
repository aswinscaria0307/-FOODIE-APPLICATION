package com.niit.order;

import com.niit.order.domain.Item;
import com.niit.order.domain.Order;
import com.niit.order.repository.IOrderRepository;
import com.niit.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private IOrderRepository iOrderRepository;
    @InjectMocks
    private OrderService orderService;
    private Item item;
    private Order order;

    @BeforeEach
    public void setUp(){
        item=new Item(1,"pizza",200,5.5f,"image1.jpg",1,"incart");
        order=new Order("1212","test@example.com","Iritty", Arrays.asList(item));
    }
    @AfterEach
    public void tearDown(){
        item=null;
        order=null;
//        iOrderRepository.deleteAll();
    }
    @Test
    public void testGetItemsSuccess() {
        String email = "test@example.com";
        String status = "incart";
        List<Order> orders=Arrays.asList(order);
        when(iOrderRepository.findByCustomerId(email)).thenReturn(orders);
        List<Item> items=orderService.getItems(status,email);
        assertEquals(1,items.size());
        assertEquals(item, items.get(0));
        verify(iOrderRepository, times(1)).findByCustomerId(email);
    }
    @Test
    public void testGetItemsFailure()  {
        String email = "t@example.com";
        String status = "shipped";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.emptyList());
        List<Item> items=orderService.getItems(status,email);
        assertTrue(items.isEmpty());
        verify(iOrderRepository, times(1)).findByCustomerId(email);
    }
    @Test
    public void testInsertOrder_New_Order(){
        String email = "test@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.emptyList());
        when(iOrderRepository.save(order)).thenReturn(order);
        Order result = orderService.insertOrder(order);
        assertNotNull(result);
        assertEquals(order, result);
        verify(iOrderRepository, times(1)).findByCustomerId(order.getCustomerId());
        verify(iOrderRepository, times(1)).save(order);
    }
    @Test
    public void testInsertOrder_UpdateExistingOrder_ExistingItem(){
        String email = "test@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order));
        when(iOrderRepository.save(order)).thenReturn(order);
        Order result = orderService.insertOrder(order);
        assertNotNull(result);
        assertEquals(order, result);
        assertEquals(2, order.getItems().get(0).getCount());

        verify(iOrderRepository, times(1)).findByCustomerId(order.getCustomerId());
        verify(iOrderRepository, times(1)).save(order);
    }
    @Test
    public void testInsertOrder_UpdateExistingOrder_NewItem_Success(){
        String email = "test@example.com";
        Order order1=new Order("1212","test@example.com","Iritty", new ArrayList<>());

        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order1));
        when(iOrderRepository.save(order)).thenReturn(order);
        Order result = orderService.insertOrder(order);
        assertNotNull(result);
        assertEquals(order, result);
        assertTrue(order1.getItems().contains(item));

        verify(iOrderRepository, times(1)).findByCustomerId(order.getCustomerId());
        verify(iOrderRepository, times(1)).save(order);
    }
    @Test
    public void testAddItem_ItemExistsInOrder_Success(){
        String email = "test@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order));
        when(iOrderRepository.save(order)).thenReturn(order);
        boolean result=orderService.addItem(email,item);
        assertTrue(result);
        verify(iOrderRepository,times(1)).findByCustomerId(email);
        verify(iOrderRepository,times(1)).save(order);
    }
    @Test
    public void testAddItem_ItemDoesNotExistInOrder_Success(){
        String email = "test@example.com";
        Item newItem = new Item(2,"Mrgta",200,5.5f,"image1.jpg",3,"shipped");
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order));
        when(iOrderRepository.save(order)).thenReturn(order);
        boolean result=orderService.addItem(email,newItem);
        assertTrue(result);
        assertTrue(order.getItems().contains(item));
        assertEquals(1,item.getCount());
        verify(iOrderRepository, times(1)).findByCustomerId(email);
        verify(iOrderRepository, times(1)).save(order);
    }
    @Test
    public void testAddItem_NoOrdersForCustomer_Failure() {
        String email = "t@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.emptyList());
        boolean result=orderService.addItem(email,item);
        assertFalse(result);
        verify(iOrderRepository, times(1)).findByCustomerId(email);
        verify(iOrderRepository, never()).save(any());
    }
    @Test
    public void testRemoveItem_ItemExistsInOrder_GT1_Success() {
        String email = "t@example.com";
        Item item1=new Item(1,"pizza",200,5.5f,"image1.jpg",3,"shipped");
        Order order1=new Order("1212","t@example.com","Iritty", Arrays.asList(item1));
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order1));
        when(iOrderRepository.save(order1)).thenReturn(order1);
        boolean result = orderService.removeItem(email, item1);
        assertTrue(result);
        assertEquals(2, item1.getCount());

        verify(iOrderRepository, times(1)).findByCustomerId(email);
        verify(iOrderRepository, times(1)).save(order1);
    }
//    @Test
//    public void testRemoveItem_ItemExistsInOrder_EQ1_Success() {
//        String email = "t@example.com";
//        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order));
//        when(iOrderRepository.save(order)).thenReturn(order);
//        boolean result = orderService.removeItem(email, item);
//        assertTrue(result);
//        assertFalse(order.getItems().contains(item));
//        assertEquals(0, order.getItems().size());
//        verify(iOrderRepository, times(1)).findByCustomerId(email);
//        verify(iOrderRepository, times(1)).save(order);
//    }
    @Test
    public void testRemoveItem_ItemDoesNotExistInOrder_Success() {
        String email = "test@example.com";
        Item newItem = new Item(2,"Mrgta",200,5.5f,"image1.jpg",3,"shipped");

        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order));
        boolean result = orderService.removeItem(email, newItem);
        assertTrue(result);
        assertEquals(1, item.getCount());
        verify(iOrderRepository, times(1)).findByCustomerId(email);
        verify(iOrderRepository,never()).save(any());
    }

    @Test
    public void testRemoveItem_NoOrdersForCustomer_Failure() {
        String email = "test@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.emptyList());
        boolean result = orderService.removeItem(email, item);
        assertFalse(result);
        verify(iOrderRepository, times(1)).findByCustomerId(email);
        verify(iOrderRepository, never()).save(any());
    }
    @Test
    public void testCancelOrder_Order_Exists_and_status_Ordered(){
        String email = "test@example.com";
        Item item1=new Item(1,"pizza",200,5.5f,"image1.jpg",3,"ordered");
        Order order1=new Order("1212","t@example.com","Iritty", Arrays.asList(item1));
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order1));
        when(iOrderRepository.save(order1)).thenReturn(order1);
        boolean result=orderService.cancelOrder(email,Arrays.asList(item1));
        assertTrue(result);
        assertEquals("cancelled",item1.getStatus());
        verify(iOrderRepository,times(1)).findByCustomerId(email);
        verify(iOrderRepository,times(1)).save(order1);
    }
    @Test
    public void testCancelOrder_Failure(){
        String email = "t@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.emptyList());
        boolean result=orderService.cancelOrder(email,Arrays.asList(item));
        assertFalse(result);
        verify(iOrderRepository,times(1)).findByCustomerId(email);
    }
    @Test
    public void testPlaceOrder_Order_Exists_and_status_incart(){
        String email = "test@example.com";
        Item item1=new Item(1,"pizza",200,5.5f,"image1.jpg",3,"incart");
        Order order1=new Order("1212","t@example.com","Iritty", Arrays.asList(item1));
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.singletonList(order1));
        when(iOrderRepository.save(order1)).thenReturn(order1);
        boolean result=orderService.placeOrder(email,Arrays.asList(item1));
        assertTrue(result);
        assertEquals("ordered",item1.getStatus());
        verify(iOrderRepository,times(1)).findByCustomerId(email);
        verify(iOrderRepository,times(1)).save(order1);
    }
    @Test
    public void testPlaceOrder_Failure(){
        String email = "t@example.com";
        when(iOrderRepository.findByCustomerId(email)).thenReturn(Collections.emptyList());
        boolean result=orderService.placeOrder(email,Arrays.asList(item));
        assertFalse(result);
        verify(iOrderRepository,times(1)).findByCustomerId(email);
    }

}
