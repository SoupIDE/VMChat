package de.soup.vmchat.config.configs.general;

import de.soup.vmchat.config.Config;
import de.soup.vmchat.config.configs.general.elements.GeneralSettings;

import java.io.File;

public class GeneralConfig extends Config<GeneralSettings> {
    public GeneralConfig(File parent) {
        super(new File(parent,"config.json"),GeneralSettings.class);
        save();
    }
}
