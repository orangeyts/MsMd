package com.demo.util.websocket;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.demo.util.websocket.model.CommandType;
import com.demo.util.websocket.model.GroupMessage;
import com.demo.util.websocket.model.JoinGroup;
import com.demo.util.websocket.model.Login;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

@Slf4j
public class MyWebSocketClient extends WebSocketClient{

    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        log.info("握手...");
        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
            String key = it.next();
            log.info(key+":"+shake.getFieldValue(key));
        }
    }

    @Override
    public void onMessage(String paramString) {
        log.info("接收到消息："+paramString);
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        log.info("关闭...");
    }

    @Override
    public void onError(Exception e) {
        log.info("异常"+e);

    }

    public static MyWebSocketClient connectServerAndLogin(String url,String loginMessage) throws Exception{
        MyWebSocketClient client = new MyWebSocketClient(url);
        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            log.info("还没有打开");
        }
        log.info("建立websocket连接");
        client.send(loginMessage);
        return client;
    }

    public static void main(String[] args) {
        try {
            String url = "ws://127.0.0.1:12345/ws";
            String loginMessage = JSON.toJSONString(new Login("1"));
            MyWebSocketClient myWebSocketClient = MyWebSocketClient.connectServerAndLogin(url,loginMessage);
            myWebSocketClient.send(JSON.toJSONString(new JoinGroup("1","2",CommandType.joinGroup)));
            myWebSocketClient.send(JSON.toJSONString(new GroupMessage("2","群发消息",CommandType.groupMessage)));

            int i = 0;
            while (true){
                myWebSocketClient.send(JSON.toJSONString(new GroupMessage("2","群发消息"+(i++),CommandType.groupMessage)));
                TimeUnit.SECONDS.sleep(5);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
