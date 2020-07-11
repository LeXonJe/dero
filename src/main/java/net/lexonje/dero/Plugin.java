package net.lexonje.dero;

import net.lexonje.dero.commands.AlertCommand;
import net.lexonje.dero.commands.HelpCommand;
import net.lexonje.dero.commands.InfoCommand;
import net.lexonje.dero.commands.MessageCommand;
import net.lexonje.dero.game.GameSystem;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    public static final String prefix = "§4§lDERO";
    public static final String version = "2.1.1";

    private GameSystem system;

    @Override
    public void onEnable() {
        registerCommands();
        this.system = new GameSystem(this);
    }

    @Override
    public void onDisable() {
        system.onDisable();
    }

    private void registerCommands() {
        getCommand("alert").setExecutor(new AlertCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("info").setExecutor(new InfoCommand());
        getCommand("msg").setExecutor(new MessageCommand());
    }
}
