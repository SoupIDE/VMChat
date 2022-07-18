package de.soup.vmchat.command.commands;

import de.soup.vmchat.command.Command;
import de.soup.vmchat.command.CommandManager;
import de.soup.vmchat.gui.chat.settings.ChatSettingsGUI;
import de.soup.vmchat.util.ScreenHelper;
import net.minecraft.util.text.StringTextComponent;

@Command.Declaration(name = "ChatSettings",syntax = "settings",description = "Opens 2ndChat-Settings-GUI.")
public class ChatSettingsCommand extends Command{
    @Override
    public void run(String[] args) {
        if(args.length == 0) new ScreenHelper(new ChatSettingsGUI(new StringTextComponent("settings")));
        else CommandManager.throwSyntaxHelp(args,this," ");
    }
}
