package com.tds.carparkapi.service;

import com.tds.carparkapi.respository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;
}
