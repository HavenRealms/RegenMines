package dev.pillage.regenmines;

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
		if (MineManager.isBypassing(e.getPlayer())) return;

		MineSequence sequence = MineManager.getSequence(e.getBlock().getLocation());

		if (sequence != null) {
			Material nextMaterial = sequence.getNextMaterial(e.getBlock().getType());

			Location blockLocation = e.getBlock().getLocation();
			Bukkit.getScheduler().runTaskLater(RegenMines.getInstance(), () -> blockLocation.getBlock().setType(nextMaterial), 1L);

			if (nextMaterial == Material.BEDROCK) {
				double delay = sequence.getDelay();

				Bukkit.getScheduler().runTaskLater(RegenMines.getInstance(), () -> blockLocation.getBlock().setType(sequence.getFirstMaterial()), (long) (delay * 20));
			}
		}
	}
}
