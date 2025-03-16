package com.xushu.exquicker.service;

/**
 *
 * @author 谭亚军 at 2022/5/3 17:54
 */
public interface ExportService {
    /**
     * 使用EasyPOI工具导出-单线程
     */
    void exportWithSingleThread();
    /**
     * 使用EasyPOI工具导出-多线程
     */
    void exportWithMultiThread();
    /**
     * 使用原生POI工具导出-单线程
     */
    void exportWithSingleThreadByPoi();
    /**
     * 使用原生POI工具导出-多线程
     */
    void exportWithMultiThreadByPoi();
}
