package dev.pillage.regenmines;

import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class RegenMines extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		Common.log("RegenMines has been enabled!");
		this.saveDefaultConfig();
	}

	@Override
	protected void onReloadablesStart() {
		reloadConfig();
		MineManager.init();
		MineManager.scanRegions();
	}

	@Override
	protected void onPluginPreReload() {
		Bukkit.getScheduler().cancelTasks(this); // Stop all tasks so we can reset the mines
		MineManager.resetSequences(); // Prepares it for scanning
	}

	@Override
	protected void onPluginStop() {
		Bukkit.getScheduler().cancelTasks(this);
		MineManager.resetSequences();
	}

	public static RegenMines getInstance() {
		return (RegenMines) SimplePlugin.getInstance();
	}
}
