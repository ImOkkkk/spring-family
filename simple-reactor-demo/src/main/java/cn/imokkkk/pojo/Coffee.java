package cn.imokkkk.pojo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

/**
 * @author ImOkkkk
 * @date 2022/4/16 19:13
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coffee {

/*  private Long id;

  private String name;

  private Long price;*/

  private String id;

  private String name;

  private Money price;

  private Date createTime;

  private Date updateTime;


}
