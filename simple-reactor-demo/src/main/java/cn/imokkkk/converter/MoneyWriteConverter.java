package cn.imokkkk.converter;

import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

/**
 * @author ImOkkkk
 * @date 2022/4/16 21:36
 * @since 1.0
 */
public class MoneyWriteConverter implements Converter<Money, Long> {

  @Override
  public Long convert(Money money) {
    return money.getAmountMinorLong();
  }
}
