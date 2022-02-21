 package me.pronil.sprackb;



import ga.strikepractice.events.KitSelectEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

 public final class StrikeKnockback
   extends JavaPlugin implements Listener {
Plugin plugin = this;
     public static FileConfiguration config;

     public void onEnable() {
         System.out.println("\n \n ------------- \n \n StrikePractice KnockBack \n \n Successfully Loaded \n \n Author - Pro_Nil \n \n ------------- ");
         Bukkit.getPluginManager().registerEvents(this, this);
         getConfig().options().copyDefaults(true);
         saveConfig();
     }


     public void onDisable() {
         System.out.println("\n \n ------------- \n \n StrikePractice KnockBack \n \n Successfully Disabled \n \n Author - Pro_Nil \n \n -------------");
     }

     @EventHandler
     public void onMatchStart(KitSelectEvent event) {

         Player player = event.getPlayer();
         String kit = event.getKit().getName();
         String kbcommand = this.getConfig().getString("knockback-command").replace("{kit}", kit).replace("{player}", player.getName());
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), kbcommand);

     }



 }