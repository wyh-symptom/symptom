package com.chenfeng.symptom.domain.repository.mybatis;

import com.chenfeng.symptom.domain.model.mybatis.SyndromeElement;

public interface SyndromeElementMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_syndrome_element
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_syndrome_element
     *
     * @mbggenerated
     */
    int insert(SyndromeElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_syndrome_element
     *
     * @mbggenerated
     */
    int insertSelective(SyndromeElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_syndrome_element
     *
     * @mbggenerated
     */
    SyndromeElement selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_syndrome_element
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SyndromeElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_syndrome_element
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SyndromeElement record);
}