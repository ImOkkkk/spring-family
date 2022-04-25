package cn.imokkkk.converter;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

/**
 * @author ImOkkkk
 * @date 2022/4/16 21:34
 * @since 1.0
 */
public class MoneyReadConverter implements Converter<Long, Money> {

  @Override
  public Money convert(Long aLong) {
    return Money.ofMinor(CurrencyUnit.of("CNY"), aLong);
  }
}
