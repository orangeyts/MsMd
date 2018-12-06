package com.demo.io;

import com.alibaba.fastjson.JSON;
import com.demo.io.client.HelloClientStarter;
import com.demo.io.common.HelloPacket;
import com.demo.io.common.User;
import com.demo.io.server.HelloServerStarter;
import com.jfinal.core.Controller;
import org.tio.core.Tio;

import java.io.UnsupportedEncodingException;

/**
 * 发送消息给服务端：http://localhost/tio/helloserver
 * 发送消息给客户端：http://localhost/tio/helloclient
 * 绑定userId参考：com/seven/server/HelloServerAioHandler.java:107
 */
public class HelloTioController extends Controller {

    /**
     * 发送消息去客户端
     */
    public void helloserver() throws UnsupportedEncodingException {

        User user = new User();
        user.setId("1");
        user.setMsg("hello tio");
        user.setName("seven");

        String body = JSON.toJSONString(user);

        HelloPacket packet = new HelloPacket();
        packet.setBody(body.getBytes(HelloPacket.CHARSET));
        Tio.send(HelloClientStarter.clientChannelContext,packet);
        renderText("success");
    }



    /**
     * 发送消息去客户端
     */
    public void helloclient() throws UnsupportedEncodingException {
        HelloPacket packet = new HelloPacket();
        packet.setBody("hello client,i amd server".getBytes(HelloPacket.CHARSET));
        Tio.sendToUser(HelloServerStarter.serverGroupContext,"1",packet);
        renderText("success");
    }
}
