package com.chenfeng.symptom.service.syndrome_element;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.domain.repository.mybatis.syndrome_element.SyndromeElementMapper;
import com.chenfeng.symptom.service.CrudServiceImpl;

@Service
public class SyndromeElementServiceImpl extends CrudServiceImpl<SyndromeElement, Long, SyndromeElementMapper> implements
        SyndromeElementService {

    @Resource
    @Override
    public void setRepository(SyndromeElementMapper syndromeElementMapper) {
        super.setRepository(syndromeElementMapper);
    }
}
