/**
 * Project Name:demo-service
 * File Name:ProductVO.java
 * Package Name:com.baiwang.cloud.demo.service.model
 * Date:2017年10月20日下午3:42:55
 * Copyright (c) 2017, wuyuegang@baiwang.com All Rights Reserved.
 *
*/

package com.baiwang.cloud.common.model;

import com.baiwang.cloud.common.model.BaseJsonModel;

/**
 * ClassName:ProductVO测试对象，可以删除 <br/>
 * Function: 测试，必须继承基类BaseJsonModel. <br/>
 * Reason: 测试. <br/>
 * Date: 2017年10月20日 下午3:42:55 <br/>
 * 
 * @author wuyuegang
 * @version
 * @since JDK 1.8
 * @see
 */
public class ProductVO extends BaseJsonModel
{
    /**
     * name
     */
    private String name;
    
    /**
     * address
     */
    private String address;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
}
