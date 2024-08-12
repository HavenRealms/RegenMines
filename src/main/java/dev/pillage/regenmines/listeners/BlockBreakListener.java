package dev.pillage.regenmines.listeners;

import dev.pillage.regenmines.MineManager;
import dev.pillage.regenmines.RegenMines;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class BlockBreakListener implements Listener {
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e) {
		Location location = e.getBlock().getLocation();

		MineManager.getRegions(e.getBlock().getWorld()).forEach(region -> {
			if (region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
				Material material = e.getBlock().getType();
				Material next = MineManager.getReplacementMaterial(material);

				if (next != null) {
					MineManager.recordBlockBreak(location, material);

					Bukkit.getScheduler().runTaskLater(RegenMines.getInstance(), () -> {
						location.getBlock().setType(next);
					}, 1L);
				}
			}
		});
	}
}
