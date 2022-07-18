package de.soup.vmchat.config.configs.filter.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChatFilters {

    private List<ChatFilter> filters = new ArrayList<>();
    public List<ChatFilter> getChatFilters() { return this.filters; }
    public boolean isFilterSaved(String name){return getChatFilters().stream().anyMatch(filter->filter.getName().equalsIgnoreCase(name));}

    public static class ChatFilter {
        private String name;
        private String pattern;
        private FilterType type;

        public ChatFilter(String name, String pattern, FilterType type) {
            this.name = name;
            this.pattern = pattern;
            this.type = type;
        }

        public Pattern getPattern() { return parsePattern(this.pattern); }
        public String getPatternUnformatted(){ return this.pattern;}
        public String getName(){ return this.name; }
        public FilterType getType(){ return this.type; }

        public void setPattern(String pattern){ this.pattern = pattern; }
        public void setName(String name){ this.name = name; }
        public void setType(FilterType type){ this.type = type; }

        private Pattern parsePattern(String pattern){
            pattern = pattern.replaceAll("(?<!\\\\)(\\\\\\\\)+(?!\\\\)", "*")
                    .replaceAll("(?<!\\\\)[?]*[*][*?]+", "*")
                    .replaceAll("(?<!\\\\)([|\\[\\]{}(),.^$+-])", "\\\\$1")
                    .replaceAll("(?<!\\\\)[?]", "(.?)")
                    .replaceAll("(?<!\\\\)[*]", "(.*)");

            return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        }
    }
}

