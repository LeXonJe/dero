package net.lexonje.dero.config;

public class ConfigManager {

    private PluginConfig pluginConfig;
    private PlayerConfig playerConfig;
    private TeamConfig teamConfig;

    public ConfigManager() {
        this.pluginConfig = new PluginConfig("plugins/Dero/config.yml");
        this.playerConfig = new PlayerConfig("plugins/Dero/players.yml");
        this.teamConfig = new TeamConfig("plugins/Dero/teams.yml");
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public TeamConfig getTeamConfig() {
        return teamConfig;
    }
}