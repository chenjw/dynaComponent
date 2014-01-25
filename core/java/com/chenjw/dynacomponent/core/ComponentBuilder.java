/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.core;

import com.chenjw.dynacomponent.model.ResourceFile;

/**
 * 实例化并初始化组件
 * 
 * @author junwen.chenjw
 * @version $Id: ComponentBuilder.java, v 0.1 2013年8月1日 下午9:33:01 junwen.chenjw Exp $
 */
public interface ComponentBuilder {
    /**
     * 根据spi的接口名和源码创建目标实例
     * 
     * @param clazz 目标类型，可以是某个接口
     * @param source 目标源码
     * @return 组件实例
     */
    public <T> T build(Class<T> clazz, ResourceFile source);
}
