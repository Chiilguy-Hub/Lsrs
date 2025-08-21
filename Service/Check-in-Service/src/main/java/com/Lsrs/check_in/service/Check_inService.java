package com.Lsrs.check_in.service;

import com.Lsrs.check_in.Pojo.Qr;
import com.example.Dao.QrRequest;

import java.util.List;

public interface Check_inService {
    List<Qr> creatqr(List<QrRequest> qrRequest);
}
