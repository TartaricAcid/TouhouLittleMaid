package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.tree.RawGeometryTree;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;


public interface IGeoBuilder {
    GeoModel constructGeoModel(RawGeometryTree geometryTree);
}
