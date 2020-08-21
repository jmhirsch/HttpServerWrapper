package com.flynnbuc.httpserverwrapper.model;

import com.flynnbuc.httpserverwrapper.enums.ServerMethodType;

/**
 * Record containing the path, CRUD message type, and notification to be called on a specific http context
 * <p>
 * @param  path  The path from "/" of a specific http context url
 * @param  type  The type of operation the server expects, as noted in 'ServerMethodType' {@link ServerMethodType}
 * @param notification The notification Notification {@link Notification} object containing string of the notification sent
 *  </p>
 */
public record Context(String path, ServerMethodType type, Notification notification){}
