package hero.bane.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class GhostBedBlock extends Block {
    public static final EnumProperty<BedPart> PART = BedBlock.PART;

    // Define voxel shapes for each combination of BedPart and HORIZONTAL_FACING
    private static final VoxelShape NORTH_HEAD_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg1Shape(),
            createLeg3Shape()
    );
    private static final VoxelShape NORTH_FOOT_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg2Shape(),
            createLeg4Shape()
    );

    private static final VoxelShape SOUTH_HEAD_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg2Shape(),
            createLeg4Shape()
    );
    private static final VoxelShape SOUTH_FOOT_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg1Shape(),
            createLeg3Shape()
    );

    private static final VoxelShape EAST_HEAD_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg3Shape(),
            createLeg4Shape()
    );
    private static final VoxelShape EAST_FOOT_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg1Shape(),
            createLeg2Shape()
    );

    private static final VoxelShape WEST_HEAD_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg1Shape(),
            createLeg2Shape()
    );
    private static final VoxelShape WEST_FOOT_SHAPE = VoxelShapes.union(
            createTopShape(),
            createLeg3Shape(),
            createLeg4Shape()
    );

    private static VoxelShape createTopShape() {
        return Block.createCuboidShape(0.0, 3.0, 0.0, 16.0, 9.0, 16.0);
    }

    private static VoxelShape createLeg1Shape() {
        return Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 3.0, 3.0);
    }

    private static VoxelShape createLeg2Shape() {
        return Block.createCuboidShape(0.0, 0.0, 13.0, 3.0, 3.0, 16.0);
    }

    private static VoxelShape createLeg3Shape() {
        return Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 3.0, 3.0);
    }

    private static VoxelShape createLeg4Shape() {
        return Block.createCuboidShape(13.0, 0.0, 13.0, 16.0, 3.0, 16.0);
    }

    public GhostBedBlock() {
        super(AbstractBlock.Settings.create()
                .nonOpaque()
                .strength(-1.0f)
                .dropsNothing()
                .replaceable()
                .sounds(BlockSoundGroup.WOOL));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(PART, BedPart.HEAD)
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(Properties.OCCUPIED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, PART, Properties.OCCUPIED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(Properties.HORIZONTAL_FACING);
        BedPart part = state.get(PART);

        return switch (facing) {
            case NORTH -> part == BedPart.HEAD ? NORTH_HEAD_SHAPE : NORTH_FOOT_SHAPE;
            case SOUTH -> part == BedPart.HEAD ? SOUTH_HEAD_SHAPE : SOUTH_FOOT_SHAPE;
            case EAST -> part == BedPart.HEAD ? EAST_HEAD_SHAPE : EAST_FOOT_SHAPE;
            default -> part == BedPart.HEAD ? WEST_HEAD_SHAPE : WEST_FOOT_SHAPE;
        };
    }
}
