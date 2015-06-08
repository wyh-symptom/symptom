package com.chenfeng.symptom.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementInput;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementService;
import com.mysql.fabric.xmlrpc.base.Array;


public class Bz {
    
    @SuppressWarnings("unchecked")
	public static Map<String, Object> findRelate(String[][] zs, SyndromeElementService syndromeElementService){
        List<String> topList = generateTopList(zs);
        List<String> zeroList = getZeroList(zs, topList);    //入度为0的证素集合
        int len = zeroList.size();
        String des = "该有向图符合ISO-R筛选法则，为一级辨证";
        String[][] newZs = null;
        if (len > 1) {		//添加元素之间的关系
        	//从数据库查询顶点集合中个证素之间的关系,形成一个新的证素关系集合 0->start,1->end, 2->关系类型, 3->关系描素
            newZs = generateNewZs(topList, zs, syndromeElementService);
            zeroList = getZeroList(newZs, topList);    //入度为0的证素集合
            len = zeroList.size();
            if (len > 1) {
            	//如果长度大于1，则说明不能形成有且仅有一个入度为0的有向图。则从元素表中添加一个元素， 并且添加该元素与该toplist中元素的关系，重新构造有向图
	        	List<SyndromeElement> list = syndromeElementService.findAll();
	        	Object[] object = generateOneNewTopList(list, newZs, topList, syndromeElementService);
	        	boolean flag = (boolean)object[0];
	        	if (flag) {
	        		zeroList = (List<String>)object[1];
	        		newZs = (String[][])object[2];
	        		topList = (List<String>)object[3];
	        		len = zeroList.size();
	        		des = "该有向图符合ISO-R筛选法则，为三级辨证("+object[4]+")";
	        	} else {	//没有找到匹配的元素，则从关系表中查找一条关系添加到证素关系中，并且添加2个元素与原顶点集合的所有元素的关系
	        		object = generateTwoNewTopList(list, newZs, topList, syndromeElementService);
	        		flag = (boolean)object[0];
	        		if (flag) {
	            		zeroList = (List<String>)object[1];
	            		newZs = (String[][])object[2];
		        		topList = (List<String>)object[3];
	            		len = zeroList.size();
	            		des = "该有向图符合ISO-R筛选法则，为四级辨证("+object[4]+")";
	            	} else {
	            		des = "不能成满足条件的有向图";
	            	}
	        	}
            } else {
            	des = "该有向图符合ISO-R筛选法则，为二级辨证";
            }
        	
        } else {
        	String[][] oneZs = new String[zs.length][4];
            for (int i = 0; i < zs.length; i++) {
            	oneZs[i][0] = zs[i][0];
            	oneZs[i][1] = zs[i][1];
            	oneZs[i][2] = "1";	//默认设置为因果关系
            	oneZs[i][3] = "";	//症状表中没有关系备注信息
            }
        	newZs = oneZs;
        }
        if (len > 1)  {
        	return null;
        }
        //计算度最多的证素
        //List<String> maxList = calucMaxZs(topList, newZs);
        //Map<String, Object> relateMap = new HashMap<String, Object>();
        //根据新的证素关系集合去掉重复的
        String key = "";
        StringBuffer mermaidStr = new StringBuffer();;
        //relateArr[0]--这是备注备注备注备注备注备注备注备注-->relateArr[1]
        for (String[] relateArr : newZs) {
        	if ("".equals(relateArr[3]) || relateArr[3] == null) {	//备注为空显示关系类型
        		key = relateArr[0] + "--" + getRelateStr(Integer.parseInt(relateArr[2])) + "-->" + relateArr[1];
        	} else {
        		key = relateArr[0] + "--" + relateArr[3] + "-->" + relateArr[1];
        	}
        	
        	mermaidStr.append(key+";");
        }
        //mermaidStr.append("肺阴虚--因果关系-->阴虚肺燥，肺系不利则咳嗽、咯痰;");
        String finalMermaidStr = mermaidStr.toString();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("len", len);      //len代表当前组合可以画出几个有向图。
        resultMap.put("zeroList", zeroList);
        resultMap.put("relate", finalMermaidStr.substring(0, finalMermaidStr.length()-1).replaceAll("，", ",")
        		.replaceAll("、", ","));
        resultMap.put("subList", topList);
        resultMap.put("des", des);
        /*resultMap.put("maxList", maxList);
        if (len == 1) {
        	String des = "根节点证素:"+ zeroList.get(0) + "   关键证素:";
        	for (int i = 0; i < maxList.size(); i++) {
        		des += maxList.get(i) + ",";
        	}
        	resultMap.put("des", des.substring(0, des.length() - 1));
        }*/
        return resultMap;
    }
    
