package com.swf.attence.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.swf.attence.controller.CameraController;
import com.swf.attence.entity.CameraMsg;
import com.swf.attence.entity.UserMsg;
import com.swf.attence.hikConfig.ClientDemo;
import com.swf.attence.hikConfig.FDLibBox;
import com.swf.attence.mapper.CameraMsgMapper;
import com.swf.attence.service.ICameraMsgService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.swf.attence.service.IUserMsgService;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.swf.attence.controller.UploadController.PATH;
import static com.swf.attence.controller.UploadController.USERDATAPATH;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
@Service
public class CameraMsgServiceImpl extends ServiceImpl<CameraMsgMapper, CameraMsg> implements ICameraMsgService {
    @Autowired
     CameraMsgMapper cameraMsgMapper;
    @Autowired
     IUserMsgService iUserMsgService;



    private static final Logger logger = LoggerFactory.getLogger(CameraMsgServiceImpl.class);


    /**
     * 检查添加的设备id是否存在，存在返回false，不存在返回true
     *
     * @param cameraMsg
     * @return
     */
    @Override
    public boolean cameraidExist(CameraMsg cameraMsg) {
        CameraMsg cameraMsg1 = cameraMsgMapper.selectOne(cameraMsg);
        System.out.println(cameraMsg1);
        if (cameraMsg1 == null) {
            return true;
        } else {
            return false;
        }
        /*  return cameraMsg1==null;*/
    }

    @Override
    public List<ClientDemo> createClientDemo() {
        ArrayList<ClientDemo> clientDemos = new ArrayList<>(16);
        int count = selectCount(new EntityWrapper<CameraMsg>().eq("1", 1));
        for (int i=0;i<count;i++){
            ClientDemo clientDemo = new ClientDemo();
            clientDemos.add(clientDemo);
        }
        return clientDemos;
    }

    @Override
    public List<ClientDemo> cameraInitAndcameraRegisterAndsetupAlarmChan() {
        ArrayList<ClientDemo> demos = new ArrayList<>();
        List<ClientDemo> clientDemos = createClientDemo();
        Integer integer = cameraMsgMapper.selectCount(new EntityWrapper<CameraMsg>().eq("1", 1));
        for (int i = 1; i <= integer; i++) {
            ClientDemo clientDemo = clientDemos.get(i - 1);
            if (clientDemo.CameraInit()) {
                    CameraMsg cameraMsg = cameraMsgMapper.selectById(i);
                    if (cameraMsg != null) {
                        boolean b = clientDemo.register(cameraMsg.getCameraName(), cameraMsg.getCameraPassword(), cameraMsg.getCameraid());
                        if (b) {
                            if (clientDemo.SetupAlarmChan()) {
                                logger.info(clientDemo+"  布防成功");
                                demos.add(clientDemo);
                            }
                        }
                    }
                }
        }
        return demos;
    }

    @Override
    public boolean uploadUserPicAndUserMessage() throws IOException {
        List<ClientDemo> clientDemos = cameraInitAndcameraRegisterAndsetupAlarmChan();
        Iterator<ClientDemo> iterator = clientDemos.iterator();
        while (iterator.hasNext()){
            FDLibBox fdLibBox = new FDLibBox(iterator.next());
            if (fdLibBox.SearchFDLib()) {
                List<UserMsg> userMsgs = iUserMsgService.selectList(new EntityWrapper<UserMsg>().eq("1", 1));
                for (UserMsg user : userMsgs
                        ) {
                    String realUserpicPath = PATH + user.getUserpic();
                    String realUserdataPath = USERDATAPATH + user.getUserid() + ".xml";
                    xmlControl(user);
                    fdLibBox.UploadFaceLinData(0, realUserpicPath, realUserdataPath);
                }
        }
            return true;
        }
        return false;
    }

    @Override
    public void deleteUserpicAndUserMsgByOne(UserMsg userMsg) {
        List<ClientDemo> clientDemos = cameraInitAndcameraRegisterAndsetupAlarmChan();
        Iterator<ClientDemo> iterator = clientDemos.iterator();
        while (iterator.hasNext()){
            FDLibBox fdLibBox = new FDLibBox(iterator.next());
            if (fdLibBox.SearchFDLib()) {
                fdLibBox.DeleteFaceAppendData(0, userMsg.getUserid());
            }
        }
    }

    @Override
    public void uploadUserPicAndUserMessageByOne(UserMsg userMsg) throws IOException {
        List<ClientDemo> clientDemos = cameraInitAndcameraRegisterAndsetupAlarmChan();
        Iterator<ClientDemo> iterator = clientDemos.iterator();
        while (iterator.hasNext()){
            FDLibBox fdLibBox = new FDLibBox(iterator.next());
            if (fdLibBox.SearchFDLib()) {
                String realUserpicPath = PATH + userMsg.getUserpic();
                String realUserdataPath = USERDATAPATH + userMsg.getUserid() + ".xml";
                xmlControl(userMsg);
                fdLibBox.UploadFaceLinData(0, realUserpicPath, realUserdataPath);
                logger.info("图片已成功上传到摄像头");
            }
        }
    }

    @Override
    public synchronized void xmlControl(UserMsg userMsg) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element element = document.addElement("FaceAppendData");
        Element name = element.addElement("name");
        name.setText(userMsg.getUsername());
//        Element certificateNumber = element.addElement("certificateNumber");
//        certificateNumber.setText(userMsg.getUserid());
        Element bornTime = element.addElement("bornTime");
        bornTime.setText("1994-01-24T00:00:00Z");
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("utf-8");
        /*Writer out;
        out = new FileWriter(USERDATAPATH + userMsg.getUserid() + ".xml");*/
        FileOutputStream fileOutputStream = new FileOutputStream(USERDATAPATH + userMsg.getUserid() + ".xml");
        XMLWriter xmlWriter = new XMLWriter(fileOutputStream, outputFormat);
        xmlWriter.write(document);
        xmlWriter.close();
        System.out.println("成功生成工号为： " + userMsg.getUserid() + " 的数据文件");
    }


}
