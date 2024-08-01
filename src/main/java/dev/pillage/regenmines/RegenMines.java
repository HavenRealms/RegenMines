package dev.pillage.regenmines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Map;

public final class RegenMines extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		Common.log("RegenMines has been enabled!");
		this.saveDefaultConfig();
	}

	@Override
	protected void onReloadablesStart() {
		reloadConfig();
		SequenceManager.init();
		SequenceManager.scanRegions();
	}

	@Override
	protected void onPluginPreReload() {
		Bukkit.getScheduler().cancelTasks(this); // Stop all tasks so we can reset the mines
		SequenceManager.resetSequences(); // Prepares it for scanning
	}

	@Override
	protected void onPluginStop() {
		Bukkit.getScheduler().cancelTasks(this);
		SequenceManager.resetSequences();
	}

	public static RegenMines getInstance() {
		return (RegenMines) SimplePlugin.getInstance();
	}
}
