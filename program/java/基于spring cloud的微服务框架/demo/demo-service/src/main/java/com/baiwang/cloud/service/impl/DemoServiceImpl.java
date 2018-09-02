package com.baiwang.cloud.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.baiwang.cloud.common.enumutil.SystemErrorEnum;
import com.baiwang.cloud.common.exception.SystemException;
import com.baiwang.cloud.common.model.ProductVO;
import com.baiwang.cloud.dao.DemoDao;
import com.baiwang.cloud.integration.DemoIntegration;
import com.baiwang.cloud.service.DemoService;

/**
 * 业务操作实现类，实现查询分页查询，单个查询等功能</br>
 * TODO:测试接口，上线前可以删除</br>
 * 
 * @author wuyuegang
 * 
 */
@Component
@ComponentScan(basePackages = {"com.baiwang.cloud"})
public class DemoServiceImpl implements DemoService
{
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // 注入DAO
    @Autowired
    private DemoDao demoDao;
    
    // 注入远程接口代理类
    @Autowired
    private DemoIntegration demoIntegration;
    
    /**
     * TODO 简单描述该方法的实现功能（可选）.
     * 
     * @see com.baiwang.cloud.service.demo.service.DemoService#getProductList()
     */
    public List<ProductVO> getProductList()
    {
        Random random = new Random();
        int a = random.nextInt(10);
        
        // 从DB中查询数据，返回list结果
        String name = demoDao.getProducts();
        
        List<ProductVO> list = new ArrayList<ProductVO>();
        // 包装测试数据对象返回
        ProductVO vo = new ProductVO();
        vo.setAddress(name + ".address.1");
        vo.setName(name + ".name.1");
        ProductVO vo2 = new ProductVO();
        vo2.setAddress(name + ".address.2");
        vo2.setName(name + ".name.2");
        list.add(vo);
        list.add(vo2);
        
        // test exception
        if (a > 5)
        {
            // 自定义异常测试代码，只需定义好全局异常枚举，抛出异常，上层无需捕获，框架会拦截所有异常，返回json格式信息
            logger.warn("getProductList failed");
            throw new SystemException(SystemErrorEnum.SYSTEM_ERROR);
        }
        else
        {
            if (logger.isInfoEnabled())
            {
                // 输出服务结果日志，包括返回结果信息
                logger.info("getProductList result, request入参:{} 返回结果:{} ", "", list);
            }
        }
        return list;
    }
    
    /**
     * 
     * TODO 简单描述该方法的实现功能（可选）.
     * 
     * @see com.baiwang.cloud.service.DemoService#getProductByCode(java.lang.String)
     */
    public ProductVO getProductByCode(String itemCode)
    {
        Random random = new Random();
        int a = random.nextInt(10);
        // 从远程接口查询数据，返回结果
        String result = demoIntegration.getInfoFromRemote(itemCode);
        ProductVO vo = new ProductVO();
        vo.setAddress(result);
        vo.setName(result);
        if (a > 5)
        { // 自定义异常测试代码，只需定义好全局异常枚举，抛出异常，上层无需捕获，框架会拦截所有异常，返回json格式信息
            logger.warn("getProductByCode failed request param {}", itemCode);
            throw new SystemException(SystemErrorEnum.UNKNOWN_ERROR);
        }
        else
        {
            // 输出服务出口日志，包括入参摘要，返回结果信息摘要
            logger.info("getProductByCode success request入参:{} 返回结果:{}", itemCode, vo.toString());
        }
        return vo;
    }
    
}
