package com.flynnbuc.httpserverwrapper.interfaces;

import com.flynnbuc.httpserverwrapper.model.Notification;

/**
 * Creates a listener for specific notifications with id
 */
public interface NotificationListener {
    /**
     * @param notification {@link Notification} object containing string of the notification sent
     * @param obj Any object that the sender wishes to pass, can be null
     * @param id ID to idenfity the specific notification
     */
     void notificationReceived(Notification notification, Object obj, long id);
}
