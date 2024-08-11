//package com.github.tartaricacid.touhoulittlemaid.util;
//
//import com.google.gson.*;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.TagParser;
//import net.minecraft.util.GsonHelper;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.crafting.Ingredient;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.annotation.Nullable;
//import java.util.Optional;
//
//public final class EntityCraftingHelper {
//    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//    private static final String TYPE_TAG = "type";
//    private static final String NBT_TAG = "nbt";
//    private static final String COPY_TAG = "copy";
//    private static final String INGREDIENT_TAG = "ingredient";
//    private static final String TAG_TAG = "tag";
//
//    public static Output getEntityData(JsonObject json) {
//        try {
//            Optional<EntityType<?>> optional = EntityType.byString(GsonHelper.getAsString(json, TYPE_TAG));
//            if (optional.isPresent()) {
//                EntityType<?> type = optional.get();
//
//                JsonElement nbtElement = json.get(NBT_TAG);
//                CompoundTag outputData = new CompoundTag();
//                if (nbtElement != null) {
//                    if (nbtElement.isJsonObject()) {
//                        outputData = TagParser.parseTag(GSON.toJson(nbtElement));
//                    } else {
//                        outputData = TagParser.parseTag(GsonHelper.convertToString(nbtElement, NBT_TAG));
//                    }
//                }
//
//                JsonElement copyElement = json.get(COPY_TAG);
//                if (copyElement != null && copyElement.isJsonObject()) {
//                    JsonObject copyObject = copyElement.getAsJsonObject();
//                    Ingredient ingredient = copyObject
//                    String tag = null;
//                    if (copyObject.has(TAG_TAG) && StringUtils.isNotBlank(copyObject.get(TAG_TAG).getAsString())) {
//                        tag = copyObject.get(TAG_TAG).getAsString();
//                    }
//                    return new Output(type, outputData, ingredient, tag);
//                }
//                return new Output(type, outputData);
//            }
//            throw new JsonParseException("Entity Type Tag Not Found");
//        } catch (CommandSyntaxException e) {
//            throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
//        }
//    }
//
//    public static class Output {
//        private final EntityType<?> type;
//        private final CompoundTag data;
//        private final Ingredient copyInput;
//        private final String copyTag;
//
//        public Output(EntityType<?> type, CompoundTag data, Ingredient copyInput, @Nullable String copyTag) {
//            this.type = type;
//            this.data = data;
//            this.copyInput = copyInput;
//            this.copyTag = copyTag;
//        }
//
//        public Output(EntityType<?> type, CompoundTag data) {
//            this(type, data, Ingredient.EMPTY, null);
//        }
//
//        public EntityType<?> getType() {
//            return type;
//        }
//
//        public CompoundTag getData() {
//            return data;
//        }
//
//        public Ingredient getCopyInput() {
//            return copyInput;
//        }
//
//        @Nullable
//        public String getCopyTag() {
//            return copyTag;
//        }
//    }
//}
