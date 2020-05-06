package com.swf.attence.mqtt;
import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Title:Server Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 */
@Service
@Component
public class ServerMQTT implements ApplicationRunner {
    @Resource
    PushCallback pushCallback;
    // tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://192.168.0.180:1883";
    // 定义一个主题
    public static final String TOPIC = "topic2";
    // 定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "server11";

    private static MqttClient client;
    public static MqttTopic topic11;
    //用户名和密码
    private String userName = "admin";
    private String passWord = "admin";

    public MqttMessage message;

    /**
     * 构造函数
     *
     * @throws MqttException
     */
//    public ServerMQTT() throws MqttException {
//        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
//        client = new MqttClient(HOST, clientid, new MemoryPersistence());
//        connect();
//    }

    /**
     * 用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
//            client.setCallback(new PushCallback());
            client.setCallback(pushCallback);
            client.connect(options);
            topic11 = client.getTopic(TOPIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! " + token.isComplete());
    }

    /**
     * 启动入口
     *
     * @param args
     * @throws MqttException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws MqttException, UnsupportedEncodingException {
        ServerMQTT server = new ServerMQTT();
        server.message = new MqttMessage();
        /* Qos服务质量等级：
         * 0，最多一次，不管是否接收到；
         * 1，最少一次，保证信息将会被至少发送一次给接受者
         * 2，只一次，确保每个消息都只被接收到的一次，他是最安全也是最慢的服务等级
         */
        server.message.setQos(2);
        server.message.setRetained(true);
        server.message.setPayload("test".getBytes("UTF-8"));
        server.publish(server.topic11, server.message);
        System.out.println(server.message.isRetained() + "------ratained状态");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }
}
