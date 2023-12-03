package know_wave.comma.order_.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order_.entity.OrderInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

// descending order of order creation date (asc)
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String> {

    List<OrderInfo> findAllByAccount(Account account);

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.account " +
            "where oi.status = 'APPLIED' " +
            "order by oi.createdDate")
    List<OrderInfo> findAllApplyStatus(Pageable pageable);

    @Query("select oi " +
            "from OrderInfo oi " +
            "where oi.status = 'APPLIED' " +
            "and oi.account in :list " +
            "and oi.orderNumber not in :orderNumbers ")
    List<OrderInfo> findAllApplyStatusByRelatedAccount(Set<Account> list, List<String> orderNumbers);

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.orders oo " +
            "join fetch oi.account " +
            "join fetch oo.arduino " +
            "where oi.orderNumber = :orderNumber")
    Optional<OrderInfo> findFetchOrdersArduinoAccountById(String orderNumber);

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.account " +
            "where oi.orderNumber = :orderNumber")
    Optional<OrderInfo> findFetchAccountById(String orderNumber);

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.account " +
            "where oi.status = 'CANCELLATION_REQUEST' " +
            "order by oi.createdDate")
    List<OrderInfo> findAllCancelRequestStatus(Pageable pageable);

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.account " +
            "order by oi.createdDate")
    List<OrderInfo> findAllOrderByCreateDate(Pageable pageable);
}
