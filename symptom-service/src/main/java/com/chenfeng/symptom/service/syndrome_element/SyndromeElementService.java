package com.chenfeng.symptom.service.syndrome_element;

import java.util.List;

import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.service.CrudService;

public interface SyndromeElementService extends CrudService<SyndromeElement, Long> {

	/**
	 * 
	 * @param syndromeElementInput
	 *
	 * @author wangyuhao
	 * @date 2015年4月27日 下午10:26:19
	 */
	void create(SyndromeElementInput syndromeElementInput);

	/**
	 * 
	 * @return
	 *
	 * @author wangyuhao
	 * @date 2015年4月27日 下午10:29:39
	 */
	List<SyndromeElement> findAll();

}
