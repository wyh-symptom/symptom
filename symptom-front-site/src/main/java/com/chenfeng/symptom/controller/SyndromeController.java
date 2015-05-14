package com.chenfeng.symptom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementService;
import com.chenfeng.symptom.util.Bz;

@Controller
@RequestMapping(value = "syndrome")
public class SyndromeController {
    @Autowired
    private SyndromeService syndromeService;
    
    @Autowired
    private SyndromeElementService syndromeElementService;
    
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
    	String des;
    	//List<String[]> relateList = new ArrayList<String[]>();
    	//有向图的规则是选择任意一种证状中任意一组证素关系的相互组合。比如{A-B,C-D},{B-C,D->E}。形成2*2的4种组合。
    	//如果没有选择证素，则该证状不做任何证素组合。
    	//description  = 症状名字 + && + 症状描素  + && + start + ## + end
    	String zzSplitStr = "&&";
    	String relateSplitStr = "##";
    	Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
    	String[] desArr;
    	String[] relateArr;
    	List<String[]> relateList = null;
    	String zz;
    	List<String> keyList = new ArrayList<String>();
    	for (int i = 0; i < description.size(); i++ ) {
    		des = description.get(i);
    		desArr = des.split(zzSplitStr);
    		zz = desArr[1];
    		relateArr = desArr[2].split(relateSplitStr);
    		if (map.containsKey(zz)) {	//包含这个症状，则取出其描素的list，将当前元素添加进去
    			relateList = map.get(zz);
    		} else {
    			keyList.add(zz);
    			relateList = new ArrayList<String[]>();
    		}
    		relateList.add(relateArr);
    		map.put(zz, relateList);
    	}
    	
    	
    	for (int i = 0; i < keyList.size(); i++) {
    		List<String[]> list = map.get(keyList.get(i));
    	}
    	int mapSize = map.size();
    	String[][] zs = new String[mapSize][2];
    	for (int i = 0; i < relateList.size(); i++) {
    		zs[i] = relateList.get(i);
    	}
    	Map<String, Object> result = Bz.findRelate(zs, syndromeElementService);
    	net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
    	model.addAttribute("result", json);
    	return "syndrome/image";
    }
    
    /**
     * 整理需要对比的证素
     * @param list
     * @param relateList
     */
    private void generateZsRelate(List<Syndrome> list, List<String[]> relateList){
    	for (Syndrome zz:list) {
    		String[] relate = {zz.getSyndromeElementStart(), zz.getSyndromeElementEnd()};
    		relateList.add(relate);
    	}
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
