package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class Bone implements Serializable {
    @SerializedName("bind_pose_rotation")
    private double[] bindPoseRotation;
    @SerializedName("cubes")
    private Cube[] cubes;
    @SerializedName("debug")
    private Boolean debug;
    @SerializedName("inflate")
    private Double inflate;
    @SerializedName("locators")
    private Map<String, LocatorValue> locators;
    @SerializedName("mirror")
    private Boolean mirror;
    @SerializedName("name")
    private String name;
    @SerializedName("neverRender")
    private Boolean neverRender;
    @SerializedName("parent")
    private String parent;
    @SerializedName("pivot")
    private double[] pivot = new double[]{0, 0, 0};
    @SerializedName("poly_mesh")
    private PolyMesh polyMesh;
    @SerializedName("render_group_id")
    private Long renderGroupID;
    @SerializedName("reset")
    private Boolean reset;
    @SerializedName("rotation")
    private double[] rotation = new double[]{0, 0, 0};
    @SerializedName("texture_meshes")
    private TextureMesh[] textureMeshes;

    public double[] getBindPoseRotation() {
        return bindPoseRotation;
    }

    public void setBindPoseRotation(double[] value) {
        this.bindPoseRotation = value;
    }

    public Cube[] getCubes() {
        return cubes;
    }

    public void setCubes(Cube[] value) {
        this.cubes = value;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean value) {
        this.debug = value;
    }

    public Double getInflate() {
        return inflate;
    }

    public void setInflate(Double value) {
        this.inflate = value;
    }

    public Map<String, LocatorValue> getLocators() {
        return locators;
    }

    public void setLocators(Map<String, LocatorValue> value) {
        this.locators = value;
    }

    public Boolean getMirror() {
        return mirror;
    }

    public void setMirror(Boolean value) {
        this.mirror = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Boolean getNeverRender() {
        return neverRender;
    }

    public void setNeverRender(Boolean value) {
        this.neverRender = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String value) {
        this.parent = value;
    }

    public double[] getPivot() {
        return pivot;
    }

    public void setPivot(double[] value) {
        this.pivot = value;
    }

    public PolyMesh getPolyMesh() {
        return polyMesh;
    }

    public void setPolyMesh(PolyMesh value) {
        this.polyMesh = value;
    }

    public Long getRenderGroupID() {
        return renderGroupID;
    }

    public void setRenderGroupID(Long value) {
        this.renderGroupID = value;
    }

    public Boolean getReset() {
        return reset;
    }

    public void setReset(Boolean value) {
        this.reset = value;
    }

    public double[] getRotation() {
        return rotation;
    }

    public void setRotation(double[] value) {
        this.rotation = value;
    }

    public TextureMesh[] getTextureMeshes() {
        return textureMeshes;
    }

    public void setTextureMeshes(TextureMesh[] value) {
        this.textureMeshes = value;
    }
}
