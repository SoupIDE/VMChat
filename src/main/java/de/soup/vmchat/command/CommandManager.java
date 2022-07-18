package de.soup.vmchat.command;

import de.soup.vmchat.Reference;
import de.soup.vmchat.command.commands.ChatFilterCommand;
import de.soup.vmchat.command.commands.ChatMacroCommand;
import de.soup.vmchat.command.commands.ChatSettingsCommand;
import de.soup.vmchat.util.MessageBus;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.*;

public class CommandManager {

    public static final String COMMAND_PREFIX = "/vmc";

    private static final Set<Command> commandList = new HashSet<>();

    public static void init()
    {
        commandList.add(new ChatFilterCommand());
        commandList.add(new ChatSettingsCommand());
        commandList.add(new ChatMacroCommand());
    }

    public static void callCommand(String[] args)
    {
        if(args.length > 0)
        {
            String cmd = args[0];
            for(Command command : commandList)
            {
              if(command.getSyntax().split(" ")[1].equalsIgnoreCase(cmd))
              {
                  command.run(Arrays.copyOfRange(args,1,args.length));
                  return;
              }
            }
        }
        getHelp();
    }

    public static void throwSyntaxHelp(String[] args,Command command,String usage)
    {
        MessageBus.sendToClient(" ","&f",false);
        MessageBus.sendToClient("Syntax error from &e"+command.getSyntax()+" "+String.join(" ",args),MessageBus.DEFAULT_FAIL_COLOR,true);
        MessageBus.sendToClient("Usage: "+command.getSyntax()+" &e"+usage, MessageBus.DEFAULT_FAIL_COLOR,true);
    }

    private static void getHelp()
    {
        MessageBus.sendToClient(" ","&f",false);
        MessageBus.sendToClient("&m                                                           ","&8",false);
        MessageBus.sendToClient("&9&lV&b&lM&3&lChat&r&3 "+ Reference.MOD_VERSION +"&b by &3SoupDE","&f",false);
        MessageBus.sendToClient("&m                                                           ","&8",false);
        MessageBus.sendToClient("Help...","&7&o",false);


        CommandManager.commandList.forEach(command ->
        {
                StringTextComponent syntax = new StringTextComponent(MessageBus.parseTextFormattingString("&7&l↪&r &3"+command.getSyntax()));
                HoverEvent syntaxHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,new StringTextComponent(MessageBus.parseTextFormattingString("&bClick to suggest command &3"+command.getSyntax())));
                ClickEvent syntaxClick = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,command.getSyntax());
                syntax.setStyle(syntax.getStyle().withClickEvent(syntaxClick).withHoverEvent(syntaxHover));
                MessageBus.sendToClient((StringTextComponent) syntax.append(new StringTextComponent(MessageBus.parseTextFormattingString("&f - &b"+command.getDescription()))), false);
        });

        MessageBus.sendToClient(" ","&f",false);
        MessageBus.sendToClient("&m                                                           ","&8",false);
        MessageBus.sendToClient(" ","&f",false);
    }
}
