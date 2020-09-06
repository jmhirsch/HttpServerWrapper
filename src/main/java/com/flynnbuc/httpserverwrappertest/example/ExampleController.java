package com.flynnbuc.httpserverwrappertest.example;

import com.flynnbuc.httpserverwrapper.enums.ServerMethodType;
import com.flynnbuc.httpserverwrapper.exceptions.CouldNotFindIPException;
import com.flynnbuc.httpserverwrapper.interfaces.ContextManager;
import com.flynnbuc.httpserverwrapper.main.JSONServerController;
import com.flynnbuc.httpserverwrapper.model.Context;
import com.flynnbuc.httpserverwrapper.model.Notification;
import com.flynnbuc.httpserverwrapper.services.IPAddressChecker;
import org.json.JSONObject;

/**
 * Demo class showing example implementation of a manager for JSONServerController
 *
 */
public class ExampleController implements ContextManager {

    private final String GET_ROOT = "/data/";
    private final String PING = "/ping/";
    private final int portNum = 3006;

    private final JSONServerController server;


    //Start Server
    public ExampleController() {
        server = new JSONServerController(this);
        server.startServerService(portNum, this::serverStarted);
    }

    public Void serverStarted(boolean started){

        if (started){
            System.out.println("Server started, contexts created!");
            try {
                String ip = IPAddressChecker.getLocalIP();
                System.out.println("To test the server on LAN, head to http://" + ip + ":" + portNum + "/data/ or http://" + ip + ":" + portNum + "/ping/");
            } catch (CouldNotFindIPException e) {
                e.printStackTrace();
            }


        }else{
            System.err.println("Error starting the server. Check that port is not in use and not restricted");
        }

        return null;
    }

    //Create static contexts
    @Override
    public void requestData() {
        Context testGet = new Context(GET_ROOT, ServerMethodType.GET, new Notification(GET_ROOT));
        Context testPing = new Context(PING, ServerMethodType.GET, new Notification(PING));
        server.createContexts(testGet, testPing);
    }

    //Handle notification events when specific contexts are called.
    @Override
    public void notificationReceived(Notification notification, Object obj, long id) {
        switch (notification.name()){
            case GET_ROOT -> handleGet(id);
            case PING -> handlePing(obj, id);
        }
    }

    //Handle get: response with desired data here
    private void handleGet(long id){
        JSONObject response = new JSONObject();
        response.put("response", "server received get request");
        server.handleRequestResponse(id, response, 200);
    }

    //Handle ping: echo data received
    // in JSONServerController, headers are embedded in request. If desired,
    // they can be retrieved with the key "headers"
    private void handlePing(Object obj, long id){
        JSONObject response = new JSONObject();
        int responseCode;

        if (obj instanceof JSONObject request){
            response.put("request headers", request.remove("headers"));
            response.put("response", request.toString());
            responseCode = 200;
        }else{
            response.put("response", "request cannot be determined for path " + PING);
            responseCode = 401;
        }
        server.handleRequestResponse(id, response, responseCode);
    }

    public static void main(String[] args) {
        new ExampleController();
    }
}
