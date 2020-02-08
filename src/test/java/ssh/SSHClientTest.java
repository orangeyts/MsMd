package ssh;

import com.demo.command.linux.SSHClient;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SSHClientTest {

    private SSHClient sshClient;

    @Before
    public void setUp() throws Exception {
        sshClient = new SSHClient();
        sshClient.setHost("localhost").setPort(22).setUsername("root").setPassword("");
        sshClient.login();
    }

    @Test
    public void sendCmd() throws Exception {
        String ret = sshClient.sendCmd("pwd",null);

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);

//        ret = sshClient.sendCmd("vmstat");
        ret = sshClient.sendCmd("ifconfig",null);

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);
    }

    @Test
    public void uploadFile() throws Exception {
        sshClient.putFile("E:\\", "微信图片_20180413173224.png","/data/sdf/asfsdf",null);
    }

    @Test
    public void mkdir() throws Exception {
        Channel channelSftp = sshClient.session.openChannel("sftp");
        channelSftp.connect();
        ChannelSftp c = (ChannelSftp) channelSftp;


        String remotePath = "/data/ss/dd";
        String[] folders = remotePath.split( "/" );
        boolean isFirst = true;
        for ( String folder : folders ) {
            if ( folder.length() > 0 ) {
                try {
                    c.cd( isFirst?"/"+folder:folder );
                }
                catch ( SftpException e ) {
                    c.mkdir(folder);
                    c.cd( folder );
                }
                isFirst = false;
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        sshClient.logout();
    }
}