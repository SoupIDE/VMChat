package de.soup.vmchat.util;

import de.soup.vmchat.gui.chat.renderer.SecondChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class Variables {

    private final SecondChat secondChat = new SecondChat();

    public Minecraft getClient(){return Minecraft.getInstance();}
    public ClientPlayerEntity getClientPlayer(){return getClient().player;}
    public SecondChat getSecondChat(){ return this.secondChat; }
}
