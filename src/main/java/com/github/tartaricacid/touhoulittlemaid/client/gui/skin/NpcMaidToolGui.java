package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SendNpcMaidModelMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityCustomNpc;

import java.util.Optional;

/**
 * @author TartaricAcid
 * @date 2019/12/20 13:53
 **/
public class NpcMaidToolGui extends GuiScreen {
    private static int index;
    private EntityCustomNpc npc;
    private String[] idArray;

    public NpcMaidToolGui(EntityCustomNpc npc) {
        this.npc = npc;
        idArray = ClientProxy.MAID_MODEL.getModelIdSet().toArray(new String[0]);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        int x = this.width / 2;
        int y = this.height / 2;
        for (int i = 0; i < 9; i++) {
            int num = i + index * 9;
            if (num >= idArray.length) {
                break;
            }
            Optional<ModelItem> item = ClientProxy.MAID_MODEL.getInfo(idArray[num]);
            if (item.isPresent()) {
                this.addButton(new GuiButton(i, x - 100, y - 100 + i * 20, ParseI18n.parse(item.get().getName())));
            }
        }
        this.addButton(new GuiButton(9, x - 100, y + 80, 20, 20, "-"));
        this.addButton(new GuiButton(10, x + 80, y + 80, 20, 20, "+"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(fontRenderer, String.format("%d/%d", index, idArray.length / 9), x, y + 86, 0xffffff);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (0 <= button.id && button.id < 9) {
            int num = button.id + index * 9;
            Optional<ModelItem> item = ClientProxy.MAID_MODEL.getInfo(idArray[num]);
            if (item.isPresent()) {
                ModelItem modelItem = item.get();
                Entity entity = npc.modelData.getEntity(npc);
                if (entity instanceof EntityMaid) {
                    // 客户端数据更改
                    // 虽然不更改也会在半秒后自动同步，但是强迫症使然
                    ((EntityMaid) entity).setModelId(modelItem.getModelId().toString());
                    npc.textureLocation = item.get().getTexture();
                    // 服务端数据更改
                    CommonProxy.INSTANCE.sendToServer(new SendNpcMaidModelMessage(npc.getUniqueID(),
                            modelItem.getModelId().toString(), modelItem.getTexture().toString()));
                }
            }
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            return;
        }
        // 向前翻页
        if (button.id == 9) {
            if (index > 0) {
                index--;
                this.initGui();
            }
            return;
        }
        // 向后翻页
        if (button.id == 10) {
            if (index < idArray.length / 9) {
                index++;
                this.initGui();
            }
        }
    }
}
