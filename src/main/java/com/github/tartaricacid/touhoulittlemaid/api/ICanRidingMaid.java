package com.github.tartaricacid.touhoulittlemaid.api;

/**
 * @author TartaricAcid
 * @date 2020/1/20 15:20
 * <p>
 * 拥有此接口的实体，可以主动骑上女仆
 **/
public interface ICanRidingMaid {
    /**
     * 能否主动骑上女仆
     *
     * @param maid 骑着的女仆对象
     * @return 能否主动骑上女仆
     */
    default boolean canRiding(AbstractEntityMaid maid) {
        return true;
    }

    /**
     * 当骑在女仆身上时，每次女仆更新所应用的更新
     *
     * @param maid 女仆对象
     */
    default void updatePassenger(AbstractEntityMaid maid) {
    }
}
