package com.demo.common;

import com.jfinal.log.ILogFactory;
import com.jfinal.log.Log;

public class Slf4jLogFactory implements ILogFactory {

	@Override  
    public Log getLog(Class<?> aClass) {  
        return new Slf4jLog(aClass);  
    }  
  
    @Override  
    public Log getLog(String name) {  
        return new Slf4jLog(name);  
    }  

}
