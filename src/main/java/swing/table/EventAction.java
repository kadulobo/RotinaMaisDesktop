package swing.table;

/**
 * Generic event callback used by table action buttons. Implementations can
 * decide how to handle update and delete events for the provided data type.
 *
 * @param <T> Type of the row data associated with the action buttons
 */
public interface EventAction<T> {

    void delete(T data);

    void update(T data);
}
