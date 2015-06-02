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
        //从数据库查询顶点集合中个证素之间的关系,形成一个新的证素关系集合 0->start,1->end 2->关系类型
        String[][] newZs = generateNewZs(topList, zs, syndromeElementService);
        
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
        
        int len = zeroList.size();
        //计算度最多的证素
        List<String> maxList = calucMaxZs(topList, newZs);
        Map<String, Object> relateMap = new HashMap<String, Object>();
        //根据新的证素关系集合去掉重复的
        for (String[] relateArr : newZs) {
            relateMap.put(relateArr[0] + relateArr[1], relateArr[2]);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("len", len);      //len代表可以画出几个有向图。
        resultMap.put("zeroList", zeroList);
        resultMap.put("relate", relateMap);
        topList.removeAll(zeroList);
        topList.removeAll(maxList);
        resultMap.put("subList", topList);
        resultMap.put("maxList", maxList);
        if (len == 1) {
        	String des = "根节点证素:"+ zeroList.get(0) + "   <br/>关键证素:";
        	for (int i = 0; i < maxList.size(); i++) {
        		des += maxList.get(i) + ",";
        	}
        	resultMap.put("des", des.substring(0, des.length() - 1));
        }
        return resultMap;
    }
    
    /**
     * 生成新的证素关系集合
     * @param topList
     * @param zs
     */
    private static String[][] generateNewZs(List<String> topList, String[][] zs, SyndromeElementService syndromeElementService) {
        List<Map<Integer, String>> newRelateList = new ArrayList<Map<Integer,String>>();
        int[] currentRelate = new int[2];
        for (int i = 0; i < topList.size(); i++) {
            for (int j = 0; j < topList.size(); j++) {
                if (i == j || topList.get(i).equals(topList.get(j))) {    //不比较证素本身,但是要比较相互之间的关系如A->B,还需要比较B->A;
                    continue;
                }
                currentRelate = compareElement(topList.get(i), topList.get(j), syndromeElementService);
                if (currentRelate[0] == 2) {
                    Map<Integer, String> map = new HashMap<Integer, String>();
                    map.put(0, topList.get(i));
                    map.put(1, topList.get(j));
                    map.put(2, Integer.toString(currentRelate[1]));
                    newRelateList.add(map);
                }
            }
        }
        
        int len = newRelateList.size();
        String[][] newZs = new String[zs.length + len][3];
        for (int i = 0; i < zs.length; i++) {
            newZs[i][0] = zs[i][0];
            newZs[i][1] = zs[i][1];
            newZs[i][2] = "1";	//默认设置为因果关系
        }
        if (len > 0) {
            for (int i = zs.length; i < newZs.length; i++) {
                newZs[i][0] = newRelateList.get(newZs.length - i - 1).get(0);
                newZs[i][1] = newRelateList.get(newZs.length - i - 1).get(1);
                newZs[i][2] = newRelateList.get(newZs.length - i - 1).get(2);
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
    public static int[] compareElement(String element, String compareElement, SyndromeElementService syndromeElementService) {
        SyndromeElementInput zs = new SyndromeElementInput();
        zs.setSyndromeElementStart(element);
        zs.setSyndromeElementEnd(compareElement);
        SyndromeElement zs0 = new SyndromeElement();
        BeanUtils.copyProperties(zs, zs0);
        List<SyndromeElement>  list = syndromeElementService.findRelateByZs(zs0);
        int relate = (list != null && list.size() > 0) ? list.get(0).getIsRelate() : 0;
        int relateType = (list != null && list.size() > 0) ? list.get(0).getRelateType() : 1;	//关系类型
        int[] returnArr = {relate , relateType};
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
    		if (currentMax > max) {
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
