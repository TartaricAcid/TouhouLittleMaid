package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneTopLevelSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

import java.util.List;

public class GeoBone {
    private static final String GLOWING_PREFIX = "ysmGlow";

    private GeoBone parent;
    private final List<GeoBone> children;

    private final String name;
    private final Vector3f pivot;
    private final Vector3f rotation;
    private final List<GeoCube> cubes;

    private final Boolean mirror;
    private final Double inflate;
    private final Boolean dontRender;
    /**
     * 我也不知道这个参数有啥用，但是 json 里面就有
     */
    private final Boolean reset;

    private final BoneSnapshot initialSnapshot;
    private final boolean glow;

    public GeoBone(List<GeoBone> children, String name, Vector3f pivot, Vector3f rotation, List<GeoCube> cubes, Boolean mirror, Double inflate, Boolean dontRender, Boolean reset) {
        this.children = ObjectLists.unmodifiable(new ObjectArrayList<>(children));
        this.name = name;
        this.pivot = pivot;
        this.rotation = rotation;
        this.cubes = ObjectLists.unmodifiable(new ObjectArrayList<>(cubes));

        this.mirror = mirror;
        this.inflate = inflate;
        this.dontRender = dontRender;
        this.reset = reset;

        this.initialSnapshot = new BoneTopLevelSnapshot(new AnimatedGeoBone(this, null));
        this.glow = name.startsWith(GLOWING_PREFIX);
    }

    public GeoBone parent() {
        return parent;
    }

    public List<GeoBone> children() {
        return children;
    }

    public List<GeoCube> cubes() {
        return cubes;
    }

    public String name() {
        return name;
    }

    public Vector3f pivot() {
        return pivot;
    }

    public Vector3f rotation() {
        return rotation;
    }

    public Boolean mirror() {
        return mirror;
    }

    public Double inflate() {
        return inflate;
    }

    public Boolean dontRender() {
        return dontRender;
    }

    public Boolean reset() {
        return reset;
    }

    public BoneSnapshot initialSnapshot() {
        return initialSnapshot;
    }

    public boolean glow() {
        return glow;
    }

    public void setParent(GeoBone parent) {
        this.parent = parent;
    }
}
