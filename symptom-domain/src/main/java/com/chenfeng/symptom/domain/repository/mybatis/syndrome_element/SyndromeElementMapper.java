package com.chenfeng.symptom.domain.repository.mybatis.syndrome_element;

import com.chenfeng.symptom.domain.common.annotation.MyBatisRepository;
import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;
import com.chenfeng.symptom.domain.repository.mybatis.CrudMapper;

@MyBatisRepository
public interface SyndromeElementMapper extends CrudMapper<SyndromeElement, Long> {
    
}