    /**
     * 不满足第三点有向图条件，重新构造顶点集合和证素关系
     * 从元素表中添加一个元素到顶点集合， 并且添加该元素与该toplist中元素的关系，重新构造有向图
     * @param list
     * @param zs
     * @param topList
     * @param syndromeElementService
     * @return{boolean, zeroList}
     */
    private static Object[] generateOneNewTopList(List<SyndromeElement> list, String[][] zs, List<String> topList, SyndromeElementService syndromeElementService){
    	int all = list.size();
    	SyndromeElement el = null;
    	List<String> elementList = new ArrayList<String>();
    	for (int i = 0; i < all; i++) {
    		el = list.get(i);
    		if (!elementList.contains(el.getSyndromeElementStart())) {
    			elementList.add(el.getSyndromeElementStart());
    		}
    		if (!elementList.contains(el.getSyndromeElementEnd())) {
    			elementList.add(el.getSyndromeElementEnd());
    		}
    	}
    	List<String> temp = null;
    	String[][] tempZs = null;
    	List<String> zeroList = null;
    	boolean flag = false;
    	List<String> oneList = null;
    	String[][] newZs = null;
    	Object[] object = new Object[5];
    	object[0] = flag;
    	for (int i = 0; i < elementList.size(); i++) {
    		if (topList.contains(elementList.get(i))) {
    			continue;
    		}
    		temp = new ArrayList<String>();	//不修改原始的顶点集合
    		temp.addAll(topList);
    		tempZs = copyArray(zs, 0);	//不修改原始的证素关系
    		oneList = new ArrayList<String>();
    		oneList.add(elementList.get(i));
    		newZs = generateNewZs(temp, oneList, tempZs, syndromeElementService);
    		temp.add(elementList.get(i));
    		zeroList = getZeroList(newZs, temp);
    		if (zeroList.size() == 1) {		//找到满足条件的元素；则停止循环
    			topList.add(elementList.get(i));
    			flag = true;
    			object[0] = flag;
    			object[1] = zeroList;
    			object[2] = newZs;
    			object[3] = topList;
    			object[4] = elementList.get(i);
    			break;
    		}
    	}
    	return object;
    }
    
