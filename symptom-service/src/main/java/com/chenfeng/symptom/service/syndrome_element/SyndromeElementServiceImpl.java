package com.chenfeng.symptom.service.syndrome_element;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenfeng.symptom.common.constant.Constant;
import com.chenfeng.symptom.domain.common.pagehelper.Page;
import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.domain.repository.mybatis.syndrome_element.SyndromeElementMapper;
import com.chenfeng.symptom.service.CrudServiceImpl;

@Service
@Transactional(readOnly = true)
public class SyndromeElementServiceImpl extends
		CrudServiceImpl<SyndromeElement, Long, SyndromeElementMapper> implements
		SyndromeElementService {

	@Resource
	@Override
	public void setRepository(SyndromeElementMapper syndromeElementMapper) {

	    super.setRepository(syndromeElementMapper);
	}

	@Override
	@Transactional
	public void create(SyndromeElementInput syndromeElementInput) {
		SyndromeElement syndromeElement = new SyndromeElement();
		BeanUtils.copyProperties(syndromeElementInput, syndromeElement);
		repository.insertSelective(syndromeElement);
	}

	@Override
	public List<SyndromeElement> findAll() {
		return repository.findAll();
	}

	@Override
	public List<SyndromeElement> findRelateByZs(SyndromeElement zs) {
		return repository.findRelateByZs(zs);
	}

    @Override
    public Page<SyndromeElement> findPageSyndromeElement(int page) {
        
        return repository.findPageSyndromeElement(new RowBounds(page * Constant.PAGE_SIZE, Constant.PAGE_SIZE));
    }
}
