package de.soup.vmchat.event.events;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.util.Keyboard;
import de.soup.vmchat.util.MessageBus;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MacroEvent {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(VMChat.getVars().getClientPlayer() != null && VMChat.getVars().getClient().screen == null)
        {
            VMChat.getMacroConfig().getSettings().getMacros().forEach(macro->{
                if (macro.isAvailable() && macro.getKeys().stream().allMatch(Keyboard::isKeyPressed)) {
                    macro.setAvailable(false);
                    if (macro.isAutoSend()) MessageBus.sendFromClient(macro.getMessage());
                    else VMChat.getVars().getClient().setScreen(new ChatScreen(macro.getMessage()));
                }
                if (!macro.getKeys().stream().allMatch(Keyboard::isKeyPressed) && !macro.isAvailable()) macro.setAvailable(true);
            });
        }
    }
}
