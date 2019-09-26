package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@RestController
public class showController {
    @RequestMapping("test/getDetailPage")
    public ModelAndView getDetailPage(){
        System.out.println("我还只会打印");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("test");
        return mv;
    }
}
