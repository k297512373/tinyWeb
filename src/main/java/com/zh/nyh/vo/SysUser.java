package com.zh.nyh.vo;

import java.security.Principal;
import java.util.Date;

public class SysUser implements Principal{
	/**id*/
	private Integer id;		
	
	/**用户名*/
	private String userName;	
	/**密码*/
	private String userPass;		
	/**昵称*/
	private String nickName;		
	/**角色id*/
	private String roleIds;			
	/**删除标识*/
	private Integer delFlag;		
	/**创建时间*/
	private Date createTime;		
	/**创建人*/
	private String createBy;		
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	@Override
	public String getName() {
		return userName;
	}
	
}
