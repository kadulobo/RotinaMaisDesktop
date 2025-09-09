package swing.table;

/**
 * Wrapper model used by the table to keep the row data and the callbacks that
 * should be invoked when the action buttons are pressed.
 *
 * @param <T> Type of the row data
 */
public class ModelAction<T> {

    private T data;
    private EventAction<T> event;

    public ModelAction(T data, EventAction<T> event) {
        this.data = data;
        this.event = event;
    }

    public ModelAction() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public EventAction<T> getEvent() {
        return event;
    }

    public void setEvent(EventAction<T> event) {
        this.event = event;
    }
}
