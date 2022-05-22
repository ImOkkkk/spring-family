package cn.imokkkk;

import cn.imokkkk.model.Coffee;
import cn.imokkkk.model.CoffeeOrder;
import cn.imokkkk.model.OrderState;
import cn.imokkkk.repository.CoffeeRepository;
import cn.imokkkk.service.CoffeeOrderService;
import cn.imokkkk.service.CoffeeService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:41
 * @since 1.0
 */
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableAspectJAutoProxy
public class SpringBucksApplication implements ApplicationRunner {

  @Autowired private CoffeeRepository coffeeRepository;
  @Autowired private CoffeeService coffeeService;
  @Autowired private CoffeeOrderService orderService;

  public static void main(String[] args) {
    SpringApplication.run(SpringBucksApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("All Coffee: {}", coffeeRepository.findAll());
    Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
    if (latte.isPresent()) {
      CoffeeOrder order = orderService.createOrder("Li Lei", latte.get());
      log.info("Update INIT to PAID: {}", orderService.updateState(order, OrderState.PAID));
      log.info("Update PAID to INIT: {}", orderService.updateState(order, OrderState.INIT));
    }
  }
}
