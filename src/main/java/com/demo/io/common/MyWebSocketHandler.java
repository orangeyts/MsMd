package com.demo.io.common;

import com.alibaba.fastjson.JSONObject;
import com.demo.util.websocket.model.CommandType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("客户端收到服务器数据：" + msg.text());
        String msgText = msg.text();
        JSONObject jsonObject = JSONObject.parseObject(msgText);
        Map<String, Object> map = jsonObject;
        String groupId = jsonObject.getString("groupId");
        String userId = jsonObject.getString("userId");
        String message = jsonObject.getString("message");
        Integer type = jsonObject.getInteger("type");
        String longId = ctx.channel().id().asLongText();
        if (map.containsKey("login")) {
            login(map, ctx.channel().id());
        }else if(type != null && type.intValue() == CommandType.joinGroup){
            if (log.isInfoEnabled()){
                log.info("用户 [{}] ,加入群组 [{}]",userId,groupId);
            }
            joinGroup(groupId,userId);
        }else if(type != null && type.intValue() == CommandType.groupMessage){
            if (log.isInfoEnabled()){
                log.info("群发消息 [{}]  [{}]",groupId,message);
            }
            sendGroupMsg(groupId,message);
        } else {
            if (log.isInfoEnabled()){
                log.info("私信消息 [{}]  ",map);
            }
            sendToSomeone(map);
        }
    }

    //群发消息
    private void sendGroupMsg(String groupId, String message) {
        MyChannelHandlerPool.sendGroupMsg(groupId,message);
    }

    /**
     * 加入一个组
     * @param groupId
     * @param userId
     */
    private void joinGroup(String groupId, String userId) {
        MyChannelHandlerPool.createOrJoinGroup(groupId,userId);
    }

    //ping、pong
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //用于触发用户事件，包含触发读空闲、写空闲、读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                //关闭无用channel，以防资源浪费
                channel.close();
            }
        }
    }

    private void sendAllMessage(String message){
        //收到信息后，群发给所有channel
        MyChannelHandlerPool.channelGroup.writeAndFlush( new TextWebSocketFrame(message));
    }
    private void sendToSomeone(Map<String, Object> map) {
//        ChannelId channelId = (ChannelId) redisOperator.getObject(6, map.get("toId").toString());
//        ChannelId fromId = MyChannelHandlerPool.channelIdMap.get(map.get("fromId"));
//        MyChannelHandlerPool.channelGroup.find(fromId).writeAndFlush(new TextWebSocketFrame(map.toString()));

        ChannelId toId = MyChannelHandlerPool.channelIdMap.get(map.get("toId"));
        MyChannelHandlerPool.channelGroup.find(toId).writeAndFlush(new TextWebSocketFrame(map.toString()));
    }

    private void login(Map<String, Object> map, ChannelId channelId) {
        String uid = map.get("login").toString();
//        redisOperator.set(6, uid, channelId);
        MyChannelHandlerPool.channelIdMap.put(uid, channelId);
    }
}