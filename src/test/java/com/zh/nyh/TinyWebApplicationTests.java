package com.zh.nyh;

import org.junit.jupiter.api.Test;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

class TinyWebApplicationTests {

    @Test
    void contextLoads() {
    	QrConfig config = QrConfig.create();
    	
    	QrCodeUtil.generate("", config);
    }

}
