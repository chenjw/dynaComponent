/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.spi;

import java.util.List;

/**
 * 
 *  同一类组件存放在单独的容器中，通过onReload刷新，组件容器可以实现自定义的方法提供组件查询
 * 
 * @author junwen.chenjw
 * @version $Id: ComponentContainer.java, v 0.1 2013年7月12日 下午9:14:07 junwen.chenjw Exp $
 */
public interface ComponentContainer<T> {
    /**
     *  重新加载组件
     * 
     * @param components
     */
    public void onReload(List<T> components);

    /**
     * 组件的接口类型
     * @return
     */
    public Class<T> componentType();
}
