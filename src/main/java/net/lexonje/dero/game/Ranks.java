package net.lexonje.dero.game;

import org.bukkit.entity.Player;

public enum Ranks {

    ADMIN("§4§lAdmin", 2),
    PLAYER("§6§lSpieler", 1),
    GUEST("§7§lGast", 0);

    private String title;
    private int value;

    Ranks(String title, int value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public int getValue() {
        return value;
    }

    public static boolean isRankOrHigher(Player player, Ranks ranks) {
        return getRank(player).value >= ranks.getValue();
    }

    public static Ranks getRank(Player player) {
        if (player.hasPermission("dero.title.admin")) {
                return ADMIN;
            } else if (player.hasPermission("dero.title.player"))  {
                return PLAYER;
            } else {
                return GUEST;
        }
    }
}
