package org.example.domain;

import java.util.Objects;

public class MyObject {
    private int threadId;
    private int totalItems;
    private String tipOperatie;

    public MyObject(int threadId, int totalItems, String tipOperatie) {
        this.threadId = threadId;
        this.totalItems = totalItems;
        this.tipOperatie = tipOperatie;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getTipOperatie() {
        return tipOperatie;
    }

    public void setTipOperatie(String tipOperatie) {
        this.tipOperatie = tipOperatie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyObject myObject = (MyObject) o;
        return threadId == myObject.threadId && totalItems == myObject.totalItems && Objects.equals(tipOperatie, myObject.tipOperatie);
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "threadId=" + threadId +
                ", totalItems=" + totalItems +
                ", tipOperatie='" + tipOperatie + '\'' +
                '}';
    }
}
