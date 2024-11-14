package com.github.tartaricacid.touhoulittlemaid.molang.runtime;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Variable {
    @Nullable Object evaluate(final @NotNull ExecutionContext<?> context);
}
