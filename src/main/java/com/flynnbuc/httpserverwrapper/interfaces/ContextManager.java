package com.flynnbuc.httpserverwrapper.interfaces;

/**
 * Defines an object which will handle notification events from the {@link com.flynnbuc.httpserverwrapper.services.ServerService}, and create data when server has been started
 */

public interface ContextManager extends NotificationListener{

    /**
     * Method called when {@link com.flynnbuc.httpserverwrapper.services.ServerService} has been started
     */
    void requestData();
}
