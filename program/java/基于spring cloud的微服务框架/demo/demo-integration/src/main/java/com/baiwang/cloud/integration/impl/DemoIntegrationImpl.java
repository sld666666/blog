package com.baiwang.cloud.integration.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baiwang.cloud.integration.DemoIntegration;

/**
 * 封装引入远程接口实现类，建议使用feign访问springcloud服务，使用RestTemplat访问普通http服务</br>
 * 模板测试接口，可以删除</br>
 * 
 * @author wuyuegang
 * 
 */
@Component
public class DemoIntegrationImpl implements DemoIntegration
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 
     * @see com.baiwang.cloud.integration.demo.integration.DemoIntegration#getInfoFromRemote(java.lang.String)
     */
    public String getInfoFromRemote(String itemCode)
    {
        long start = System.currentTimeMillis();
        // 输出服务入口日志，用于服务监控统计,必须输出
        logger.info("getInfoFromRemote begin request入参:{}", itemCode);
        
        // 从远程服务查询数据，返回
        
        // TODO: get result from remote service
        
        // 输出服务出口日志，包括入参摘要，返回结果信息摘要和接口耗时cost
        // 日志标准格式 : 入参:aaa 返回结果:bbb cost:[100]ms 业务标识-success
        if (logger.isInfoEnabled())
        {
            logger.info(" getInfoFromRemote success request入参:{} 返回结果:{} cost {} ms",
                "",
                "detail",
                (System.currentTimeMillis() - start));
        }
        return "detail";
    }
}
