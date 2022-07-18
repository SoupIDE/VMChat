package de.soup.vmchat.config.configs.filter;

import de.soup.vmchat.config.Config;
import de.soup.vmchat.config.configs.filter.elements.ChatFilters;

import java.io.File;

public class ChatFilterConfig extends Config<ChatFilters> {

    public ChatFilterConfig(File parent) {
        super(new File(parent,"filters.json"), ChatFilters.class);
        save();
    }
}
