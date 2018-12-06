package com.demo.io.client;

import com.alibaba.fastjson.JSON;
import com.demo.io.common.Const;
import com.demo.io.common.HelloPacket;
import com.demo.io.common.User;
import com.jfinal.plugin.IPlugin;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

/**
 *
 * @author tanyaowu
 *
 */
public class HelloClientStarter implements IPlugin {
	//服务器节点
	public static Node serverNode = new Node(Const.SERVER, Const.PORT);

	//handler, 包括编码、解码、消息处理
	public static ClientAioHandler tioClientHandler = new HelloClientAioHandler();

	//事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	public static ClientAioListener aioListener = null;

	//断链后自动连接的，不想自动连接请设为null
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	//一组连接共用的上下文对象
	public static ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);

	public static TioClient tioClient = null;
	public static ClientChannelContext clientChannelContext = null;

	/**
	 * 启动程序入口
	 */
	public static void main(String[] args) throws Exception {
		clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
		tioClient = new TioClient(clientGroupContext);
		clientChannelContext = tioClient.connect(serverNode);
		//连上后，发条消息玩玩
		send();
	}

	private static void send() throws Exception {

		User user = new User();
		user.setId("1");
		user.setMsg("hello tio server,i am client");
		user.setName("seven");
		String body = JSON.toJSONString(user);

		HelloPacket packet = new HelloPacket();
		packet.setBody(body.getBytes(HelloPacket.CHARSET));
		Tio.send(clientChannelContext, packet);
	}

	@Override
	public boolean start() {
		try {
			clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
			tioClient = new TioClient(clientGroupContext);
			clientChannelContext = tioClient.connect(serverNode);
			//连上后，发条消息玩玩
			send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean stop() {
		return false;
	}
}
