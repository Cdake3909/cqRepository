package com.example.demo;

import com.example.demo.service.ThreadLocalService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@RestController
@Scope("prototype")
public class showController {
    @Autowired
    private ThreadLocalService threadLocalService;
    @RequestMapping("test/getDetailPage")
    public ModelAndView getDetailPage(){
        System.out.println("我还只会打印");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("test");
        return mv;
    }

    @RequestMapping(value = "test/doService")
    public String doService(){
        threadLocalService.doService();
        return "成功调用";
    }


}
