package com.example.controller;

import com.example.service.TblTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tran")
public class TblTranController {
    @Autowired
    private TblTranService tranService;

    @Value("${session.user}")
    private String USER;
}
