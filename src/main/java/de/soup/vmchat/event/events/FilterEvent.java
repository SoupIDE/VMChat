package de.soup.vmchat.event.events;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.filter.elements.ChatFilters;
import de.soup.vmchat.config.configs.filter.elements.FilterType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.regex.Pattern;

public class FilterEvent {

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event)
    {
        for(ChatFilters.ChatFilter filter : VMChat.getFilterConfig().getSettings().getChatFilters())
        {
            if(matchesMessage(TextFormatting.stripFormatting(event.getMessage().getString()),filter.getPattern()))
            {
                if(filter.getType() == FilterType.IGNORE || filter.getType() == FilterType.SECOND_CHAT) event.setCanceled(true);
                if(filter.getType() == FilterType.SECOND_CHAT || filter.getType() == FilterType.COPY_SECOND_CHAT) VMChat.getVars().getSecondChat().addMessage(event.getMessage());
                break;
            }
        }
    }

    private boolean matchesMessage(String message, Pattern pattern) { return pattern.matcher(message).matches(); }
}
