package com.swf.attence.service;

import com.swf.attence.entity.CameraMsg;
import com.baomidou.mybatisplus.service.IService;
import com.swf.attence.entity.UserMsg;
import com.swf.attence.hikConfig.ClientDemo;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
public interface ICameraMsgService extends IService<CameraMsg> {
    /**
     * 判断添加的设备是否存在，存在返回false。不存在返回true
     * @param cameraMsg
     * @return
     */
    boolean cameraidExist(CameraMsg cameraMsg);

    /**
     * 设备的初始化、注册,成功注册返回true
     * @return
     */
    List<ClientDemo> cameraInitAndcameraRegisterAndsetupAlarmChan();

    /**
     * 批量下发人脸信息
     * @return
     */
    boolean uploadUserPicAndUserMessage() throws  IOException;

    /**
     * 单个下发人脸信息
     * @param userMsg
     */
    void uploadUserPicAndUserMessageByOne(UserMsg userMsg) throws IOException;

    /**
     * 操作xml 构造图片对应数据文档
     * @param userMsg
     * @return
     */
    void xmlControl(UserMsg userMsg) throws DocumentException, IOException;

    /**
     *
     */
    void deleteUserpicAndUserMsgByOne(UserMsg userMsg);

    /**
     * 为每一个摄像头创建一个实例
     */
    List<ClientDemo> createClientDemo();

}
