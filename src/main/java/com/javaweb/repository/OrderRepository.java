package com.javaweb.repository;

import com.javaweb.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByUser_Id(Pageable pageable, Long userId);
    Page<OrderEntity> findByUserId(Pageable pageable, Long userId);
    // Thêm phương thức để lấy đơn hàng theo userId và trạng thái không phải "CANCELLED"
    @Query("SELECT o FROM OrderEntity o WHERE o.user.id = :userId AND o.status <> :status")
    List<OrderEntity> findByUserIdAndStatusNot(@Param("userId") Long userId, @Param("status") String status);

    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o WHERE o.status = 'COMPLETED'")
    Double getTotalRevenue();

    @Query("SELECT COUNT(o) FROM OrderEntity o")
    Long getTotalOrderCount();

    @Query("SELECT FUNCTION('MONTH', o.order_date), SUM(o.totalAmount) FROM OrderEntity o WHERE FUNCTION('YEAR', o.order_date) = :year AND o.status = 'COMPLETED' GROUP BY FUNCTION('MONTH', o.order_date)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);
}
