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
		//�����ݿ��ѯ���㼯���и�֤��֮��Ĺ�ϵ,�γ�һ���µ�֤�ع�ϵ���� 
		String[][] newZs = generateNewZs(topList, zs, syndromeElementService);
		
		boolean flag = true;
		List<String> zeroList = new ArrayList<String>();	//���Ϊ0��֤�ؼ���
		for(String str : topList) {
			for (int i = 0; i < zs.length;i++) {
				if (str.equals(zs[i][1])) {	//ֻҪ���㼯���κ�һ��֤����֤�ع�ϵ�������֤�أ���ö��㼯��֤�ز������Ϊ0 ���Ǹ�֤��
					flag = false;
					break;
				}
			}
			if (flag) {
				zeroList.add(str);
			}
		}
		
		int len = zeroList.size();
		Map<String, Object> relateMap = new HashMap<String, Object>();
		//List<String> zsList = new ArrayList<String>();	//���ļ���
		//int relate = 0;
		/*String currentZs,compareZs;
		for(int i = 0; i < topList.size(); i++) {
			currentZs = topList.get(i);
			for(int j = 0; j < topList.size(); j++) {
				compareZs = topList.get(j);
				if (currentZs.equals(compareZs)) {	//���Ƚ�֤�ر���
					continue;
				}
				relate = compareElement(currentZs, compareZs, zs);
				if (relate == 1) {
					if (!relateMap.containsKey(currentZs + compareZs)) {
						relateMap.put(currentZs + compareZs, relate);
					}
				}
			}
		}*/
		//�����µ�֤�ع�ϵ����ȥ���ظ���
		for (String[] relateArr : newZs) {
			relateMap.put(relateArr[0] + relateArr[1], 1);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("len", len);		//len������Ի�����������ͼ��
		resultMap.put("zeroList", zeroList);
		resultMap.put("relate", relateMap);
		resultMap.put("topList", topList);
		return resultMap;
	}
	
	/**
	 * �����µ�֤�ع�ϵ����
	 * @param topList
	 * @param zs
	 */
	private static String[][] generateNewZs(List<String> topList, String[][] zs, SyndromeElementService syndromeElementService) {
		List<Map<Integer, String>> newRelateList = new ArrayList<Map<Integer,String>>();
		int currentRelate = 0;
		for (int i = 0; i < topList.size(); i++) {
			for (int j = 0; j < topList.size(); j++) {
				if (topList.get(i).equals(topList.get(j))) {	//���Ƚ�֤�ر���,����Ҫ�Ƚ��໥֮��Ĺ�ϵ��A->B,����Ҫ�Ƚ�B->A;
					continue;
				}
				currentRelate = compareElement(topList.get(i), topList.get(j), syndromeElementService);
				if (currentRelate == 1) {
					Map<Integer, String> map = new HashMap<Integer, String>();
					map.put(0, topList.get(i));
					map.put(1, topList.get(j));
					newRelateList.add(map);
				}
			}
		}
		
		int len = newRelateList.size();
		String[][] newZs = new String[zs.length + newRelateList.size()][2];
		for (int i = 0; i < zs.length; i++) {
			newZs[i][0] = zs[i][0];
			newZs[i][1] = zs[i][1];
		}
		if (len > 0) {
			for (int i = zs.length; i < newZs.length; i++) {
				newZs[i][0] = newRelateList.get(newZs.length - i - 1).get(0);
				newZs[i][1] = newRelateList.get(newZs.length - i - 1).get(1);
			}
		}
		
		return newZs;
	}

	/**
	 * ��ѯ���ݿ⣬�Ƚ�����֤��֮��Ĺ�ϵ
	 * @param element
	 * @param compareElement
	 * @return
	 */
	public static int compareElement(String element, String compareElement, SyndromeElementService syndromeElementService) {
		SyndromeElementInput zs = new SyndromeElementInput();
		zs.setSyndromeElementStart(element);
		zs.setSyndromeElementEnd(compareElement);
		SyndromeElement zs0 = new SyndromeElement();
		BeanUtils.copyProperties(zs, zs0);
		List<SyndromeElement>  list = syndromeElementService.findRelateByZs(zs0);
		int relate = (list != null && list.size() > 0) ? list.get(0).getIsRelate() : 0;
		return relate;
	}
	
	/**
	 * �Ƚ�2��֤��֮��Ĺ�ϵ
	 * @param element
	 * @param compareElement
	 * @param zs
	 */
	public static int compareElement(String element, String compareElement, String[][] zs) {
		int relate = 0;
		for(int i = 0; i < zs.length; i++) {
			if (element.equals(zs[i][0]) && compareElement.equals(zs[i][1])) {
				relate = 1;
				break;
			}
		}
		return relate;
	}
	
	/**
	 * ���ɶ��㼯��
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
	 * @param args
	 */
	public static void main(String[] args) {
		String[][] zs = {{"A","B"}, {"A","C"},{"C", "D"},{"B", "F"},{"D","F"}};
		//findRelate(zs);
	}

}
