package com.demo.vo;


import lombok.Data;

/**
 *
 */
@Data
public class SubProjectVO {
    private String projectPath;
    private String projectName;
    private Integer sshId;
    //是否需要压缩
    private Boolean zip;
    //选择压缩的文件夹或者文件 用空格分开
    private String zipFolderOrFile;
}
