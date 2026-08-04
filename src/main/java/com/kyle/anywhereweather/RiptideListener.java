package com.kyle.anywhereweather;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Vanilla Riptide only launches the player when they're wet (in water or rain).
 * Otherwise, right-clicking with a Riptide trident just throws it. This listener
 * intercepts that right-click and always performs the riptide launch instead,
 * regardless of location or weather.
 */
public class RiptideListener implements Listener {

    private final AnywhereWeather plugin;

    public RiptideListener(AnywhereWeather plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // avoid double-firing from the off-hand event
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.TRIDENT) {
            return;
        }

        int riptideLevel = item.getEnchantmentLevel(Enchantment.RIPTIDE);
        if (riptideLevel <= 0) {
            return;
        }

        if (!player.hasPermission("anywhereweather.riptide")) {
            return;
        }

        if (player.hasCooldown(Material.TRIDENT)) {
            return;
        }

        // Prevent the vanilla "throw the trident" behavior and do our own launch instead.
        event.setCancelled(true);

        Vector direction = player.getLocation().getDirection().normalize();
        double speed = 1.1 + (riptideLevel * 0.6);
        Vector velocity = direction.multiply(speed);

        player.setVelocity(velocity);
        player.setFallDistance(0f);

        player.getWorld().playSound(player.getLocation(),
                Sound.ITEM_TRIDENT_RIPTIDE_2, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.SPLASH,
                player.getLocation(), 20, 0.3, 0.1, 0.3, 0.05);

        player.setCooldown(Material.TRIDENT, 10);
    }
}
