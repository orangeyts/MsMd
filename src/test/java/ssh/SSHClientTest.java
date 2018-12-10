package ssh;

import com.demo.command.linux.SSHClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SSHClientTest {

    private SSHClient sshClient;

    @Before
    public void setUp() throws Exception {
        sshClient = new SSHClient();
        sshClient.setHost("ip").setPort(22).setUsername("root").setPassword("root");
        sshClient.login();
    }

    @Test
    public void sendCmd() throws Exception {
        String ret = sshClient.sendCmd("pwd");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);

//        ret = sshClient.sendCmd("vmstat");
        ret = sshClient.sendCmd("ifconfig");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);
    }

    @Test
    public void uploadFile() throws Exception {
        sshClient.putFile("E:\\", "微信图片_20180413173224.png","/data");
    }

    @After
    public void tearDown() throws Exception {
        sshClient.logout();
    }
}