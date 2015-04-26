package com.chenfeng.symptom.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
        return "syndrome/create";
    }
    
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String searchSympotm() {
    	
    	return "syndrome/search";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String doSearchSympotm(@RequestParam("symptomName") List<String> symptomName , @RequestParam("description") List<String> description, Model model) {
    	System.out.println(symptomName);
    	System.out.println(description);
    	return "syndrome/search";
    }

    @RequestMapping(value = "init", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<SyndromeInitOutput> init() {
    	
    	return syndromeService.findSyndromeInitData();
    }
    
    
}
