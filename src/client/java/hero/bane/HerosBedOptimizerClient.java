package hero.bane;

import hero.bane.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.state.property.Properties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class HerosBedOptimizerClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// Set render layer for the ghost bed block to be translucent
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GHOST_BED, RenderLayer.getTranslucent());

		// Register event for right-clicking a bed
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world.isClient()) {
				BlockPos pos = hitResult.getBlockPos();
				BlockState clickedState = world.getBlockState(pos);
				Block clickedBlock = clickedState.getBlock();

				if (clickedBlock instanceof BedBlock) {
					boolean isSingleplayer = MinecraftClient.getInstance().isInSingleplayer();

					if (!player.isSneaking() && !isSingleplayer) {

						replaceBedParts(world, pos, clickedState);
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.PASS;
		});
	}

	private void replaceBedParts(World world, BlockPos clickedPos, BlockState clickedState) {
		BedPart part = clickedState.get(BedBlock.PART);
		Direction facing = clickedState.get(Properties.HORIZONTAL_FACING);
		BlockPos oppositePos;
		BedPart oppositePart;

		if (part == BedPart.HEAD) {
			oppositePos = clickedPos.offset(facing.getOpposite());
			oppositePart = BedPart.FOOT;
		} else {
			oppositePos = clickedPos.offset(facing);
			oppositePart = BedPart.HEAD;
		}

		BlockState oppositeState = world.getBlockState(oppositePos);
		Block oppositeBlock = oppositeState.getBlock();

		Block replacementBlock = ModBlocks.GHOST_BED;
		BlockState ghostBed = replacementBlock.getDefaultState()
				.with(Properties.HORIZONTAL_FACING, facing)
				.with(BedBlock.PART, part)
				.with(Properties.OCCUPIED, clickedState.get(Properties.OCCUPIED));

		world.setBlockState(clickedPos, ghostBed, Block.NOTIFY_ALL);

		if (oppositeBlock instanceof BedBlock) {

			BlockState oppositeGhostBed = replacementBlock.getDefaultState()
					.with(Properties.HORIZONTAL_FACING, facing)
					.with(BedBlock.PART, oppositePart)
					.with(Properties.OCCUPIED, oppositeState.get(Properties.OCCUPIED));

			world.setBlockState(oppositePos, oppositeGhostBed, Block.NOTIFY_ALL);
		}
	}
}
