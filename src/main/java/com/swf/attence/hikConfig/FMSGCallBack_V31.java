package com.swf.attence.hikConfig;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.swf.attence.entity.ImeetingMsg;
import com.swf.attence.service.IImeetingMsgService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {
    @Autowired
    public IImeetingMsgService iImeetingMsgService;
    @Autowired
    public static FMSGCallBack_V31 fmsgCallBack_v31;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FMSGCallBack_V31.class);

    @PostConstruct
    public void init() {
        fmsgCallBack_v31 = this;
        fmsgCallBack_v31.iImeetingMsgService = this.iImeetingMsgService;
    }

    //报警信息回调函数
    @Override
    public void invoke(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        System.out.println("已成功进入回调函数,携带的参数有" + lCommand);
        logger.info("已成功进入回调函数,携带的参数有" + lCommand);
        try {
            String sAlarmType = new String();
            //报警时间
            Date today = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String[] sIP = new String[2];
            sAlarmType = new String("lCommand=") + lCommand.intValue();
            //lCommand是传的报警类型
            System.out.println("接收到的报警值为：" + lCommand.intValue());
            logger.info("接收到的报警值为：" + lCommand.intValue());
            if (lCommand.intValue() == HCNetSDK.COMM_SNAP_MATCH_ALARM) {
                HCNetSDK.NET_VCA_FACESNAP_MATCH_ALARM strFaceSnapMatch = new HCNetSDK.NET_VCA_FACESNAP_MATCH_ALARM();
                strFaceSnapMatch.write();
                Pointer pFaceSnapMatch = strFaceSnapMatch.getPointer();
                pFaceSnapMatch.write(0, pAlarmInfo.getByteArray(0, strFaceSnapMatch.size()), 0, strFaceSnapMatch.size());
                strFaceSnapMatch.read();
                sAlarmType = sAlarmType + "：签到比对报警，相识度：" + strFaceSnapMatch.fSimilarity + "，姓名：" + new String(strFaceSnapMatch.struBlackListInfo.struBlackListInfo.struAttribute.byName, "GBK").trim() + "，\n证件信息：" + new String(strFaceSnapMatch.struBlackListInfo.struBlackListInfo.struAttribute.byCertificateNumber).trim();
                //获取人脸库ID
                byte[] FDIDbytes;
                if ((strFaceSnapMatch.struBlackListInfo.dwFDIDLen > 0) && (strFaceSnapMatch.struBlackListInfo.pFDID != null)) {
                    ByteBuffer FDIDbuffers = strFaceSnapMatch.struBlackListInfo.pFDID.getByteBuffer(0, strFaceSnapMatch.struBlackListInfo.dwFDIDLen);
                    FDIDbytes = new byte[strFaceSnapMatch.struBlackListInfo.dwFDIDLen];
                    FDIDbuffers.rewind();
                    FDIDbuffers.get(FDIDbytes);
                    sAlarmType = sAlarmType + "，人脸库的ID:" + new String(FDIDbytes).trim();
                }
                //获取人脸图片ID
                byte[] PIDbytes;
                if ((strFaceSnapMatch.struBlackListInfo.dwPIDLen > 0) && (strFaceSnapMatch.struBlackListInfo.pPID != null)) {
                    ByteBuffer PIDbuffers = strFaceSnapMatch.struBlackListInfo.pPID.getByteBuffer(0, strFaceSnapMatch.struBlackListInfo.dwPIDLen);
                    PIDbytes = new byte[strFaceSnapMatch.struBlackListInfo.dwPIDLen];
                    PIDbuffers.rewind();
                    PIDbuffers.get(PIDbytes);
                    sAlarmType = sAlarmType + "，人脸图片的ID:" + new String(PIDbytes).trim();
                }
                //报警类型
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                System.out.println("报警时间" + dateFormat.format(today) + "  报警IP" + sIP[0] + "   报警类型" + sAlarmType);
                String uuid = UUID.randomUUID().toString().replace("-", "");
                /**
                 * 时间
                 */
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simpleDateFormat.format(today);
                /**
                 * 设备来源
                 */
                String s = sIP[0];
                /**
                 * 姓名
                 */
                String trim = new String(strFaceSnapMatch.struBlackListInfo.struBlackListInfo.struAttribute.byName, "GBK").trim();
                /**
                 * 工号 8位以上
                 */
                String trim1 = new String(strFaceSnapMatch.struBlackListInfo.struBlackListInfo.struAttribute.byCertificateNumber).trim();
                if (s != null && strFaceSnapMatch.struBlackListInfo.struBlackListInfo.struAttribute.byName != null && strFaceSnapMatch.struBlackListInfo.struBlackListInfo.struAttribute.byCertificateNumber != null) {
                    ImeetingMsg imeetingMsg = new ImeetingMsg();
                    imeetingMsg.setId(uuid);
                    imeetingMsg.setImeetingTime(format);
                    imeetingMsg.setImeetingCameraid(s);
                    imeetingMsg.setImeetingUserid(trim1);
                    imeetingMsg.setImeetingUsername(trim);
                    System.out.println(imeetingMsg);
                    fmsgCallBack_v31.iImeetingMsgService.insert(imeetingMsg);
                    logger.info("数据已插入数据表，本次回调结束，插入时间为： " + format);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}
