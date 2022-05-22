package cn.imokkkk.repository;

import cn.imokkkk.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:31
 * @since 1.0
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {

}
