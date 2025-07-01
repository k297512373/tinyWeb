package com.zh.nyh.config.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

@RestControllerAdvice
public class GlobalExceptionHandler{

	Log log = LogFactory.get();
	
	@ExceptionHandler(NotLoginException.class)
	public void handlerNotLoginException() {
		String ctx = SpringMVCUtil.getRequest().getContextPath();
		SaHolder.getResponse().redirect(ctx+"/login");
	}

	@ExceptionHandler(NotRoleException.class)
	public ModelAndView handlerNotRoleException() {
		String ctx = SpringMVCUtil.getRequest().getContextPath();
		return new ModelAndView(ctx+"/error/unauth");
	}

	// 全局异常拦截
	@ExceptionHandler
	public Object handlerException(Exception e) {
		log.error("全局异常处理",e);
		return SaResult.error();
	}
}