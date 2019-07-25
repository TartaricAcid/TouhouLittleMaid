package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;

public interface TaskHandler {
    boolean canExecute(AbstractEntityMaid maid);
}
