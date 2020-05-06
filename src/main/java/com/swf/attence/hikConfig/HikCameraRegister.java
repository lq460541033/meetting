package com.swf.attence.hikConfig;

import com.swf.attence.service.ICameraMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目部署之后开始注册设备
 */
@Component
public class HikCameraRegister implements ApplicationRunner {
    @Autowired
   private ICameraMsgService iCameraMsgService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (iCameraMsgService.cameraInitAndcameraRegisterAndsetupAlarmChan().size()!=0){
            System.out.println("设备已全部注册成功");
        }
    }
}
