package com.flynnbuc.httpserverwrapper.model;

import com.sun.net.httpserver.HttpHandler;
/**
 * Defines a basic Handler to be passed to {@link com.flynnbuc.httpserverwrapper.services.ServerService}
 */
public abstract class Handler implements HttpHandler {
    private final String path;

    /**
     * Constructor
     *
     * @param path URL Path from "/" of the Handler
     */
    public Handler(String path) {
        this.path = path;
    }

    /**
     *
     * @return path as String
     */
    public String getPath(){
        return path;
    }
}
