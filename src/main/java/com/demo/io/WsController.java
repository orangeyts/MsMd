package com.demo.io;

import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class WsController extends Controller {

    public void  index(){
        Random random = new Random();
        getRequest().setAttribute("uid",random.nextInt(10000));
        render("/project/socket.html");
    }

    public void createjs(){

        render("/project/createjs.html");
    }
    public void wheel(){

        render("/project/wheel.html");
    }
}
