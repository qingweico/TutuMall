package cn.qingweico.entity.bo;

import cn.qingweico.entity.Award;
import cn.qingweico.entity.UserAwardMap;
import lombok.Data;

/**
 * @author:qiming
 * @date: 2021/11/4
 */
@Data
public class AwardVO {
   private UserAwardMap userAwardMap;
   private Integer usedStatus;
   private Award award;

}
