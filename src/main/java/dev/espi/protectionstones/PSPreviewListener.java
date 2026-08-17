package dev.espi.protectionstones;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Particle;
import java.util.*;

public class PSPreviewListener implements Listener {

    private final Map<UUID, Location> last = new HashMap<>();
    private final Map<UUID, Material> items = new HashMap<>();

    public PSPreviewListener() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    update(p);
                }
            }
        }.runTaskTimer(ProtectionStones.getInstance(), 0L, 5L);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        last.remove(e.getPlayer().getUniqueId());
        items.remove(e.getPlayer().getUniqueId());
    }

    private void update(Player p) {
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand == null || hand.getType() == Material.AIR) {
            last.remove(p.getUniqueId());
            return;
        }

        // Preview only for protection stone items registered in PS
        PSProtectBlock options = ProtectionStones.getBlockOptions(hand);
        if (options == null) {
            last.remove(p.getUniqueId());
            items.remove(p.getUniqueId());
            return;
        }

        Location center = p.getLocation().getBlock().getLocation();
        Material type = hand.getType();

        // ProtectionStone preview size from block configuration
        int radius = options.xRadius;

        Location old = last.get(p.getUniqueId());
        if (old != null && old.equals(center) && items.get(p.getUniqueId()) == type) {
            draw(center, radius);
            return;
        }

        last.put(p.getUniqueId(), center);
        items.put(p.getUniqueId(), type);
        draw(center, radius);
    }

    private void draw(Location c, int r) {
        World w = c.getWorld();
        int y = c.getBlockY() + 1;

        for (int x = -r; x <= r; x++) {
            particle(w, c.getBlockX()+x, y, c.getBlockZ()-r);
            particle(w, c.getBlockX()+x, y, c.getBlockZ()+r);
        }

        for (int z = -r; z <= r; z++) {
            particle(w, c.getBlockX()-r, y, c.getBlockZ()+z);
            particle(w, c.getBlockX()+r, y, c.getBlockZ()+z);
        }
    }

    private void particle(World w,int x,int y,int z) {
        w.spawnParticle(Particle.HAPPY_VILLAGER,
                new Location(w,x+0.5,y,z+0.5),
                1,0,0,0,0);
    }
}
