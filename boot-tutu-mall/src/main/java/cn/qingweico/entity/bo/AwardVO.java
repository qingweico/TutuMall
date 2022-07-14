package cn.qingweico.entity.bo;

import cn.qingweico.entity.Award;
import cn.qingweico.entity.UserReceivingAwardRecord;
import lombok.Data;

/**
 * @author zqw
 * @date 2021/11/4
 */
@Data
public class AwardVO {
   private UserReceivingAwardRecord userAwardMap;
   private Integer usedStatus;
   private Award award;
}
