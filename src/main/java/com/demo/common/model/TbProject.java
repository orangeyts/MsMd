package com.demo.common.model;

import com.demo.common.model.base.BaseTbProject;
import com.jfinal.plugin.activerecord.Db;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class TbProject extends BaseTbProject<TbProject> {

    /**
     * 更新工程以及相关工程的 运行状态
     * @param status
     */
    public boolean updateProjectStatus(Integer status,Integer currentBuildId){
        boolean tx = Db.tx(() -> {
            this.setStatus(status);
            this.setBuildId(currentBuildId);
            this.update();
            Integer relatedId = this.getRelatedId();
            if (relatedId != null && relatedId.intValue() != 0){
                TbProject relatedProject = findById(relatedId);
                relatedProject.setStatus(status);
                relatedProject.update();
            }
            return true;
        });
        return tx;
    }
}
