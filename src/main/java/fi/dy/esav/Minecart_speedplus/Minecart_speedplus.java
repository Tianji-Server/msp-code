package fi.dy.esav.Minecart_speedplus;

import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Minecart_speedplus extends JavaPlugin {
    Logger log = Logger.getLogger("Minecraft");

    private final Minecart_speedplusVehicleListener VehicleListener = new Minecart_speedplusVehicleListener(this);

    private final Minecart_speedplusSignListener SignListener = new Minecart_speedplusSignListener(this);

    public TitleManagerAPI titleApi;

    static double speedmultiplier = 1.25D;

    boolean result;

    double multiplier;

    public static double getSpeedMultiplier() {
        return speedmultiplier;
    }

    public boolean setSpeedMultiplier(double multiplier) {
        if ((((0.0D < multiplier) ? 1 : 0) & ((multiplier <= 50.0D) ? 1 : 0)) != 0) {
            speedmultiplier = multiplier;
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        this.log.info(String.valueOf(getDescription().getName()) + " version " + getDescription().getVersion() + " started.");
        PluginManager pm = getServer().getPluginManager();
        this.titleApi = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
        pm.registerEvents(this.VehicleListener, this);
        pm.registerEvents(this.SignListener, this);
    }

    public void onDisable() {
        this.log.info(String.valueOf(getDescription().getName()) + " version " + getDescription().getVersion() + " stopped.");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("msp")) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (!player.hasPermission("msp.cmd")) {
                    player.sendMessage("You don't have the right to do that");
                    return true;
                }
            }
            try {
                this.multiplier = Double.parseDouble(args[0]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.DARK_BLUE + "Minecart_speed+: Multiplier should be a number");
                return false;
            }
            this.result = setSpeedMultiplier(this.multiplier);
            if (this.result) {
                sender.sendMessage(ChatColor.DARK_BLUE + "Minecart_speed+: Speed multiplier for new carts set to: " + this.multiplier);
                return true;
            }
            sender.sendMessage(ChatColor.DARK_BLUE + "minecart_speed+: Value must be non-zero and under 50");
            return true;
        }
        return false;
    }
}