package com.zh.nyh.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.ssssssss.magicapi.modules.db.SQLModule;

import com.zh.nyh.vo.LoginForm;
import com.zh.nyh.vo.SysUser;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/login")
public class LoginController implements ApplicationListener<ContextRefreshedEvent>{
	
	private final TimedCache<String, AbstractCaptcha> captchaCache = CacheUtil.newTimedCache(60*1000);
	
	@Autowired
	private SQLModule db;
	
	@Autowired
	private AmisController amisController;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		captchaCache.schedulePrune(1000);
	}
	
	@GetMapping("/captcha")
	public SaResult getCaptcha() {
		
		String uid = UUID.fastUUID().toString();
		AbstractCaptcha captcha = CaptchaUtil.createGifCaptcha(300, 100);
		captchaCache.put(uid, captcha);
		
		return SaResult.data(Dict.of("key",uid,"img",captcha.getImageBase64Data()));
	}
	
	@PostMapping
	public SaResult login(LoginForm form) {
		
		//校验验证码
		if(!captchaCache.containsKey(form.getKey())) {
			return SaResult.error("验证码已过期");
		}
		
		AbstractCaptcha captcha = captchaCache.get(form.getKey());
		captchaCache.remove(form.getKey());
		
		if(!captcha.verify(form.getCode())) {
			return SaResult.error("验证码不正确");
		}
		
		Object user = db.camel().table("sys_user").where().eq("user_name", form.getUserName()).and().eq("del_flag", "0").selectOne(null);
		if(user == null) {
			return SaResult.error("用户名或密码不正确");
		}
		
		SysUser sysUser = Dict.parse(user).toBean(SysUser.class);
		
		if(!StrUtil.equals( SaSecureUtil.sha256(Base64.decodeStr(form.getUserPass())+form.getUserName()) ,sysUser.getUserPass())) {
			return SaResult.error("用户名或密码不正确");
		}
		
		List<Object> roles = db.camel()
				.table("sys_role")
				.column("role_key")
				.where().in("id", StrUtil.split(sysUser.getRoleIds(),","))
				.select(null)
				.stream().map(it->it.get("roleKey"))
				.collect(Collectors.toList());
		
		StpUtil.login(sysUser.getId());
		StpUtil.getSession().set("user", sysUser);
		StpUtil.getSession().set("roles", roles);
		
		return SaResult.ok();
	}
	
	@GetMapping
	public ModelAndView login(ModelMap mm) {
		
		if(StpUtil.isLogin()) {
			SaHolder.getResponse().redirect("/index");
		}
		
		//获取登录页
		Object page = db.camel().table("amis_page").where().eq("page_type", "0").orderBy("page_weight").selectOne(null);
		return new ModelAndView(amisController.view(Dict.parse(page).getInt("id"), mm));
	}
	
	@GetMapping("out")
	public void loginout(ModelMap mm) {
		StpUtil.logout();
		SaHolder.getResponse().redirect("/login");
	}
	
}
