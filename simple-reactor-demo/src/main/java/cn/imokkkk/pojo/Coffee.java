package cn.imokkkk.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  private Long id;

  private String name;

  private Long price;

}
