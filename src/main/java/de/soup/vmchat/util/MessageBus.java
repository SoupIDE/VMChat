package de.soup.vmchat.util;

import de.soup.vmchat.VMChat;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

public class MessageBus {

    public static final String CHAT_PREFIX = "&8[&9P&bV&3C&8]";

    public static final String DEFAULT_SUCCESS_COLOR = "&a";
    public static final String DEFAULT_FAIL_COLOR = "&c";
    public static final String DEFAULT_HIGHLIGHT_COLOR = "&3";

    public static void sendToClient(String message,String colorTheme,boolean prefix)
    {
        VMChat.getVars().getClientPlayer()
                .sendMessage(new StringTextComponent((((prefix) ? parseTextFormattingString(CHAT_PREFIX+" ") : "") +parseTextFormattingString(colorTheme+message))), Util.NIL_UUID);
    }
    public static void sendToClient(StringTextComponent message,boolean prefix)
    {
        VMChat.getVars().getClientPlayer()
                .sendMessage(new StringTextComponent((prefix) ? parseTextFormattingString(CHAT_PREFIX+" ") : "").append(message), Util.NIL_UUID);
    }

    public static void sendFromClient(String message)
    {
        VMChat.getVars().getClientPlayer().chat(message);
    }

    public static String parseTextFormattingString(String tfsMsg){
        String[] colorCodes = {"&0","&1","&2","&3","&4","&5","&6","&7","&8","&9","&a","&b","&c","&d","&e","&f","&k","&l","&m","&n","&o","&r"};
        StringBuilder convertedText = new StringBuilder();
        String[] wordChars = tfsMsg.split("");

        for (int i = 0; i < tfsMsg.length()-1; i++) {
            String currentPair = wordChars[i] + wordChars[i + 1];

            if(i == tfsMsg.length()-2){
                convertedText.append(currentPair);
                break;
            }

            if (Arrays.asList(colorCodes).contains(currentPair.toLowerCase())) {
                convertedText.append(TextFormatting.getByCode(currentPair.charAt(1)));
                i++;
            } else {
                convertedText.append(currentPair.charAt(0));
            }

            if(i == tfsMsg.length()-2)convertedText.append(wordChars[i+1]);

        }
        return convertedText.toString();
    }
}
