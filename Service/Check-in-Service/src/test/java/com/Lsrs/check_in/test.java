package com.Lsrs.check_in;

import com.Lsrs.check_in.until.CodeUtil;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class test {
    @Test
    public void test() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 调用生成方法，将二维码写入内存流
            CodeUtil.createCodeToOutputStream("1234", baos);

            // 确保流已关闭并获取字节数组
            System.out.println(   baos.toByteArray().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
