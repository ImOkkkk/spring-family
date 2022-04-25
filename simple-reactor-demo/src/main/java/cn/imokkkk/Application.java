package cn.imokkkk;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import cn.imokkkk.converter.MoneyReadConverter;
import cn.imokkkk.converter.MoneyWriteConverter;
import cn.imokkkk.pojo.Coffee;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author ImOkkkk
 * @date 2022/4/14 21:20
 * @since 1.0
 */
@SpringBootApplication
@Slf4j
public class Application implements ApplicationRunner {
  private static final String KEY = "COFFEE_MENU";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ReactiveStringRedisTemplate redisTemplate;

  @Autowired
  private ReactiveMongoTemplate mongoTemplate;

  private CountDownLatch latch = new CountDownLatch(2);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

/*  @Override
  public void run(ApplicationArguments args) throws Exception {
    Flux.range(1, 6)
        .publishOn(Schedulers.elastic())  //后续代码执行在elastic线程池
        .doOnRequest(n -> log.info("Request {} number", n))
        .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
        .map(i -> {
          log.info("Publish {}, {}", Thread.currentThread(), i);
//          return 10 / (i - 3);
          return i;
        }).doOnComplete(() -> log.info("Publisher COMPLETE 2"))
        .subscribeOn(Schedulers.single())
        .onErrorResume(e -> {
          log.error("Exception{}", e.toString());
          return Mono.just(-1);
        })
//        .onErrorReturn(-1)
        .subscribe(i -> log.info("Subscribe {}:{}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber COMPLETE"),
            s -> s.request(4));
    Thread.sleep(2000);
  }*/

  @Bean
  ReactiveStringRedisTemplate reactiveStringRedisTemplate(
      ReactiveRedisConnectionFactory redisConnectionFactory) {
    return new ReactiveStringRedisTemplate(redisConnectionFactory);
  }

/*  @Override
  public void run(ApplicationArguments args) throws Exception {
    ReactiveHashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    CountDownLatch latch = new CountDownLatch(1);
    List<Coffee> list = jdbcTemplate.query("select * from t_coffee", (rs, i) -> Coffee.builder()
        .id(rs.getLong("id"))
        .name(rs.getString("name"))
        .price(rs.getLong("price"))
        .build());
    Flux.fromIterable(list).publishOn(Schedulers.single()).doOnComplete(() -> log.info("list OK !"))
        .flatMap(c -> {
          log.info("try to put {}, {}", c.getName(), c.getPrice());
          return hashOperations.put(KEY, c.getName(), String.valueOf(c.getPrice()));
        }).doOnComplete(() -> log.info("set OK !"))
        .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
        .doOnComplete(() -> log.info("expire OK !"))
        .onErrorResume(e -> {
          log.error("exception {}", e.getMessage());
          return Mono.just(false);
        }).subscribe(b -> log.info("Boolean:{}", b), e -> log.error("Exception{}", e.getMessage()),
            () -> latch.countDown());
    log.info("Waiting");
    latch.await();
  }*/


  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(
        Arrays.asList(new MoneyReadConverter(), new MoneyWriteConverter()));
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
//    startFromInsertion(()->log.info("Runnable"));
    startFromInsertion(() -> {
      log.info("Runnable");
      decreaseHighPrice();
    });
    log.info("after starting");
//    decreaseHighPrice();
    latch.await();
    log.info("end");
  }

  private void startFromInsertion(Runnable runnable) {
    mongoTemplate.insertAll(initCoffee())
        .publishOn(Schedulers.elastic())
        .doOnNext(c -> log.info("Next: {}", c))
        .doOnComplete(runnable)
        .doFinally(s -> {
          latch.countDown();
          log.info("Finnally 1, {}", s);
        })
        .count()
        .subscribe(c -> log.info("Insert {} records", c));
  }

  private void decreaseHighPrice() {
    mongoTemplate.updateMulti(query(where("price").gte(3000L)),
            new Update().inc("price", -500L)
                .currentDate("updateTime"), Coffee.class)
        .doFinally(s -> {
          latch.countDown();
          log.info("Finnally 2, {}", s);
        })
        .subscribe(r -> log.info("Result is {}", r));
  }

  private List<Coffee> initCoffee() {
    Coffee espresso = Coffee.builder()
        .name("espresso")
        .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    Coffee latte = Coffee.builder()
        .name("latte")
        .price(Money.of(CurrencyUnit.of("CNY"), 30.0))
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    return Arrays.asList(espresso, latte);
  }

}
