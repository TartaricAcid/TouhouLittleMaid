package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;

import java.util.List;

public class GeoBone implements IBone {
    public GeoBone parent;
    public List<GeoBone> childBones = new ObjectArrayList<>();
    public List<GeoCube> childCubes = new ObjectArrayList<>();
    public String name;
    public Boolean mirror;
    public Double inflate;
    public Boolean dontRender;
    public boolean isHidden;
    public boolean areCubesHidden = false;
    public boolean hideChildBonesToo;
    /**
     * 我也不知道这个参数有啥用，但是 json 里面就有
     */
    public Boolean reset;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public Matrix4f rotMat;
    private BoneSnapshot initialSnapshot;
    private float scaleX = 1;
    private float scaleY = 1;
    private float scaleZ = 1;
    private float positionX;
    private float positionY;
    private float positionZ;
    private float rotateX;
    private float rotateY;
    private float rotateZ;
    private Matrix4f modelSpaceXform;
    private Matrix4f localSpaceXform;
    private Matrix4f worldSpaceXform;
    private Matrix3f worldSpaceNormal;
    private boolean trackXform;

    public GeoBone() {
        modelSpaceXform = new Matrix4f();
        modelSpaceXform.setIdentity();
        localSpaceXform = new Matrix4f();
        localSpaceXform.setIdentity();
        worldSpaceXform = new Matrix4f();
        worldSpaceXform.setIdentity();
        worldSpaceNormal = new Matrix3f();
        worldSpaceNormal.setIdentity();
        trackXform = false;
        rotMat = null;
    }

    public static void removeMatrixTranslation(Matrix4f matrix) {
        matrix.m03 = 0;
        matrix.m13 = 0;
        matrix.m23 = 0;
    }

    @Override
        public void setModelRendererName(String modelRendererName) {
        this.name = modelRendererName;
    }

    @Override
        public void saveInitialSnapshot() {
        if (this.initialSnapshot == null) {
            this.initialSnapshot = new BoneSnapshot(this, true);
        }
    }

    @Override
        public BoneSnapshot getInitialSnapshot() {
        return this.initialSnapshot;
    }


    @Override
        public String getName() {
        return this.name;
    }

    @Override
        public float getRotationX() {
        return rotateX;
    }

    @Override
        public void setRotationX(float value) {
        this.rotateX = value;
    }

    @Override
        public float getRotationY() {
        return rotateY;
    }

    @Override
        public void setRotationY(float value) {
        this.rotateY = value;
    }

    @Override
        public float getRotationZ() {
        return rotateZ;
    }

    @Override
        public void setRotationZ(float value) {
        this.rotateZ = value;
    }

    @Override
        public float getPositionX() {
        return positionX;
    }

    @Override
        public void setPositionX(float value) {
        this.positionX = value;
    }

    @Override
        public float getPositionY() {
        return positionY;
    }

    @Override
        public void setPositionY(float value) {
        this.positionY = value;
    }

    @Override
        public float getPositionZ() {
        return positionZ;
    }

    @Override
        public void setPositionZ(float value) {
        this.positionZ = value;
    }

    @Override
        public float getScaleX() {
        return scaleX;
    }

    @Override
        public void setScaleX(float value) {
        this.scaleX = value;
    }

    @Override
        public float getScaleY() {
        return scaleY;
    }

    @Override
        public void setScaleY(float value) {
        this.scaleY = value;
    }

    @Override
        public float getScaleZ() {
        return scaleZ;
    }

    @Override
        public void setScaleZ(float value) {
        this.scaleZ = value;
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
        return this.rotationPointX;
    }

    @Override
        public void setPivotX(float value) {
        this.rotationPointX = value;
    }

    @Override
        public float getPivotY() {
        return this.rotationPointY;
    }

    @Override
        public void setPivotY(float value) {
        this.rotationPointY = value;
    }

    @Override
        public float getPivotZ() {
        return this.rotationPointZ;
    }

    @Override
        public void setPivotZ(float value) {
        this.rotationPointZ = value;
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

    public GeoBone getParent() {
        return parent;
    }

    public boolean isTrackingXform() {
        return trackXform;
    }

    public void setTrackXform(boolean trackXform) {
        this.trackXform = trackXform;
    }

    public Matrix4f getModelSpaceXform() {
        setTrackXform(true);
        return modelSpaceXform;
    }

    public void setModelSpaceXform(Matrix4f modelSpaceXform) {
        this.modelSpaceXform.set(modelSpaceXform);
    }

    public Vector3d getModelPosition() {
        Matrix4f matrix = getModelSpaceXform();
        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix);
        return new Vector3d(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
    }

    public void setModelPosition(Vector3d pos) {
        // 不适用于具有父转换的骨骼
        GeoBone parent = getParent();
        Matrix4f identity = new Matrix4f();
        identity.setIdentity();
        Matrix4f matrix = parent == null ? identity : parent.getModelSpaceXform().copy();
        matrix.invert();
        Vector4f vec = new Vector4f(-(float) pos.x / 16f, (float) pos.y / 16f, (float) pos.z / 16f, 1);
        vec.transform(matrix);
        setPosition(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
    }

    public Matrix4f getLocalSpaceXform() {
        setTrackXform(true);
        return localSpaceXform;
    }

    public void setLocalSpaceXform(Matrix4f localSpaceXform) {
        this.localSpaceXform.set(localSpaceXform);
    }

    public Vector3d getLocalPosition() {
        Matrix4f matrix = getLocalSpaceXform();
        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix);
        return new Vector3d(vec.x(), vec.y(), vec.z());
    }

    public Matrix4f getWorldSpaceXform() {
        setTrackXform(true);
        return worldSpaceXform;
    }

    public void setWorldSpaceXform(Matrix4f worldSpaceXform) {
        this.worldSpaceXform.set(worldSpaceXform);
    }

    public Vector3d getWorldPosition() {
        Matrix4f matrix = getWorldSpaceXform();
        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix);
        return new Vector3d(vec.x(), vec.y(), vec.z());
    }

    public Matrix4f getModelRotationMat() {
        Matrix4f matrix = getModelSpaceXform().copy();
        removeMatrixTranslation(matrix);
        return matrix;
    }

    public void setModelRotationMat(Matrix4f mat) {
        rotMat = mat;
    }

    public void addPosition(Vector3d vec) {
        addPosition((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public void addPosition(float x, float y, float z) {
        addPositionX(x);
        addPositionY(y);
        addPositionZ(z);
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

    public void addRotationOffsetFromBone(GeoBone source) {
        setRotationX(getRotationX() + source.getRotationX() - source.getInitialSnapshot().rotationValueX);
        setRotationY(getRotationY() + source.getRotationY() - source.getInitialSnapshot().rotationValueY);
        setRotationZ(getRotationZ() + source.getRotationZ() - source.getInitialSnapshot().rotationValueZ);
    }
}
