package com.chenfeng.symptom.domain.repository.mybatis.syndrome;

import java.util.List;

import com.chenfeng.symptom.domain.common.annotation.MyBatisRepository;
import com.chenfeng.symptom.domain.model.mybatis.Syndrome;
import com.chenfeng.symptom.domain.repository.mybatis.CrudMapper;

@MyBatisRepository
public interface SyndromeMapper extends CrudMapper<Syndrome, Long> {

	/**
	 * 
	 * @return
	 *
	 * @author wangyuhao
	 * @date 2015年4月25日 下午9:27:40
	 */
	List<Syndrome> findAll();

}