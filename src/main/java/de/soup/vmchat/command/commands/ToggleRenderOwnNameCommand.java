package de.soup.vmchat.command.commands;

import de.soup.vmchat.VMChat;
import de.soup.vmchat.command.Command;
import de.soup.vmchat.command.CommandManager;

@Command.Declaration(name="ToggleOwnName",syntax = "togglename",description = "Toggle Own Name Rendering.")
public class ToggleRenderOwnNameCommand extends Command{
    @Override
    public void run(String[] args) {
        boolean renderName = VMChat.getGeneralConfig().getSettings().renderOwnName;
        if(args.length == 0) VMChat.getGeneralConfig().getSettings().renderOwnName = !renderName;
        else CommandManager.throwSyntaxHelp(args,this," ");
    }
}
