package com.chenfeng.symptom.service.syndrome_element;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.chenfeng.symptom.common.constant.Constant;
import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.domain.repository.mybatis.syndrome_element.SyndromeElementMapper;
import com.chenfeng.symptom.service.CrudServiceImpl;

@Service
public class SyndromeElementServiceImpl extends
		CrudServiceImpl<SyndromeElement, Long, SyndromeElementMapper> implements
		SyndromeElementService {

	@Resource
	@Override
	public void setRepository(SyndromeElementMapper syndromeElementMapper) {
		super.setRepository(syndromeElementMapper);
	}

	@Override
	public void create(SyndromeElementInput syndromeElementInput) {
		SyndromeElement syndromeElement = new SyndromeElement();
		BeanUtils.copyProperties(syndromeElementInput, syndromeElement);
		syndromeElement.setIsRelate(Constant.SYNDROME_ELEMENT_RELATE_TRUE);
		repository.insertSelective(syndromeElement);
	}

	@Override
	public List<SyndromeElement> findAll() {
		
		return repository.findAll();
	}
}
