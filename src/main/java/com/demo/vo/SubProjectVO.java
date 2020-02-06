package com.demo.vo;


import lombok.Data;

/**
 *
 */
@Data
public class SubProjectVO {
    private String projectPath;
    private String projectName;
    private Boolean zip;
    private Integer sshId;
}
