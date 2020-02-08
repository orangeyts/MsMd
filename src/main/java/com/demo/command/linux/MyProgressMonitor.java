package com.demo.command.linux;

import com.demo.common.model.TbBuild;
import com.jcraft.jsch.SftpProgressMonitor;


/**
 * 文件上传监听器
 */
public class MyProgressMonitor implements SftpProgressMonitor {
    private long transfered;
    private long currentM;

    private TbBuild tbBuild;

    public MyProgressMonitor(TbBuild tbBuild) {
        this.tbBuild = tbBuild;
    }

    @Override
    public boolean count(long count) {
        transfered = transfered + count;
        long tempM = transfered/(1024*1024);
        String msg = "当前一共传递了多少字节: " + tempM + " M";
        if (tempM > currentM){
            tbBuild.appendOutputNoBR(msg);
            currentM = tempM;
        }
        return true;
    }

    @Override
    public void end() {
        tbBuild.appendOutput("传输完成");
        System.out.println("Transferring done.");
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        tbBuild.appendOutput("开始传输");
        System.out.println("Transferring begin.");
    }
}
