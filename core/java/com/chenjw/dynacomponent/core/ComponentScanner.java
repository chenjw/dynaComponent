/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.core;

/**
 * 监测组件定义变化，并更新到组件容器
 * 
 * @author junwen.chenjw
 * @version $Id: ComponentScanner.java, v 0.1 2013年8月1日 下午9:33:09 junwen.chenjw Exp $
 */
public interface ComponentScanner {
    /**
     * 
     * 启动组件监听
     * 
     * @param extensions 监听的扩展名
     * @param callback 见到到变化后的回调方法
     */
    public void start(String[] extensions, ReloadCallback callback);
}
