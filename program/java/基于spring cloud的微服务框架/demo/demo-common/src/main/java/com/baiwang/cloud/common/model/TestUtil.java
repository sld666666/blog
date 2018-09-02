/**
 * Project Name:demo-common
 * File Name:TestUtil.java
 * Package Name:com.baiwang.cloud.common.model
 * Date:2018年1月24日下午5:46:55
 * Copyright (c) 2018, wuyuegang@baiwang.com All Rights Reserved.
 *
*/

package com.baiwang.cloud.common.model;

import com.baiwang.cloud.common.model.BaseJsonModel;

/**
 * ClassName:TestUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年1月24日 下午5:46:55 <br/>
 * @author   wuyuegang
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class TestUtil
{
    
    public static void main(String[] args)
    {
        
        ProductVO vo=new ProductVO();
        vo.setAddress("abc");
        vo.setName("test");
        if(vo instanceof BaseJsonModel) {
            BaseJsonModel newvo=(BaseJsonModel)vo;
            String objstr = newvo.toString();
            objstr = objstr.replaceAll("\\{|\\}|\"", "");  
            System.out.println(objstr);
        }
        
        
    }
    
}

