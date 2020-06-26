Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        dim = entity.getDim();

        overWorldHidden = modelMap.get("overWorldHidden");
        overWorldShow = modelMap.get("overWorldShow");
        netherWorldHidden = modelMap.get("netherWorldHidden");
        netherWorldShow = modelMap.get("netherWorldShow");
        endWorldHidden = modelMap.get("endWorldHidden");
        endWorldShow = modelMap.get("endWorldShow");

        if (overWorldHidden != undefined) {
            overWorldHidden.setHidden(dim === 0)
        }
        if (overWorldShow != undefined) {
            overWorldShow.setHidden(dim !== 0)
        }
        if (netherWorldHidden != undefined) {
            netherWorldHidden.setHidden(dim === -1)
        }
        if (netherWorldShow != undefined) {
            netherWorldShow.setHidden(dim !== -1)
        }
        if (endWorldHidden != undefined) {
            endWorldHidden.setHidden(dim === 1)
        }
        if (endWorldShow != undefined) {
            endWorldShow.setHidden(dim !== 1)
        }
    }
})