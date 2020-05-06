package com.swf.attence.hikConfig;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FDLibBox {
    public ClientDemo m_alarm;
    public static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    public String m_FDID; // 人脸库ID
    public boolean m_isSupportFDLib; // 是否支持人脸功能
    public List<HCNetSDK.NET_DVR_FDLIB_PARAM> m_FDLibList;
    public NativeLong m_lUploadHandle;
    public NativeLong m_UploadStatus;
    public String m_picID;
    public String strModeData;

    public FDLibBox(ClientDemo obj) {
        m_alarm = obj;
        m_FDLibList = new ArrayList<HCNetSDK.NET_DVR_FDLIB_PARAM>();
        m_lUploadHandle = new NativeLong(-1);
        m_UploadStatus = new NativeLong(-1);
        try {
            //查询是否支持人脸库
            if (GetFaceCapabilities()) {
                m_isSupportFDLib = true;
                System.out.println("支持人脸库");
            } else {
                m_isSupportFDLib = false;
                System.out.println("不支持人脸库");
            }
        } catch (DocumentException ex) {
            Logger.getLogger(FDLibBox.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //人脸能力集获取
    public boolean GetFaceCapabilities() throws DocumentException {
        HCNetSDK.NET_DVR_XML_CONFIG_INPUT inBuf = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        inBuf.dwSize = inBuf.size();
        String url = "GET /ISAPI/Intelligent/FDLib/capabilities\r\n";

        HCNetSDK.BYTE_ARRAY ptrUrl = new HCNetSDK.BYTE_ARRAY(url.length());
        /**
         *
         */
        System.arraycopy(url.getBytes(), 0, ptrUrl.byValue, 0, url.length());
        ptrUrl.write();
        inBuf.lpRequestUrl = ptrUrl.getPointer();
        inBuf.dwRequestUrlLen = url.length();

        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT outBuf = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        outBuf.dwSize = outBuf.size();
        HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_DATA_LEN);
        outBuf.lpOutBuffer = ptrOutByte.getPointer();
        outBuf.dwOutBufferSize = HCNetSDK.ISAPI_DATA_LEN;
        outBuf.write();

        if (hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, inBuf, outBuf)) {
            return true;
        } else {
            int code = hCNetSDK.NET_DVR_GetLastError();
            System.out.println("获取人脸能力集失败: " + code);
            return false;
        }
    }

    /**
     * 人脸库的查方法  注意是针对整个库
     *
     * @return
     */
    public boolean SearchFDLib() {
        try {
            if (m_isSupportFDLib) {
                //返回true，说明支持人脸
                HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
                struInput.dwSize = struInput.size();
                String str = "GET /ISAPI/Intelligent/FDLib\r\n";
                HCNetSDK.BYTE_ARRAY ptrUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
                System.arraycopy(str.getBytes(), 0, ptrUrl.byValue, 0, str.length());
                ptrUrl.write();
                struInput.lpRequestUrl = ptrUrl.getPointer();
                struInput.dwRequestUrlLen = str.length();

                HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
                struOutput.dwSize = struOutput.size();

                HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_DATA_LEN);
                struOutput.lpOutBuffer = ptrOutByte.getPointer();
                struOutput.dwOutBufferSize = HCNetSDK.ISAPI_DATA_LEN;

                HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_STATUS_LEN);
                struOutput.lpStatusBuffer = ptrStatusByte.getPointer();
                struOutput.dwStatusSize = HCNetSDK.ISAPI_STATUS_LEN;
                struOutput.write();

                if (hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
                    String xmlStr = struOutput.lpOutBuffer.getString(0);
                    //dom4j解析xml
                    Document document = DocumentHelper.parseText(xmlStr);
                    //获取根节点元素对象
                    Element FDLibBaseCfgList = document.getRootElement();
                    //同时迭代当前节点下面的所有子节点
                    Iterator<Element> iterator = FDLibBaseCfgList.elementIterator();
                    while (iterator.hasNext()) {
                        HCNetSDK.NET_DVR_FDLIB_PARAM tmp = new HCNetSDK.NET_DVR_FDLIB_PARAM();
                        Element e = iterator.next();
                        Iterator<Element> iterator2 = e.elementIterator();
                        while (iterator2.hasNext()) {
                            Element e2 = iterator2.next();
                            if (e2.getName().equals("id")) {
                                String id = e2.getText();
                                tmp.dwID = Integer.parseInt(id);
                            }
                            if (e2.getName().equals("name")) {
                                tmp.szFDName = e2.getText();
                            }
                            if (e2.getName().equals("FDID")) {
                                tmp.szFDID = e2.getText();
                            }
                        }
                        m_FDLibList.add(tmp);
                    }
                } else {
                    int code = hCNetSDK.NET_DVR_GetLastError();
                    System.out.println("创建人脸库失败: " + code);
                    return false;
                }
            } else {
                return false;
            }
        } catch (DocumentException ex) {
            Logger.getLogger(FDLibBox.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }


    /**
     * 人脸库的增方法
     *
     * @param FDLibName
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean CreateFDLib(String FDLibName) throws UnsupportedEncodingException {
        try {
            if (m_isSupportFDLib) {
                //返回true，说明支持人脸
                HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
                struInput.dwSize = struInput.size();

                String str = "POST /ISAPI/Intelligent/FDLib\r\n";
                HCNetSDK.BYTE_ARRAY ptrUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
                System.arraycopy(str.getBytes(), 0, ptrUrl.byValue, 0, str.length());
                ptrUrl.write();
                struInput.lpRequestUrl = ptrUrl.getPointer();
                struInput.dwRequestUrlLen = str.length();

                byte[] byFDLibName = FDLibName.getBytes("UTF-8");
                String strInBuffer1 = new String("<CreateFDLibList><CreateFDLib><id>1</id><name>");
                String strInBuffer2 = new String("</name><thresholdValue>1</thresholdValue><customInfo /></CreateFDLib></CreateFDLibList>");
                int iStringSize = byFDLibName.length + strInBuffer1.length() + strInBuffer2.length();

                HCNetSDK.BYTE_ARRAY ptrByte = new HCNetSDK.BYTE_ARRAY(iStringSize);
                System.arraycopy(strInBuffer1.getBytes(), 0, ptrByte.byValue, 0, strInBuffer1.length());
                System.arraycopy(byFDLibName, 0, ptrByte.byValue, strInBuffer1.length(), byFDLibName.length);
                System.arraycopy(strInBuffer2.getBytes(), 0, ptrByte.byValue, strInBuffer1.length() + byFDLibName.length, strInBuffer2.length());
                ptrByte.write();

                struInput.lpInBuffer = ptrByte.getPointer();
                struInput.dwInBufferSize = ptrByte.byValue.length;
                struInput.write();

                HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
                struOutput.dwSize = struOutput.size();

                HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_DATA_LEN);
                struOutput.lpOutBuffer = ptrOutByte.getPointer();
                struOutput.dwOutBufferSize = HCNetSDK.ISAPI_DATA_LEN;

                HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_STATUS_LEN);
                struOutput.lpStatusBuffer = ptrStatusByte.getPointer();
                struOutput.dwStatusSize = HCNetSDK.ISAPI_STATUS_LEN;
                struOutput.write();

                if (hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
                    String xmlStr = struOutput.lpOutBuffer.getString(0);

                    //dom4j解析xml
                    Document document = DocumentHelper.parseText(xmlStr);
                    //获取根节点元素对象
                    Element FDLibInfoList = document.getRootElement();

                    //同时迭代当前节点下面的所有子节点
                    Iterator<Element> iterator = FDLibInfoList.elementIterator();
                    Element FDLibInfo = iterator.next();
                    Iterator<Element> iterator2 = FDLibInfo.elementIterator();
                    while (iterator2.hasNext()) {
                        Element e = iterator2.next();
                        if (e.getName().equals("FDID")) {
                            m_FDID = e.getText();
                        }
                        System.out.println(e.getName() + "：" + e.getText());
                    }
                } else {
                    int code = hCNetSDK.NET_DVR_GetLastError();
                    JOptionPane.showMessageDialog(null, "创建人脸库失败: " + code);
                    return false;
                }
            } else {
                return false;
            }
        } catch (DocumentException ex) {
            Logger.getLogger(FDLibBox.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * 调用本函数前需要先使用SearchFDLib()，m_FDLibList才会被填充值  是人脸库的删
     *
     * @param index
     * @return
     */
    public boolean DeleteFDLib(int index) {
        HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        struInput.dwSize = struInput.size();
        String id = m_FDLibList.get(index).szFDID;
        String str = "DELETE /ISAPI/Intelligent/FDLib/" + id;
        HCNetSDK.BYTE_ARRAY ptrUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
        System.arraycopy(str.getBytes(), 0, ptrUrl.byValue, 0, str.length());
        ptrUrl.write();
        struInput.lpRequestUrl = ptrUrl.getPointer();
        struInput.dwRequestUrlLen = str.length();

        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        struOutput.dwSize = struOutput.size();
        struOutput.write();

        if (hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
            System.out.println("删除人脸库成功");
            return true;
        } else {
            int code = hCNetSDK.NET_DVR_GetLastError();
            System.out.println("删除人脸库失败: " + code);
            return false;
        }
    }

    /**
     * 被UploadFaceLinData调用，上传方法
     *
     * @param index
     * @return
     */
    public boolean UploadFile(int index) {
        if (m_isSupportFDLib) {
            //返回true，说明支持人脸
            HCNetSDK.NET_DVR_FACELIB_COND struInput = new HCNetSDK.NET_DVR_FACELIB_COND();
            struInput.dwSize = struInput.size();
            struInput.szFDID = m_FDLibList.get(index).szFDID.getBytes();
            struInput.byConcurrent = 0;
            struInput.byCover = 1;
            struInput.byCustomFaceLibID = 0;
            struInput.write();
            Pointer lpInput = struInput.getPointer();
            NativeLong ret = hCNetSDK.NET_DVR_UploadFile_V40(m_alarm.lUserID, HCNetSDK.IMPORT_DATA_TO_FACELIB, lpInput, struInput.size(), null, null, 0);
            if (ret.longValue() == -1) {
                int code = hCNetSDK.NET_DVR_GetLastError();
                System.out.println("上传图片文件失败: " + code);
                return false;
            } else {
                m_lUploadHandle = ret;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 被UploadFaceLinData调用 用来上传图片和图片附加信息
     *
     * @param userpicPath
     * @param userdataPath
     */
    public void UploadSend(String userpicPath, String userdataPath) {
        FileInputStream picfile = null;
        FileInputStream xmlfile = null;
        int picdataLength = 0;
        int xmldataLength = 0;

        try {
            picfile = new FileInputStream(new File(userpicPath));
            xmlfile = new FileInputStream(new File(userdataPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            picdataLength = picfile.available();
            xmldataLength = xmlfile.available();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        if (picdataLength < 0 || xmldataLength < 0) {
            System.out.println("input file/xml dataSize < 0");
            return;
        }

        HCNetSDK.BYTE_ARRAY ptrpicByte = new HCNetSDK.BYTE_ARRAY(picdataLength);
        HCNetSDK.BYTE_ARRAY ptrxmlByte = new HCNetSDK.BYTE_ARRAY(xmldataLength);


        try {
            picfile.read(ptrpicByte.byValue);
            xmlfile.read(ptrxmlByte.byValue);
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        ptrpicByte.write();
        ptrxmlByte.write();


        HCNetSDK.NET_DVR_SEND_PARAM_IN struSendParam = new HCNetSDK.NET_DVR_SEND_PARAM_IN();

        struSendParam.pSendData = ptrpicByte.getPointer();
        struSendParam.dwSendDataLen = picdataLength;
        struSendParam.pSendAppendData = ptrxmlByte.getPointer();
        struSendParam.dwSendAppendDataLen = xmldataLength;
        if (struSendParam.pSendData == null || struSendParam.pSendAppendData == null || struSendParam.dwSendDataLen == 0 || struSendParam.dwSendAppendDataLen == 0) {
            System.out.println("input file/xml data err");
            return;
        }

        struSendParam.byPicType = 1;
        struSendParam.dwPicMangeNo = 0;
        struSendParam.write();

        NativeLong iRet = hCNetSDK.NET_DVR_UploadSend(m_lUploadHandle, struSendParam.getPointer(), null);

        System.out.println("iRet=" + iRet);
        if (iRet.longValue() < 0) {
            System.out.println("NET_DVR_UploadSend fail,error=" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_UploadSend success");
            System.out.println("dwSendDataLen =" + struSendParam.dwSendDataLen);
            System.out.println("dwSendAppendDataLen =" + struSendParam.dwSendAppendDataLen);
        }

        try {
            picfile.close();
            xmlfile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 下发人脸信息 ，index是指人脸库的索引 但是得先调用SearchFDLib 填充值到m_FDlibList
     *
     * @param index
     * @param userpicPath
     * @param userdataPath
     */
    public synchronized void UploadFaceLinData(int index, String userpicPath, String userdataPath) {
        if (m_lUploadHandle.longValue() != -1) {
            if (hCNetSDK.NET_DVR_UploadClose(m_lUploadHandle)) {
                System.out.println("NET_DVR_UploadClose success");
            } else {
                System.out.println("NET_DVR_UploadClose fail,error=" + hCNetSDK.NET_DVR_GetLastError());
            }
        }
        UploadFile(index);

        if (m_lUploadHandle.longValue() < 0) {
            System.out.println("NET_DVR_UploadFile_V40 fail,error=" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_UploadFile_V40 success");
        }
        Thread thread = new Thread() {
            public void run() {
                UploadSend(userpicPath,userdataPath);
                while (true) {
                    if (-1 == m_lUploadHandle.longValue()) {
                        return;
                    }

                    m_UploadStatus = getUploadState();
                    if (m_UploadStatus.longValue() == 1) {
                        HCNetSDK.NET_DVR_UPLOAD_FILE_RET struPicRet = new HCNetSDK.NET_DVR_UPLOAD_FILE_RET();
                        struPicRet.write();
                        Pointer lpPic = struPicRet.getPointer();

                        boolean bRet = hCNetSDK.NET_DVR_GetUploadResult(m_lUploadHandle, lpPic, struPicRet.size());
                        if (!bRet) {
                            System.out.println("NET_DVR_GetUploadResult failed with:" + hCNetSDK.NET_DVR_GetLastError());
                        } else {
                            System.out.println("NET_DVR_GetUploadResult succ");
                            struPicRet.read();
                            m_picID = new String(struPicRet.sUrl);
                            System.out.println("PicID:" + m_picID);
                            System.out.println("图片上传成功 PID:" + m_picID);
                        }


                        if (hCNetSDK.NET_DVR_UploadClose(m_lUploadHandle)) {
                            m_lUploadHandle.setValue(-1);
                        }

                    } else if (m_UploadStatus.longValue() >= 3 || m_UploadStatus.longValue() == -1) {
                        System.out.println("m_UploadStatus = " + m_UploadStatus);
                        hCNetSDK.NET_DVR_UploadClose(m_lUploadHandle);
                        m_lUploadHandle.setValue(-1);
                        break;
                    }
                }
            }
        };
        thread.start();
    }


    /**
     * 被UploadFaceLinData调用，上传进度函数
     *
     * @return
     */
    public NativeLong getUploadState() {
        IntByReference pInt = new IntByReference(0);
        m_UploadStatus = hCNetSDK.NET_DVR_GetUploadState(m_lUploadHandle, pInt);
        if (m_UploadStatus.longValue() == -1) {
            System.out.println("NET_DVR_GetUploadState fail,error=" + hCNetSDK.NET_DVR_GetLastError());
        } else if (m_UploadStatus.longValue() == 2) {
            System.out.println("is uploading!!!!  progress = " + pInt.getValue());
        } else if (m_UploadStatus.longValue() == 1) {
            System.out.println("progress = " + pInt.getValue());
            System.out.println("Uploading Succ!!!!!");
        } else {
            System.out.println("NET_DVR_GetUploadState fail  m_UploadStatus=" + m_UploadStatus);
            System.out.println("NET_DVR_GetUploadState fail,error=" + hCNetSDK.NET_DVR_GetLastError());
        }
        return m_UploadStatus;
    }

    /**
     * @param index
     */
    public void SetFaceAppendData(int index, String m_picID) {
        HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        struInput.dwSize = struInput.size();
        String id = m_FDLibList.get(index).szFDID;
        String str = "PUT /ISAPI/Intelligent/FDLib/" + id + "/picture/" + m_picID;
        HCNetSDK.BYTE_ARRAY ptrSetFaceAppendDataUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
        System.arraycopy(str.getBytes(), 0, ptrSetFaceAppendDataUrl.byValue, 0, str.length());
        ptrSetFaceAppendDataUrl.write();
        struInput.lpRequestUrl = ptrSetFaceAppendDataUrl.getPointer();
        struInput.dwRequestUrlLen = str.length();
        String strInBuffer = "<FaceAppendData><name>Name</name><bornTime>2000-01-01</bornTime><sex>male</sex><province /><certificateType /><certificateNumber /></FaceAppendData>";

        HCNetSDK.BYTE_ARRAY ptrByte = new HCNetSDK.BYTE_ARRAY(10 * 1024);
        ptrByte.byValue = strInBuffer.getBytes();
        ptrByte.write();
        struInput.lpInBuffer = ptrByte.getPointer();
        struInput.dwInBufferSize = strInBuffer.length();
        struInput.write();

        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        struOutput.dwSize = struOutput.size();

        HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_DATA_LEN);
        struOutput.lpOutBuffer = ptrOutByte.getPointer();
        struOutput.dwOutBufferSize = HCNetSDK.ISAPI_DATA_LEN;

        HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_STATUS_LEN);
        struOutput.lpStatusBuffer = ptrStatusByte.getPointer();
        struOutput.dwStatusSize = HCNetSDK.ISAPI_STATUS_LEN;
        struOutput.write();

        if (!hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
            System.out.println("PUT /ISAPI/Intelligent/FDLib/1/picture/1 failed with:" + m_alarm.lUserID + " " + hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("PUT /ISAPI/Intelligent/FDLib/1/picture/1 success");
            System.out.println("dwReturnXMLSize=" + struOutput.dwReturnedXMLSize);
        }
    }


    public void DeleteFaceAppendData(int index, String m_picID) {
        String id = m_FDLibList.get(index).szFDID;

        String str = "DELETE /ISAPI/Intelligent/FDLib/" + id + "/picture/" + m_picID;

        HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        struInput.dwSize = struInput.size();

        HCNetSDK.BYTE_ARRAY ptrDeleteFaceLibUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
        System.arraycopy(str.getBytes(), 0, ptrDeleteFaceLibUrl.byValue, 0, str.length());
        ptrDeleteFaceLibUrl.write();
        struInput.lpRequestUrl = ptrDeleteFaceLibUrl.getPointer();
        struInput.dwRequestUrlLen = str.length();
        struInput.lpInBuffer = null;
        struInput.dwInBufferSize = 0;
        struInput.write();

        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        struOutput.dwSize = struOutput.size();
        struOutput.lpOutBuffer = null;
        struOutput.dwOutBufferSize = 0;

        HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_STATUS_LEN);
        struOutput.lpStatusBuffer = ptrStatusByte.getPointer();
        struOutput.dwStatusSize = HCNetSDK.ISAPI_STATUS_LEN;
        struOutput.write();

        if (!hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
            System.out.println("lpRequestUrl:" + str);
            System.out.println("NET_DVR_STDXMLConfig DELETE failed with:" + " " + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("lpRequestUrl:" + str);
            System.out.println("NET_DVR_STDXMLConfig DELETE Succ!!!!!!!!!!!!!!!");
            System.out.println("图片删除成功 PID:" + m_picID);
        }
    }

    public void GetFaceAppendData(int index, String m_picID) {
        String id = m_FDLibList.get(index).szFDID;
        HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        struInput.dwSize = struInput.size();

        String str = "GET /ISAPI/Intelligent/FDLib/" + id + "/picture/" + m_picID;
        HCNetSDK.BYTE_ARRAY ptrGetFaceAppendDataUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
        System.arraycopy(str.getBytes(), 0, ptrGetFaceAppendDataUrl.byValue, 0, str.length());
        ptrGetFaceAppendDataUrl.write();
        struInput.lpRequestUrl = ptrGetFaceAppendDataUrl.getPointer();
        struInput.dwRequestUrlLen = str.length();


        struInput.lpInBuffer = null;
        struInput.dwInBufferSize = 0;
        struInput.write();

        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        struOutput.dwSize = struOutput.size();

        HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_DATA_LEN);
        struOutput.lpOutBuffer = ptrOutByte.getPointer();
        struOutput.dwOutBufferSize = HCNetSDK.ISAPI_DATA_LEN;

        HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_STATUS_LEN);
        struOutput.lpStatusBuffer = ptrStatusByte.getPointer();
        struOutput.dwStatusSize = HCNetSDK.ISAPI_STATUS_LEN;
        struOutput.write();

        if (!hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
            System.out.println(str + "failed with:" + m_alarm.lUserID + " " + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println(str + "success");
            System.out.println("dwReturnXMLSize=" + struOutput.dwReturnedXMLSize);
            System.out.println();
        }
    }

    public boolean SetThresholdValue(int value, int index) {
        if (m_isSupportFDLib) {
            //返回true，说明支持人脸
            HCNetSDK.NET_DVR_XML_CONFIG_INPUT struInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
            struInput.dwSize = struInput.size();

            String str = "PUT /ISAPI/Intelligent/channels/1/faceContrast\r\n";
            HCNetSDK.BYTE_ARRAY ptrUrl = new HCNetSDK.BYTE_ARRAY(HCNetSDK.BYTE_ARRAY_LEN);
            System.arraycopy(str.getBytes(), 0, ptrUrl.byValue, 0, str.length());
            ptrUrl.write();
            struInput.lpRequestUrl = ptrUrl.getPointer();
            struInput.dwRequestUrlLen = str.length();

            String strInBuffer = new String("<FaceContrastList><FaceContrast><id>1</id><enable>true</enable><faceContrastType>faceContrast</faceContrastType><thresholdValue>" + value + "</thresholdValue><FDLibList><FDLib><id>" + m_FDLibList.get(index).dwID + "</id><FDID>" + m_FDLibList.get(index).szFDID + "</FDID></FDLib></FDLibList></FaceContrast></FaceContrastList>");
            HCNetSDK.BYTE_ARRAY ptrByte = new HCNetSDK.BYTE_ARRAY(10 * HCNetSDK.BYTE_ARRAY_LEN);
            ptrByte.byValue = strInBuffer.getBytes();
            ptrByte.write();
            struInput.lpInBuffer = ptrByte.getPointer();
            struInput.dwInBufferSize = strInBuffer.length();
            struInput.write();

            HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
            struOutput.dwSize = struOutput.size();

            HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_DATA_LEN);
            struOutput.lpOutBuffer = ptrOutByte.getPointer();
            struOutput.dwOutBufferSize = HCNetSDK.ISAPI_DATA_LEN;

            HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(HCNetSDK.ISAPI_STATUS_LEN);
            struOutput.lpStatusBuffer = ptrStatusByte.getPointer();
            struOutput.dwStatusSize = HCNetSDK.ISAPI_STATUS_LEN;
            struOutput.write();

            if (hCNetSDK.NET_DVR_STDXMLConfig(m_alarm.lUserID, struInput, struOutput)) {
                System.out.println("设置阀值成功: " + value);
            } else {
                int code = hCNetSDK.NET_DVR_GetLastError();
                System.out.println("设置阀值失败: " + code);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }


}
