package com.github.guyapooye.clockworkaddons.behaviours;


import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;


public class QuadLinkBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<LinkBehaviour> TYPE = new BehaviourType<>();
    public LinkBehaviour linkBehaviour1;
    public LinkBehaviour linkBehaviour2;
    public LinkBehaviour linkBehaviour3;
    public LinkBehaviour linkBehaviour4;


    public QuadLinkBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
}
