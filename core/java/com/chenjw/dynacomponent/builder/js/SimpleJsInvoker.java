/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.builder.js;

import javax.script.Invocable;
import javax.script.ScriptException;

import com.chenjw.logger.Logger;

/**
 * 默认js加载实现
 * 
 * @author chao.xiuc
 * @version $Id: SimpleJsInvoker.java, v 0.1 2013-8-10 下午4:42:50 chao.xiuc Exp $
 */
public class SimpleJsInvoker implements JsInvoker {

    /** 日志 */
    public static final Logger LOGGER = Logger.getLogger(SimpleJsInvoker.class);
    /** 文件名 */
    private String             name;
    /** 脚本Invocable */
    private Invocable          invoker;

    /**
     * 调用js中的某个方法
     * 
     * @param func 方法名
     * @param args 参数
     * @return
     */
    public String invokeFunction(String func, Object... args) {
        try {
            Object result = invoker.invokeFunction(func, args);
            return result.toString();
        } catch (ScriptException e) {
            LOGGER.error("脚本加载异常," + func, e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("未找到方法," + func, e);
        }
        return null;
    }

    /**
     * Setter method for property <tt>name</tt>.
     * 
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>invoker</tt>.
     * 
     * @param invoker value to be assigned to property invoker
     */
    public void setInvoker(Invocable invoker) {
        this.invoker = invoker;
    }

}
