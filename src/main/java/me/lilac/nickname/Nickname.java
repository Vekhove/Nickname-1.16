package me.lilac.nickname;

import me.lilac.nickname.utils.HexUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Nickname extends JavaPlugin implements CommandExecutor, Listener {

    private boolean playerlist = false;

    @Override
    public void onEnable() {
        getCommand("nick").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        playerlist = getConfig().getBoolean("show-in-playerlist");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HexUtils.colorify("&cThe console cannot send this command!"));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(HexUtils.colorify("&cPlease enter a nickname."));
            return false;
        }

        Player player = (Player) sender;
        String nickname = HexUtils.colorify(args[0]);

        if (nickname.equalsIgnoreCase("off")) {
            player.setDisplayName(player.getName());
            if (playerlist) player.setPlayerListName(player.getName());
        } else {
            player.setDisplayName(nickname);
            if (playerlist) player.setPlayerListName(nickname);
        }

        player.sendMessage(HexUtils.colorify("&7Your nickname is now: &f"
                + (nickname.equalsIgnoreCase("off") ? player.getName() : nickname)));

        getConfig().set("nicknames." + player.getUniqueId().toString(), (nickname.equalsIgnoreCase("off") ? null : nickname));
        saveConfig();
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!getConfig().contains("nicknames." + player.getUniqueId().toString())) return;
        String nickname = HexUtils.colorify(getConfig().getString("nicknames." + player.getUniqueId().toString()));
        player.setDisplayName(nickname);
        if (playerlist) player.setPlayerListName(nickname);
    }
}
