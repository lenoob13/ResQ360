package core;

import java.util.List;
/**
 * Generic interface for basic CRUD operations on a data type.
 *
 * @param <T> the type of object managed by the DAO
 * 
 * @author resQ360
 */
public interface DAO<T> {
     /**
     * Retrieves an object by its ID.
     *
     * @param id the ID of the object
     * @return the corresponding object, or null if not found
     */
    T get(int id);
    /**
     * Retrieves all objects of type T.
     *
     * @return a list of all objects
     */
    List<T> getAll();
    /**
     * Inserts a new object into the data source.
     *
     * @param t the object to insert
     * @return true if the operation succeeded
     */
    boolean insert(T t);
    /**
     * Updates an existing object in the data source.
     *
     * @param t the object with updated values
     * @return true if the operation succeeded
     */
    boolean update(T t);
    /**
     * Deletes an object by its ID.
     *
     * @param id the ID of the object to delete
     * @return true if the operation succeeded
     */
    boolean delete(int id);
}
