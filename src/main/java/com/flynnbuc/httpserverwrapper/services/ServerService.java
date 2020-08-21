package com.flynnbuc.httpserverwrapper.services;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.*;
import java.util.function.Function;

/**
 * Defines an HTTP Server with a specified port num
 */
public class ServerService {

    private InetSocketAddress address;
    private HttpServer server;
    private int portNum = -1;

    /**
     * Creates a ServerService with the specified port num
     *
     * @param portNum int value of the port
     */
    public ServerService(int portNum){
        this.portNum = portNum;
    }

    /**
     * Creates an empty ServerService. Port num must be specified in startServer()
     */
    public ServerService(){}


    /**
     * Returns the specified port num. Returns -1 if none were set
     * @return int value of portNum
     */
    public int getPortNum() {
        return portNum;
    }

    /**
     * Starts a server with a specified port num
     *
     * @param portNum int value of port to be used
     * @return true if server was started, false otherwise
     */
    public boolean startServer(int portNum){
        this.portNum = portNum;
        return startServer();
    }

    /**
     * Starts a server with a portnum already specified
     *
     * @return true if server was started, false otherwise
     */
    public boolean startServer(){
        if (portNum == -1){
            return false;
        }
        try {
            address = new InetSocketAddress(portNum);
            server = HttpServer.create(address, 0);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } {
        }
        return true;
    }

    /**
     * Stop the server with no delay
     */
    public void exit() {
        exit(0);
    }

    /**
     * Stops the server with specified delay
     *
     * @param delay int value of the delay before exiting the server
     */
    public void exit(int delay){
        try {
            server.stop(delay);
            System.out.println("Server Closed");
        } catch(Exception e) {
            System.err.println("Server exited with error : " + e.getLocalizedMessage());
        }
    }

    /**
     * Adds a context to this with a specified path and handler for that path
     *
     * @param path String representation of the path from "/" of the context
     * @param handler Handler object to process and respond to user requests
     */
    public void addContext(String path, HttpHandler handler){
        server.createContext(path, handler);
    }


    /**
     * Removes a context at a specified path
     *
     * @param path String of the context path from "/"
     */
    public void removeContext(String path){
        try {
            server.removeContext(path);
        } catch(Exception e){
            System.err.println("Error in server for removing path: " + path + " " +e.getMessage());
        }
    }
}
