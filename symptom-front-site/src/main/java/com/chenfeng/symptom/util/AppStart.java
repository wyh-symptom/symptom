package com.chenfeng.symptom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator 初始化application
 * 
 */
public class AppStart {
	public static void init(ServletContext context) {
		Log log = LogFactory.getLog(AppStart.class);
		if (context == null) {
			System.err.println("***********加载应用启动信息时出错*********");
		} else {
				/*1、一般症状；2、心血肺胸症状；3、脾系腹部症状；4、、头面颈部症状；5、眼部症状；6、耳鼻症状；
				7、口腔咽喉症状；8、经精生殖症状；9、腰小便二阴症状；10、皮肤症状；11、骨肉肢体症状；
				12、精神症状；13、形体动态症状；14、舌象；
				15、脉象*/
			try{
			    List<String> zzCategory = new ArrayList<String>();
			    zzCategory.add("一般症状");
			    zzCategory.add("心血肺胸症状");
			    zzCategory.add("脾系腹部症状");
			    zzCategory.add("头面颈部症状");
			    zzCategory.add("眼部症状");
			    zzCategory.add("耳鼻症状");
			    zzCategory.add("口腔咽喉症状");
			    zzCategory.add("经精生殖症状");
			    zzCategory.add("腰小便二阴症状");
			    zzCategory.add("皮肤症状");
			    zzCategory.add("骨肉肢体症状");
			    zzCategory.add("精神症状");
			    zzCategory.add("形体动态症状");
			    zzCategory.add("舌象");
			    zzCategory.add("脉象");
			    Map<Integer, String> relateTypes = new HashMap<Integer, String>();
			    relateTypes.put(1, "因果关系");
			    relateTypes.put(2, "从属关系");
			    relateTypes.put(3, "并列关系");
				context.setAttribute("zzCategory", zzCategory);
				context.setAttribute("relateTypes", relateTypes);
				
			} catch (Exception e) {
				System.err.println("***********加载应用启动信息时出错*********");
				log.error("加载应用启动信息时出错：", e);
			}

		}
	}
}
