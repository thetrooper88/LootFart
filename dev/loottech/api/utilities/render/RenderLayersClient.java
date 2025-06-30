package dev.loottech.api.utilities.render;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.asm.mixins.accessor.AccessorRenderPhase;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.class_156;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_4668;
import net.minecraft.class_918;
import org.lwjgl.opengl.GL11;

public class RenderLayersClient
implements Util {
    public static final class_2960 SHULKER_BOXES_ATLAS_TEXTURE = class_2960.method_60654((String)"textures/atlas/shulker_boxes.png");
    public static final class_2960 CHEST_ATLAS_TEXTURE = class_2960.method_60654((String)"textures/atlas/chest.png");
    public static final class_1921 GLINT = class_1921.method_24048((String)"glint", (class_293)class_290.field_1585, (class_293.class_5596)class_293.class_5596.field_27382, (int)256, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_4668.field_29422).method_34577((class_4668.class_5939)new class_4668.class_4683(class_918.field_43087, true, false)).method_23616(class_4668.field_21350).method_23603(class_4668.field_21345).method_23604((class_4668.class_4672)new DepthTest()).method_23615(class_4668.field_21368).method_23614(class_4668.field_21381).method_23617(false));
    public static final Function<class_2960, class_1921> ENTITY_NO_OUTLINE = class_156.method_34866(texture -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29411).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23603(class_1921.field_21345).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23616(class_1921.field_21350).method_23617(false);
        return class_1921.method_24049((String)"entity_no_outline", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final BiFunction<class_2960, Boolean, class_1921> ENTITY_TRANSLUCENT = class_156.method_34865((texture, affectsOutline) -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29407).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23603(class_1921.field_21345).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23617(affectsOutline.booleanValue());
        return class_1921.method_24049((String)"entity_translucent", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)true, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final Function<class_2960, class_1921> ENTITY_SOLID = class_156.method_34866(texture -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29450).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21364).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23617(true);
        return class_1921.method_24049((String)"entity_solid", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)false, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final Function<class_2960, class_1921> ITEM_ENTITY_TRANSLUCENT_CULL_2 = class_156.method_34866(texture -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29405).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23610(class_1921.field_25643).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23616(class_4668.field_21349).method_23617(true);
        return class_1921.method_24049((String)"item_entity_translucent_cull", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)true, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final Function<class_2960, class_1921> ENTITY_TRANSLUCENT_CULL = class_156.method_34866(texture -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29406).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23617(true);
        return class_1921.method_24049((String)"entity_translucent_cull", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)true, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final Function<class_2960, class_1921> ENTITY_CUTOUT = class_156.method_34866(texture -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29451).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21364).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23617(true);
        return class_1921.method_24049((String)"entity_cutout", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)false, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final Function<class_2960, class_1921> ENTITY_CUTOUT_NO_CULL = class_156.method_34866(texture -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29452).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21364).method_23603(class_1921.field_21345).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23617(true);
        return class_1921.method_24049((String)"entity_cutout_no_cull", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)false, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final BiFunction<class_2960, Boolean, class_1921> ENTITY_CUTOUT_NO_CULL_Z_OFFSET = class_156.method_34865((texture, affectsOutline) -> {
        class_1921.class_4688 multiPhaseParameters = class_1921.class_4688.method_23598().method_34578(class_1921.field_29404).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21364).method_23603(class_1921.field_21345).method_23608((class_4668.class_4676)new Lightmap()).method_23611(class_1921.field_21385).method_23607(class_1921.field_22241).method_23617(affectsOutline.booleanValue());
        return class_1921.method_24049((String)"entity_cutout_no_cull_z_offset", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)true, (boolean)false, (class_1921.class_4688)multiPhaseParameters);
    });
    public static final Function<class_2960, class_1921> TEXT = class_156.method_34866(texture -> class_1921.method_24049((String)"text", (class_293)class_290.field_20888, (class_293.class_5596)class_293.class_5596.field_27382, (int)786432, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_29427).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23617(false)));
    public static final class_1921 TEXT_BACKGROUND = class_1921.method_24049((String)"text_background", (class_293)class_290.field_21468, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_42517).method_34577(class_1921.field_21378).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23617(false));
    public static final Function<class_2960, class_1921> TEXT_INTENSITY = class_156.method_34866(texture -> class_1921.method_24049((String)"text_intensity", (class_293)class_290.field_20888, (class_293.class_5596)class_293.class_5596.field_27382, (int)786432, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_33628).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23617(false)));
    public static final Function<class_2960, class_1921> TEXT_POLYGON_OFFSET = class_156.method_34866(texture -> class_1921.method_24049((String)"text_polygon_offset", (class_293)class_290.field_20888, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_29427).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23607(class_1921.field_21353).method_23617(false)));
    public static final Function<class_2960, class_1921> TEXT_INTENSITY_POLYGON_OFFSET = class_156.method_34866(texture -> class_1921.method_24049((String)"text_intensity_polygon_offset", (class_293)class_290.field_20888, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_33628).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23607(class_1921.field_21353).method_23617(false)));
    public static final Function<class_2960, class_1921> TEXT_SEE_THROUGH = class_156.method_34866(texture -> class_1921.method_24049((String)"text_see_through", (class_293)class_290.field_20888, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_29428).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23604(class_1921.field_21346).method_23616(class_1921.field_21350).method_23617(false)));
    public static final class_1921 TEXT_BACKGROUND_SEE_THROUGH = class_1921.method_24049((String)"text_background_see_through", (class_293)class_290.field_21468, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_42518).method_34577(class_1921.field_21378).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23604(class_1921.field_21346).method_23616(class_1921.field_21350).method_23617(false));
    public static final Function<class_2960, class_1921> TEXT_INTENSITY_SEE_THROUGH = class_156.method_34866(texture -> class_1921.method_24049((String)"text_intensity_see_through", (class_293)class_290.field_20888, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (boolean)false, (boolean)true, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_1921.field_33629).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, false, false)).method_23615(class_1921.field_21370).method_23608((class_4668.class_4676)new Lightmap()).method_23604(class_1921.field_21346).method_23616(class_1921.field_21350).method_23617(false)));

    protected static class Lightmap
    extends class_4668.class_4676 {
        public Lightmap() {
            super(false);
            ((AccessorRenderPhase)((Object)this)).hookSetBeginAction(() -> Managers.LIGHTMAP.enable());
            ((AccessorRenderPhase)((Object)this)).hookSetEndAction(() -> Managers.LIGHTMAP.disable());
        }
    }

    protected static class DepthTest
    extends class_4668.class_4672 {
        public DepthTest() {
            super("depth_test", 519);
        }

        public void method_23516() {
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDepthFunc((int)514);
        }

        public void method_23518() {
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glDepthFunc((int)515);
            GL11.glDepthFunc((int)519);
        }
    }
}
