package org.example.deliveryweb.repository;

import com.example.entity.order.Orders;
import com.example.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findOrdersByStatus(String status);

    List<Orders> findByCustomer(User user);
}
