package de.soup.vmchat;

import de.soup.vmchat.command.CommandManager;
import de.soup.vmchat.config.configs.filter.ChatFilterConfig;
import de.soup.vmchat.config.configs.general.GeneralConfig;
import de.soup.vmchat.config.configs.macro.MacroConfig;
import de.soup.vmchat.config.configs.shortcut.ShortcutConfig;
import de.soup.vmchat.event.EventManager;
import de.soup.vmchat.util.Variables;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(Reference.MOD_ID)
public class VMChat
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final IEventBus EVENT_BUS = MinecraftForge.EVENT_BUS;

    private File mainDirectory;

    private static ChatFilterConfig filterConfig;
    private static MacroConfig macroConfig;
    private static GeneralConfig generalConfig;
    private static ShortcutConfig shortcutConfig;

    private static final Variables VARIABLES = new Variables();

    public VMChat() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void init(final FMLClientSetupEvent event) {
        CommandManager.init();
        LOGGER.info("Commands initialized!");
        EventManager.init();
        LOGGER.info("Initialized Events!");
        setupConfigs();
        LOGGER.info("Loaded Configs!");
    }

    private void setupConfigs()
    {
        mainDirectory = new File(VMChat.getVars().getClient().gameDirectory+"/config",Reference.MOD_ID);

        generalConfig = new GeneralConfig(mainDirectory);
        filterConfig = new ChatFilterConfig(mainDirectory);
        macroConfig = new MacroConfig(mainDirectory);
        shortcutConfig = new ShortcutConfig(mainDirectory);
    }

    public static Variables getVars(){return VARIABLES;}

    public static ChatFilterConfig getFilterConfig() { return filterConfig; }
    public static MacroConfig getMacroConfig(){ return macroConfig; }
    public static GeneralConfig getGeneralConfig() { return generalConfig; }
    public static ShortcutConfig getShortcutConfig(){ return shortcutConfig; }
}
