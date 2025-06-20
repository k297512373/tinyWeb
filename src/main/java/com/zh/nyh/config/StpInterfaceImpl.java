package com.zh.nyh.config;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONArray;

@Component
public class StpInterfaceImpl implements StpInterface {

	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		return null;
	}

	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		return new JSONArray(StpUtil.getSession().get("roles")).toList(String.class);
	}

}
