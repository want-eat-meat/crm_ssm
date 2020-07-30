package com.example.listenner;

import com.example.pojo.TblDicValue;
import com.example.service.TblDicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class DicListenner implements ServletContextListener {
    @Autowired
    private TblDicService dicService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        context.getAutowireCapableBeanFactory().autowireBean(this);
        HashMap<String, Set<TblDicValue>> dics = dicService.getDics();
        sce.getServletContext().setAttribute("dic", dics);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
