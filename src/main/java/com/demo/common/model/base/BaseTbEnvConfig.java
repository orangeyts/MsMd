package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTbEnvConfig<M extends BaseTbEnvConfig<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setCGroup(java.lang.String cGroup) {
		set("cGroup", cGroup);
	}
	
	public java.lang.String getCGroup() {
		return getStr("cGroup");
	}

	public void setCKey(java.lang.String cKey) {
		set("cKey", cKey);
	}
	
	public java.lang.String getCKey() {
		return getStr("cKey");
	}

	public void setCValue(java.lang.String cValue) {
		set("cValue", cValue);
	}
	
	public java.lang.String getCValue() {
		return getStr("cValue");
	}

}
