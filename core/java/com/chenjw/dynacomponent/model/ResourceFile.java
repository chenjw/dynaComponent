/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.model;

/**
 *  用于组件定义的资源文件
 * 
 * @author junwen.chenjw
 * @version $Id: ResourceFile.java, v 0.1 2013年11月21日 上午11:04:19 junwen.chenjw Exp $
 */
public class ResourceFile {

    /** 
     * 资源扩展名
     *  */
    private String extension;
    /** 
     * 资源文件名(不包含扩展名部分)
     *  */
    private String name;

    /** 资源内容 */
    private byte[] content;

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getName() {
        return name;
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
     * Getter method for property <tt>content</tt>.
     * 
     * @return property value of content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Setter method for property <tt>content</tt>.
     * 
     * @param content value to be assigned to property content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * Getter method for property <tt>extension</tt>.
     * 
     * @return property value of extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Setter method for property <tt>extension</tt>.
     * 
     * @param extension value to be assigned to property extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

}
