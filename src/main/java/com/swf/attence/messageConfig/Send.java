package com.swf.attence.messageConfig;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class Send {

    public static String SMS(String postData, String postUrl) {
        try {
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "发送失败，请重新发现";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "短信动作发送完成";
    }

    public String postData(String sname,String spwd,String scorpid,String sprdid,String sdst) throws UnsupportedEncodingException {
        String PostData = "sname="+sname+"&spwd="+spwd+"&scorpid="+scorpid+"&sprdid="+sprdid+"&sdst="+sdst+"&smsg="+java.net.URLEncoder.encode("这是一条测试短信【仰天信息】","utf-8");
        return PostData;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Send send = new Send();
        String s = send.postData("dlhzytxx", "YT123456", "", "1012808", "18758861624");
        //out.println(PostData);
        String ret = Send.SMS(s, "http://cf.51welink.com/submitdata/Service.asmx/g_Submit");
        System.out.println(ret);
    }
}