    /**
     * 不满足第4点有向图条件，重新构造顶点集合和证素关系
     * 从元素表中添加一组关系， 并且添加2个元素与该toplist中元素的关系，重新构造有向图
     * @param list
     * @param zs
     * @param topList
     * @param syndromeElementService
     * @return{boolean, zeroList}
     */
    private static Object[] generateTwoNewTopList(List<SyndromeElement> list, String[][] zs, List<String> topList, SyndromeElementService syndromeElementService){
    	int all = list.size();
    	SyndromeElement el = null;
    	List<String> temp = null;
    	String[][] tempZs = null;
    	List<String> zeroList = null;
    	List<String> twoList = new ArrayList<String>();
    	boolean flag = false;
    	Object[] object = new Object[5];
    	object[0] = flag;
    	for (int i = 0; i < all; i++) {
    		el = list.get(i);
    		if (el.getIsRelate().intValue() == 1) {
    			if (topList.contains(el.getSyndromeElementStart()) && topList.contains(el.getSyndromeElementEnd())) {
        			continue;
        		}
    			temp = new ArrayList<String>();	//不修改原始的顶点集合
        		temp.addAll(topList);
        		tempZs = copyArray(zs, 1);	//不修改原始的证素关系
        		tempZs[tempZs.length - 1][0] = el.getSyndromeElementStart();
        		tempZs[tempZs.length - 1][1] = el.getSyndromeElementEnd();
        		tempZs[tempZs.length - 1][2] = el.getRelateType().toString();
        		tempZs[tempZs.length - 1][3] = el.getDescription();
        		twoList.add(el.getSyndromeElementStart());
        		twoList.add(el.getSyndromeElementEnd());
        		String[][] newZs = generateNewZs(temp, twoList, tempZs, syndromeElementService);
        		temp.addAll(twoList);
        		zeroList = getZeroList(newZs, temp);
        		if (zeroList.size() == 1) {		//找到满足条件的元素；则停止循环
        			zs = newZs;
        			if (!topList.contains(el.getSyndromeElementStart())) {
        				topList.add(el.getSyndromeElementStart());
        			}
        			if (!topList.contains(el.getSyndromeElementEnd())) {
        				topList.add(el.getSyndromeElementEnd());
        			}
        			flag = true;
        			object[0] = flag;
        			object[1] = zeroList;
        			object[2] = newZs;
        			object[3] = topList;
        			object[4] = el.getSyndromeElementStart() + "-->" + el.getSyndromeElementEnd();
        			break;
        		}
    		}
    	}
    	return object;
    }
    
    
    private static String getRelateStr(int relate){
    	String relateStr = "";
    	if (relate == 1) {
    		relateStr = "因果关系";
    	} else if (relate == 2) {
    		relateStr = "从属关系";
    	} else {
    		relateStr = "并列关系";
    	}
    	return relateStr;
    	
    }
    
    /**
     * @param newZs	症素关系
     * @param topList	顶点集合
     * @return
     */
    private static List<String> getZeroList(String[][] newZs, List<String> topList){
    	 boolean flag = true;
         List<String> zeroList = new ArrayList<String>();    //入度为0的证素集合
         for(String str : topList) {
         	flag = true;
             for (int i = 0; i < newZs.length;i++) {
                 if (str.equals(newZs[i][1])) { //只要顶点集合任何一个证素是证素关系链后面的证素，则该顶点集合证素不是入度为0 的那个证素
                     flag = false;
                     break;
                 }
             }
             if (flag) {
                 zeroList.add(str);
             }
         }
         return zeroList;
    }
    
    /**
     * 生成新的证素关系集合
     * @param topList
     * @param zs
     */
    private static String[][] generateNewZs(List<String> topList, String[][] zs, SyndromeElementService syndromeElementService) {
        List<Map<Integer, String>> newRelateList = new ArrayList<Map<Integer,String>>();
        String[] currentRelate = new String[3];
        for (int i = 0; i < topList.size(); i++) {
            for (int j = i + 1; j < topList.size(); j++) {
                currentRelate = compareElement(topList.get(i), topList.get(j), syndromeElementService);
                if (Integer.parseInt(currentRelate[0]) == 1) {
                    Map<Integer, String> map = new HashMap<Integer, String>();
                    map.put(0, topList.get(i));
                    map.put(1, topList.get(j));
                    map.put(2, currentRelate[1]);
                    map.put(3, currentRelate[2]);
                    newRelateList.add(map);
                }
                currentRelate = compareElement(topList.get(j), topList.get(i), syndromeElementService);
                if (Integer.parseInt(currentRelate[0]) == 1) {
                    Map<Integer, String> map = new HashMap<Integer, String>();
                    map.put(0, topList.get(j));
                    map.put(1, topList.get(i));
                    map.put(2, currentRelate[1]);
                    map.put(3, currentRelate[2]);
                    newRelateList.add(map);
                }
            }
        }
        
        int len = newRelateList.size();
        String[][] newZs = new String[zs.length + len][4];
        for (int i = 0; i < zs.length; i++) {
            newZs[i][0] = zs[i][0];
            newZs[i][1] = zs[i][1];
        	newZs[i][2] = "1";	//默认设置为因果关系
        	newZs[i][3] = "";	//症状表中没有关系备注信息
        }
        if (len > 0) {
            for (int i = zs.length; i < newZs.length; i++) {
                newZs[i][0] = newRelateList.get(newZs.length - i - 1).get(0);
                newZs[i][1] = newRelateList.get(newZs.length - i - 1).get(1);
                newZs[i][2] = newRelateList.get(newZs.length - i - 1).get(2);
                newZs[i][3] = newRelateList.get(newZs.length - i - 1).get(3);
            }
        }
        
        return newZs;
    }
    
