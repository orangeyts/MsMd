package com.demo.util.edas;


import java.util.List;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.edas.model.v20170801.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.exceptions.ClientException;


/**
 * edas api
 * 如果担心 账户泄露可以把 拷贝一份 EdasAPI 为 EdasAPIInternal, git会自动忽略,方便本地调试
 * https://help.aliyun.com/document_detail/62123.html?spm=5176.215339.1147949.32.525562f6FKQNm5
 */
public class EdasAPI {

    //请填写阿里云主账号或子账号 AccessKey ID.
    public static final String aliyun_user_ak = "";
    //请填写阿里云主账号或子账号 AccessKey Secret.
    public static final String aliyun_user_sk = "";
    //请填写要执行 API 调用的应用所在地域 ID.
    public static final String region_id = "";

    /**
     * 列出 APP列表
     * @param defaultAcsClient
     */
    public static void listApp(DefaultAcsClient defaultAcsClient){
        ListApplicationRequest applist_req = new ListApplicationRequest();
        try {
            ListApplicationResponse applist_resp = defaultAcsClient.getAcsResponse(applist_req);
            if (applist_resp.getCode() == 200) {
                List<ListApplicationResponse.Application> applist = applist_resp.getApplicationList();
                if (applist != null && applist.size() > 0) {
                    for (ListApplicationResponse.Application app : applist) {
                        String app_name = app.getName();
                        String app_id = app.getAppId();
                        System.out.println("应用名称 : " + app_name + ", 应用Id : " + app_id);
                        ListDeployGroupRequest dglist_req = new ListDeployGroupRequest();
                        dglist_req.setAppId(app_id);
                        ListDeployGroupResponse dglist_resp = defaultAcsClient.getAcsResponse(dglist_req);
                        if (dglist_resp.getCode() == 200) {
                            List<ListDeployGroupResponse.DeployGroup> dglist = dglist_resp.getDeployGroupList();
                            for (ListDeployGroupResponse.DeployGroup dg : dglist) {
                                String dg_name = dg.getGroupName();
                                if ("_DEFAULT_GROUP".equals(dg_name)) {
                                    dg_name = "默认分组";
                                }
                                String dg_id = dg.getGroupId();
                                System.out.println("\t分组名 : " + dg_name + ", 分组Id : " + dg_id);
                            }
                        }
                    }
                } else {
                    System.out.println("获取到的应用列表为空, 请检查上面设置的region_id中是否存在应用.");
                }
            } else {
                // 打印错误原因
                System.out.println("API调用返回异常!\nMessage: " + applist_resp.getMessage() + "\nRequestId: " + applist_resp.getRequestId());
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出历史版本
     * @param defaultAcsClient
     */
    public static void ListHistoryDeployVersion(DefaultAcsClient defaultAcsClient,String appId){
        ListHistoryDeployVersionRequest applist_req = new ListHistoryDeployVersionRequest();
        applist_req.setAppId(appId);
        try {
            ListHistoryDeployVersionResponse applist_resp = defaultAcsClient.getAcsResponse(applist_req);
            if (applist_resp.getCode() == 200) {
                List<ListHistoryDeployVersionResponse.PackageVersion> applist = applist_resp.getPackageVersionList();
                if (applist != null && applist.size() > 0) {
                    for (ListHistoryDeployVersionResponse.PackageVersion version : applist) {
                        String packageVersion = version.getPackageVersion();
                        String app_id = version.getAppId();
                        System.out.println("版本名称 : " + packageVersion + ", 应用Id : " + app_id+ ", 应用Id : " + version.getWarUrl());
                    }
                } else {
                    System.out.println("获取到的应用列表为空, 请检查上面设置的region_id中是否存在应用.");
                }
            } else {
                // 打印错误原因
                System.out.println("API调用返回异常!\nMessage: " + applist_resp.getMessage() + "\nRequestId: " + applist_resp.getRequestId());
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    /**
     * 回滚应用
     * @param defaultAcsClient
     */
    public static String rollback(DefaultAcsClient defaultAcsClient,String appId,String historyVersion,String groupId){
        RollbackApplicationRequest applist_req = new RollbackApplicationRequest();
        applist_req.setAppId(appId);
        applist_req.setHistoryVersion(historyVersion);
        applist_req.setGroupId(groupId);
        String back = null;
        try {
            RollbackApplicationResponse applist_resp = defaultAcsClient.getAcsResponse(applist_req);
            if (applist_resp.getCode() == 200) {
                back = "变更id : " + applist_resp.getChangeOrderId() + ", message : " + applist_resp.getMessage()+ ", code : " + applist_resp.getCode();
                System.out.println(back);
            } else {
                // 打印错误原因
                back = "API调用返回异常!\nMessage: " + applist_resp.getMessage() + "\nRequestId: " + applist_resp.getRequestId();
                System.out.println(back);
            }
        } catch (ClientException e) {
            back = e.getMessage();
            e.printStackTrace();
        }
        return back;
    }



    public static void main(String args[]) {
//        listApp(getDefaultAcsClient());
//        ListHistoryDeployVersion(getDefaultAcsClient(),"-e522-4782-b27a-bca0db9e777b");
//        rollback(getDefaultAcsClient(),"-e522-4782-b27a-","-12-12 10:42:07","all");
    }

    public static DefaultAcsClient getDefaultAcsClient(String region_id,String aliyun_user_ak,String aliyun_user_sk) {
        DefaultProfile defaultProfile = DefaultProfile.getProfile(region_id, aliyun_user_ak, aliyun_user_sk);
        return new DefaultAcsClient(defaultProfile);
    }
}
