package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage;

public interface ITempVariableStorage {
    Object getTemp(int address);

    void setTemp(int address, Object value);
}
