package com.zh.nyh.config.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

@RestControllerAdvice
public class GlobalExceptionHandler{

	Log log = LogFactory.get();

	@ExceptionHandler(NotLoginException.class)
	public void handlerNotLoginException() {
		SaHolder.getResponse().redirect("/login");
	}

	@ExceptionHandler(NotRoleException.class)
	public ModelAndView handlerNotRoleException() {
		return new ModelAndView("/error/unauth");
	}

	// 全局异常拦截
	@ExceptionHandler
	public Object handlerException(Exception e) {
		log.error("全局异常处理",e);
		return SaResult.error();
	}
}