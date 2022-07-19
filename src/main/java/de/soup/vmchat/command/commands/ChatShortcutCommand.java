package de.soup.vmchat.command.commands;

import de.soup.vmchat.command.Command;
import de.soup.vmchat.command.CommandManager;
import de.soup.vmchat.gui.tools.shortcut.SelectShortcutGUI;
import de.soup.vmchat.util.ScreenHelper;
import net.minecraft.util.text.StringTextComponent;

@Command.Declaration(name="ChatShortcut",syntax = "shortcut",description = "Opens Shortcut-GUI")
public class ChatShortcutCommand extends Command {
    @Override
    public void run(String[] args) {
        if(args.length == 0) new ScreenHelper(new SelectShortcutGUI(new StringTextComponent("select")));
        else CommandManager.throwSyntaxHelp(args,this," ");
    }
}
