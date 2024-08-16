package com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.clockwork.util.blocktype.ConnectedWingAlike;

import java.util.function.Supplier;

public class CopycatWingalikeModel extends CopycatModel {
    protected static final AABB CUBE_AABB;

    public CopycatWingalikeModel(BakedModel originalModel) {
        super(originalModel);
    }

    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        Direction facing = state.getOptionalValue(ConnectedWingAlike.Companion.getFACING()).orElse(Direction.SOUTH);
        BakedModel model = getModelOf(material);
        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));
        MeshBuilder meshBuilder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();
        context.pushTransform((quad) -> {
            if (cullFaceRemovalData.shouldRemove(quad.cullFace())) {
                quad.cullFace(null);
            } else if (occlusionData.isOccluded(quad.cullFace())) {
                RenderMaterial quadMaterial = quad.material();
                quad.copyTo(emitter);
                emitter.material(quadMaterial);
                emitter.emit();
                return false;
            }
            Vec3 normalScaled2 = normal.scale(0.875);
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
                    Direction direction = quad.lightFace();
                    if ((!front || direction != facing) && (front || direction != facing.getOpposite()) && (top || direction != Direction.UP) && (!top || direction != Direction.DOWN)) {
                        RenderMaterial quadMaterialx = quad.material();
                        quad.copyTo(emitter);
                        emitter.material(quadMaterialx);
                        BakedModelHelper.cropAndMove(emitter, spriteFinder.find(emitter, 0), bb1, offset);
                        emitter.emit();
                    }
                }
            }

            return false;
        });
        ((FabricBakedModel)model).emitBlockQuads(blockView, material, pos, randomSupplier, context);
        context.popTransform();
        context.meshConsumer().accept(meshBuilder.build());
    }

    static {
        CUBE_AABB = new AABB(0,0,0,1,1,1);
    }
}
