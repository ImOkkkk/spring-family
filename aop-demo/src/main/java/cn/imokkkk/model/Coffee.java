package cn.imokkkk.model;

import cn.imokkkk.model.BaseEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:25
 * @since 1.0
 */
@Entity
@Table(name = "T_COFFEE")
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {
  private String name;

  @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
      parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
  private Money money;

}
