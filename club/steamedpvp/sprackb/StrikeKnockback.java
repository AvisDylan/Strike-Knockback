package club.steamedpvp.sprackb;

import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.battlekit.BattleKit;
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

     // DISCLAIMER I MADE THIS IN LIKE 20 MINUTES I KNOW THE CODE IS BAD FEEL FREE TO IMPROVE IG???

     public static FileConfiguration config;
     public static Map<String, String> kitProfiles = new HashMap<>();
     public static boolean debug = false;

     public void onEnable() {
         getLogger().info("\n \n ------------- \n \n StrikePractice KnockBack \n \n Successfully Loaded \n \n Author - Pro_Nil/Avis \n \n ------------- ");

         Bukkit.getPluginManager().registerEvents(this, this);

         config = getConfig();

         config.options().copyDefaults(true);
         saveConfig();

         load();
     }


     public void onDisable() {
         getLogger().info("\n \n ------------- \n \n StrikePractice KnockBack \n \n Successfully Disabled \n \n Author - Pro_Nil/Avis \n \n -------------");

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
         String kbcommand = config.getString("knockback-command")
                 .replace("{kitkb}", kbProfile)
                 .replace("{player}", player.getName());

         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), kbcommand);

         if(debug)
             Bukkit.broadcastMessage("Set " + player.getName() + "'s kb profile to " + kbProfile);
     }

     @Override
     public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if(command.getName().equalsIgnoreCase("strikekb") && sender instanceof Player && sender.hasPermission("strikekb.admin")){
             switch(args[0]){
                 case "setkb":
                     boolean foundKit = false;

                     for(BattleKit kit : StrikePractice.getAPI().getKits()){
                         if(kit.getName().toLowerCase().equalsIgnoreCase(args[1])){
                             foundKit = true;
                             break;
                         }
                     }

                     if(foundKit){
                         kitProfiles.put(args[1].toLowerCase(), args[2].toLowerCase());
                         sender.sendMessage("Set " + args[1] + "'s kb to " + args[2]);
                     }else
                         sender.sendMessage("Kit " + args[1] + " doesn't exist");

                     break;
                 case "debug":
                     debug = !debug;

                     sender.sendMessage(debug ? "Enabled" : "Disabled" + " debug mode");

                     break;
                 case "save":
                     save();

                     break;
                 default:
                     sender.sendMessage("Subcommand not found");
             }
         }
         return true;
     }

     public void save(){
         config.set("kits", null);

         for(Map.Entry<String, String> entry : kitProfiles.entrySet()){
             config.set("kits." + entry.getKey(), entry.getValue());
         }

         saveConfig();
     }

     public void load(){
         if(config.contains("kits")){
             for(String kitName : config.getConfigurationSection("kits").getKeys(false)){
                 kitProfiles.put(kitName.toLowerCase(), config.getString("kits." + kitName).toLowerCase());
             }
         }
     }
 }