package com.chenfeng.symptom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphFindCycle {
	 public List<String> servList;  
     
	    public void setServList(List<String> servList){  
	        this.servList = servList;  
	    }  
	      
	    class ServRel{  
	        public String relateStart;  
	        public String relateEnd;  
	    }  
	      
	    /**
	     * @param relateStart
	     * @param relateList
	     * @return
	     */
	    public List<String> findRelateEnd(String relateStart,String[][] relateArr){  
	        List<String> relateEndList = new ArrayList<String>();  
	        for(int i=0;i<relateArr.length;i++){  
	            String[] realte = relateArr[i];  
	            if(relateStart.equals(realte[0])){  
	            	if (!relateEndList.contains(realte[1])) {
	            		relateEndList.add(realte[1]);  
	            	}
	            }  
	        }
	        return relateEndList;  
	    }  
	      
	    public void findCycleServ(String topElement,String cyclePath,String[][] relateArr, boolean first ){  
	            String id = topElement;  
	            List<String> relateEndList = findRelateEnd(topElement,relateArr);  
	            if(!(relateEndList.size()>0)){  
	                return;  
	            }  
	            else if(cyclePath.indexOf(topElement)>=0){  
	                System.out.println(cyclePath+"-->"+id);  
	                return;  
	            }  
	            else{
	            	if(first)
	            		cyclePath = id;
	            	else 
	            		cyclePath = cyclePath+"-->"+id;  
	                for(int j=0;j<relateEndList.size();j++){  
	                    String childServId = (String)relateEndList.get(j);  
	                    findCycleServ(childServId,cyclePath,relateArr, false);  
	                }  
	            }  
	    } 
	    
	    /**
	     * 查找2点之间的距离。
	     * @param topElement 其中一个顶点元素
	     * @param path		字符串
	     * @param relateArr	关系数组
	     * @param map		存放顶点之间路径长度的map
	     * @param first		是否第一次访问
	     */
	    public void findPath(String topElement, String path, String[][] relateArr, Map<String, Integer> map, boolean first){
	    	String id = topElement;  
            List<String> relateEndList = findRelateEnd(topElement, relateArr);  
        	if (path.indexOf("&&") > 0) {
        		String[] str = path.split("&&");
        		map.put(str[0] + str[str.length-1], str.length - 1);
        	}
            if(!(relateEndList.size()>0)){  
            	if (first)
            		path = id;
            	else 
            		path = path+"&&"+id; 
            	
            	if (path.indexOf("&&") > 0) {	//记录2个点之间的距离
            		String[] str = path.split("&&");
            		map.put(str[0] + str[str.length-1], str.length - 1);
            	}
            	//System.out.println(path);
            	return;
            } else {
            	if (first)
            		path = id;
            	else 
            		path = path+"&&"+id;  
            	
            	 for(int j=0;j<relateEndList.size();j++){  
	                    String childServId = (String)relateEndList.get(j); 
	                    
	                    if (path.indexOf(childServId) >= 0) {	//如果路径中包含该end元素，则说明形成了环状图。则不需要遍历这一个节点了 否则就成了死循环
	                    	if (path.indexOf("&&") > 0) {	//记录2个点之间的距离
	                    		String[] str = path.split("&&");
	                    		map.put(str[0] + str[str.length-1], str.length - 1);
	                    	}
	                    	//System.out.println(path);
	                    	continue;
	                    }
	                    //map.put(id + childServId, 1);//相邻两个点之间的距离设置为1
	                    findPath(childServId,path,relateArr, map, false);  
	                }  
            }
	    	
	    }
	    
	    /**
	     *  判断能否生成有向图
	     * @param topList	顶点集合
	     * @param relateArr	关系表
	     * @return
	     */
	    public boolean  decideGenerateGraph(List<String> topList, String[][] relateArr) {
	    	Map<String, Integer> map = null;
	    	GraphFindCycle gfc = new GraphFindCycle();
	    	int unArrive = 10000;	//假设任意两个点之间的距离10000.也就是说明任意2点间都不能达到。
	    	boolean flag = true;
	    	for(int i = 0; i < topList.size(); i++) {
	    		map = new HashMap<String, Integer>();
	    		flag = true;
	    		for(int j = 0; j < topList.size(); j++) {
	    			if (i == j){	//不计算本身顶点的距离
	    				continue;
	    			}
	    			map.put(topList.get(i) + topList.get(j), unArrive);
		    	}
	    		gfc.findPath(topList.get(i), "", relateArr, map,  true);
	    		for(String key : map.keySet()) {
	    			if (map.get(key) == unArrive) {
	    				flag = false;	//其中有2个点之间不能到达，说明不满足有向图，则继续查找。
	    				break;
	    			}
	    		}
	    		if(flag) {	//如果任意2点间都能到达，则说明满足有向图。
	    			break;
	    		}
	    		
	    	}
	    	return flag;
	    }
	      
	      
	    public static void main(String[] args) {  
	        GraphFindCycle gfc = new GraphFindCycle();  
	        List<String> topList = new ArrayList<String>();  
	        topList.add("A");  
	       /* topList.add("B");  
	        topList.add("C");  
	        topList.add("D");  */
	       /* topList.add("D");  
	        topList.add("E");  
	        topList.add("F");  */
	         
	        List<ServRel> relateList = new ArrayList<ServRel>();  
	        String[][] relateArr = new String[4][4];
	        ServRel sr = gfc.new ServRel();  
	        String[] arr1 = {"A", "B", "1", "des"};
	        sr.relateStart = "A";  
	        sr.relateEnd = "B";  
	        relateList.add(sr); 
	        ServRel sr1 = gfc.new ServRel();  
	        String[] arr2 = {"B", "C", "1", "des"};
	        sr1.relateStart = "B";  
	        sr1.relateEnd = "A";  
	        relateList.add(sr1);  
	        ServRel sr2 = gfc.new ServRel();  
	        String[] arr3 = {"C", "D", "1", "des"};
	        sr2.relateStart = "A";  
	        sr2.relateEnd = "C";  
	        relateList.add(sr2);  
	        ServRel sr3 = gfc.new ServRel();  
	        String[] arr4 = {"D", "B", "1", "des"};
	        sr3.relateStart = "A";  
	        sr3.relateEnd = "D";  
	        relateList.add(sr3);  
	        relateArr[0] = arr1;
	        relateArr[1] = arr2;
	        relateArr[2] = arr3;
	        relateArr[3] = arr4;
	        Map<String, Integer> map = new HashMap<String, Integer>();
	        map.put("AB", 1000);
	        map.put("AC", 1000);
	        map.put("AD", 1000);
	        
	        /*
	        ServRel sr4 = gfc.new ServRel();  
	        sr4.relateStart = "E";  
	        sr4.relateEnd = "A";  
	        relateList.add(sr4);  
	        ServRel sr5 = gfc.new ServRel();  
	        sr5.relateStart = "FfindPath";  
	        sr5.relateEnd = "B";  
	        relateList.add(sr5);  */
	        for(int i=0;i<topList.size();i++){  
	            String topElement = (String)topList.get(i);  
	            List<String> list = new ArrayList<String>();
	           // gfc.findCycleServ(topElement,"",relateList, true);  
	            gfc.findPath(topElement, "", relateArr, map,  true);
	            for (String key : map.keySet()) {
		        	System.out.println(key + "==" + map.get(key));
		        }
	            System.out.println("========================");
	        }  
	    }  

}
