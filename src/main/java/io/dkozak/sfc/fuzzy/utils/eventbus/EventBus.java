package io.dkozak.sfc.fuzzy.utils.eventbus;

import java.util.HashMap;
import java.util.Map;

public class EventBus {
    private static final Object EMPTY_OBJECT = new Object();
    private final Map<String, EventBusListener> registeredListeners = new HashMap<>();

    public void register(String id, EventBusListener listener) {
        registeredListeners.put(id, listener);
    }

    public void unregister(String id) {
        if (registeredListeners.remove(id) == null) {
            throw new RuntimeException("No listener with id " + id + " found");
        }
    }

    public void unregister(EventBusListener listener) {
        String id = null;
        for (Map.Entry<String, EventBusListener> entry : registeredListeners.entrySet()) {
            if (entry.getValue()
                     .equals(listener)) {
                id = entry.getKey();
                break;
            }
        }

        if (id != null)
            registeredListeners.remove(id);
        else
            throw new RuntimeException("This listener has not been registered yet");
    }

    public void broadcast(String messageID) {
        broadcast(messageID, EMPTY_OBJECT);
    }

    public void broadcast(String messageID, Object param) {
        registeredListeners.forEach((id, listener) -> listener.onMessage(messageID, param));
    }

    public void unicast(String targetID, String messageID) {
        unicast(targetID, messageID, EMPTY_OBJECT);
    }

    public void unicast(String targetID, String messageID, Object param) {
        EventBusListener listener = registeredListeners.get(targetID);
        if (listener != null) {
            listener.onMessage(messageID, param);
        } else {
            throw new RuntimeException("No target with id " + targetID + " found");
        }
    }
}
