package de.soup.vmchat.event;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.event.events.CommandEvent;
import de.soup.vmchat.event.events.FilterEvent;
import de.soup.vmchat.event.events.MacroEvent;
import de.soup.vmchat.util.Keyboard;

public class EventManager {

    public static void init()
    {
        VMChat.EVENT_BUS.register(new CommandEvent());
        VMChat.EVENT_BUS.register(new FilterEvent());
        VMChat.EVENT_BUS.register(new Keyboard());
        VMChat.EVENT_BUS.register(new MacroEvent());
    }
}
