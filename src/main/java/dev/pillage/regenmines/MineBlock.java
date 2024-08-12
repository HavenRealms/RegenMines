package dev.pillage.regenmines;

import lombok.Data;
import org.bukkit.Material;

@Data
public class MineBlock {
	private final Material originalMaterial;
	private final Material currentMaterial;
	private final int delaySeconds;
}
