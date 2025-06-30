package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.events.RenderGameTextEvent;
import dev.loottech.client.font.FontRenderer;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;

/*
 * Uses jvm11+ dynamic constants - pseudocode provided - see https://www.benf.org/other/cfr/dynamic-constants.html
 */
@RegisterModule(name="Font", category=Module.Category.CLIENT, tag="Font", description="Manages client's custom font.", drawn=false)
public class Font
extends Module {
    public final ValueEnum font = new ValueEnum("Font", "Font", "What custom font to use.", (Enum)Fonts.Verdana);
    public final ValueBoolean globalFont = new ValueBoolean("Global", "Global", "Set the font for the whole game to custom font.", false);

    /*
     * Exception decompiling
     */
    public FontRenderer getFont() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Can't turn ConstantPoolEntry into Literal - got DynamicInfo value=1,106
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void onEnable() {
        if (Font.nullCheck()) {
            return;
        }
        Managers.FONT.mcFont = false;
        Managers.FONT = this.getFont();
    }

    @Override
    public void onDisable() {
        if (Font.nullCheck()) {
            return;
        }
        Managers.FONT.mcFont = true;
    }

    @Override
    public void onUpdate() {
        if (Font.nullCheck()) {
            return;
        }
        if (Managers.FONT != null || !Managers.FONT.equals(this.getFont())) {
            Managers.FONT = this.getFont();
        }
    }

    @Override
    public void onClient(ClientEvent event) {
        if (Font.nullCheck()) {
            return;
        }
        if (event.getValue() == this.font && (Managers.FONT != null || Managers.FONT.equals(this.getFont()))) {
            Managers.FONT = this.getFont();
        }
    }

    @Override
    public void onRenderGameText(RenderGameTextEvent event) {
        if (this.globalFont.getValue()) {
            if (event.isShadow()) {
                Managers.FONT.drawWithShadow(event.getMatrices(), event.getText(), event.getX(), event.getY(), event.getColor());
            } else {
                Managers.FONT.draw(event.getMatrices(), event.getText(), event.getX(), event.getY(), event.getColor());
            }
            event.cancel();
        }
    }

    public static enum Fonts {
        Arial,
        TimesNewRoman,
        Verdana,
        Bahnschift,
        ComicSans;

    }
}
