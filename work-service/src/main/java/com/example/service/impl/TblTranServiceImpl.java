package com.example.service.impl;

import com.example.mapper.TblTranMapper;
import com.example.service.TblTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TblTranServiceImpl implements TblTranService {

    @Autowired
    private TblTranMapper tranMapper;

}
