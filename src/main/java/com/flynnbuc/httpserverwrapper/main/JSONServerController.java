package com.flynnbuc.httpserverwrapper.main;

import com.flynnbuc.httpserverwrapper.interfaces.ContextManager;
import com.sun.net.httpserver.HttpExchange;
import com.flynnbuc.httpserverwrapper.enums.ServerMethodType;
import com.flynnbuc.httpserverwrapper.interfaces.NotificationListener;
import com.flynnbuc.httpserverwrapper.model.NetworkRequest;
import com.flynnbuc.httpserverwrapper.model.Context;
import com.flynnbuc.httpserverwrapper.model.Handler;
import com.flynnbuc.httpserverwrapper.model.Notification;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.flynnbuc.httpserverwrapper.services.ServerService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/** <p>
 * Defines a basic controller for {@link ServerService}. <br>
 * If a class has dynamically generated handlers, those can be added by adding this
 * class as a {@link PropertyChangeListener}, and firing a propertyChangeEvent. <br>
 * In this case, evt.getPropertyName should be this.PROPERTY_CHANGE_STR, and
 * evt.newValue should be an instance of {@link Handler}
 * </p>
 */
public class JSONServerController extends ServerController<JSONObject> {

    private final RequestNumberGenerator numGenerator = new RequestNumberGenerator();
    private final Map<Long, NetworkRequest<JSONObject>> requestQueue = new ConcurrentHashMap<>();

    /**
     * Creates a ServerController, with the expected manager to handle context creation and notification events
     * @param manager Class responsible for receiving notification events and creating contexts for this
     */
    public JSONServerController(ContextManager manager) {
       super(manager);
    }

    /**
     * Create context from specified {@link Context} objects
     *
     * @param contextsToCreate {@link Context} objects to be created and added to server
     */
    public synchronized void createContexts(Context ... contextsToCreate) {
            for (Context context : contextsToCreate) {
            serverService.addContext(context.path(), new ServerHandler(context.path(), context.type(), context.notification()));
        }
    }


    /**
     * Function to be called to handle response to the user after the data has been processed by ContextManager

     * @param requestNum id of the request that should be processed
     * @param response JSONObject representation of the request that should be sent back to client
     * @param responseCode int code for the response code sent in the headers
     */
    public synchronized void handleRequestResponse(long requestNum, JSONObject response, int responseCode) {
        NetworkRequest<JSONObject> request = requestQueue.remove(requestNum);
        if (request == null) {
            System.out.println("request is null");
        }else {
            respondToRequest(response, request.getExchange(), responseCode);
        }
    }

    /**
     * Respond to request by writing JSONObject to client
     *
     * @param response JSONObject containing the response
     * @param exchange HTTPExchange received from handler
     * @param responseCode response code received from listener
     */
    protected synchronized void respondToRequest(JSONObject response, HttpExchange exchange, int responseCode) {
        try {
            byte[] responseBytes = response.toString().getBytes();
            exchange.sendResponseHeaders(responseCode, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a specified handler when a PropertyChangeEvent is fired
     *
     * @param evt event fired. To properly be added, evt.getPropertyName should be this.PROPERTY_CHANGE_STR,
     *            evt.getNewValue should be an instance of {@link Handler}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase(PROPERTY_CHANGE_STR)){
            if (evt.getNewValue() instanceof Handler httpHandler){
                serverService.addContext(httpHandler.getPath(), httpHandler);

            }
        }
    }

    private void fireNotifications(Notification notification, JSONObject body, long id){
        super.fireNotification(notification, body, id);
    }


    private class ServerHandler extends Handler{
        private final ServerMethodType type;
        private final Notification notification;
        private JSONObject response;
        public ServerHandler(String path, ServerMethodType type, Notification notification){
            super(path);
            this.type = type;
            this.notification = notification;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                 response = new JSONObject();
                if (!exchange.getRequestURI().toString().equalsIgnoreCase(getPath())) {
                    response.put("message", "invalid path " + getPath());
                    respondToRequest(response, exchange, 404); // Invalid URL
                    return;
                } else if (!exchange.getRequestMethod().equalsIgnoreCase(type.toString())) {
                    response.put("message", "invalid request type " + exchange.getRequestMethod() + " at path " + getPath());
                    respondToRequest(response, exchange, 406); // Not Acceptable
                    return;
                }
                JSONObject requestBody;
                InputStream is = exchange.getRequestBody();
                String requestStr = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                if (requestStr.trim().equalsIgnoreCase("") || requestStr.isBlank() || requestStr.isEmpty()) {
                    requestBody = new JSONObject();

                }else{
                    JSONTokener parser = new JSONTokener(requestStr);
                    requestBody = new JSONObject(parser);
                }

                if (exchange.getRequestURI().toString().equalsIgnoreCase(getPath())) {
                    NetworkRequest<JSONObject> request = new NetworkRequest<>(numGenerator.incrementAndGet(), exchange, getPath());
                    requestQueue.put(request.getRequestNum(), request);
                    fireNotifications(notification, requestBody, request.getRequestNum());
                }
            }catch(Exception e){
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}


