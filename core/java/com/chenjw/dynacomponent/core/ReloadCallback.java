/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.core;

import java.util.List;

import com.chenjw.dynacomponent.model.ResourceFile;

/**
 * 组件配置变化时的回调接口
 * 
 * @author junwen.chenjw
 * @version $Id: ReloadCallback.java, v 0.1 2013年8月1日 下午9:33:27 junwen.chenjw Exp $
 */
public interface ReloadCallback {

    /**
     * 当组件定义脚本变化时的回调方法
     * 
     * @param type 组件类型
     * @param contents 组件定义文件
     */
    public void onReload(String type, List<ResourceFile> contents);

}
