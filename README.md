# Sun HTTP Server Wrapper


Sun HTTP Server wrapper with JSON responses, pre-created Handlers and quick context creators

## Features
  - Handles creation and management of the HTTP Server
  - Easily add & remove contexts from the server
  - Handles JSON responses to the client
  - Provides a Notification framework for requests from clients
  - Easily extendable using the provided Request and Handler abstractions
  - Abstract ServerController template enables easy custom controller creations
  
  
  #### Requires Java 14.0.1 preview or higher
  
  
  #### Link to [Javadoc](https://flynn-buc.github.io/HttpServerWrapper/docs/index.html)

## Installation
Maven: 
```
<dependency>
  <groupId>io.github.flynn-buc</groupId>
  <artifactId>HttpServerWrapper</artifactId>
  <version>1.0.1</version>
</dependency>
```
    
Jar: Pre-compiled jar available in releases


## Usage
Please see the example in tests/example
- Todo: add example file 

- Initiate the ServerController from a class implementing ContextManager
``` 
serverController = new JSONServerController(this)
```

- Define a function to handle the callback for starting the server
This can be in a different class
```
void callback(boolean arg){
    //Do something
    return null;
}
```

- Implement requestData()
```
public void requestData(){
    // Create Contexts
    // Contexts should have a path, a ServerMethodType, and a Notification specified
    serverController.createContexts(Context [])
}
```

- Implement notificationReceived
```
public void notificationReceived(Notification notification, Object obj, long id){
    switch(notification.name){
        //handle notification, i.e call handle(obj, id)
    }
}
```

- Handle the notification, request the ServerController to respond to client
```
public void handle(Object obj, long id){
    int responseCode = -1
    JSONObject response = new JSONObject();
    if (obj instanceof JSONObject requestObject){
        // process data from requestObject
        responseCode = 200
        response.put("key", data);
    }else{
        responseCode = 400
        response.put("key", "error msg");
    }
    
    serverController.handleRequestResponse(id, response, responseCode);
}
```

Start the Server
```
serverController.startServerService(portNum, this::callback) 
// callback can also be in an external class, simply change the parameter to : instance::callback
```


### For dynamically created Handlers:
- Extend Handler (i.e. MyHandler)
- Create a local field of type PropertyChangeSupport
- Add serverController as a listener
- call:
```
    propertyChangeSupport.firePropertyChange(ServerController.PROPERTY_CHANGE_STR, null, new MyHandler(path))
```
