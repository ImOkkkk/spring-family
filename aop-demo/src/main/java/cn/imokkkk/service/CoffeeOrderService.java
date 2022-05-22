package cn.imokkkk.service;

import cn.imokkkk.model.Coffee;
import cn.imokkkk.model.CoffeeOrder;
import cn.imokkkk.model.OrderState;
import cn.imokkkk.repository.CoffeeOrderRepository;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:37
 * @since 1.0
 */
@Service
@Slf4j
public class CoffeeOrderService {

  @Autowired private CoffeeOrderRepository coffeeOrderRepository;

  public CoffeeOrder createOrder(String customer, Coffee... coffee) {
    CoffeeOrder coffeeOrder =
        CoffeeOrder.builder()
            .customer(customer)
            .items(new ArrayList<>(Arrays.asList(coffee)))
            .state(OrderState.INIT)
            .build();
    CoffeeOrder saved = coffeeOrderRepository.save(coffeeOrder);
    log.info("New Order: {}", saved);
    return saved;
  }

  public boolean updateState(CoffeeOrder order, OrderState state) {
    if (state.compareTo(order.getState()) <= 0) {
      log.warn("Wrong State order: {}, {}", state, order.getState());
      return false;
    }
    order.setState(state);
    coffeeOrderRepository.save(order);
    log.info("Updated Order: {}", order);
    return true;
  }
}
