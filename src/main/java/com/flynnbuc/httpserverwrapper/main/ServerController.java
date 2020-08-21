package com.flynnbuc.httpserverwrapper.main;

import com.flynnbuc.httpserverwrapper.interfaces.ContextManager;
import com.flynnbuc.httpserverwrapper.interfaces.NotificationListener;
import com.flynnbuc.httpserverwrapper.model.Context;
import com.flynnbuc.httpserverwrapper.model.NetworkRequest;
import com.flynnbuc.httpserverwrapper.model.Notification;
import com.flynnbuc.httpserverwrapper.services.ServerService;
import com.sun.net.httpserver.HttpExchange;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * Abstract template class for a server controller
 *
 * @param <T> type of data to be used to get/send requests from/to client
 */
public abstract class ServerController<T> implements PropertyChangeListener {

    public static String PROPERTY_CHANGE_STR = "Handler Added";
    protected final Map<Long, NetworkRequest<T>> requestQueue = new ConcurrentHashMap<>();
    protected ServerService serverService;
    private RequestNumberGenerator requestNumberGenerator = new RequestNumberGenerator();

    private final List<NotificationListener> notificationListenerList;
    private final ContextManager manager;

    public <T>ServerController(ContextManager manager){
        serverService = new ServerService();
        this.manager = manager;
        notificationListenerList = new ArrayList<>();
        notificationListenerList.add(manager);
    }

    /**
     * Adds another Notification Listener to handle events
     *
     * @param notificationListener listener for user requests
     */
    public void addNotificationListener(NotificationListener notificationListener) {
        notificationListenerList.add(notificationListener);
    }

    /**
     * Attemps to start {@link ServerService} with specified port num<br>
     * Expects a callback function with boolean value of whether the server is running<br>
     * Callback function can be empty if desired
     *
     * @param portNum port number to be used in the server
     * @param callback function accepting a Boolean value and returning null to be called when server was attempted to start
     * @return true if server is running, false otherwise
     */
    public synchronized boolean startServerService(int portNum, Function<Boolean, Void> callback) {
        boolean created = serverService.startServer(portNum);
        callback.apply(created);
        manager.requestData();
        return created;
    }

    /**
     * Stops the server<br>
     * Callback function always passes false, as server is not running<br>
     * Callback function can be empty if desired
     *
     * @param callback function accepting a Boolean value and returning null, to be called after server has been stopped
     */
    public synchronized void stopServerService(Function<Boolean, Void> callback) {
        serverService.exit();
        serverService = null;
        callback.apply(false);
    }

    /**
     * Create context from specified {@link Context} objects
     *
     * @param contextsToCreate {@link Context} objects to be created and added to server
     */
    public abstract void createContexts(Context ... contextsToCreate);

    /**
     * Removes a context at specified path
     *
     * @param path path from "/" of the context to be removed
     */
    public void removeContext(String path) {
        serverService.removeContext(path);
    }

    /**
     * Respond to request by writing JSONObject to client
     *
     * @param response Object containing the response
     * @param exchange HTTPExchange received from handler
     * @param responseCode response code received from listener
     */
    protected abstract void respondToRequest(T response, HttpExchange exchange, int responseCode);

    /**
     * Function to be called to handle response to the user after the data has been processed by ContextManager

     * @param requestNum id of the request that should be processed
     * @param response Object representation of the request that should be sent back to client
     * @param responseCode int code for the response code sent in the headers
     */
    public abstract void handleRequestResponse(long requestNum, T response, int responseCode);

    /**
     * Notify listeners that handler has received data
     * @param notification {@link Notification} received from handler
     * @param data data containing the request information
     * @param id id of the request
     */
    protected void fireNotification(Notification notification, T data, long id){
        for(NotificationListener notificationListener: notificationListenerList) {
            notificationListener.notificationReceived(notification, data, id);
        }
    }

    /**
     * Generate request num, guarantees uniqueness between LONG.MIN and LONG.MAX number of requests
     *
     * @return long value for request num
     */
    protected long generateRequestNum(){
        return requestNumberGenerator.incrementAndGet();
    }

    /**
     * Generates ID Numbers for requests, between Long.MIN_VALUE and Long.MAX_VALUE
     */
    private static class RequestNumberGenerator {
        private long requestNum;

        public synchronized long incrementAndGet() {
            if (requestNum >= Long.MAX_VALUE) {
                requestNum = Long.MIN_VALUE;
            }
            return ++requestNum;
        }
    }
}
