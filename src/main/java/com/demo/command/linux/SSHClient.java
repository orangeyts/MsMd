package com.demo.command.linux;


import com.alibaba.fastjson.JSON;
import com.demo.common.model.TbBuild;
import com.demo.util.websocket.MyWebSocketClient;
import com.demo.util.websocket.model.CommandType;
import com.demo.util.websocket.model.GroupMessage;
import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * https://www.jianshu.com/p/b2c928ea7d50
 */
public class SSHClient {
    /**
     * Server Host IP Address，default value is localhost
     */
    private String host = "localhost";

    /**
     * Server SSH Port，default value is 22
     */
    private Integer port = 22;

    /**
     * SSH Login Username
     */
    private String username = "";

    /**
     * SSH Login Password
     */
    private String password = "";

    /**
     * JSch
     */
    private JSch jsch = null;

    /**
     * ssh session
     */
    private Session session = null;

    /**
     * ssh channel
     */
    private Channel channel = null;

    /**
     * timeout for session connection
     */
    private final Integer SESSION_TIMEOUT = 30000;

    /**
     * timeout for channel connection
     */
    private final Integer CHANNEL_TIMEOUT = 30000;

    /**
     * the interval for acquiring ret
     */
    private final Integer CYCLE_TIME = 100;

    public SSHClient() {
        // initialize
        jsch = new JSch();
    }

    public SSHClient setHost(String host) {
        this.host = host;
        return this;
    }

    public SSHClient setPort(Integer port) {
        this.port = port;
        return this;
    }

    public SSHClient setUsername(String username) {
        this.username = username;
        return this;
    }

    public SSHClient setPassword(String password) {
        this.password = password;
        return this;
    }


    /**
     * login to server
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {

        this.username = username;
        this.password = password;

        try {
            if (null == session) {

                session = jsch.getSession(this.username, this.host, this.port);
                session.setPassword(this.password);
                session.setUserInfo(new MyUserInfo());

                // It must not be recommended, but if you want to skip host-key check,
                // invoke following,
                session.setConfig("StrictHostKeyChecking", "no");
            }

            session.connect(SESSION_TIMEOUT);
        } catch (JSchException e) {
            System.err.println(e);
            this.logout();
        }
    }

    /**
     * login to server
     */
    public void login() {
        this.login(this.username, this.password);
    }

    /**
     * logout of server
     */
    public void logout() {
        this.session.disconnect();
    }

    /**
     * 上传文件
     *
     * @param localPath
     *            本地路径,若为空,表示当前路径
     * @param localFile
     *            本地文件名,若为空或是“*”,表示目前下全部文件
     * @param remotePath
     *            远程路径,若为空,表示当前路径,若服务器上无此目录,则会自动创建
     * @throws Exception
     */
    public void putFile(String localPath, String localFile, String remotePath)
            throws Exception {
        Channel channelSftp = session.openChannel("sftp");
        channelSftp.connect();
        ChannelSftp c = (ChannelSftp) channelSftp;
        String remoteFile = null;
        if (remotePath != null && remotePath.trim().length() > 0) {
            try {
                c.mkdir(remotePath);
            } catch (Exception e) {
            }
            remoteFile = remotePath + "/.";
        } else {
            remoteFile = ".";
        }
        String file = null;
        if (localFile == null || localFile.trim().length() == 0) {
            file = "*";
        } else {
            file = localFile;
        }
        if (localPath != null && localPath.trim().length() > 0) {
            if (localPath.endsWith("/")) {
                file = localPath + file;
            } else {
                file = localPath + "/" + file;
            }
        }
        c.put(file, remoteFile);

        channelSftp.disconnect();
    }


    /**
     * send command through the ssh session,return the ret of the channel
     *
     * @return
     */
    public String sendCmd(String command, TbBuild tbBuild) {

        String ret = "";

        // judge whether the session or channel is connected
        if (!session.isConnected()) {
            this.login();
        }

        // open channel for sending command
        try {
            this.channel = session.openChannel("exec");
            ((ChannelExec) this.channel).setCommand(command);
            this.channel.connect(CHANNEL_TIMEOUT);

            // no output stream
            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            // acquire for ret
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;

                    ret = new String(tmp, 0, i);
                    tbBuild.appendOutput(ret);
                    System.out.print(ret);
                }

                // quit the process of waiting for ret
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }

                // wait every 100ms
                try {
                    Thread.sleep(CYCLE_TIME);
                } catch (Exception ee) {
                    System.err.println(ee);
                }
            }

        } catch (JSchException e) {
            e.printStackTrace();
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e);
        } finally {
            // close channel
            this.channel.disconnect();
        }

        return ret;
    }

    /**
     * customized userinfo
     */
    private static class MyUserInfo implements UserInfo {
        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String s) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return false;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:" + s);
        }
    }
}