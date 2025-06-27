package com.zh.nyh.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.modules.db.SQLModule;
import org.ssssssss.magicapi.core.model.Option;

import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

@Component
public class MagicSaInterceptor extends SaInterceptor implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private SQLModule db;

	@Autowired
	private MagicResourceService magicResourceService;

	private JSONArray pages;
	
	private List<ApiInfo> apis;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		refreshRouter();
	}

	public void refreshRouter() {

		pages = new JSONArray(db.camel().table("amis_page").where().eq("page_type", "3").select(null));
		
		apis = magicResourceService.files("api").stream().map(it->{
			ApiInfo api = (ApiInfo) it;
			return api;
		}).collect(Collectors.toList());
		
		auth = handel -> {
			List<String> notMatchs = new ArrayList<>();
			notMatchs.addAll(ListUtil.of("/login", "/login/captcha", "/**/*.js", "/**/*.css"));

			Map<String, SaParamFunction<SaRouterStaff>> checks = new HashMap<>();
			checks.put("/magic/**", r -> StpUtil.checkRole("admin"));

			// 初始化页面权限
			for (Object o : pages) {

				// 超级管理员可以访问所有页面
				if (StpUtil.hasRole("admin")) {
					break;
				}

				JSONObject page = new JSONObject(o);
				JSONObject power = page.getJSONObject("pagePower");
				
				if (power != null && StrUtil.equals("any", power.getStr("power"))) {
					notMatchs.add("/amis/view/" + page.getStr("id"));
					continue;
				} else if (power == null) {
					continue;
				}

				JSONArray roles = power.getJSONArray("roles");

				if (roles != null && !roles.isEmpty()) {
					checks.put("/amis/view/" + page.getStr("id"),r -> StpUtil.checkRoleOr(roles.toArray(new String[0])));
				}

			}
			
			for(ApiInfo api : apis) {
				
				// 超级管理员可以访问所有接口
				if (StpUtil.hasRole("admin")) {
					break;
				}
				
				List<Option> options = api.getOptions();
				
				String apiId = api.getId();
				
				List<String> paths = magicResourceService.getGroupsByFileId(apiId).stream().map(it->it.getPath()).collect(Collectors.toList());
				
				String path = "/"+StrUtil.join("/",paths)+"/" + api.getPath();
				
				path = ReUtil.replaceAll(path,"\\{.*\\}","**");
				path = StrUtil.replace(path, "//", "/");
				
				JSONArray roles = new JSONArray();
				
				for(Option option : options) {
					
					String name = option.getName();
					String value = option.getValue().toString();
					
					if(StrUtil.equals("anonymous", name)) {
						notMatchs.add(path);
						break;
					}
					
					if(StrUtil.equals("role", name)) {
						roles.add(value);
					}
				}
				
				checks.put(path, r -> StpUtil.checkRoleOr(roles.toArray(new String[0])));
			}

			SaRouter.match("/**").notMatch(notMatchs).check(r -> StpUtil.checkLogin());

			for (Entry<String, SaParamFunction<SaRouterStaff>> check : checks.entrySet()) {
				SaRouter.match(check.getKey(), check.getValue());
			}
		};

	}

}
