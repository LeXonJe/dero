package net.lexonje.dero.config;

public class PluginConfig extends Config {

    public PluginConfig(String path) {
        super(path);
        if (!exists()){
            save();
        }
    }
}
