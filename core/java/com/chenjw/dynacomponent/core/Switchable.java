/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.core;

/**
 * 实现这个接口的组件，在实例化后会判断enable()，如果false这个组件就不加入组件容器，enable会在组件初始化之前执行
 * 
 * @author junwen.chenjw
 * @version $Id: Switchable.java, v 0.1 2013年11月21日 上午11:03:59 junwen.chenjw Exp $
 */
public interface Switchable {
    /**
     * 开关是否开启
     * 
     * @return 该组件是否有效
     */
    public boolean enable();
}
