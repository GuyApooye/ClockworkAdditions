package com.github.guyapooye.clockworkadditions.forge.blocks.copycats.wingalikes;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatStepBlock;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.valkyrienskies.clockwork.util.blocktype.ConnectedWingAlike;

import java.util.ArrayList;
import java.util.List;

public class CopycatWingalikeModel extends CopycatModel {

    public CopycatWingalikeModel(BakedModel originalModel) {
        super(originalModel);
    }


    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material,
                                              ModelData wrappedData, RenderType renderType) {
        Direction facing = state.getOptionalValue(ConnectedWingAlike.Companion.getFACING())
                .orElse(Direction.SOUTH);
        boolean upperHalf = state.getOptionalValue(CopycatStepBlock.HALF)
                .orElse(Half.BOTTOM) == Half.TOP;

        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData,renderType);
        int size = templateQuads.size();

        List<BakedQuad> quads = new ArrayList<>();

        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 normalScaled2 = normal.scale(0.875);
        Vec3 normalScaledN3 = normal.scale(-.75);

        // 4 Pieces
        for (boolean top : Iterate.trueAndFalse) {
            for (boolean front : Iterate.trueAndFalse) {

                AABB bb = new AABB(normalScaled2, new Vec3(1,1,1));
                if (Math.signum(normal.x) == -1 || Math.signum(normal.y) == -1 || Math.signum(normal.z) == -1) {
                    bb = new AABB(new Vec3(0,0,0), new Vec3(1,1,1).add(normalScaled2));
                }
                AABB bb1 = bb;
                Vec3 offset = normal.scale(-0.375);
                if (front) {
                    bb1 = bb.move(normal.scale(-0.875));
                    offset = offset.subtract(normal.scale(-0.75));
                }
                for (int i = 0; i < size; i++) {
                    BakedQuad quad = templateQuads.get(i);
                    Direction direction = quad.getDirection();

                    if (front && direction == facing)
                        continue;
                    if (!front && direction == facing.getOpposite())
                        continue;
                    if (!top && direction == Direction.UP)
                        continue;
                    if (top && direction == Direction.DOWN)
                        continue;

                    quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad,
                            BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), bb1, offset)));
                }

            }
        }

        return quads;
    }
}
