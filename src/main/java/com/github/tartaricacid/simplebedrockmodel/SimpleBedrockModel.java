package com.github.tartaricacid.simplebedrockmodel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 之前拆分出的基岩版模型加载器，现在合并回主 mod 中，以便于更好地管理和维护。
 * 此包不在作为单独模组加载，仅保留日志功能。
 * <p>
 * 原 SimpleBedrockModel 模组已经修改包名，并进行了大幅度功能拓展和重构。
 */
public class SimpleBedrockModel {
    public static final String NAME = "simplebedrockmodel";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
}
