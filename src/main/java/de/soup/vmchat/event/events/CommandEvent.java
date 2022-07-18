package de.soup.vmchat.event.events;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.command.CommandManager;
import de.soup.vmchat.util.CancelResponse;
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
            String[] args = message.split(" ");
            CommandManager.callCommand(Arrays.copyOfRange(args,1,args.length));
            new CancelResponse((VMChat.getVars().getClient().getCurrentServer() == null) ? 2 : 1);
        }
    }
}
