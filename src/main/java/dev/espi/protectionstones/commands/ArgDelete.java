package dev.espi.protectionstones.commands;

import dev.espi.protectionstones.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.*;

public class ArgDelete implements PSCommandArg{
 public List<String> getNames(){return Collections.singletonList("delete");}
 public boolean allowNonPlayersToExecute(){return false;}
 public List<String> getPermissionsToExecute(){return Collections.singletonList("protectionstones.delete");}
 public HashMap<String,Boolean> getRegisteredFlags(){return null;}
 public boolean executeArgument(CommandSender s,String[] args,HashMap<String,String> flags){
  Player p=(Player)s;
  if(args.length!=2){p.sendMessage("§cИспользование: /ps delete [номер]");return true;}
  try {
   int id=Integer.parseInt(args[1]);
   String regionId=RegionDataManager.findRegionById(id);
   if(regionId==null){p.sendMessage("§cРегион с номером "+id+" не найден");return true;}
   com.sk89q.worldguard.protection.managers.RegionManager rm = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().get(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(p.getWorld()));
   if(rm!=null){
    rm.removeRegion(regionId);
    rm.save();
   }
   RegionDataManager.remove(regionId);
   p.sendMessage("§aРегион №"+id+" удалён");
  } catch(Exception e){p.sendMessage("§cНеверный номер региона");}
  return true;
 }
 public List<String> tabComplete(CommandSender s,String a,String[] args){return null;}
}
