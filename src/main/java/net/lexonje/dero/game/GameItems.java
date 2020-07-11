package net.lexonje.dero.game;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class GameItems {

    ///NAVIGATOR///
    public static ItemStack navigator = new ItemBuilder(Material.COMPASS)
            .setName("§6§lNavigator §7{§eRechtsklick§7}")
            .setLore("§fTeleportiert dich zu Orten.")
            .build();

    public static ItemStack spawnItem = new ItemBuilder(Material.BEACON)
            .setName("§b§lSpawn")
            .setLore("§7Teleportiert dich zum Spawn.")
            .build();

    public static ItemStack berndItem = new ItemBuilder(Material.COOKED_BEEF)
            .setName("§e§lBernd")
            .setLore("§c§oIN ARBEIT!")
            .build();

    ///JOINITEM///
    public static ItemStack joinItem = new ItemBuilder(Material.FEATHER)
            .setName("§f§lMinecraft §4§lDERO §7{§eRechtsklick§7}")
            .setLore("§fBetritt die Welt.")
            .setMagic(true)
            .build();

    ///PROFILE///
    public static ItemStack profilItem = new ItemBuilder(Material.BOOK)
            .setName("§6§lProfil §7{§eRechtsklick§7}")
            .setLore("§fEinstellungen und vieles mehr.")
            .build();

    public static ItemStack coinsItem = new ItemBuilder(Material.GOLD_INGOT)
            .setName("§e§lMünzenverwaltung")
            .setLore("§fVerwalte deine Münzen.")
            .build();

    public static ItemStack transactionItem = new ItemBuilder(Material.PAPER)
            .setName("§3§lMünzen versenden")
            .setLore("§fGebe anderen Spielern ein paar Münzen.")
            .build();

    public static ItemStack teamItem = new ItemBuilder(Material.IRON_HELMET)
            .setName("§b§lTeam")
            .setLore("§fVerwalte dein Team.")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .build();

    public static ItemStack createTeamItem = new ItemBuilder(Material.DIAMOND)
            .setName("§3§lNeues Team erstellen.")
            .setLore("§fErstelle dein eigenes Team.")
            .setMagic(true)
            .build();

    public static ItemStack joinTeamItem = new ItemBuilder(Material.ENDER_EYE)
            .setName("§2§lTeam beitreten")
            .setLore("§fTrete einem Team bei.")
            .build();

    public static ItemStack listTeamItem = new ItemBuilder(Material.KNOWLEDGE_BOOK)
            .setName("§3§lTeamsübersicht")
            .setLore("§fListet alle Teams auf.")
            .build();

    public static ItemStack noTeamsItem = new ItemBuilder(Material.BARRIER)
            .setName("§c§lEs wurden keine Teams gefunden!")
            .setLore("§fEs wird Zeit, ein eigenes zu gründen.")
            .build();

    public static ItemStack settingsTeamItem = new ItemBuilder(Material.CLOCK)
            .setName("§5§lTeameinstellungen")
            .setLore("§fEinstellungen des Teams verwalten.")
            .build();

    public static ItemStack acceptPendingItem = new ItemBuilder(Material.GREEN_CONCRETE)
            .setName("§2§lAnnehmen")
            .setLore("§fSpieleranfrage akzeptieren.")
            .build();

    public static ItemStack deletePendingItem = new ItemBuilder(Material.RED_CONCRETE)
            .setName("§c§lAblehnen")
            .setLore("§fSpieleranfrage löschen.")
            .build();

    public static ItemStack noPendingItem = new ItemBuilder(Material.BARRIER)
            .setName("§c§lZurzeit gibt es keine Spieleranfragen.")
            .build();

    public static ItemStack leavePendingItem = new ItemBuilder(Material.RED_DYE)
            .setName("§c§lAnfrage löschen")
            .setLore("§fZieht deine Anfrage zurück.")
            .build();

    public static ItemStack quitTeamItem = new ItemBuilder(Material.FIRE_CORAL)
            .setName("§c§lTeam verlassen")
            .setLore("§fVerlasse das Team.")
            .build();

    public static ItemStack deleteTeamItem = new ItemBuilder(Material.FIRE_CORAL)
            .setName("§c§lTeam löschen")
            .setLore("§fLöscht dein Team.")
            .build();

    public static ItemStack settingsItem = new ItemBuilder(Material.REDSTONE_TORCH)
            .setName("§4§lEinstellungen")
            .setLore("§fEinstellungen anpassen.")
            .build();

    //SPECTATOR//
    public static ItemStack teamTeleporterItem = new ItemBuilder(Material.HEART_OF_THE_SEA)
            .setName("§6§lSpielerteleporter §7{§eRechtsklick§7}")
            .setLore("§fTeleportiert dich zu Spielern aus deinem Team.")
            .build();

    public static ItemStack noOthersTeamItem = new ItemBuilder(Material.BARRIER)
            .setName("§c§lZurzeit sind keine andere Spieler von deinem Team online.")
            .build();

    public static ItemStack noTeamItem = new ItemBuilder(Material.BARRIER)
            .setName("§c§lDu bist in keinem Team.")
            .build();

    //ADMINSPECTATOR//
    public static ItemStack adminToolsItem = new ItemBuilder(Material.MAGMA_CREAM)
            .setName("§b§lSpectatortools §7{§eRechtsklick§7}")
            .setLore("§fPasst die Spectatoreinstellungen an.")
            .build();

    //ADMINSPEC PANEL//
    public static ItemStack visibleOnItem = new ItemBuilder(Material.GLOWSTONE_DUST)
            .setName("§e§lSichtbar")
            .setLore("§fVerändert, ob du für andere Spieler sichtbar bist.")
            .build();

    public static ItemStack visibleOffItem = new ItemBuilder(Material.GUNPOWDER)
            .setName("§8§lUnsichtbar")
            .setLore("§fVerändert, ob du für andere Spieler sichtbar bist.")
            .build();

    public static ItemStack buildOnItem = new ItemBuilder(Material.LIME_DYE)
            .setName("§a§lInteraktiver Modus §7- §aAktiviert")
            .setLore("§fBestimmt, ob du bauen und abbauen kannst.")
            .build();

    public static ItemStack buildOffItem = new ItemBuilder(Material.GRAY_DYE)
            .setName("§8§lInteraktiver Modus §7- §cDeaktiviert")
            .setLore("§fBestimmt, ob du bauen und abbauen kannst.")
            .build();

    public static ItemStack pickupOnItem = new ItemBuilder(Material.HONEY_BOTTLE)
            .setName("§6§lItems aufheben §7- §aAktiviert")
            .setLore("§fBestimmt, ob du Items aufheben kannst.")
            .build();

    public static ItemStack pickupOffItem = new ItemBuilder(Material.GLASS_BOTTLE)
            .setName("§8§lItems aufheben §7- §cDeaktiviert")
            .setLore("§fBestimmt, ob du Items aufheben oder droppen kannst.")
            .build();

    public static ItemStack clearWeatherItem = new ItemBuilder(Material.PUFFERFISH_SPAWN_EGG)
            .setName("§6§lSonnenschein")
            .setLore("§fEntfernt Regen oder Gewitter.")
            .build();

    public static ItemStack rainWeatherItem = new ItemBuilder(Material.VEX_SPAWN_EGG)
            .setName("§3§lRegen")
            .setLore("§fAktiviert Regen.")
            .build();

    public static ItemStack thunderWeatherItem = new ItemBuilder(Material.PHANTOM_SPAWN_EGG)
            .setName("§9§lGewitter")
            .setLore("§fAktiviert Regenfall, Blitze und Donner.")
            .build();

    public static ItemStack morningTimeItem = new ItemBuilder(Material.OXEYE_DAISY)
            .setName("§f§lMorgen")
            .setLore("§fSetzt die Zeit auf 1000.")
            .build();

    public static ItemStack noonTimeItem = new ItemBuilder(Material.DANDELION)
            .setName("§e§lMittag")
            .setLore("§fSetzt die Zeit auf 6000.")
            .build();

    public static ItemStack afternoonTimeItem = new ItemBuilder(Material.BLUE_ORCHID)
            .setName("§b§lNachmittag")
            .setLore("§fSetzt die Zeit auf 9000.")
            .build();

    public static ItemStack sunshineTimeItem = new ItemBuilder(Material.CORNFLOWER)
            .setName("§9§lSonnenuntergang")
            .setLore("§fSetzt die Zeit auf 12400.")
            .build();

    public static ItemStack nightTimeItem = new ItemBuilder(Material.WITHER_ROSE)
            .setName("§1§lNacht")
            .setLore("§fSetzt die Zeit auf 16000.")
            .build();

    //OTHER//
    public static ItemStack comingSoon = new ItemBuilder(Material.OAK_SIGN)
            .setName("§c§lIn Arbeit!")
            .build();

    public static ItemStack backItem = new ItemBuilder(Material.EMERALD)
            .setName("§c§lZurück")
            .build();

    public static ItemStack blackItem = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
            .setName(" ")
            .build();

    public static ItemStack redItem = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
            .setName("§cNicht möglich!")
            .build();

    public static ItemStack agreeItem = new ItemBuilder(Material.GREEN_CONCRETE)
            .setName("§2§lBestätigen")
            .setLore("§fVorgang abschließen.")
            .build();

    public static ItemStack disagreeItem = new ItemBuilder(Material.RED_CONCRETE)
            .setName("§c§lAbbrechen")
            .setLore("§fVorgang abbrechen.")
            .build();

    public static ItemStack infoItem = new ItemBuilder(Material.OAK_SIGN)
            .setName("§f§lMinecraft §4§lDERO §fv" + Plugin.version)
            .setLore("", "§7● §eSoftware §fvon LeXonJe",
                    "§7● §fUnter der §eLeitung §fvon Flowig", "", "§fEin Projekt von §6Devastation")
            .build();
}