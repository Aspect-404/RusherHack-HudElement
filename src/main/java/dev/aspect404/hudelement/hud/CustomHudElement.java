package dev.aspect404.hudelement.hud;

import org.rusherhack.client.api.feature.hud.HudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.font.IFontRenderer;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.core.setting.*;
import org.rusherhack.client.api.setting.ColorSetting;

import java.awt.Color;
import java.util.*;
import java.util.stream.IntStream;

public class CustomHudElement extends HudElement {
    private final NullSetting stringSettings = new NullSetting("Strings", "The list of strings which are displayed.");
    private final BooleanSetting addSetting = new BooleanSetting("Add", "Add a string on the end", true);
    private final BooleanSetting removeSetting = new BooleanSetting("Remove", "Remove the last string.", true);

    public CustomHudElement() {
        super("CustomHudElement");
        this.setDescription("Super customizable text hud element for rusherhack!");
        this.getSettings().clear();
        this.addSetting.setChangeAction(this::addSettingAction);
        this.removeSetting.setChangeAction(this::removeSettingAction);

        final String[] strings = {"RusherHack", "+ ", "[", "Deluxe", "]"};
        final Integer[] colors = {0xFFFF0000, 0xFF000000, 0xFF808080, 0x00FFFFFF, 0xFF808080};
        IntStream.range(0, 50).forEach((index) -> {
            final Boolean shouldDisplay = index < 5;
            final String text = shouldDisplay ? strings[index] : "";
            final Color color = shouldDisplay ? new Color(colors[index], true) : Color.RED;
            final StringSetting stringSetting = new StringSetting("String", "The string to display.", text);
            final ColorSetting colorSetting = new ColorSetting("Color", "The color to display this string as.", color);
            stringSetting.addSubSettings(colorSetting); this.stringSettings.addSubSettings(stringSetting);
            stringSetting.setHidden(!shouldDisplay); });

        this.registerSettings(this.stringSettings, this.addSetting, this.removeSetting);
    }

    private List<Setting<?>> getVisible() {
        return this.stringSettings.getSubSettings().stream().filter(setting -> !setting.isHidden()).toList();
    }
    private void addSettingAction() {
        if (this.addSetting.getValue()) return;
        this.addSetting.setValue(true);
        if (this.getVisible().size() == 50) return;
        this.stringSettings.getSubSettings().get(this.getVisible().size()).setHidden(false);
    }
    private void removeSettingAction() {
        if (this.removeSetting.getValue()) return;
        this.removeSetting.setValue(true);
        if (this.getVisible().isEmpty()) return;
        this.stringSettings.getSubSettings().get(this.getVisible().size() - 1).setHidden(true);
    }

    @Override
    public void renderContent(RenderContext context, double mouseX, double mouseY) {
        final IFontRenderer renderer = this.getFontRenderer();
        renderer.begin();
        double position = 0.0;
        for (Setting<?> setting : this.getVisible()) {
            final String text = ((StringSetting) setting).getValue();
            final int color = ((ColorSetting) setting.getSubSetting("Color")).getValueRGB();
            position = renderer.drawString(text, position, 0, color);
        }
        renderer.end();
    }

    @Override
    public double getWidth() {
        return this.getVisible().stream().mapToDouble((setting) ->
            this.getFontRenderer().getStringWidth(((StringSetting) setting).getValue())).sum();
    }
    @Override
    public double getHeight() {
        return this.getFontRenderer().getFontHeight();
    }
}