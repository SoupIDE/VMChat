package de.soup.vmchat.util;

import de.soup.vmchat.VMChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CancelResponse {

    private final int maxCancels;
    private int currentCancels;

    public CancelResponse(int maxCancels)
    {
        this.maxCancels = maxCancels;
        VMChat.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event)
    {
        if(currentCancels != maxCancels)
        {
            event.setCanceled(true);
            currentCancels++;
        }
        else VMChat.EVENT_BUS.unregister(this);
    }
}
