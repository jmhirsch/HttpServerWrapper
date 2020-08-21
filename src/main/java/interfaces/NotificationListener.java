package interfaces;

import model.Notification;

public interface NotificationListener {
     void notificationReceived(Notification notification, Object obj, long id);
}
