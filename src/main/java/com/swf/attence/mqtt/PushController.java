//package com.swf.attence.mqtt;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.Subject;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
//import org.eclipse.paho.client.mqttv3.MqttTopic;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.fh.controller.base.BaseController;
//import com.fh.entity.Page;
//import com.fh.entity.system.User;
//import com.fh.service.information.push.PushService;
//import com.fh.service.system.dept.DeptService;
//import com.fh.util.Const;
//import com.fh.util.DateUtil;
//import com.fh.util.PageData;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
///**
// * @author zzm
// * mqtt给80服务端推送功能
// */
//@Controller
//@RequestMapping(value="/push")
//public class PushController   extends BaseController{
//    @Resource(name="deptService")
//    private DeptService deptService;
//
//    @Resource(name="pushService")
//    private PushService pushService;
//
//    // tcp://MQTT安装的服务器地址:MQTT定义的端口号
//    public static final String HOST = "tcp://192.168.10.80:1883";
//    // 定义一个主题
//    //public static final String TOPIC = "IM/admin/Inbox";
//    // 定义MQTT的ID，可以在MQTT服务配置中指定
//    private static final String clientid = "server11";
//
//    private static MqttClient client;
//    private static MqttTopic topic11;
//    private String userName = "admin";
//    private String passWord = "admin";
//
//    private MqttMessage message;
//
//    /**
//     *  构造函数
//     *
//     * @throws MqttException
//     */
//    public PushController() throws MqttException {
//        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
//        client = new MqttClient(HOST, clientid, new MemoryPersistence());
//        connect();
//    }
//    /**
//     * 用来连接服务器
//     */
//    private void connect() {
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setCleanSession(false);
//        options.setUserName(userName);
//        options.setPassword(passWord.toCharArray());
//        // 设置超时时间
//        options.setConnectionTimeout(10);
//        // 设置会话心跳时间
//        options.setKeepAliveInterval(20);
//        try {
//            client.setCallback(new PushCallback());
//            client.connect(options);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *
//     * @param topic
//     * @param message
//     * @throws MqttPersistenceException
//     * @throws MqttException
//     */
//    public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
//        MqttDeliveryToken token = topic.publish(message);
//        token.waitForCompletion();
//        System.out.println("message is published completely! " + token.isComplete());
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping(value="/save")
//    public ModelAndView save(HttpServletRequest request,
//                             @RequestParam(value="title",required=false) String title,
//                             @RequestParam(value="content",required=false) String content,
//                             @RequestParam(value="usernames",required=false) String usernames){
//        ModelAndView mv = new ModelAndView();
//        PageData pd = new PageData();
//        pd = this.getPageData();
//        pd.put("title", title);
//        pd.put("content", content);
//        String publisher = ((User)this.getRequest().getSession().getAttribute(Const.SESSION_USER)).getNAME();
//        pd.put("publisher", publisher);
//        pd.put("username", usernames);
//        pd.put("pushtime", DateUtil.getTime());				//新增时间
//        try {
//            int insertNum = pushService.save(pd);
//            //插入的条数
//            Long id = (Long) pd.get("id");//获取id
//            //创建MQTTserver
//            PushController server = new PushController();
//            server.message = new MqttMessage();
//            server.message.setQos(2);
//            server.message.setRetained(false);
//            //添加内容
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//            if(title.length() > 10) {
//                title = title.substring(0,10) +"...";
//            }
//            //content去除html标签并缩写
//            String contstr = "";
//            contstr = content.replaceAll("<[.[^>]]*>","");
//            contstr = contstr.replaceAll(" ", "");
//            if(contstr.length() > 15) {
//                contstr = contstr.substring(0,15) +"...";
//            }
//            //拼接要传入的json字符串
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("title", title);
//            jsonObj.put("message", contstr);
//            jsonObj.put("time", df.format(new Date()));
//            jsonObj.put("type", 4);
//            jsonObj.put("Id", id);
//            //放入mqtt server  转utf-8防止乱码
//            server.message.setPayload(jsonObj.toString().getBytes("UTF-8"));
//
//            String[] names = usernames.split(",");
//            for(String i:names) {
//                //设置topic
//                topic11 = client.getTopic("IM/"+i+"/Inbox");
//                //推送
//                server.publish(server.topic11, server.message);
//                System.out.println(server.message.isRetained() + "------ratained状态");
//            }
//
//            mv.addObject("msg","success");
//            mv.setViewName("information/push/push_list");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mv;
//    }
//}
