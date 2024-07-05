package me.towdium.jecharacters;

import com.google.gson.GsonBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.towdium.jecharacters.JechConfig.Spell;
import me.towdium.jecharacters.utils.Match;
import me.towdium.jecharacters.utils.Profiler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static me.towdium.jecharacters.JustEnoughCharacters.printMessage;
import static net.minecraft.commands.Commands.literal;

public class JechCommand {

    static LiteralArgumentBuilder<CommandSourceStack> builder;

    static {
        builder = literal("jech")
                .executes((c) -> {
                    printMessage(Component.translatable("jecharacters.chat.help"));
                    return 0;
                })
                .then(literal("profile").executes(c -> profile()))
                .then(literal("verbose")
                        .then(literal("true").executes(c -> {
                            JechConfig.enableVerbose.set(true);
                            return 0;
                        })).then(literal("false").executes(c -> {
                            JechConfig.enableVerbose.set(false);
                            return 0;
                        })))
                .then(literal("silent").executes(c -> {
                    JechConfig.enableChat.set(false);
                    return 0;
                }))
                .then(literal("keyboard")
                        .then(literal("quanpin").executes(c -> setKeyboard(Spell.QUANPIN)))
                        .then(literal("daqian").executes(c -> setKeyboard(Spell.DAQIAN)))
                        .then(literal("xiaohe").executes(c -> setKeyboard(Spell.XIAOHE)))
                        .then(literal("ziranma").executes(c -> setKeyboard(Spell.ZIRANMA)))
                        .then(literal("sougou").executes(c -> setKeyboard(Spell.SOUGOU)))
                        .then(literal("guobiao").executes(c -> setKeyboard(Spell.GUOBIAO)))
                        .then(literal("microsoft").executes(c -> setKeyboard(Spell.MICROSOFT)))
                        .then(literal("pinyinjiajia").executes(c -> setKeyboard(Spell.PINYINPP)))
                        .then(literal("ziguang").executes(c -> setKeyboard(Spell.ZIGUANG))));
    }

    private static int setKeyboard(Spell keyboard) {
        JechConfig.enumKeyboard.set(keyboard);
        JechConfig.enableQuote.set(false);
        Match.onConfigChange();
        return 0;
    }

    private static int profile() {
        Thread t = new Thread(() -> {
            printMessage(Component.translatable("jecharacters.chat.start"));
            Profiler.Report r = Profiler.run();
            try (FileOutputStream fos = new FileOutputStream("logs/jecharacters.txt")) {
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write(new GsonBuilder().setPrettyPrinting().create().toJson(r));
                osw.flush();
                printMessage(Component.translatable("jecharacters.chat.saved"));
            } catch (IOException e) {
                printMessage(Component.translatable("jecharacters.chat.error"));
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return builder;
    }

    public static void onClientCommandRegister(IEventBus eventBus) {
        eventBus.addListener(RegisterClientCommandsEvent.class, event -> {
            event.getDispatcher().register(builder);
        });
    }
}
