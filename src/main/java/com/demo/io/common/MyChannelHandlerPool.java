package com.demo.io.common;

import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 连接池管理
 * 参考 https://www.jianshu.com/p/75865e54b01f
 * @author orange
 * @date 2019/12/04
 */
@Slf4j
public class MyChannelHandlerPool {

    public MyChannelHandlerPool(){}

    public static Map<String, ChannelId> channelIdMap = new HashMap<>();

    /**
     * 群组管理
     * 例如一个工程构建,会产生一个ChannelGroup,后续查看这个工程 滚动输出信息的客户端 都要加入到这个组里
     * 这样 就可以广播消息到这群组了
     *
     * key : buildId
     * value : uid List
     * 表示这个组里里面加入了哪些 uid,消息分发的时候，到channelIdMap(总的链接列表)取到对应的Channel依次发送 群消息即可
     */
    public static Map<String, List<String>> channelGroupMap = new ConcurrentHashMap<>();

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /**
     * 创建 或者 加入组
     * @param groupId
     * @param uid
     */
    public static void createOrJoinGroup(String groupId, String uid){
        List<String> oldGroup = channelGroupMap.get(groupId);
        if (oldGroup != null && oldGroup.size() > 0){
            //已经存在这个组,把新的用户加入这个组
            oldGroup.add(uid);
        }else {
            //创建一个新的组
            List<String> newGroup = new ArrayList<>();
            newGroup.add(uid);
            channelGroupMap.put(groupId,newGroup);
        }
    }

    /**
     * 群发消息
     * @param groupId
     * @param message
     */
    public static void sendGroupMsg(String groupId, String message){
        List<String> groupUsers = channelGroupMap.get(groupId);
        if (groupUsers == null && groupUsers.size() == 0){
            log.info("该群组 [{}] 没有人");
            return;
        }
        for(String userId : groupUsers){
            ChannelId channelId = channelIdMap.get(userId);
            MyChannelHandlerPool.channelGroup.find(channelId).writeAndFlush(new TextWebSocketFrame(message));
        }
    }
}
