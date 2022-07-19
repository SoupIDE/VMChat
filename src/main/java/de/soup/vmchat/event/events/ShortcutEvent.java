package de.soup.vmchat.event.events;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.shortcut.elements.Shortcuts;
import de.soup.vmchat.util.MessageBus;
import de.soup.vmchat.util.ScreenHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ShortcutEvent {

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String message = event.getMessage();
        boolean autoSend = true;
        boolean shortcutMatch = false;

        List<Shortcuts.Shortcut> shortcuts = VMChat.getShortcutConfig().getSettings().getShortcuts();
        for (int i = 0;i<shortcuts.size();i++)
        {
            Shortcuts.Shortcut currentShortcut = shortcuts.get(i);
            if (!currentShortcut.isIgnoreCommands() && message.startsWith("/")) continue;

            if (message.contains(currentShortcut.getShortcut()))
            {
                message = message.replaceAll(currentShortcut.getShortcut(),currentShortcut.getReplacement());
                autoSend = currentShortcut.isAutoSend();
                shortcutMatch = true;
                i = -1; // For nested shortcuts.
            }
        }
        if(shortcutMatch)
        {
            event.setCanceled(true);
            if(autoSend)
            {
                MessageBus.sendFromClient(message);
                VMChat.getVars().getClient().gui.getChat().addRecentChat(event.getMessage());
            }
            else new ScreenHelper(new ChatScreen(message));

        }
    }
}
