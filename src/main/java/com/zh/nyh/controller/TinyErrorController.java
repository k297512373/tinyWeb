package com.zh.nyh.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TinyErrorController implements ErrorController{
	
	@Autowired
	private AmisController amisController;
	
	@RequestMapping("/error")
	public Object errorHandler(HttpServletRequest req,HttpServletResponse res,ModelMap mm) throws IOException {
		
		if(res.getStatus() == 500){
			res.sendRedirect(req.getContextPath()+"/login");
			return null;
		}
		
		String err = "500";
		
		if(res.getStatus() == 404) {
			err = "404";
		}
		
		if(res.getStatus() == 403) {
			err = "403";
		}
		
		ModelAndView view = amisController.getTypeView(err, mm);
		
		if(view != null) {
			return view;
		}
		
		String ctx = req.getContextPath();
		
		if(res.getStatus() == 403) {
			err = "unauth";
		}
		
		return new ModelAndView(ctx+"/error/"+err);
		
	}

}
