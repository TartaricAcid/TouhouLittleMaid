package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MaidScriptBookManager {
    private static final String STORE_TAG = "MaidScriptBook";
    private static final String SEPARATOR = "\n\n";

    private final Map<String, List<ChatText>> scripts;

    public MaidScriptBookManager() {
        this.scripts = Maps.newHashMap();
    }

    private static List<String> readBookPages(WrittenBookContent bookContent) {
        return bookContent.getPages(false).stream().map(Component::getString).toList();
    }

    private static List<String> readBookPages(WritableBookContent bookContent) {
        return bookContent.getPages(false).toList();
    }

    private static WritableBookContent replaceBookContentWithPages(final List<String> pages, final WritableBookContent book) {
        return book.withReplacedPages(pages.stream().map(Filterable::passThrough).toList());
    }

    private static void readScriptsFromPages(final List<String> pages, Map<String, List<ChatText>> scripts) {
        scripts.clear();
        for (var page : pages) {
            String[] split = StringUtils.split(page, SEPARATOR);
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

    private static void writeScriptsToPages(final Map<String, List<ChatText>> scripts, List<String> pages) {
        pages.clear();
        for (Map.Entry<String, List<ChatText>> entry : scripts.entrySet()) {
            String type = entry.getKey();
            List<ChatText> scriptList = entry.getValue();
            if (scriptList.isEmpty()) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(type).append(SEPARATOR);
            for (ChatText chatText : scriptList) {
                builder.append(chatText.getText()).append(SEPARATOR);
            }
            pages.add(builder.toString());
        }
    }

    public boolean installScript(ItemStack stack) {
        WrittenBookContent writtenBookContent = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent != null && !writtenBookContent.getPages(false).isEmpty()) {
            var pages = readBookPages(writtenBookContent);
            readScriptsFromPages(pages, this.scripts);
            return true;
        }

        WritableBookContent writableBookContent = stack.get(DataComponents.WRITABLE_BOOK_CONTENT);
        if (writableBookContent != null && writableBookContent.getPages(false).findAny().isPresent()) {
            var pages = readBookPages(writableBookContent);
            readScriptsFromPages(pages, this.scripts);
            return true;
        }

        return false;
    }

    public void removeScript() {
        this.scripts.clear();
    }

    public boolean copyScript(ItemStack stack) {
        // 拷贝只能拷贝书与笔，成书不能拷贝
        var book = stack.get(DataComponents.WRITABLE_BOOK_CONTENT);
        if (book == null || !book.pages().isEmpty()) {
            return false;
        }
        List<String> pages = Lists.newArrayList();
        writeScriptsToPages(this.scripts, pages);
        var newBook = replaceBookContentWithPages(pages, book);
        stack.set(DataComponents.WRITABLE_BOOK_CONTENT, newBook);
        return true;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        ListTag scriptsTags = new ListTag();
        List<String> pages = Lists.newArrayList();
        writeScriptsToPages(this.scripts, pages);
        for (String page : pages) {
            scriptsTags.add(StringTag.valueOf(page));
        }
        compound.put(STORE_TAG, scriptsTags);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        if (!compound.contains(STORE_TAG, Tag.TAG_LIST)) {
            return;
        }
        ListTag scriptsTags = compound.getList(STORE_TAG, Tag.TAG_STRING);
        List<String> pages = Lists.newArrayList();
        for (Tag tag : scriptsTags) {
            pages.add(tag.getAsString());
        }
        readScriptsFromPages(pages, this.scripts);
    }

    public List<ChatText> getScripts(String type) {
        return this.scripts.getOrDefault(type, Lists.newArrayList());
    }
}
