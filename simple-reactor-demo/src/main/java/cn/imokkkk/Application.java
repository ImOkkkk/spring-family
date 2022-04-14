package cn.imokkkk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
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
  }
}
