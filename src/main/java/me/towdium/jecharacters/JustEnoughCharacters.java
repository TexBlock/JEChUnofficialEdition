package me.towdium.jecharacters;

import me.towdium.jecharacters.utils.Greetings;
import me.towdium.jecharacters.utils.Match;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static me.towdium.jecharacters.JechConfig.Spell.QUANPIN;

@Mod(JustEnoughCharacters.MODID)
public class JustEnoughCharacters {
    public static final String MODID = "jecharacters";
    public static Logger logger = LogManager.getLogger(MODID);
    static boolean messageSent = false;

    public JustEnoughCharacters(IEventBus modEventBus) {
        if (FMLLoader.getDist().isClient()) {
            JechConfig.register();

            modEventBus.addListener(ModConfigEvent.class, event -> {
                Match.onConfigChange();
            });
            modEventBus.addListener(RegisterClientCommandsEvent.class, event -> {
                event.getDispatcher().register(JechCommand.getBuilder());
            });

            modEventBus.addListener(FMLConstructModEvent.class, event -> {
                Greetings.send(logger, MODID);
            });

            modEventBus.addListener(EntityJoinLevelEvent.class, event -> {
                if (event.getEntity() instanceof Player && event.getLevel().isClientSide
                        && JechConfig.enableChat.get() && !messageSent
                        && (JechConfig.enumKeyboard.get() == QUANPIN)
                        && "zh_tw".equals(Minecraft.getInstance().options.languageCode)) {
                    printMessage(Component.translatable("jecharacters.chat.taiwan"));
                    messageSent = true;
                }
            });
        }
    }

    public static void printMessage(Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }
}

