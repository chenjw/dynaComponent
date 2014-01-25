/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.builder.js;

import javax.script.Invocable;

/**
 * js组件接口
 * 
 * @author chao.xiuc
 * @version $Id: JsInvoker.java, v 0.1 2013-8-10 下午4:42:50 chao.xiuc Exp $
 */
public interface JsInvoker {

    /**
     * 设置组件名称
     * @param name 组件名称
     */
    public void setName(String name);

    /**
     * 设置调用器
     * 
     * @param invoker 调用器
     */
    public void setInvoker(Invocable invoker);

}
