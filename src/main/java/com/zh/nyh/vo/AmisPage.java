package com.zh.nyh.vo;

import java.util.Date;

public class AmisPage {
	
	/**id*/
	private Integer id;
	/**上级id*/
	private Integer preId;
	/**页面类型*/
	private String pageType;
	/**页面权重*/
	private Integer pageWeight;
	/**页面标题*/
	private String pageTitle;
	/**页面内容*/
	private String pageContent;
	/**页面权限*/
	private String pagePower;
	private Integer pageHid;
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
	public Integer getPreId() {
		return preId;
	}
	public void setPreId(Integer preId) {
		this.preId = preId;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public Integer getPageWeight() {
		return pageWeight;
	}
	public void setPageWeight(Integer pageWeight) {
		this.pageWeight = pageWeight;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getPageContent() {
		return pageContent;
	}
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public String getPagePower() {
		return pagePower;
	}
	public void setPagePower(String pagePower) {
		this.pagePower = pagePower;
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
	public Integer getPageHid() {
		return pageHid;
	}
	public void setPageHid(Integer pageHid) {
		this.pageHid = pageHid;
	}
	
}
