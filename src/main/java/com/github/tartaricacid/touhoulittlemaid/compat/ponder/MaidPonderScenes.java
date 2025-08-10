package com.github.tartaricacid.touhoulittlemaid.compat.ponder;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class MaidPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> registrationHelper) {
        PonderSceneRegistrationHelper<Item> helper = registrationHelper.withKeyFunction(ForgeRegistries.ITEMS::getKey);

        helper.forComponents(InitItems.HAKUREI_GOHEI.get(), InitItems.SANAE_GOHEI.get())
                .addStoryBoard("altar", MaidPonderScenes::altar);
    }

    public static void altar(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("altar", "How to build an altar");
        scene.scaleSceneView(0.5f);
        scene.showBasePlate();

        for (int i = 1; i < 7; i++) {
            scene.world().showSection(util.select().layer(i), Direction.DOWN);
            scene.addKeyframe();
            scene.idle(10);
        }

        BlockPos pos = new BlockPos(5, 4, 1);
        Selection clickPos = util.select().position(pos);

        scene.overlay().showOutlineWithText(clickPos, 50)
                .colored(PonderPalette.BLUE)
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(pos, Direction.NORTH, 0.25f))
                .text("Use Gohei click here to build the altar");

        scene.idle(60);

        scene.overlay().showOutlineWithText(clickPos, 50)
                .colored(PonderPalette.BLUE)
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(pos, Direction.NORTH, 0.25f))
                .text("Paying attention to the direction of the click");
    }
}
