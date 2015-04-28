package com.chenfeng.symptom.domain.repository.mybatis.syndrome_element;

import java.util.List;

import com.chenfeng.symptom.domain.common.annotation.MyBatisRepository;
import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.domain.repository.mybatis.CrudMapper;

@MyBatisRepository
public interface SyndromeElementMapper extends CrudMapper<SyndromeElement, Long> {

	/**
	 * 
	 * @return
	 *
	 * @author wangyuhao
	 * @date 2015年4月27日 下午10:30:13
	 */
	List<SyndromeElement> findAll();
	
	/**
	 * 查找2个证素之间的关系
	 * @param zs
	 * @return
	 */
	List<SyndromeElement> findRelateByZs(SyndromeElement zs);
    
}