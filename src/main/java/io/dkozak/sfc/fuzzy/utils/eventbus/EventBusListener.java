package io.dkozak.sfc.fuzzy.utils.eventbus;

public interface EventBusListener {
    void onMessage(String messageID, Object content);
}
