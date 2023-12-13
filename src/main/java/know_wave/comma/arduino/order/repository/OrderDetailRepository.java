package know_wave.comma.arduino.order.repository;

import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("select od from OrderDetail od join fetch od.order o, od.arduino oa where o = :order")
    List<OrderDetail> findFetchAllByOrder(Order order);
}
