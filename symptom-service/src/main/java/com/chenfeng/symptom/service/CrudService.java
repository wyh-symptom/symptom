package com.chenfeng.symptom.service;


import java.io.Serializable;

/**
 * CRUD Service interface.
 *
 * @param <T> Your resource POJO to manage, maybe an entity or DTO class
 * @param <ID> Resource id type, usually Long or String
 */
public interface CrudService<T, ID extends Serializable> {

    /**
     * Create new resource.
     * 
     * @param resource
     *            Resource to create
     * @return new resource
     */
    T create(T resource);

    /**
     * Update existing resource.
     * 
     * @param resource
     *            Resource to update
     * @return resource updated
     */
    T update(T resource);


    /**
     * Delete existing resource.
     * 
     * @param id
     *            Resource id
     */
    void delete(ID id);
    
    /**
     * Find resource by id.
     * 
     * @param id
     *            Resource id
     * @return resource
     */
    T findOne(ID id);
    
//    /**
//     * Delete all existing resource. Do not use cascade remove (not a choice -> JPA specs)
//     */
//    void deleteAll();
//
//    /**
//     * Delete all existing resource, including linked entities with cascade delete
//     */
//    void deleteAllWithCascade();
 
    
//    /**
//    * determine if the Entity of specified ID exists or not.
//    * 
//    * @return
//    */
//    boolean exists(ID id);
//
//    /**
//     * Find all resources.
//     * 
//     * @return a list of all resources.
//     */
//    List<T> findAll();
//
//    /**
//     * Find all resources (pageable).
//     * 
//     * @param pageable
//     *            page request
//     * @return resources
//     */
//    Page<T> findAll(Pageable pageable);

//    /**
//     * Count all resources.
//     * 
//     * @return number of resources
//     */
//    long count();
   
}
