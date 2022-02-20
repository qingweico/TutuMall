package cn.qingweico.config;


import cn.qingweico.service.ProductSellDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import java.util.Objects;

/**
 * 任务调度
 *
 * @author 周庆伟
 * @date 2020/10/8
 */
@Configuration
public class QuartzConfiguration {

    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactory;
    @Autowired
    private CronTriggerFactoryBean productSellDailyTriggerFactory;

    /**
     * 创建jobDetailFactory并返回
     *
     * @return MethodInvokingJobDetailFactoryBean
     */
    @Bean(name = "jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail() {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactoryBean.setName("product_sell_daily_job");
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        jobDetailFactoryBean.setConcurrent(false);
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");
        return jobDetailFactoryBean;
    }

    /**
     * 创建cronTriggerFactory并返回
     *
     * @return CronTriggerFactoryBean
     */
    @Bean("productSellDailyTriggerFactory")
    public CronTriggerFactoryBean createProductSellDailyTrigger() {
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        triggerFactory.setName("product_sell_daily_trigger");
        triggerFactory.setGroup("job_product_sell_daily_group");
        triggerFactory.setJobDetail(Objects.requireNonNull(jobDetailFactory.getObject()));
        triggerFactory.setCronExpression("0 0 0 * * ? *");
        return triggerFactory;
    }

    /**
     * 创建调度工厂并返回
     *
     * @return SchedulerFactoryBean
     */
    @Bean("schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());
        return schedulerFactory;
    }
}
