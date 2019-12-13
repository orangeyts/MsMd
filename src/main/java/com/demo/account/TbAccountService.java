package com.demo.account;

import com.demo.common.model.TbAccount;

import java.util.List;

public class TbAccountService {

    private TbAccount dao = new TbAccount().dao();

    public TbAccount getTbAccount(long id){
        return dao.findById(id);
    }
    public List<TbAccount> getTbAccountList(){
        return dao.find("select * from tb_account");
    }
    public TbAccount getTbAccountList(String type){
        return dao.findFirst("select * from tb_account where type=?",type);
    }
}
