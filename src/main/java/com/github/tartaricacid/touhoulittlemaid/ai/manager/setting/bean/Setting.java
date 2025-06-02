package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.bean;

public final class Setting {
    private MetaData meta = new MetaData();
    private String setting = "";

    public Setting() {
    }

    public Setting(MetaData meta, String setting) {
        this.meta = meta;
        this.setting = setting;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }
}
