package com.niit.order.service;

import com.niit.order.domain.Item;
import com.niit.order.domain.Order;
import com.niit.order.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService{
    @Autowired
    IOrderRepository iOrderRepository;
    @Override
    public List<Item> getItems(String status, String email) {
        List<Order> orders = iOrderRepository.findByCustomerId(email);
        List<Item> items = new ArrayList<>();
        for (Order order : orders) {
            for (Item item : order.getItems()) {
                if (item.getStatus().equals(status)) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public Order insertOrder(Order order){
        Item receivedItem = order.getItems().get(0);
        List<Order> orders = iOrderRepository.findByCustomerId(order.getCustomerId());
        if (orders.isEmpty()) { // if there is no existing order for this customer
            return iOrderRepository.save(order); // create a new order
        } else {
            Order existingOrder = orders.get(0);
            boolean itemExists = false;
            for (Item item : existingOrder.getItems()) {
                if (item.getItemName().equals(receivedItem.getItemName())&&item.getStatus().equals(receivedItem.getStatus())) { // if the item already exists in the existing order
                    item.setCount(item.getCount() + 1); // update the count of the existing item
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) { // if the item does not exist in the existing order
                existingOrder.getItems().add(receivedItem); // add the new item to the existing order
            }
            return iOrderRepository.save(existingOrder); // update the existing order
        }
    }
    @Override
    public boolean addItem(String email,Item item) {
        List<Order> orders = iOrderRepository.findByCustomerId(email);
        if (orders != null && !orders.isEmpty()) {
            for (Order order : orders) {
                Item existingPizza = order.getItems().stream()
                        .filter(i -> i.getItemName().equals(item.getItemName())&&i.getStatus().equals(item.getStatus()))
                        .findFirst().orElse(null);
                if (existingPizza != null) {
                    int count = existingPizza.getCount() + 1;
                    existingPizza.setCount(count);
                }
                iOrderRepository.save(order);
            }
            return true;
        }
        return false;
    }
        @Override
        public boolean removeItem(String email, Item item) {
            List<Order> orders = iOrderRepository.findByCustomerId(email);
            if (orders != null && !orders.isEmpty()) {
                for (Order order : orders) {
                    Item existingItem = order.getItems().stream()
                            .filter(i -> i.getItemName().equals(item.getItemName())&&i.getStatus().equals(item.getStatus()))
                            .findFirst().orElse(null);
                    if (existingItem != null) {
                        int count = existingItem.getCount();
                        if (count > 1) {
                            count--;
                            existingItem.setCount(count);
                        } else if (count == 1) {
                            order.getItems().remove(existingItem);
                        }
                        iOrderRepository.save(order);
                    }
                }
                return true;
            }
            return false;
        }
    @Override
    public boolean cancelOrder(String email, List<Item> items) {
        List<Order> orders = iOrderRepository.findByCustomerId(email);
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                boolean orderUpdated = false;
                for (Item item : order.getItems()) {
                    if (item.getStatus().equals("ordered") && items.contains(item)) {
                        item.setStatus("cancelled");
                        orderUpdated = true;
                    }
                }
                if (orderUpdated) {
                    iOrderRepository.save(order);
                }
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean placeOrder(String email, List<Item> items) {
        List<Order> orders = iOrderRepository.findByCustomerId(email);
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                boolean orderUpdated = false;
                for (Item item : order.getItems()) {
                    if (item.getStatus().equals("incart") && items.contains(item)) {
                        item.setStatus("ordered");
                        orderUpdated = true;
                    }
                }
                if (orderUpdated) {
                    iOrderRepository.save(order);
                }
            }
            return true;
        }
        return false;
    }


}
