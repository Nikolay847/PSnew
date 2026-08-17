package dev.espi.protectionstones;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;

public class RegionDataManager {
    private static final Map<String,Integer> ids = new HashMap<>();
    private static int next = 1;
    private static File file;

    public static void init(){
        file = new File(ProtectionStones.getInstance().getDataFolder(), "regions-id.yml");
        if(!file.exists()) return;
        YamlConfiguration y=YamlConfiguration.loadConfiguration(file);
        next=y.getInt("next",1);
        for(String k:y.getKeys(false)) if(!k.equals("next")) ids.put(k,y.getInt(k));
    }
    public static int getOrCreate(String region){
        if(!ids.containsKey(region)){ids.put(region,next++); save();}
        return ids.get(region);
    }
    public static int get(String region){return ids.getOrDefault(region,-1);}
    public static String findRegionById(int id){ for (Map.Entry<String,Integer> e: ids.entrySet()) if(e.getValue()==id) return e.getKey(); return null; }
    public static void remove(String region){ids.remove(region);save();}
    private static void save(){
        if(file==null)return;
        YamlConfiguration y=new YamlConfiguration();
        y.set("next",next); ids.forEach(y::set);
        try{y.save(file);}catch(Exception ignored){}
    }
}
