package me.gameisntover.knockbackffa.gui.guis.kit;

import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.kit.KnockKit;

public class EditKitMainGUI extends LightGUI {
    public EditKitMainGUI(KnockKit kit) {
        super("Edit " + kit.getName(), 1);
        LightButton editKitItems = LightButtonManager.createButton(ItemBuilder.builder().name("&bEdit kit items").build(),e -> {

        });
    }
}
