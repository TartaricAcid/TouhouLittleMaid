package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.IAnimatedModel;

public interface IGeoEntity {
    IMaid getMaid();

    MaidModelInfo getMaidInfo();

    IAnimatedModel getGeoModel();

    void setMaidInfo(MaidModelInfo info);

    void setYsmModel(String modelId, String texture);
}
