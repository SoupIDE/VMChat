package de.soup.vmchat.command.commands;

import de.soup.vmchat.command.Command;
import de.soup.vmchat.command.CommandManager;
import de.soup.vmchat.gui.tools.filter.SelectFilterGUI;
import de.soup.vmchat.util.ScreenHelper;
import net.minecraft.util.text.StringTextComponent;

@Command.Declaration(name = "ChatFilter",syntax = "filter",description = "Opens Filter-GUI.")
public class ChatFilterCommand extends Command {
    @Override
    public void run(String[] args) {
        if(args.length == 0) new ScreenHelper(new SelectFilterGUI(new StringTextComponent("filter")));
        else CommandManager.throwSyntaxHelp(args,this," ");
    }
}
