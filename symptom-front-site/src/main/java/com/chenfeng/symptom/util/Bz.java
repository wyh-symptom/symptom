package com.chenfeng.symptom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementInput;
import com.chenfeng.symptom.service.syndrome_element.SyndromeElementService;


public class Bz {
    
    public static Map<String, Object> findRelate(String[][] zs, SyndromeElementService syndromeElementService){
        List<String> topList = generateTopList(zs);
        //从数据库查询顶点集合中个证素之间的关系,形成一个新的证素关系集合 0->start,1->end, 2->关系类型, 3->关系描素
        String[][] newZs = generateNewZs(topList, zs, syndromeElementService);
        List<String> zeroList = getZeroList(newZs, topList);    //入度为0的证素集合
        
        int len = zeroList.size();
        //如果长度大于1，则说明不能形成有且仅有一个入度为0的有向图。则从元素表中添加一个元素， 并且添加该元素与该toplist中元素的关系，重新构造有向图，如果
        if (len > 1) {	
        	
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
        String finalMermaidStr = mermaidStr.toString();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("len", len);      //len代表当前组合可以画出几个有向图。
        resultMap.put("zeroList", zeroList);
        resultMap.put("relate", finalMermaidStr.substring(0, finalMermaidStr.length()-1));
        resultMap.put("subList", topList);
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
            for (int j = 0; j < topList.size(); j++) {
                if (i == j || topList.get(i).equals(topList.get(j))) {    //不比较证素本身,但是要比较相互之间的关系如A->B,还需要比较B->A;
                    continue;
                }
                currentRelate = compareElement(topList.get(i), topList.get(j), syndromeElementService);
                if (Integer.parseInt(currentRelate[0]) == 1) {
                    Map<Integer, String> map = new HashMap<Integer, String>();
                    map.put(0, topList.get(i));
                    map.put(1, topList.get(j));
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
     * 查询数据库，比较两个证素之间的关系
     * @param element
     * @param compareElement
     * @return {关系,关系类型}
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
    	List<Integer> list1 = new ArrayList();
    	list1.add(1);
    	List<Integer> list2 = new ArrayList();
    	list2.add(2);
    	list2.add(3);
    	List<Integer> list3 = new ArrayList();
    	list3.add(1);
    	list3.add(2);
    	list3.add(3);
    	list3.add(4);
    	list3.add(5);
    	list3.removeAll(list2);
    	System.out.println(list3);
    	
    }


}
