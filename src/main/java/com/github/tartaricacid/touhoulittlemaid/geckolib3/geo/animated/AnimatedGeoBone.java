package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class AnimatedGeoBone implements IBone {
    private final GeoBone geoBone;

    private final List<AnimatedGeoBone> children;

    private boolean isHidden = false;
    private boolean areCubesHidden = false;
    private boolean hideChildBonesToo = false;

    private final Vector3f scale = new Vector3f(1, 1, 1);
    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();

    public AnimatedGeoBone(GeoBone geoBone, @Nullable Map<String, AnimatedGeoBone> bones) {
        this.geoBone = geoBone;
        this.rotation.set(geoBone.rotation());

        if (bones != null) {
            bones.put(geoBone.name(), this);
            this.children = ObjectLists.unmodifiable(new ObjectArrayList<>(geoBone.children().stream().map(b -> new AnimatedGeoBone(b, bones)).toList()));
        } else {
            this.children = ObjectLists.emptyList();
        }
    }

    public GeoBone geoBone() {
        return geoBone;
    }

    public List<AnimatedGeoBone> children() {
        return children;
    }

    @Override
    public BoneSnapshot getInitialSnapshot() {
        return this.geoBone.initialSnapshot();
    }

    @Override
    public String getName() {
        return this.geoBone.name();
    }

    @Override
    public float getRotationX() {
        return rotation.x;
    }

    @Override
    public void setRotationX(float value) {
        this.rotation.x = value;
    }

    @Override
    public float getRotationY() {
        return rotation.y;
    }

    @Override
    public void setRotationY(float value) {
        this.rotation.y = value;
    }

    @Override
    public float getRotationZ() {
        return rotation.z;
    }

    @Override
    public void setRotationZ(float value) {
        this.rotation.z = value;
    }

    @Override
    public float getPositionX() {
        return position.x;
    }

    @Override
    public void setPositionX(float value) {
        this.position.x = value;
    }

    @Override
    public float getPositionY() {
        return position.y;
    }

    @Override
    public void setPositionY(float value) {
        this.position.y = value;
    }

    @Override
    public float getPositionZ() {
        return position.z;
    }

    @Override
    public void setPositionZ(float value) {
        this.position.z = value;
    }

    @Override
    public float getScaleX() {
        return scale.x;
    }

    @Override
    public void setScaleX(float value) {
        this.scale.x = value;
    }

    @Override
    public float getScaleY() {
        return scale.y;
    }

    @Override
    public void setScaleY(float value) {
        this.scale.y = value;
    }

    @Override
    public float getScaleZ() {
        return scale.z;
    }

    @Override
    public void setScaleZ(float value) {
        this.scale.z = value;
    }

    @Override
    public boolean isHidden() {
        return this.isHidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.setHidden(hidden, hidden);
    }

    @Override
    public float getPivotX() {
        return this.geoBone.pivot().x;
    }

    @Override
    public float getPivotY() {
        return this.geoBone.pivot().y;
    }

    @Override
    public float getPivotZ() {
        return this.geoBone.pivot().z;
    }

    @Override
    public boolean cubesAreHidden() {
        return areCubesHidden;
    }

    @Override
    public boolean childBonesAreHiddenToo() {
        return hideChildBonesToo;
    }

    @Override
    public void setCubesHidden(boolean hidden) {
        this.areCubesHidden = hidden;
    }

    @Override
    public void setHidden(boolean selfHidden, boolean skipChildRendering) {
        this.isHidden = selfHidden;
        this.hideChildBonesToo = skipChildRendering;
    }

    public void addPositionX(float x) {
        setPositionX(getPositionX() + x);
    }

    public void addPositionY(float y) {
        setPositionY(getPositionY() + y);
    }

    public void addPositionZ(float z) {
        setPositionZ(getPositionZ() + z);
    }

    public void setPosition(float x, float y, float z) {
        setPositionX(x);
        setPositionY(y);
        setPositionZ(z);
    }

    public Vector3d getPosition() {
        return new Vector3d(getPositionX(), getPositionY(), getPositionZ());
    }

    public void setPosition(Vector3d vec) {
        setPosition((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public void addRotation(Vector3d vec) {
        addRotation((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public void addRotation(float x, float y, float z) {
        addRotationX(x);
        addRotationY(y);
        addRotationZ(z);
    }

    public void addRotationX(float x) {
        setRotationX(getRotationX() + x);
    }

    public void addRotationY(float y) {
        setRotationY(getRotationY() + y);
    }

    public void addRotationZ(float z) {
        setRotationZ(getRotationZ() + z);
    }

    public void setRotation(float x, float y, float z) {
        setRotationX(x);
        setRotationY(y);
        setRotationZ(z);
    }

    public Vector3d getRotation() {
        return new Vector3d(getRotationX(), getRotationY(), getRotationZ());
    }

    public void setRotation(Vector3d vec) {
        setRotation((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public void multiplyScale(Vector3d vec) {
        multiplyScale((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public void multiplyScale(float x, float y, float z) {
        setScaleX(getScaleX() * x);
        setScaleY(getScaleY() * y);
        setScaleZ(getScaleZ() * z);
    }

    public void setScale(float x, float y, float z) {
        setScaleX(x);
        setScaleY(y);
        setScaleZ(z);
    }

    public Vector3d getScale() {
        return new Vector3d(getScaleX(), getScaleY(), getScaleZ());
    }

    public void setScale(Vector3d vec) {
        setScale((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public void addRotationOffsetFromBone(AnimatedGeoBone source) {
        setRotationX(getRotationX() + source.getRotationX() - source.getInitialSnapshot().rotationValueX);
        setRotationY(getRotationY() + source.getRotationY() - source.getInitialSnapshot().rotationValueY);
        setRotationZ(getRotationZ() + source.getRotationZ() - source.getInitialSnapshot().rotationValueZ);
    }
}
