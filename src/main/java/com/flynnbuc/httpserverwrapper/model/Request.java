package com.flynnbuc.httpserverwrapper.model;

/**
 * Generic request received by a client
 *
 * @param <T> Type of the expected data from the client to be processed in this request
 */
public abstract class Request<T> {
    private final long requestNum; // Should be a uniquely generated number

    /**
     * Defines a generic Request
     *
     * @param requestNum unique ID of the request
     */
    public Request(long requestNum){
        this.requestNum = requestNum;
    }

    /**
     * @return the request id
     */
    public long getRequestNum(){
        return requestNum;
    }

    /**
     * Defines a function to generify the processing of information passed to a type T request
     *
     * @param data data to be processed
     * @return T data returned
     */
    public abstract T completeRequest(T data);
}
