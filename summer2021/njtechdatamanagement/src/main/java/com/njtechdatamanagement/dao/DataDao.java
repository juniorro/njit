package com.njtechdatamanagement.dao;

import org.springframework.dao.DataAccessException;

import java.util.Collection;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/23/2021
 */

public interface DataDao<T> {
    T create(T data) throws DataAccessException;
    Collection<T> list(int limit) throws DataAccessException;
    T get(Long id) throws DataAccessException;
    T update(T data) throws DataAccessException;
    boolean delete(Long id) throws DataAccessException;
}
