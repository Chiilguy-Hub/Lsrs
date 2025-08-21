package com.Lsrs.check_in.service.Impl;

import com.Lsrs.check_in.Pojo.Qr;
import com.Lsrs.check_in.service.Check_inService;
import com.Lsrs.check_in.until.CodeUtil;
import com.Lsrs.check_in.until.untils.AliOssUtil;
import com.example.Dao.QrRequest;
import com.example.redis.IRedis;
import com.example.redis.RedisService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class Check_inServiceImpl implements Check_inService {

@Autowired
    AliOssUtil aliOssUtil;
@Autowired

private RedisService redisService;



    @Override
    public List<Qr> creatqr(List<QrRequest> qrRequest) {
        List<Qr> qrs = new ArrayList<Qr>();
        try {
            //原始文件名
            for (QrRequest request : qrRequest) {
                Qr qr = new Qr();
                Integer seatFloor = request.getSeatFloor();
                String seatNumber = request.getSeatNumber();
               Integer zoneNumber = request.getZoneNumber();
               String uuid = UUID.randomUUID().toString();
               String filename=seatFloor+"-"+zoneNumber+"-"+seatNumber;
               redisService.set(filename,uuid,3600*24*30); //三十天有效期
                byte[] bytes = generateQrCodeBytes(uuid);//qr
                //构造新文件名称

                String objectName = "qr/"+filename + ".png";
                filename+=".png";
                //文件的请求路径
                String filePath = aliOssUtil.upload(bytes, objectName);
               qr.setUrl(filePath);
               qr.setFilename(filename);
               qrs.add(qr);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return qrs;
    }
        //获取流文件
        public   byte[] generateQrCodeBytes(String content) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                // 调用生成方法，将二维码写入内存流
                CodeUtil.createCodeToOutputStream(content, baos);

                // 确保流已关闭并获取字节数组
                return baos.toByteArray();
            }
        }

}


