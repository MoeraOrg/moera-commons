package org.moera.commons.crypto;

public class RestoredObject<T> {

    private T object;
    private int available;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

}
