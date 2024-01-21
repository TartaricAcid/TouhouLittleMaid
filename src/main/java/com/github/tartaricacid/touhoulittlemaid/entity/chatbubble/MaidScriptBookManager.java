package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MaidScriptBookManager {
    private static final Gson GSON = new Gson();
    private static final String STORE_TAG = "MaidScriptBook";
    private static final String PAGES_TAG = "pages";
    private static final String SEPARATOR = "\n\n";

    private final Map<String, List<ChatText>> scripts;
    private ListNBT scriptsTags;

    public MaidScriptBookManager() {
        this.scripts = Maps.newHashMap();
        this.scriptsTags = new ListNBT();
    }

    public boolean installScript(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag == null || !tag.contains(PAGES_TAG, Constants.NBT.TAG_LIST)) {
            return false;
        }
        ListNBT pagesTag = tag.getList(PAGES_TAG, Constants.NBT.TAG_STRING);
        if (pagesTag.isEmpty()) {
            return false;
        }
        if (stack.getItem() == Items.WRITTEN_BOOK) {
            ListNBT list = pagesTag.copy();
            this.scriptsTags = new ListNBT();
            for (int i = 0; i < list.size(); i++) {
                BookText bookText = GSON.fromJson(list.getString(i), BookText.class);
                this.scriptsTags.add(StringNBT.valueOf(bookText.text));
            }
        } else {
            this.scriptsTags = pagesTag.copy();
        }
        loadFromTag(this.scriptsTags);
        return true;
    }

    public void removeScript() {
        this.scripts.clear();
        this.scriptsTags = new ListNBT();
    }

    public boolean copyScript(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag == null || !tag.contains(PAGES_TAG, Constants.NBT.TAG_LIST) || tag.getList(PAGES_TAG, Constants.NBT.TAG_STRING).isEmpty()) {
            CompoundNBT stackTag = stack.getOrCreateTag();
            stackTag.put(PAGES_TAG, this.scriptsTags.copy());
            return true;
        }
        return false;
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.put(STORE_TAG, this.scriptsTags);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.contains(STORE_TAG, Constants.NBT.TAG_LIST)) {
            this.scriptsTags = compound.getList(STORE_TAG, Constants.NBT.TAG_STRING);
            loadFromTag(this.scriptsTags);
        }
    }

    private void loadFromTag(ListNBT pages) {
        scripts.clear();
        for (int i = 0; i < pages.size(); i++) {
            String pageText = pages.getString(i);
            String[] split = StringUtils.split(pageText, SEPARATOR);
            if (split.length < 2) {
                continue;
            }
            String type = StringUtils.strip(split[0]).toLowerCase(Locale.US);
            if (StringUtils.isBlank(type)) {
                continue;
            }
            List<ChatText> scriptList = scripts.computeIfAbsent(type, k -> Lists.newArrayList());
            for (int j = 1; j < split.length; j++) {
                String script = StringUtils.strip(split[j]);
                if (StringUtils.isNotBlank(script)) {
                    ChatText chatText = new ChatText(ChatTextType.TEXT, ChatText.EMPTY_ICON_PATH, script);
                    scriptList.add(chatText);
                }
            }
        }
    }

    public List<ChatText> getScripts(String type) {
        return this.scripts.getOrDefault(type, Lists.newArrayList());
    }

    private static class BookText {
        @SerializedName("text")
        private String text;
    }
}