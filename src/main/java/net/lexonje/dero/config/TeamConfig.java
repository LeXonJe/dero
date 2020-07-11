package net.lexonje.dero.config;

import net.lexonje.dero.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamConfig extends Config {

    private ArrayList<Team> teams;

    public TeamConfig(String path) {
        super(path);
        teams = new ArrayList<>();
        if (!exists()) {
            save();
        } else {
            loadTeams();
        }
    }

    public Team getTeamByName(String name) {
        for (Team team : teams) {
            if (team.name.equals(name))
                return team;
        }
        return null;
    }

    public Team getTeamByPrefix(String prefix) {
        for (Team team : teams) {
            if (team.prefix.equals(prefix))
                return team;
        }
        return null;
    }

    public Team getTeamByOwner(UUID ownerid) {
        for (Team team : teams) {
            if (team.owner.equals(ownerid))
                return team;
        }
        return null;
    }

    public Team getTeamByMember(UUID member) {
        for (Team team : teams) {
            if (team.members.contains(member))
                return team;
        }
        return null;
    }

    public Team getTeamByPendingMember(UUID pendingMember) {
        for (Team team : teams) {
            if (team.pending.contains(pendingMember))
                return team;
        }
        return null;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void createTeam(String name, String prefix, UUID ownerid) {
        set("team." + name + ".prefix", prefix);
        set("team." + name + ".ownerid", ownerid.toString());
        ArrayList<String> members = new ArrayList<>();
        members.add(ownerid.toString());
        set("team." + name + ".members", members);
        set("team." + name + ".pending", new ArrayList<>());
        set("team." + name + ".settings.friendlyfire", false);
        save();
        registerTeam(name);
        teams.add(new Team(this, "team." + name));
    }

    public void registerTeam(String name) {
        List<String> names = getStringList("teamlist");
        names.add(name);
        set("teamlist", names);
        save();
    }

    public void removeTeam(String name) {
        List<String> names = getStringList("teamlist");
        names.remove(name);
        teams.remove(getTeamByName(name));
        set("teamlist", names);
        remove("team." + name);
        save();
    }

    private void loadTeams() {
        List<String> names = getStringList("teamlist");
        for (String name : names) {
            teams.add(new Team(this, "team." + name));
        }
    }

    public static class Team {

        private Config config;

        private String path;
        private String name;
        private String prefix;
        private UUID owner;
        private ArrayList<UUID> members;
        private ArrayList<String> membersAsString;
        private ArrayList<UUID> pending;
        private ArrayList<String> pendingAsString;

        private boolean friendlyFire;

        public Team(TeamConfig config, String path) {
            this.config = config;
            this.name = path.replaceFirst("team.", "");
            this.path = path;
            if (config.getString(path + ".prefix") != null) {
                this.prefix = config.getString(path + ".prefix");
                this.owner = UUID.fromString(config.getString(path + ".ownerid"));
                this.members = new ArrayList<>();
                this.membersAsString = new ArrayList<>();
                List<String> ids = config.getStringList(path + ".members");
                for (String id : ids) {
                    members.add(UUID.fromString(id));
                    membersAsString.add(id);
                }
                this.pending = new ArrayList<>();
                this.pendingAsString = new ArrayList<>();
                List<String> pendingIds = config.getStringList(path + ".pending");
                for (String id : pendingIds) {
                    pending.add(UUID.fromString(id));
                    pendingAsString.add(id);
                }
                this.friendlyFire = config.getBoolean(path + ".settings.friendlyfire");
            } else
                System.out.println("SOMETHING WENT TOTALLY WRONG WITH TEAM CONFIG");
        }

        public boolean isMember(Player player) {
            return this.members.contains(player.getUniqueId());
        }

        public void setName(String name) {
            this.config.remove(this.path);
            this.path = "team." + name;
            config.set(path + ".prefix", prefix);
            config.set(path + ".ownerid", owner);
            config.set(path + ".members", membersAsString);
            config.set(path + ".pending", pendingAsString);
            config.set(path + ".settings.friendlyfire", friendlyFire);
            config.save();
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
            config.set(path + ".prefix", prefix);
            config.save();
        }

        public void setOwner(UUID owner) {
            this.owner = owner;
            config.set(path + ".ownerid", owner);
            config.save();
        }

        public void setFriendlyFire(boolean friendlyFire) {
            this.friendlyFire = friendlyFire;
            config.set(path + ".settings.friendlyfire", friendlyFire);
            config.save();
        }

        public void addMember(UUID uuid) {
            this.members.add(uuid);
            this.membersAsString.add(uuid.toString());
            this.config.set(path + ".members", membersAsString);
            this.config.save();
            for (UUID id : members) {
                if (Bukkit.getPlayer(id) != null && !id.equals(owner))
                    Bukkit.getPlayer(id).sendMessage(Plugin.prefix + " §7| §fDer Spieler §3" + Bukkit.getOfflinePlayer(uuid)
                    .getName() + " §fist dem Team §2beigetreten§f.");
            }
        }

        public void addPending(UUID uuid) {
            this.pending.add(uuid);
            this.pendingAsString.add(uuid.toString());
            this.config.set(path + ".pending", pendingAsString);
            this.config.save();
        }

        public void removeMember(UUID uuid) {
            this.members.remove(uuid);
            this.membersAsString.remove(uuid.toString());
            this.config.set(path + ".members", membersAsString);
            this.config.save();
            for (UUID id : members) {
                if (Bukkit.getPlayer(id) != null)
                    Bukkit.getPlayer(id).sendMessage(Plugin.prefix + " §7| §fDer Spieler §3" + Bukkit.getOfflinePlayer(uuid)
                            .getName() + " §fhat das Team §cverlassen§f.");
            }
        }

        public void removePending(UUID uuid) {
            this.pending.remove(uuid);
            this.pendingAsString.remove(uuid.toString());
            this.config.set(path + ".pending", pendingAsString);
            this.config.save();
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public String getPrefix() {
            return prefix;
        }

        public UUID getOwnerId() {
            return owner;
        }

        public ArrayList<UUID> getMembers() {
            return members;
        }

        public ArrayList<UUID> getPendingList() {
            return pending;
        }

        public boolean isFriendlyFire() {
            return friendlyFire;
        }
    }
}
