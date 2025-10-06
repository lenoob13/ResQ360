package core;
/**
 * Interface for controllers that need to receive external data.
 * Useful for passing objects between scenes.
 *
 * @param <T> the type of data to receive
 * 
 * @author resQ360
 */
public interface DataReceiver<T> {
    /**
     * Sets the data to be used by the implementing class.
     *
     * @param data the data object
     */
    void setData(T data);
}
