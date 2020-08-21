package com.flynnbuc.httpserverwrapper.interfaces;

import com.flynnbuc.httpserverwrapper.model.Notification;

public interface NotificationListener {
     void notificationReceived(Notification notification, Object obj, long id);
}
