package com.github.tartaricacid.touhoulittlemaid.capability;

import java.util.concurrent.Callable;

public class HasGuideHandler {
    static HasGuideHandler.Factory FACTORY = new HasGuideHandler.Factory();

    private boolean first = true;

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean isFirst) {
        this.first = isFirst;
    }

    private static class Factory implements Callable<HasGuideHandler> {
        @Override
        public HasGuideHandler call() throws Exception {
            return new HasGuideHandler();
        }
    }
}