    /**
     * 重新构造证素关系
     * @param topList
     * @param compareElementList
     * @param zs
     * @param syndromeElementService
     * @return
     */
    private static String[][] generateNewZs(List<String> topList, List<String> compareElementList, String[][] zs, SyndromeElementService syndromeElementService) {
        List<Map<Integer, String>> newRelateList = new ArrayList<Map<Integer,String>>();
        String[] currentRelate = new String[3];
        for (int i = 0; i < compareElementList.size(); i++) {
	        for (int j = 0; j < topList.size(); j++) {
	        	currentRelate = compareElement(compareElementList.get(i), topList.get(j), syndromeElementService);
	            if (Integer.parseInt(currentRelate[0]) == 1) {
	                Map<Integer, String> map = new HashMap<Integer, String>();
	                map.put(0, compareElementList.get(i));
	                map.put(1, topList.get(j));
	                map.put(2, currentRelate[1]);
	                map.put(3, currentRelate[2]);
	                newRelateList.add(map);
	            }
	            currentRelate = compareElement(topList.get(j), compareElementList.get(i), syndromeElementService);
	            if (Integer.parseInt(currentRelate[0]) == 1) {
	                Map<Integer, String> map = new HashMap<Integer, String>();
	                map.put(0, topList.get(j));
	                map.put(1, compareElementList.get(i));
	                map.put(2, currentRelate[1]);
	                map.put(3, currentRelate[2]);
	                newRelateList.add(map);
	            }
	            
	           }
        }
        
        int len = newRelateList.size();
        String[][] newZs = new String[zs.length + len][4];
        for (int i = 0; i < zs.length; i++) {
            newZs[i][0] = zs[i][0];
            newZs[i][1] = zs[i][1];
            if (zs[i][2] == null || "".equals(zs[i][2]))
            	newZs[i][2] = "1";	//默认设置为因果关系
            else {
            	newZs[i][2] = zs[i][2];;	
            }
           
            newZs[i][3] = zs[i][3];
        }
        if (len > 0) {
            for (int i = zs.length; i < newZs.length; i++) {
                newZs[i][0] = newRelateList.get(newZs.length - i - 1).get(0);
                newZs[i][1] = newRelateList.get(newZs.length - i - 1).get(1);
                newZs[i][2] = newRelateList.get(newZs.length - i - 1).get(2);
                newZs[i][3] = newRelateList.get(newZs.length - i - 1).get(3);
            }
        }
        
        return newZs;
    }
    

    /**
     * 查询数据库，比较两个证素之间的关系
     * @param element
     * @param compareElement
     * @return {关系,关系类型, 关系备注}
     */
    public static String[] compareElement(String element, String compareElement, SyndromeElementService syndromeElementService) {
        SyndromeElementInput zs = new SyndromeElementInput();
        zs.setSyndromeElementStart(element);
        zs.setSyndromeElementEnd(compareElement);
        SyndromeElement zs0 = new SyndromeElement();
        BeanUtils.copyProperties(zs, zs0);
        List<SyndromeElement>  list = syndromeElementService.findRelateByZs(zs0);
        int relate = (list != null && list.size() > 0) ? list.get(0).getIsRelate() : 0;
        int relateType = (list != null && list.size() > 0) ? list.get(0).getRelateType() : 1;	//关系类型
        String des = (list != null && list.size() > 0) ? list.get(0).getDescription() : "";	//关系备注
        String[] returnArr = {Integer.toString(relate) , Integer.toString(relateType), des};
        return returnArr;
    }
    
