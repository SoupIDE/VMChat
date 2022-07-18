package de.soup.vmchat.command.commands;

import de.soup.vmchat.command.Command;
import de.soup.vmchat.command.CommandManager;
import de.soup.vmchat.gui.tools.macros.SelectMacroGUI;
import de.soup.vmchat.util.ScreenHelper;
import net.minecraft.util.text.StringTextComponent;

@Command.Declaration(name = "ChatMacro",syntax = "macro",description = "Opens Macro-GUI.")
public class ChatMacroCommand extends Command {
    @Override
    public void run(String[] args) {
        if(args.length == 0) new ScreenHelper(new SelectMacroGUI(new StringTextComponent("macro")));
        else CommandManager.throwSyntaxHelp(args,this," ");
    }
}
