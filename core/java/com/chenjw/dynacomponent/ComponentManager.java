/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.chenjw.dynacomponent.core.ComponentBuilder;
import com.chenjw.dynacomponent.core.ComponentScanner;
import com.chenjw.dynacomponent.core.ReloadCallback;
import com.chenjw.dynacomponent.core.Switchable;
import com.chenjw.dynacomponent.model.ResourceFile;
import com.chenjw.dynacomponent.spi.ComponentContainer;
import com.chenjw.dynacomponent.util.AutowireUtils;
import com.chenjw.dynacomponent.util.ComponentUtils;

/**
 * 组件管理器
 * 
 * @author junwen.chenjw 2013年7月19日 下午1:59:01
 */
public class ComponentManager implements InitializingBean, ApplicationContextAware {
    /** 日志 */
    private static final Logger                LOGGER     = Logger
                                                              .getLogger(ComponentManager.class);
    /**  组件类型到组件容器的映射*/
    private Map<String, ComponentContainer<?>> containers = new ConcurrentHashMap<String, ComponentContainer<?>>();
    /** spring ApplicationContext */
    private ApplicationContext                 applicationContext;

    /** 文件扩展名到组件实例化器的映射  */
    private Map<String, ComponentBuilder>      builderMap;
    /** 组件监控器 */
    private ComponentScanner                   scanner;

    /** 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        //一个scan对个多个builder加载方式，根据文件扩展名确定用那个builder
        if (builderMap != null) {
            List<String> list = new ArrayList<String>();
            for (Entry<String, ComponentBuilder> entry : builderMap.entrySet()) {
                list.add(entry.getKey());
            }
            String[] extensions = list.toArray(new String[list.size()]);
            scanner.start(extensions, new ScannerCallback());
        }
        // 设置到全局变量
        ComponentUtils.setInstance(this);
    }

    /**
     * 搜索到文件时的回调
     * 
     * @author junwen.chenjw
     * @version $Id: ComponentManager.java, v 0.1 2013年11月6日 上午10:41:45 junwen.chenjw Exp $
     */
    private class ScannerCallback implements ReloadCallback {

        /** 
         * @see com.alipay.mobilecommon.dynamiccomponent.core.ReloadCallback#onReload(java.lang.String, java.util.List)
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void onReload(String type, List<ResourceFile> contents) {
            ComponentContainer<?> group = containers.get(type);
            if (group == null) {
                return;
            }
            Class<?> clazz = group.componentType();
            List newComponents = new ArrayList();
            // 只要有一个组件实例化失败了就所有组件不加载
            for (ResourceFile file : contents) {
                Object newComponent = null;
                String fileType = file.getExtension();
                ComponentBuilder builder = builderMap.get(fileType);
                if (builder == null) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("builder not found " + fileType + "," + type);
                    }
                    continue;
                }
                newComponent = builder.build(clazz, file);
                if (newComponent == null) {
                    continue;
                }
                // 实现了Switchable 并且设置enable为false的，不初始化
                if (newComponent instanceof Switchable && !((Switchable) newComponent).enable()) {
                    continue;
                }
                // 如果组件中配置了需要spring注入的属性，需要自动帮这个组件注入依赖
                AutowireUtils.autowireBean(newComponent, applicationContext);
                newComponents.add(newComponent);
            }
            group.onReload(newComponents);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(group.getClass().getSimpleName() + "(" + newComponents.size()
                            + ") reloaded! ");
            }

        }
    }

    /**
     * 根据spi获得组件组 
     * @param spiClazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ComponentContainer<?>> T getContainer(String type) {
        return (T) containers.get(type);
    }

    /**
     * Setter method for property <tt>containers</tt>.
     * 
     * @param containers value to be assigned to property containers
     */
    public void setContainers(Map<String, ComponentContainer<?>> containers) {
        this.containers = containers;
    }

    /** 
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Setter method for property <tt>builderMap</tt>.
     * 
     * @param builderMap value to be assigned to property builderMap
     */
    public void setBuilderMap(Map<String, ComponentBuilder> builderMap) {
        this.builderMap = builderMap;
    }

    /**
     * Setter method for property <tt>scanner</tt>.
     * 
     * @param scanner value to be assigned to property scanner
     */
    public void setScanner(ComponentScanner scanner) {
        this.scanner = scanner;
    }

}
