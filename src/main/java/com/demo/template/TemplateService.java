package com.demo.template;

import com.demo.common.model.TbTemplate;

import java.util.List;

public class TemplateService {

    private TbTemplate dao = new TbTemplate().dao();

    public List<TbTemplate> getList(){
        String sql = "select * from tb_template";
        return dao.find(sql);
    }

    public TbTemplate getById(Integer id,String os) {
        String sql = "select * from tb_template where id=? and os=?";
        return dao.findFirst(sql,id,os);
    }
}
