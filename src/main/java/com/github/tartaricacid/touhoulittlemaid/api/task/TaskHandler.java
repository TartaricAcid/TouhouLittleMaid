package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;

/**
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public interface TaskHandler {
    /**
     * 是否启用此 Handler
     *
     * @param maid 女仆对象
     * @return boolean
     */
    boolean canExecute(AbstractEntityMaid maid);
}
