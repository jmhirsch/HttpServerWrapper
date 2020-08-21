package com.flynnbuc.httpserverwrapper.model;

import com.flynnbuc.httpserverwrapper.enums.ServerMethodType;

public record Context(String path, ServerMethodType type, Notification notification){}
