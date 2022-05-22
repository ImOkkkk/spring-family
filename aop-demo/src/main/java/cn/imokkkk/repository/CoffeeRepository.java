package cn.imokkkk.repository;

import cn.imokkkk.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:32
 * @since 1.0
 */
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
