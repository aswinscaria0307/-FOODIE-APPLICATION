package com.niit.order.repository;

import com.niit.order.domain.Item;
import com.niit.order.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends MongoRepository<Order,Integer> {
    @Query("{'customerId': ?0}")
    List<Order> findByCustomerId(String email);
}
