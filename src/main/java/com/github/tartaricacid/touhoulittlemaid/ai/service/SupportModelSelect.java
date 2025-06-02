package com.github.tartaricacid.touhoulittlemaid.ai.service;

import java.util.Map;

/**
 * 可以选择模型的站点
 * <p>
 * 并不是所有的服务都有多个模型供选择，有些站点就一个定死的模型
 */
public interface SupportModelSelect {
    /**
     * 获取模型列表
     * <p>
     * key 是模型 id，value 是模型名称，模型名称仅用于显示
     *
     * @return 模型列表
     */
    Map<String, String> models();

    /**
     * 添加模型
     *
     * @param id   模型 id
     * @param name 模型显示名称
     */
    default void addModel(String id, String name) {
        models().put(id, name);
    }

    /**
     * 删除模型
     *
     * @param id 模型 id
     */
    default void removeModel(String id) {
        models().remove(id);
    }

    /**
     * 获取默认模型
     *
     * @return 一般返回列表内的第一个模型
     */
    default String getDefaultModel() {
        return models().keySet().iterator().next();
    }

    /**
     * 验证模型 id 是否存在
     * <p>
     * 如果没有找到指定的模型，则返回默认模型
     *
     * @param id 模型 id
     * @return 模型 id
     */
    default String getModel(String id) {
        if (models().containsKey(id)) {
            return id;
        }
        return getDefaultModel();
    }

    /**
     * 获取模型名称
     *
     * @param id 模型 id
     * @return 模型名称，仅用于显示的名称
     */
    default String getModelName(String id) {
        return models().get(id);
    }
}
