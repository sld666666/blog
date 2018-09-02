package com.baiwang.cloud.service;

import java.util.List;

import com.baiwang.cloud.common.exception.SystemException;
import com.baiwang.cloud.common.model.ProductVO;

/**
 * TODO:测试接口，上线前可以删除,注释标准请参考此接口</br>
 * 
 * @author wuyuegang
 * 
 */
public interface DemoService
{
    /**
     * 查询商品信息列表方法，返回所有商品信息，当商品不存在，或者为空时，
     * <p>
     * 会抛出自定义异常 <b>SystemException<b>
     * 
     * @return
     * @throws SystemException
     */
    public List<ProductVO> getProductList();
    
    /**
     * 根据code查询商品信息，返回商品信息，当商品不存在，或者为空时，
     * <p>
     * 会抛出自定义异常 <b>SystemException<b>
     * 
     * @param itemCode
     * @return
     */
    public ProductVO getProductByCode(String itemCode);
    
}
