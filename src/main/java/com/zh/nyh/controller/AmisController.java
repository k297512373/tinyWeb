package com.zh.nyh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssssssss.magicapi.modules.db.SQLModule;

import com.zh.nyh.vo.AmisPage;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.Dict;

@Controller
@RequestMapping("/amis")
public class AmisController {
	
	private String prifx = "amis/";
	
	private String tname = "amis_page";
	
	@Autowired
	private SQLModule db;
	
	@GetMapping("/editor/{id}")
	public String editor(@PathVariable Integer id,ModelMap mm) {
		if(id > -1) {
			Dict dic = Dict.parse(db.camel().table(tname).where().eq("id", id).selectOne(null));
			AmisPage amisPage = dic.toBean(AmisPage.class);
			mm.put("amisPage", amisPage);
		}
		return prifx + "editor";
	}
	
	@PostMapping("/save")
	@ResponseBody
	public SaResult save(AmisPage amisPage) {
		
		if(amisPage.getId() == -1) {
			amisPage.setId(null);
		}
		
		int res = (int) db.camel().table(tname).primary("id").save(null, Dict.parse(amisPage));
		
		return res > 0 ? SaResult.ok() : SaResult.error();
	}
	
	@GetMapping("/view/{id}")
	public String view(@PathVariable Integer id,ModelMap mm) {
		if(id > -1) {
			Dict dic = Dict.parse(db.camel().table(tname).where().eq("id", id).selectOne(null));
			AmisPage amisPage = dic.toBean(AmisPage.class);
			mm.put("amisJson", amisPage.getPageContent());
			mm.put("amisTitle", amisPage.getPageTitle());
		}
		return prifx + "view";
	}
}
