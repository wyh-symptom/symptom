package com.chenfeng.symptom.domain.repository.mybatis;
public interface CrudMapper<T, ID>{
	
	/**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mst_administrator
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(ID id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mst_administrator
     *
     * @mbggenerated
     */
    int insert(T record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mst_administrator
     *
     * @mbggenerated
     */
    int insertSelective(T record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mst_administrator
     *
     * @mbggenerated
     */
    T selectByPrimaryKey(ID id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mst_administrator
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mst_administrator
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(T record);
}