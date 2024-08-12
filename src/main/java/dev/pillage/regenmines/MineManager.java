package dev.pillage.regenmines;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class MineManager {

	private final RegenMines plugin = (RegenMines) RegenMines.getInstance();

	private final Map<World, List<ProtectedRegion>> regions = new HashMap<>(); // stores the regions from config and their respective world

	private final Map<Location, Material> originalBlocks = new HashMap<>(); // stores the original blocks of a lcoation

	private final List<MineBlock> mineBlocks = new ArrayList<>(); // stores the mineblocks from config

	private final Map<Location, Long> resetTimes = new HashMap<>(); // stores the time when the block should be reset to the original blocks, found in originalBlocks

	public void init() {
		reloadRegions();
		scanInitialStates();
		getMineBlocks();

		Bukkit.getScheduler().runTaskTimer(plugin, MineManager::checkAllResetTimes, 0L, 20L);
	}

	private void reloadRegions() {
		List<String> regions = plugin.getConfig().getStringList("regions");
		RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();

		for (World world : plugin.getServer().getWorlds()) {
			RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(world));

			for (ProtectedRegion region : regionManager.getRegions().values()) {
				if (regions.contains(region.getId())) {
					MineManager.regions.computeIfAbsent(world, k -> new ArrayList<>()).add(region);
				}
			}
		}
	}

	public void scanInitialStates() {
		if (regions.isEmpty()) return;

		for (Map.Entry<World, List<ProtectedRegion>> entry : regions.entrySet()) {
			World world = entry.getKey();
			List<ProtectedRegion> regionList = entry.getValue();

			for (ProtectedRegion region : regionList) {
				for (Block block : getBlocks(region, world)) {
					originalBlocks.put(block.getLocation(), block.getType());
				}
			}
		}
	}

	private void getMineBlocks() {
		FileConfiguration config = RegenMines.getInstance().getConfig();
		MemorySection section = (MemorySection) config.get("blocks");

		for (String key : section.getKeys(false)) {
			MemorySection blockSection = (MemorySection) section.get(key);
			String replacementMaterial = blockSection.getString("replace");
			int delay = blockSection.getInt("delay");

			MineBlock mineBlock = new MineBlock(Material.valueOf(key.toUpperCase()),
					Material.valueOf(replacementMaterial.toUpperCase()),
					delay);
			mineBlocks.add(mineBlock);
		}
	}

	public void reset() {
		for (Map.Entry<Location, Material> entry : originalBlocks.entrySet()) {
			Location location = entry.getKey();
			Material material = entry.getValue();

			location.getBlock().setType(material);
		}
	}

	private @NotNull Iterable<Block> getBlocks(@NotNull ProtectedRegion region, World world) {
		List<Block> blocks = new ArrayList<>();

		Location min = BukkitAdapter.adapt(world, region.getMinimumPoint());
		Location max = BukkitAdapter.adapt(world, region.getMaximumPoint());

		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Block block = world.getBlockAt(x, y, z);

					if (region.contains(BukkitAdapter.asBlockVector(block.getLocation()))) {
						blocks.add(block);
					}
				}
			}
		}
		return blocks;
	}

	public List<ProtectedRegion> getRegions(World world) {
		return regions.get(world);
	}

	public Material getReplacementMaterial(Material material) {
		for (MineBlock mineBlock : mineBlocks) {
			if (mineBlock.getOriginalMaterial() == material) {
				return mineBlock.getCurrentMaterial();
			}
		}
		return null;
	}

	public void recordBlockBreak(Location location, Material material) {
		MineBlock mineBlock = null;

		for (MineBlock block : mineBlocks) {
			if (block.getOriginalMaterial() == material) {
				mineBlock = block;
				break;
			}
		}

		if (mineBlock == null) return;

		if (resetTimes.containsKey(location)) {
			Long time = resetTimes.get(location);
			resetTimes.put(location, time + (mineBlock.getDelaySeconds() * 1000L));
		} else {
			resetTimes.put(location, System.currentTimeMillis() + (mineBlock.getDelaySeconds() * 1000L));
		}
	}

	private void checkAllResetTimes() {
		List<Location> toRemove = new ArrayList<>();
		for (Map.Entry<Location, Long> entry : resetTimes.entrySet()) {
			Location location = entry.getKey();
			Long time = entry.getValue();

			long timeLeft = time - System.currentTimeMillis();

			if (timeLeft <= 0) {
				Material material = originalBlocks.get(location);
				location.getBlock().setType(material);
				toRemove.add(location);
			}
		}

		for (Location location : toRemove) {
			resetTimes.remove(location);
		}
	}
}

