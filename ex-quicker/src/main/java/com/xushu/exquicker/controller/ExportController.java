package com.xushu.exquicker.controller;

import com.xushu.exquicker.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xushu.exquicker.utils.R;

/**

 *
 * @author xs
 */
@RestController
@RequestMapping("/export")
public class ExportController {



    @Autowired
    private ExportService exportService;

    /**
     * 使用EasyPOI工具导出-单线程
     * @return
     */
    @GetMapping("/singleThread")
    public R exportWithSingleThread(){
        exportService.exportWithSingleThread();
        return R.ok("easypoi方式-单线程导出成功！");
    }

    /**
     * 使用EasyPOI工具导出-多线程
     * @return
     */
    @GetMapping("/multiThread")
    public R exportWithMultiThread(){
        exportService.exportWithMultiThread();
        return R.ok("easypoi方式-多线程导出成功！");
    }

    /**
     * 使用原生POI工具导出-单线程
     * @return
     */
    @GetMapping("/singleThread-poi")
    public R exportWithSingleThreadByPoi(){
        exportService.exportWithSingleThreadByPoi();
        return R.ok("poi方式-单线程导出成功！");
    }

    /**
     * 使用原生POI工具导出-多线程
     * @return
     */
    @GetMapping("/multiThread-poi")
    public R exportWithMultiThreadByPoi(){
        exportService.exportWithMultiThreadByPoi();
        return R.ok("poi方式-多线程导出成功！");
    }
}
