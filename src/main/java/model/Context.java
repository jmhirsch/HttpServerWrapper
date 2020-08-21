package model;

import enums.ServerMethodType;

public record Context(String path, ServerMethodType type, Notification notification){}
