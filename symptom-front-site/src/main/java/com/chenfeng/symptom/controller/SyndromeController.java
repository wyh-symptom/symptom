package com.chenfeng.symptom.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.chenfeng.symptom.domain.model.mybatis.Syndrome;
import com.chenfeng.symptom.service.syndrome.SyndromeCreateInput;
import com.chenfeng.symptom.service.syndrome.SyndromeInitOutput;
import com.chenfeng.symptom.service.syndrome.SyndromeService;

@Controller
@RequestMapping(value = "syndrome")
public class SyndromeController {
    @Autowired
    private SyndromeService syndromeService;
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String createSympotm() {
        
        return "syndrome/create";
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String doCreateSympotm(@Valid SyndromeCreateInput syndromeCreateInput) {
        
        syndromeService.create(syndromeCreateInput);
        return "redirect:/syndrome/list";
    }
    
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String searchSympotm() {
    	
    	return "syndrome/search";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String doSearchSympotm(@RequestParam("symptomName") List<String> symptomName , @RequestParam("description") List<String> description, Model model) {
    	for (String des: description) {
    		if ("".equals(des) || des == null) {		//如果为null，则需要对比该证状对应的所有证素关系
    			
    		} else {
    			String[] relate = des.split("__");	//前台用__组装的数据 
    		}
    	}
    	return "syndrome/image";
    }

    @RequestMapping(value = "init", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<SyndromeInitOutput> init() {
    	
    	return syndromeService.findSyndromeInitData();
    }
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String list(Model model) {

        return "syndrome/list";
    }
    
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Syndrome> doList() {
        
        return syndromeService.findAll();
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("syndrome", syndromeService.findOne(id));
        return "syndrome/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String update(@Valid SyndromeCreateInput syndromeUpdateInput) {
        Syndrome syndrome = new Syndrome();
        BeanUtils.copyProperties(syndromeUpdateInput, syndrome);
        syndromeService.update(syndrome);
        return "redirect:/syndrome/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        syndromeService.delete(id);
    }
}
