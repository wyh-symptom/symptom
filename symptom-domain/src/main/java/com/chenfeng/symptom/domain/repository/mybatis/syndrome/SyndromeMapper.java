package com.chenfeng.symptom.domain.repository.mybatis.syndrome;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.chenfeng.symptom.domain.common.annotation.MyBatisRepository;
import com.chenfeng.symptom.domain.common.pagehelper.CountablePage;
import com.chenfeng.symptom.domain.common.pagehelper.Page;
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
	
	/**
     * 通过症状名字查找
     * @param zzName
     * @return
     */
    List<Syndrome> findAllByZz(String zzName);

    /**
     * 
     * @param id
     * @return
     *
     * @author wangyuhao
     */
    Syndrome findNextSyndromeById(@Param("id") Long id);

    /**
     * 
     * @return
     *
     * @author wangyuhao
     */
    Syndrome findFirstSyndrome();

    /**
     * 
     * @param rowBounds
     * @return
     *
     * @author wangyuhao
     */
    Page<Syndrome> findPageSyndrome(RowBounds rowBounds);

}