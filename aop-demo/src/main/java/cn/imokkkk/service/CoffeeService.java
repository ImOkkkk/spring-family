package cn.imokkkk.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import cn.imokkkk.model.Coffee;
import cn.imokkkk.repository.CoffeeRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:33
 * @since 1.0
 */
@Service
@Slf4j
public class CoffeeService {
  @Autowired private CoffeeRepository coffeeRepository;

  public Optional<Coffee> findOneCoffee(String name) {
    ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact().ignoreCase());
    Optional<Coffee> coffee =
        coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));
    log.info("Coffee Found: {}", coffee);
    return coffee;
  }
}
