package com.kyle.anywhereweather;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Vanilla Channeling only strikes lightning when the world is thundering
 * (and the hit location can see open sky). This listener manually strikes
 * lightning on trident impact whenever the item has Channeling, regardless
 * of current weather, as long as vanilla wouldn't already be about to do it.
 */
public class ChannelingListener implements Listener {

    private final AnywhereWeather plugin;

    public ChannelingListener(AnywhereWeather plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTridentHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        ItemStack item = trident.getItemStack();
        if (item == null || !item.containsEnchantment(Enchantment.CHANNELING)) {
            return;
        }

        if (trident.getShooter() instanceof Player shooter
                && !shooter.hasPermission("anywhereweather.channeling")) {
            return;
        }

        World world = trident.getWorld();

        // If it's already thundering, vanilla will (usually) handle the strike itself.
        // We only step in when vanilla wouldn't naturally trigger it.
        if (world.isThundering()) {
            return;
        }

        Location strikeLocation;
        Entity hitEntity = event.getHitEntity();
        if (hitEntity != null) {
            strikeLocation = hitEntity.getLocation();
        } else if (event.getHitBlock() != null) {
            strikeLocation = event.getHitBlock().getLocation().add(0.5, 1.0, 0.5);
        } else {
            strikeLocation = trident.getLocation();
        }

        world.strikeLightning(strikeLocation);
    }
}
