package de.soup.vmchat.event.events;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.command.CommandManager;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;

public class CommandEvent {

    @SubscribeEvent
    public void onChat(ClientChatEvent event)
    {
        String message = event.getMessage();
        if(message.toLowerCase().startsWith(CommandManager.COMMAND_PREFIX))
        {
            event.setCanceled(true);
            VMChat.getVars().getClient().gui.getChat().addRecentChat(event.getMessage());
            String[] args = message.split(" ");
            CommandManager.callCommand(Arrays.copyOfRange(args,1,args.length));
        }
    }
}