    /**
     * 获取入度最大的证素(出度和入度之和)
     * @param top
     */
    public static List<String> calucMaxZs(List<String> top, String[][] zsRelate){
    	int max = 0;
    	int currentMax = 0;
		String currentMaxZs = "";
		Map<String, Integer> map = new HashMap<String, Integer>();
    	for (int i = 0; i < top.size(); i++) {
    		currentMax = 0;
    		currentMaxZs = top.get(i);
    		for (int j = 0; j < zsRelate.length; j++){
    			if (zsRelate[j][0].equals(currentMaxZs)) {
    				currentMax ++;
    			}
    			if (zsRelate[j][1].equals(currentMaxZs)) {
    				currentMax ++;
    			}
    		}
    		if (currentMax >= max) {
    			map.put(currentMaxZs, currentMax);
    			max = currentMax;
    		}
    	}
    	List<String> maxList = new ArrayList<String>();
    	for (String zs : map.keySet()) {
    		if (max == map.get(zs)) {
    			maxList.add(zs);
    		}
    	}
    	return maxList;
    }
    
    
    /**
     * 生成顶点集合
     * @return
     */
    public static List<String> generateTopList(String[][] zs) {
        List<String> topList = new LinkedList<String>();
        for (String[] key:zs) {
            for (String str:key) {
                if (!topList.contains(str)){
                    topList.add(str);
                }
            }
            
        }
        return topList;
    }
    
    /**
     * 形成全排列
     * @param map
     * @param idx
     * @param index
     * @param mark
     * @param list
     * @return
     */
    public static List<Map<Integer, String[]>> recursivecalc(Map<Integer, List<String[]>> map,
			int[] idx, int index, boolean mark, List<Map<Integer, String[]>> list) {
		if (index < 0)
			return list;

		if (idx[index] >= map.get(index).size()) {
			index--;
			if (index < 0)
				return list;

			idx[index]++;

			mark = true;
		} else {
			if (mark) {
				for (int i = index + 1; i < idx.length; i++) {
					idx[i] = 0;
				}
				index = map.size() - 1;
			}

			showComposition(map, idx, list);
			idx[index]++;
			mark = false;
		}
		
		recursivecalc(map, idx, index, mark, list);
		return list;
		
	}
    
    public static void showComposition(Map<Integer, List<String[]>> map,
			int[] idx, List<Map<Integer, String[]>> list) {
    	Map<Integer, String[]> zsZh = new HashMap<Integer, String[]>();
		for (int i = 0; i < map.size(); i++) {
			zsZh.put(i, map.get(i).get(idx[i]));
		}
		list.add(zsZh);
	}
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
//        String[][] zs = {{"A","B"}, {"A","C"},{"C", "D"},{"B", "F"},{"D","F"}};
        //findRelate(zs);
    	Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
    	/*List<Integer> list1 = new ArrayList();
    	list1.add(1);
    	List<Integer> list2 = new ArrayList();
    	list2.addAll(list1);
    	list2.add(2);
    	System.out.println(list1.size());*/
    	String[][] strArray=new String[][]{   
    	        {"a","b","c"},   
    	        {"A","B","C","D"},   
    	        {"1","2"}   
    	     };  
    	
    	String[][] copyArray=new String[strArray.length + 1][];  
    	System.out.println(copyArray[3]);
    	   for(int i=0;i< strArray.length;i++){   
    	    copyArray[i]=new String[strArray[i].length];   
    	    for(int j=0;j< strArray[i].length;j++){   
    	        copyArray[i][j]=strArray[i][j];   
    	    }   
    	   } 
    	   printArray(copyArray);
    	   System.out.println(copyArray.length);
    	   
    }
    
    public static void printArray(String[][] array){
		for(int i=0;i<array.length;i++){
			for(int j=0;j<array[i].length;j++){
				System.out.print(array[i][j]+"  ");
			}
			System.out.println();
		}
	}
    
	public static String[][] copyArray(String[][] originArray, int len) {
		String[][] copyArray = new String[originArray.length + len][4];
		for (int i = 0; i < originArray.length; i++) {
			copyArray[i] = new String[originArray[i].length];
			for (int j = 0; j < originArray[i].length; j++) {
				copyArray[i][j] = originArray[i][j];
			}
		}
		return copyArray;
	}


}
