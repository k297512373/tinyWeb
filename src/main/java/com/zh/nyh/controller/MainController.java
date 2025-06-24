package com.zh.nyh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssssssss.magicapi.modules.db.SQLModule;

import cn.hutool.core.lang.Dict;

@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	private SQLModule db;
	
	@Autowired
	private AmisController amisController;
	
	@GetMapping("/index")
	public String index(ModelMap mm) {
		Object page = db.camel().table("amis_page").where().eq("page_type", "1").orderBy("page_weight").selectOne(null);
		return amisController.view(Dict.parse(page).getInt("id"), mm);
	}
	
	@GetMapping("/main")
	public String main(ModelMap mm) {
		Object page = db.camel().table("amis_page").where().eq("page_type", "4").orderBy("page_weight").selectOne(null);
		return amisController.view(Dict.parse(page).getInt("id"), mm);
	}
}
