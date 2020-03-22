package com.demo.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class DateVO {
    private String mon;
    private String sun;
}
