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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementInput;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementService;

@Controller
@RequestMapping(value = "syndrome/element")
public class SyndromeElementController {
    @Autowired
    private SyndromeElementService syndromeElementService;
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String createSympotm() {
        
        return "syndrome_element/create";
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String doCreateSympotm(@Valid SyndromeElementInput syndromeElementInput) {
        
        syndromeElementService.create(syndromeElementInput);
        return "syndrome_element/list";
    }
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String list(Model model) {

        return "syndrome_element/list";
    }
    
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<SyndromeElement> doList() {
        
        return syndromeElementService.findAll();
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("syndrome", syndromeElementService.findOne(id));
        return "syndrome_element/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String update(@Valid SyndromeElementInput syndromeElementUpdateInput) {
        SyndromeElement syndromeElement = new SyndromeElement();
        BeanUtils.copyProperties(syndromeElementUpdateInput, syndromeElement);
        syndromeElementService.update(syndromeElement);
        return "redirect:/syndrome_element/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        syndromeElementService.delete(id);
    }
}
