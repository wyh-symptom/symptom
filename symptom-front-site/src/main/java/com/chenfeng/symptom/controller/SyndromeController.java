package com.chenfeng.symptom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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

import com.chenfeng.symptom.domain.common.pagehelper.Page;
import com.chenfeng.symptom.domain.model.mybatis.Syndrome;
import com.chenfeng.symptom.service.syndrome.SyndromeCreateInput;
import com.chenfeng.symptom.service.syndrome.SyndromeInitOutput;
import com.chenfeng.symptom.service.syndrome.SyndromeService;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementService;
import com.chenfeng.symptom.util.Bz;
import com.mysql.fabric.xmlrpc.base.Array;

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
    @ResponseBody
    public void doCreateSympotm(@Valid SyndromeCreateInput syndromeCreateInput) {
        
        syndromeService.create(syndromeCreateInput);
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
    		zz = desArr[0];
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
    	
    	Map<Integer, List<String[]>> dataMap = new HashMap<Integer, List<String[]>>();
    	int index = 0;
    	for(String key:map.keySet()) {
    		dataMap.put(index, map.get(key));
    		index++;
    	}
    	int zzLen = keyList.size();	//总共选择的症状个数
    	int indexs[] = new int[zzLen];
    	for(int i = 0; i < zzLen; i++){
    		indexs[i] = 0;
    	}
    	List<Map<Integer, String[]>> list = new ArrayList<Map<Integer, String[]>>();	//每一个map 就是一组可以生成有向图的证素关系
    	Bz.recursivecalc(dataMap, indexs, indexs.length - 1, false, list);
    	Map<Integer, String[]> zuHe = null;
    	List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
    	for (int k = 0; k < list.size(); k++) {
    		zuHe = list.get(k);
    		String[][] relate = new String[zuHe.size()][2];
    		for (int m : zuHe.keySet()){
    			relate[m] = zuHe.get(m);
    		}
    		Map<String, Object> result = Bz.findRelate(relate, syndromeElementService);
    		resultList.add(result);
    	}
    	
    	net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(resultList);
    	model.addAttribute("result", json);
    	System.out.println(json);
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
    public Page<Syndrome> doList(@RequestParam(required = false, defaultValue = "1", value = "page") int page, 
            @RequestParam("syndromeName") String syndromeName) {
        
        return syndromeService.findPageSyndrome(page, syndromeName);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("syndrome", syndromeService.findOne(id));
        return "syndrome/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String update(@Valid SyndromeCreateInput syndromeUpdateInput, @RequestParam("isNext") Boolean isNext) {
        Syndrome nextSyndrome = syndromeService.update(syndromeUpdateInput);
        
        if (isNext) {
            return "redirect:/syndrome/update/"+nextSyndrome.getId();
        }
        return "redirect:/syndrome/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        syndromeService.delete(id);
    }
}
