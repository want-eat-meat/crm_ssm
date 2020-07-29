package com.example.listenner;

import com.example.service.TblDicService;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.TreeMap;

public class DicListenner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        TblDicService dicService = (TblDicService) context.getBean("dic");
        HashMap<String, TreeMap<String, String>> dicMap =  dicService.getDics();
        sce.getServletContext().setAttribute("dic", dicMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
