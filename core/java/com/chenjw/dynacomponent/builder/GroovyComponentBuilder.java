/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.builder;

import groovy.lang.GroovyClassLoader;

import org.springframework.util.Assert;

import com.chenjw.dynacomponent.core.ComponentBuilder;
import com.chenjw.dynacomponent.model.ResourceFile;

/**
 * 实例化使用groovy编写的组件
 * 
 * @author junwen.chenjw
 * @version $Id: GroovyComponentBuilder.java, v 0.1 2013年8月1日 下午9:32:52 junwen.chenjw Exp $
 */
public class GroovyComponentBuilder implements ComponentBuilder {
    /** 加载脚本文件时使用的编码 */
    private String            encoding          = "GBK";
    /** 
     *       GroovyClassLoader有innerClassLoader的机制，实际上加载的每个class是由一个独立的innerclassloader加载的
     *          当加载类的所有实例没有引用后innerClassLoader也会回收
     *      所以，这里无需考虑加载类的回收问题，以及加载类的命名冲突问题，GroovyClassLoader只需要保留一个实例
     * */
    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader(
                                                    GroovyComponentBuilder.class.getClassLoader());

    /** 
     * @see com.alipay.mobilecommon.dynamiccomponent.core.ComponentBuilder#build(java.lang.Class, com.alipay.mobilecommon.dynamiccomponent.model.ResourceFile)
     */
    public <T> T build(Class<T> clazz, ResourceFile source) {
        T component = load(clazz, source);
        return component;
    }

    /**
     * 加载并实例化组件
     * <p>组件需要保证存在无参构造函数</p>
     * 
     * @param clazz 组件类型
     * @param source 源码
     * @return 组件实例
     */
    @SuppressWarnings("unchecked")
    private <T> T load(Class<T> clazz, ResourceFile source) {
        try {
            Class<?> loadedClazz = groovyClassLoader.parseClass(new String(source.getContent(),
                encoding), source.getName());
            Assert.notNull(loadedClazz, "parse class fail " + clazz.getName());
            Object instance = loadedClazz.newInstance();
            Assert.isInstanceOf(clazz, instance, "isInstance fail " + clazz.getName());
            // 清除cache，这样当class没有被引用的时候才会被释放掉
            groovyClassLoader.clearCache();
            return (T) instance;
        } catch (Exception e) {
            throw new IllegalStateException("groovy load fail " + clazz.getName(), e);
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
