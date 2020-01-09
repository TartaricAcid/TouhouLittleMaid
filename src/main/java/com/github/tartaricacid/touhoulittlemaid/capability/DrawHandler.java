package com.github.tartaricacid.touhoulittlemaid.capability;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * @author TartaricAcid
 * @date 2020/1/2 21:05
 **/
public class DrawHandler {
    static DrawHandler.Factory FACTORY = new DrawHandler.Factory();
    private int totalDrawTimes = 0;
    private HashMap<String, Integer> drawInfo = Maps.newHashMap();

    public void addDrawnInfo(String modelId) {
        this.addDrawnInfo(modelId, 1);
    }

    public void addDrawnInfo(String modelId, int times) {
        totalDrawTimes += times;
        if (drawInfo.containsKey(modelId)) {
            times = drawInfo.get(modelId) + times;
        }
        drawInfo.put(modelId, times);
    }

    public int getDrawInfo(String modelId) {
        if (drawInfo.containsKey(modelId)) {
            return drawInfo.get(modelId);
        }
        return 0;
    }

    public HashMap<String, Integer> getDrawInfoMaps() {
        return drawInfo;
    }

    public void setDrawInfoMaps(HashMap<String, Integer> drawInfo) {
        this.drawInfo = drawInfo;
    }

    public void setTotalDrawTimes(int times) {
        this.totalDrawTimes = times;
    }

    public int getTotalDrawTimes() {
        return totalDrawTimes;
    }

    private static class Factory implements Callable<DrawHandler> {
        @Override
        public DrawHandler call() {
            return new DrawHandler();
        }
    }
}
