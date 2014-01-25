/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.dynacomponent.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.chenjw.dynacomponent.core.ComponentScanner;
import com.chenjw.dynacomponent.core.ReloadCallback;
import com.chenjw.dynacomponent.model.ResourceFile;
import com.chenjw.logger.Logger;

/**
 * 定时扫描指定文件目录
 * <p> 注意：为节省扫描性能，文件删除是没法触发重新加载的，删除后需要随便修改下剩余的某个文件 </p>
 * 
 * @author junwen.chenjw
 * 
 * @version $Id: FolderScanner.java, v 0.1 2013年8月1日 下午9:32:02 junwen.chenjw Exp $
 */
public class FolderScanner implements ComponentScanner {
    /** 日志 */
    public static final Logger LOGGER            = Logger.getLogger(FolderScanner.class);
    /** 10秒钟扫一次 */
    private long               scanInterval      = 10 * 1000;
    /** 用于备份文件夹内容标识，如果内容标识变了就表示文件有更新 */
    private Map<String, Long>  folderCheckNumMap = new HashMap<String, Long>();
    /** 监听的目录 */
    private File               folder;
    /** 监听的目录地址 */
    private String             folderPath;
    /** 监听到文件变化后的回调 */
    private ReloadCallback     callback;
    /** 监听的文件扩展名 */
    private String[]           extensions;

    /**
     * 通过最后修改日期来判断某个文件夹是否变化
     * 
     * @param f 文件夹
     * @return 是否发生变化
     */
    @SuppressWarnings("unchecked")
    private boolean checkChangedAndSaveState(File f) {
        // checknum表示文件夹是否更新
        Long oldFolderCheckNum = folderCheckNumMap.get(f.getAbsolutePath());
        Long newFolderCheckNum = 0L;
        Iterator<File> fileIterator = FileUtils.iterateFiles(f, extensions, true);
        while (fileIterator.hasNext()) {
            File ff = fileIterator.next();
            newFolderCheckNum += ff.getAbsolutePath().hashCode();
            newFolderCheckNum += ff.lastModified();
        }
        // 
        if (oldFolderCheckNum == null || !newFolderCheckNum.equals(oldFolderCheckNum)) {
            // 有变化
            folderCheckNumMap.put(f.getAbsolutePath(), newFolderCheckNum);
            return true;
        }
        return false;
    }

    /**
     * 
     * 用于子类扩展检测的后缀名
     * 
     * @param extensions 原始配置的后缀
     * @return 实际使用的后缀
     */
    protected String[] prepareExtensions(String[] extensions) {
        return extensions;
    }

    /**
     * 用于子类扩展处理读取到的资源文件
     * 
     * @param list 资源文件
     * @return 处理后的资源文件
     */
    protected List<ResourceFile> prepareResourceFiles(List<ResourceFile> list) {
        return list;
    }

    /** 
     * @see com.alipay.mobilecommon.dynamiccomponent.core.ComponentScanner#start(java.lang.String[], com.alipay.mobilecommon.dynamiccomponent.core.ReloadCallback)
     */
    @Override
    public void start(String[] extensions, ReloadCallback callback) {
        this.callback = callback;
        this.folder = new File(folderPath);
        this.extensions = prepareExtensions(extensions);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("monitor folder : " + folder.getAbsolutePath());
        }
        // 启动的时候先同步触发一次
        fireScan();
        // 然后启动一个线程定时触发
        new Timer("components-folder-scanner", true).schedule(new ScanTask(), scanInterval,
            scanInterval);
    }

    /**
     * 检测所有监听目录，如果某个目录有变化就调用回调函数
     */
    public void fireScan() {
        // 如果发生异常不中断容器加载
        try {
            for (File f : folder.listFiles()) {
                // 第一层目录只判断文件夹
                if (f.isFile()) {
                    continue;
                }
                // 文件夹没变化就不更新了
                if (!checkChangedAndSaveState(f)) {
                    continue;
                }
                // 文件夹名字作为组件类型
                String folderName = f.getName();
                List<ResourceFile> list = new ArrayList<ResourceFile>();
                File[] files = FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(f,
                    extensions, true));
                for (File ff : files) {
                    ResourceFile file = new ResourceFile();
                    file.setName(StringUtils.substringBeforeLast(ff.getName(), "."));
                    file.setContent(FileUtils.readFileToByteArray(ff));
                    file.setExtension(StringUtils.substringAfterLast(ff.getName(), "."));
                    list.add(file);
                }
                // 处理读取到的资源文件
                list = prepareResourceFiles(list);
                // 按照文件名排序“_”开头的会排在最前面（可利用这个特性控制加载顺序）
                Collections.sort(list, new Comparator<ResourceFile>() {
                    @Override
                    public int compare(ResourceFile o1, ResourceFile o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                try {
                    // 通知回调
                    callback.onReload(folderName, list);
                }
                // 这里暂时把error也捕捉掉，因为脚本编写不当可能会报NoClassDefFoundError，造成整个系统启动不了
                catch (Throwable e) {
                    LOGGER.error("onReload error", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("file scan error", e);
        }
    }

    /**
     * 定时检测文件变化的线程
     * 
     * @author junwen.chenjw
     * @version $Id: FolderScanner.java, v 0.1 2013年11月6日 上午10:17:22 junwen.chenjw Exp $
     */
    private class ScanTask extends TimerTask {

        @Override
        public void run() {
            fireScan();
        }

    }

    /**
     * Setter method for property <tt>scanInterval</tt>.
     * 
     * @param scanInterval value to be assigned to property scanInterval
     */
    public void setScanInterval(long scanInterval) {
        this.scanInterval = scanInterval;
    }

    /**
     * Getter method for property <tt>folderPath</tt>.
     * 
     * @return property value of folderPath
     */
    public String getFolderPath() {
        return folderPath;
    }

    /**
     * Setter method for property <tt>folderPath</tt>.
     * 
     * @param folderPath value to be assigned to property folderPath
     */
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

}
