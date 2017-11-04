package io.dkozak.sfc.proj.services.eventbus;

public interface EventBusListener {
    void onMessage(String messageID, Object content);
}
