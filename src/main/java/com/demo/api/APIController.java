package com.demo.api;

import com.demo.blog.BlogService;
import com.demo.common.model.TbTopology;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;

@Slf4j
public class APIController extends Controller {


    @Inject
    TopologyService service;

    public void  index(){
        Random random = new Random();
        getRequest().setAttribute("uid",random.nextInt(10000));
        render("/project/socket.html");
    }

    public void topologies(){
        Integer pageIndex = getParaToInt("pageIndex");
        Integer pageCount = getParaToInt("pageCount");
        if (log.isInfoEnabled()){
            log.info("pageIndex: [{}]  pageCount: [{}]",pageIndex,pageCount);
        }
        renderJson(service.paginate(pageIndex,pageCount));
    }

    /**
     * Post 保存流程图
     */
    public void topology(){
        String jsonString = HttpKit.readData(getRequest());
        TbTopology bean = FastJson.getJson().parse(jsonString, TbTopology.class);
        if (log.isInfoEnabled()){
            log.info("bean: [{}]  ",bean);
        }
        if (bean.getId() == null){
            bean.setCreateTime(new Date());
            bean.setUpdateTime(bean.getCreateTime());
            bean.save();
        }else{
            bean.setUpdateTime(new Date());
            bean.update();
        }
        renderJson("ok");
    }

    public void topologyOne() {
        renderJson(service.findById(getParaToInt()));
    }

    public void wheel(){

        render("/project/wheel.html");
    }
}
