package com.swf.attence.hikConfig;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Component
public class ClientDemo {
    static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;//设备信息
    public NativeLong lUserID;//用户句柄
    public NativeLong lAlarmHandle;//报警布防句柄
    public NativeLong lListenHandle;//报警监听句柄
    public FMSGCallBack_V31 fMSFCallBack_V31;//报警回调函数实现
    public String m_sDeviceIP;//已登录设备的IP地址
    public String username; //设备用户名
    public String password;//设备登录密码
    public int iPort;//设备端口号


    public ClientDemo() {
        lUserID = new NativeLong(-1);
        lAlarmHandle = new NativeLong(-1);
        lListenHandle = new NativeLong(-1);
        fMSFCallBack_V31 = new FMSGCallBack_V31();
    }
    public boolean CameraInit(){
        //初始化
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true){
            System.out.println("初始化失败");
            return false;
        }else{
            System.out.println("初始化成功");
            return true;
        }
    }
    //注册
    public boolean register(String username, String password, String m_sDeviceIP){
        //注册之前先注销已注册的用户,预览情况下不可注销
        if (lUserID.longValue() > -1) {
            //先注销
            hCNetSDK.NET_DVR_Logout(lUserID);
            lUserID = new NativeLong(-1);
        }

        //注册
        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int iPort = 8000;
        System.out.println("注册，设备IP："+m_sDeviceIP);
        lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP,(short) iPort, username, password, m_strDeviceInfo);

        long userID = lUserID.longValue();
        if (userID == -1) {
            System.out.println("注册失败");
            return false;
        } else {
            System.out.println("注册成功,lUserID:"+userID);
            return true;
        }

    }
    //注销
    public void Logout(){
        //报警撤防
        if (lAlarmHandle.intValue() > -1)
        {
            if(!hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle))
            {
                System.out.println("撤防失败");
            }else{
                lAlarmHandle = new NativeLong(-1);
                System.out.println("撤防成功");
            }
        }

        //注销
        if (lUserID.longValue() > -1) {
            if(hCNetSDK.NET_DVR_Logout(lUserID))
            {
                System.out.println("注销成功");
                lUserID = new NativeLong(-1);
            }
            hCNetSDK.NET_DVR_Cleanup();
        }
    }

    //布防
    public boolean SetupAlarmChan() {
        if (lUserID.intValue() == -1)
        {
            System.out.println("请先注册");
            return false;
        }
        if (lAlarmHandle.intValue() < 0)//尚未布防,需要布防
        {
            if (fMSFCallBack_V31 != null)
            {
                Pointer pUser = null;
                if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser))
                {
                    System.out.println("设置回调函数失败!");
                    return false;
                }
            }
            System.out.println("设置回调函数成功");
            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
            m_strAlarmInfo.dwSize=m_strAlarmInfo.size();
            m_strAlarmInfo.byLevel=1;
            m_strAlarmInfo.byAlarmInfoType=1;
            m_strAlarmInfo.write();
            lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);

            if (lAlarmHandle.intValue() == -1)
            {
                System.out.println("布防失败");
                System.out.println("错误代码："+hCNetSDK.NET_DVR_GetLastError());
                return false;

            }else{
                System.out.println("布防成功");
                return true;
            }
        }else{
            System.out.println("已经布防，不要重复操作");
            return false;
        }
    }

    //撤防
    public void CloseAlarmChan() {
        //报警撤防
        if (lAlarmHandle.intValue() > -1)
        {
            if(hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle))
            {
                System.out.println("撤防成功");
                lAlarmHandle = new NativeLong(-1);
            }
        }
    }




    public static void main(String[] args) throws UnsupportedEncodingException, DocumentException, InterruptedException {
        ClientDemo clientDemo = new ClientDemo();
        clientDemo.CameraInit();
        /*clientDemo.register("admin","ytkj123456","10.21.244.169");*/
        clientDemo.register("admin","admin123456","10.21.244.166");
        clientDemo.SetupAlarmChan();
        /*FDLibBox fdLibBox = new FDLibBox(clientDemo);
        clientDemo.SetupAlarmChan();*/
        Thread.sleep(100000);
        }

    }



