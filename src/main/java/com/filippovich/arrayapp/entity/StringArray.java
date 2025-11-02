package com.filippovich.arrayapp.entity;

import java.util.UUID;

public class StringArray {

    private final UUID id;
    private final String[] array;

    public StringArray(String[] array) {
        this.id = UUID.randomUUID();
        this.array = array != null ? array.clone() : new String[0];
    }

    public UUID getId() {
        return id;
    }

    public String[] getArray() {
        return array.clone();
    }

    public int length() {
        return array.length;
    }

    public boolean isEmpty() {
        return array.length == 0;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StringArrayImpl{id=").append(id).append(", array=[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(array[i]);
        }
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringArray that = (StringArray) o;

        if (!id.equals(that.id)) {
            return false;
        }

        if (array.length != that.array.length) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            String thisElement = array[i];
            String thatElement = that.array[i];
            if (thisElement == null) {
                if (thatElement != null) {
                    return false;
                }
            } else if (!thisElement.equals(thatElement)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        for (String element : array) {
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }
}