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

import java.util.HashMap;
import java.util.Map;

 public final class StrikeKnockback extends JavaPlugin implements Listener {

     Plugin plugin = this;
     public static FileConfiguration config;
     public static Map<String, String> kitProfiles = new HashMap<>();
     public static boolean debug = false;

     public void onEnable() {
         System.out.println("\n \n ------------- \n \n StrikePractice KnockBack \n \n Successfully Loaded \n \n Author - Pro_Nil/Avis \n \n ------------- ");
         Bukkit.getPluginManager().registerEvents(this, this);
         getConfig().options().copyDefaults(true);
         saveConfig();
     }


     public void onDisable() {
         System.out.println("\n \n ------------- \n \n StrikePractice KnockBack \n \n Successfully Disabled \n \n Author - Pro_Nil/Avis \n \n -------------");
         save();
     }

     @EventHandler
     public void onMatchStart(KitSelectEvent event) {
         Player player = event.getPlayer();
         String kitName = event.getKit().getName();
         if(!kitProfiles.containsKey(kitName.toLowerCase())){
             Bukkit.broadcastMessage(kitName + " does not have a kb profile set!");
             return;
         }
         String kbProfile = kitProfiles.get(kitName.toLowerCase());
         String kbcommand = this.getConfig().getString("knockback-command").replace("{kitkb}", kbProfile).replace("{player}", player.getName());
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), kbcommand);
         if(debug)
             Bukkit.broadcastMessage("set " + player.getName() + "'s kb profile to " + kbProfile);
     }

     @Override
     public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if(command.getName().equalsIgnoreCase("strikekb") && sender instanceof Player && sender.hasPermission("strikekb.admin")){
             switch(args[0]){
                 case "setkb":
                     kitProfiles.put(args[1].toLowerCase(), args[2].toLowerCase());
                     System.out.println("set " + args[1] + "'s kb to " + args[2]);
                     break;
                 case "debug":
                     debug = !debug;
                     break;
                 case "save":
                     save();
             }
         }
         return true;
     }

     public void save(){

     }
 }