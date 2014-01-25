/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.builder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.chenjw.dynacomponent.builder.js.JsInvoker;
import com.chenjw.dynacomponent.builder.js.SimpleJsInvoker;
import com.chenjw.dynacomponent.core.ComponentBuilder;
import com.chenjw.dynacomponent.model.ResourceFile;
import com.chenjw.logger.Logger;

/**
 * 实例化使用js编写的组件
 * 
 * @author junwen.chenjw
 * @version $Id: JsComponentBuilder.java, v 0.1 2013年8月1日 下午9:32:52 junwen.chenjw Exp $
 */
public class JsComponentBuilder implements ComponentBuilder {
    /** 加载脚本文件时使用的编码 */
    private String             encoding = "GBK";
    /** 日志 */
    public static final Logger LOGGER   = Logger.getLogger(SimpleJsInvoker.class);

    /** 
     * @see com.alipay.mobilecommon.dynamiccomponent.core.ComponentBuilder#build(java.lang.Class, com.alipay.mobilecommon.dynamiccomponent.model.ResourceFile)
     */
    @SuppressWarnings("unchecked")
    public <T> T build(Class<T> clazz, ResourceFile source) {
        try {
            T t = clazz.newInstance();
            if (!(t instanceof JsInvoker)) {
                LOGGER.error("非js组件," + clazz.getName());
            }
            JsInvoker jsInvoker = (JsInvoker) t;
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("JavaScript");
            engine.eval(new String(source.getContent(), encoding));
            jsInvoker.setInvoker((Invocable) engine);
            jsInvoker.setName(source.getName());
            return (T) jsInvoker;
        } catch (Exception e) {
            LOGGER.error("脚本加载异常," + source.getContent(), e);
            throw new IllegalStateException("js load fail " + clazz.getName(), e);
        }
    }

    /**
     * Setter method for property <tt>encoding</tt>.
     * 
     * @param encoding value to be assigned to property encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
