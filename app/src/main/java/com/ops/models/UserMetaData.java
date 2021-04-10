package com.ops.models;

public class UserMetaData<E> {
    private E userData;

    public UserMetaData(E data) {
        this.userData = data;
    }

    public E getUserData() {
        return userData;
    }

    public void setUserData(E userData) {
        this.userData = userData;
    }
}
