package me.towdium.jecharacters;

import me.towdium.jecharacters.utils.Greetings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static me.towdium.jecharacters.JechConfig.Spell.QUANPIN;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(JustEnoughCharacters.MODID)
public class JustEnoughCharacters {
    public static final String MODID = "jecharacters";
    public static Logger logger = LogManager.getLogger(MODID);
    static boolean messageSent = false;

    public JustEnoughCharacters() {
        JechConfig.register();
    }

    @SubscribeEvent
    public static void onConstruct(FMLConstructModEvent event) {
        Greetings.send(logger, MODID);
    }

    public static void printMessage(Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }

    @Mod.EventBusSubscriber
    static class EventHandler {
        @SubscribeEvent
        public static void onPlayerLogin(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Player && event.getLevel().isClientSide
                    && JechConfig.enableChat.get() && !messageSent
                    && (JechConfig.enumKeyboard.get() == QUANPIN)
                    && "zh_tw".equals(Minecraft.getInstance().options.languageCode)) {
                printMessage(Component.translatable("jecharacters.chat.taiwan"));
                messageSent = true;
            }
        }
    }
}

