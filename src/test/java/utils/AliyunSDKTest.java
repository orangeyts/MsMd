package utils;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupRequest;
import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;

@Slf4j
public class AliyunSDKTest {

    private static String accessKeyId;
    private static String accessSecret;
    private static String[] portRange;
    private static String groupId;

    /**
     * 命名空间前缀 用于切换不同的配置
     */
    private static String prefixNameSpace = "he.";
    private static String prefixNameSpace1 = "common.";

    @BeforeClass
    public static void beforeClass() throws Exception {
        PropKit.use("a_little_config_db.txt");
        accessKeyId = PropKit.get(prefixNameSpace + "accessKeyId");
        accessSecret = PropKit.get(prefixNameSpace + "accessSecret");
        groupId = PropKit.get(prefixNameSpace + "groupId");
        portRange = PropKit.get(prefixNameSpace + "portRange").split(",");

    }


    /**
     * 测试获取 测试 resources配置文件的配置
     */
    @Test
    public void getConfig(){
        PropKit.use("a_little_config_db.txt");
        System.out.println(PropKit.get("testName"));
        System.out.println(PropKit.get("testPwd"));
    }


    /**
     * 根据当前 公网IP 授权安全组 IP
     * https://help.aliyun.com/document_detail/25554.html?spm=a2c4g.11186623.6.1330.499527af6BDMJV#h2-url-2
     */
    @Test
    public void modifySecurityGroup(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-shenzhen", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

//        String[] portRange = {"27017/27017","22/22"};
//        String[] portRange = {"3306/3306","22/22"};
        //当前网络公网IP
        String currentNetPublicIP = getPublicIP();


        for (String port : portRange) {
            AuthorizeSecurityGroupRequest request = new AuthorizeSecurityGroupRequest();
            request.setRegionId("cn-shenzhen");
            request.setSecurityGroupId(groupId);
            request.setNicType("internet");
            request.setIpProtocol("TCP");
            request.setDescription("gen by SDK " + DateKit.toStr(new Date(),DateKit.timeStampPattern));
            request.setSourceCidrIp(currentNetPublicIP);
            request.setPortRange(port);
            try {
                AuthorizeSecurityGroupResponse response = client.getAcsResponse(request);
                System.out.println(new Gson().toJson(response));
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                System.out.println("ErrCode:" + e.getErrCode());
                System.out.println("ErrMsg:" + e.getErrMsg());
                System.out.println("RequestId:" + e.getRequestId());
            }
        }



    }

    @Test
    public void getRealIp() throws Exception {
        getPublicIP();
    }


    /**
     * 获取公网IP
     *
     * @return
     */
    public String getPublicIP(){
        try {
            String javascriptCode = HttpKit.get("http://pv.sohu.com/cityjson?ie=utf-8");
            System.out.println(javascriptCode);

            String JS_ENGINE_NAME= "JavaScript";
            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine engine = sem.getEngineByName(JS_ENGINE_NAME);
            Object eval = engine.eval(javascriptCode);
            ScriptObjectMirror returnCitySN = (ScriptObjectMirror)engine.get("returnCitySN");
            System.out.println(returnCitySN.get("cip"));
            return (String)returnCitySN.get("cip");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
