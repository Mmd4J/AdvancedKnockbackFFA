package me.gameisntover.knockbackffa.kit.gui.guis.kit;

import me.gameisntover.knockbackffa.kit.gui.ItemBuilder;
import me.gameisntover.knockbackffa.kit.gui.LightButton;
import me.gameisntover.knockbackffa.kit.gui.LightButtonManager;
import me.gameisntover.knockbackffa.kit.gui.LightGUI;
import me.gameisntover.knockbackffa.kit.KnockKit;

public class EditKitMainGUI extends LightGUI {
    public EditKitMainGUI(KnockKit kit) {
        super("Edit " + kit.getName(), 1);
        LightButton editKitItems = LightButtonManager.createButton(ItemBuilder.builder().name("&bEdit kit items").build(), e -> {

        });
    }
}
