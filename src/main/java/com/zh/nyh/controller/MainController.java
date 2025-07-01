package com.zh.nyh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	private AmisController amisController;
	
	@GetMapping("/index")
	public ModelAndView index(ModelMap mm) {
		return amisController.getTypeView("1", mm);
	}
	
	@GetMapping("/main")
	public ModelAndView main(ModelMap mm) {
		return amisController.getTypeView("4", mm);
	}
}
