package com.demo.util.websocket;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import com.alibaba.fastjson.JSON;
import com.demo.util.websocket.model.CommandType;
import com.demo.util.websocket.model.GroupMessage;
import com.demo.util.websocket.model.JoinGroup;
import com.demo.util.websocket.model.Login;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MyWebSocketClient extends WebSocketClient{

    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("握手...");
        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
            String key = it.next();
            System.out.println(key+":"+shake.getFieldValue(key));
        }
    }

    @Override
    public void onMessage(String paramString) {
        System.out.println("接收到消息："+paramString);
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常"+e);

    }

    public static MyWebSocketClient connectServerAndLogin(String url,String loginMessage) throws Exception{
        MyWebSocketClient client = new MyWebSocketClient(url);
        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            System.out.println("还没有打开");
        }
        System.out.println("建立websocket连接");
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
