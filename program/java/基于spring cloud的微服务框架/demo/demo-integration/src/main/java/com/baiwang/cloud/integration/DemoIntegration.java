package com.baiwang.cloud.integration;

/**
 * 远程接口代理类,封装引入远程接口实现类，建议使用feign访问springcloud服务，使用RestTemplat访问普通http服务</br>
 * 模板测试接口，可以删除</br>
 * @author wuyuegang
 * 
 */
public interface DemoIntegration
{
    /**
     * 从远程接口，查询数据
     * 
     * @param code
     * @return
     */
    public String getInfoFromRemote(String code);
}
