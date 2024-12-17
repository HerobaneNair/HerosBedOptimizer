package hero.bane.block;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block GHOST_BED = registerBlock(
            new GhostBedBlock()
    );

    private static Block registerBlock(Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of("herosbedoptimizer", "ghost_bed"), block);
    }
}
