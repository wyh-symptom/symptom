package com.chenfeng.symptom.service.syndrome;

import java.util.List;

import com.chenfeng.symptom.domain.model.mybatis.Syndrome;
import com.chenfeng.symptom.service.CrudService;

public interface SyndromeService extends CrudService<Syndrome, Long> {

	/**
	 * 
	 * @param syndromeCreateInput
	 *
	 * @author wangyuhao
	 * @date 2015年4月25日 下午9:26:01
	 */
    void create(SyndromeCreateInput syndromeCreateInput);

    /**
     * 
     * @return
     *
     * @author wangyuhao
     * @date 2015年4月25日 下午9:25:54
     */
    List<SyndromeInitOutput> findSyndromeInitData();
    
}
