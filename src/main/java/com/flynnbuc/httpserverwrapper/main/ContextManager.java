package com.flynnbuc.httpserverwrapper.main;

import com.flynnbuc.httpserverwrapper.interfaces.NotificationListener;

/**
 * Defines an object which will handle notification events from the {@link com.flynnbuc.httpserverwrapper.services.ServerService}, and create data when server has been started
 */
public abstract class ContextManager implements NotificationListener {

    /**
     * Method called when {@link com.flynnbuc.httpserverwrapper.services.ServerService} has been started
     */
    public abstract void requestData();
}
