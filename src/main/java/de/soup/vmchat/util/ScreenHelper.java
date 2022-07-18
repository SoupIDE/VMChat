package de.soup.vmchat.util;

import de.soup.vmchat.VMChat;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ScreenHelper {

    private final Screen screen;

    public ScreenHelper(Screen screen)
    {
        this.screen = screen;
        VMChat.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void openGUI(TickEvent.ClientTickEvent event)
    {
        VMChat.getVars().getClient().setScreen(this.screen);
        VMChat.EVENT_BUS.unregister(this);
    }
}
