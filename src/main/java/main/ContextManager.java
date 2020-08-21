package main;

import interfaces.NotificationListener;

public abstract class ContextManager implements NotificationListener {

    // should create contexts when this method is called
    public abstract void requestData();
}
