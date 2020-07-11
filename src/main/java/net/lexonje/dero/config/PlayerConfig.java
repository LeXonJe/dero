package net.lexonje.dero.config;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerConfig extends Config {

    public PlayerConfig(String path) {
        super(path);
        if (!exists())
            save();
    }

    public void controlPlayer(Player player) {
        if (!entryExist(player)) {
            set(player.getUniqueId() + ".coins", 0);
            set(player.getUniqueId() + ".dead", false);
            set(player.getUniqueId() + ".data.newuser", true);
            save();
        }
    }

    public boolean isDead(Player player) {
        return getBoolean(player.getUniqueId() + ".dead");
    }

    public void setCoins(Player player, int coins) {
        set(player.getUniqueId() + ".coins", coins);
    }

    public int getCoins(Player player) {
        return getInt(player.getUniqueId() + ".coins");
    }

    public Location getLastLocation(OfflinePlayer player) {
        if (entryExist(player))
            return ConfigUtilities.getLocationFromConfig(this, player.getUniqueId() + ".data.location");
        else
            return null;
    }

    public void login(Player player) {
        player.getInventory().clear();
        player.teleport(ConfigUtilities.getLocationFromConfig(this, player.getUniqueId() + ".data.location"));
        ConfigUtilities.giveInventoryFromConfig(player, this, player.getUniqueId() + ".data.inventory");
        player.setHealth(getDouble(player.getUniqueId() + ".data.hearts"));
        player.setFoodLevel(getInt(player.getUniqueId() + ".data.foodlevel"));
        player.setLevel(getInt(player.getUniqueId() + ".data.xplevel"));
        player.setExp((float) getDouble(player.getUniqueId() + ".data.xpbar"));
        player.addPotionEffects(ConfigUtilities.getEffectsFromConfig(this, player.getUniqueId() + ".data.effects"));
    }

    public void saveLogout(Player player) {
        ConfigUtilities.setLocationToConfig(player.getLocation(), this,
                player.getUniqueId() + ".data.location");
        set(player.getUniqueId() + ".data.hearts", player.getHealth());
        set(player.getUniqueId() + ".data.foodlevel", player.getFoodLevel());
        set(player.getUniqueId() + ".data.xplevel", player.getLevel());
        set(player.getUniqueId() + ".data.xpbar", player.getExp());
        ConfigUtilities.setEffectsToConfig(player.getActivePotionEffects(), this, player.getUniqueId() + ".data.effects");
        ConfigUtilities.setInventoryToConfig(player.getInventory(), this, player.getUniqueId() +
                ".data.inventory");
        save();
    }

    public void setSpectatorPosition(Player player) {
        ConfigUtilities.setLocationToConfig(player.getLocation(), this,
                player.getUniqueId() + ".data.spectator.location");
    }

    public Location getSpectatorLocation(Player player) {
        return ConfigUtilities.getLocationFromConfig(this, player.getUniqueId() + ".data.spectator.location");
    }

    public boolean entryExist(OfflinePlayer player) {
        return get(player.getUniqueId() + ".data.newuser") != null;
    }
}
