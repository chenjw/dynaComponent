/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.util;

import com.chenjw.dynacomponent.ComponentManager;
import com.chenjw.dynacomponent.spi.ComponentContainer;

/**
 * 用于方便获取组件容器的工具类
 * 
 * @author junwen.chenjw
 * @version $Id: ComponentUtils.java, v 0.1 2013年11月21日 上午11:49:05 junwen.chenjw Exp $
 */
public class ComponentUtils {
    /** 组件管理器实例， */
    private static ComponentManager instance = null;

    /**
     * 根据类型获得组件容器（直接根据输入class转好类型）
     * 
     * @param type 类型
     * @param containerType 组件容器class类型
     * @return 组件容器
     */
    @SuppressWarnings("unchecked")
    public static <T extends ComponentContainer<?>> T getContainer(String type,
                                                                   Class<T> containerType) {
        if (instance == null) {
            return null;
        }
        return (T) instance.getContainer(type);
    }

    /**
     * 根据类型获得组件容器
     * 
     * @param type 类型
     * @return 组件容器
     */
    @SuppressWarnings("unchecked")
    public static <T extends ComponentContainer<?>> T getContainer(String type) {
        if (instance == null) {
            return null;
        }
        return (T) instance.getContainer(type);
    }

    /**
     * Setter method for property <tt>instance</tt>.
     * 
     * @param instance value to be assigned to property instance
     */
    public static void setInstance(ComponentManager instance) {
        ComponentUtils.instance = instance;
    }

}
