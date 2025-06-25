package com.zh.nyh.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
//import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.modules.db.SQLModule;

import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

@Component
public class MagicSaInterceptor extends SaInterceptor implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private SQLModule db;

//	@Autowired
//	private MagicResourceService magicResourceService;

	private JSONArray pages;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		refreshRouter();
	}

	public void refreshRouter() {

		pages = new JSONArray(db.camel().table("amis_page").where().eq("page_type", "3").select(null));

		auth = handler -> {

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
					checks.put("/amis/view/" + page.getStr("id"),
							r -> StpUtil.checkRoleOr(roles.toArray(new String[0])));
				}

			}

			SaRouter.match("/**").notMatch(notMatchs).check(r -> StpUtil.checkLogin());

			for (Entry<String, SaParamFunction<SaRouterStaff>> check : checks.entrySet()) {
				SaRouter.match(check.getKey(), check.getValue());
			}

		};

	}
}
