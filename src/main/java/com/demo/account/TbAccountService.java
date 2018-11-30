package com.demo.account;

import com.demo.common.model.TbAccount;

public class TbAccountService {

    private TbAccount dao = new TbAccount().dao();

    public TbAccount getTbAccount(long id){
        return dao.findById(id);
    }
}
