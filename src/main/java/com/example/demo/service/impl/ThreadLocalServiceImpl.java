package com.example.demo.service.impl;

import com.example.demo.service.ThreadLocalService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author chengqi1
 * @date 2019-12-2 14:35
 * 描述：
 */
@Service
@Scope("prototype")
public class ThreadLocalServiceImpl implements ThreadLocalService {
    private ThreadLocal<Integer> pageIndexLocal = new ThreadLocal<>();//记录执行到第几页，在日志打印中使用
    public void doService(){
        int pageThreadResult=0;
        try{
            pageThreadResult= pageIndexLocal.get();
            System.out.println(pageThreadResult);
        }catch (Exception e){
            System.out.println("pageThreadResult null");
            pageIndexLocal.set(14);
        }
        if(pageThreadResult!=0){
            pageIndexLocal.set(5);
        }
    }
}
