package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PolyMesh implements Serializable {
    @SerializedName("normalized_uvs")
    private boolean normalizedUvs;
    @SerializedName("normals")
    private double[] normals;
    @SerializedName("polys")
    private PolysUnion polys;
    @SerializedName("positions")
    private double[] positions;
    @SerializedName("uvs")
    private double[] uvs;
}
