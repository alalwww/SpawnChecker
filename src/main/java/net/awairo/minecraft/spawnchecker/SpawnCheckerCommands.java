/*
 * SpawnChecker
 * Copyright (C) 2019 alalwww
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.awairo.minecraft.spawnchecker;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;

@Log4j2
final class SpawnCheckerCommands {
    private static final int ALL = 0;
    private static final int INTEGRATED_SERVER_OP = 2;
    private static final int DEDICATED_SERVER_OP = 4;

    private final SpawnCheckerConfig config;

    private static final ITextComponent TO_ENABLE =
        new TranslationTextComponent("spawnchecker.command.message.toEnabled");
    private static final ITextComponent TO_DISABLE =
        new TranslationTextComponent("spawnchecker.command.message.toDisabled");

    private static final ITextComponent GUIDELINE_ON =
        new TranslationTextComponent("spawnchecker.command.message.guidelineOn");
    private static final ITextComponent GUIDELINE_OFF =
        new TranslationTextComponent("spawnchecker.command.message.guidelineOff");

    private final CommandDispatcher<Source> dispatcher = new CommandDispatcher<>();

    SpawnCheckerCommands(@NonNull SpawnCheckerConfig config) {
        this.config = config;
        dispatcher.register(builder());
    }

    private Source commandSource;

    @SuppressWarnings("unchecked")
    void registerTo(@NonNull ClientPlayerEntity player) {
        this.commandSource = new Source(player.connection.getSuggestionProvider());
        player.connection.getCommandDispatcher()
            .register((LiteralArgumentBuilder<ISuggestionProvider>) (LiteralArgumentBuilder<?>) builder());
    }

    boolean parse(String message) {
        val res = dispatcher.parse(message.substring(1), commandSource);
        log.debug("res: {}", res.getContext());
        try {
            dispatcher.execute(res);
            return true;
        } catch (CommandSyntaxException e) {
            log.debug("Failed execute command.", e);
            return false;
        }
    }

    private LiteralArgumentBuilder<Source> builder() {
        return literal("spawnchecker")
            .then(literal("on").executes(ctx -> success(ctx, config::enable, TO_ENABLE)))
            .then(literal("off").executes(ctx -> success(ctx, config::disable, TO_DISABLE)))
            .then(literal("guideline")
                .then(literal("on").executes(ctx -> success(ctx, config.presetModeConfig()::guidelineOn, GUIDELINE_ON)))
                .then(literal("off").executes(ctx -> success(ctx, config.presetModeConfig()::guidelineOff, GUIDELINE_OFF)))
            )
            ;
    }

    private int success(CommandContext<Source> ctx, Runnable runnable, ITextComponent message) {
        log.debug("do executes: {}, {}", ctx, message);
        runnable.run();
        ctx.getSource().sendFeedback(message);
        return Command.SINGLE_SUCCESS;
    }

    private LiteralArgumentBuilder<Source> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    @RequiredArgsConstructor
    private static final class Source implements ISuggestionProvider {
        private final ClientSuggestionProvider underlying;

        void sendFeedback(ITextComponent message) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendMessage(message, Util.DUMMY_UUID);
            }
        }

        @Override
        @Nonnull
        public Collection<String> getPlayerNames() {
            return underlying.getPlayerNames();
        }

        @Override
        public Collection<String> getTargetedEntity() {
            return underlying.getTargetedEntity();
        }

        @Override
        public Collection<Coordinates> func_217294_q() {
            return underlying.func_217294_q();
        }

        @Override
        public Collection<Coordinates> func_217293_r() {
            return underlying.func_217293_r();
        }

        @Override
        public DynamicRegistries func_241861_q() {
            return underlying.func_241861_q();
        }

        @Override
        @Nonnull
        public Collection<String> getTeamNames() {
            return underlying.getTeamNames();
        }

        @Override
        @Nonnull
        public Collection<ResourceLocation> getSoundResourceLocations() {
            return underlying.getSoundResourceLocations();
        }

        @Override
        @Nonnull
        public Stream<ResourceLocation> getRecipeResourceLocations() {
            return underlying.getRecipeResourceLocations();
        }

        @Override
        @Nonnull
        public CompletableFuture<Suggestions> getSuggestionsFromServer(
            @Nonnull CommandContext<ISuggestionProvider> context, @Nonnull SuggestionsBuilder suggestionsBuilder) {
            return underlying.getSuggestionsFromServer(context, suggestionsBuilder);
        }

        @Override
        public Set<RegistryKey<World>> func_230390_p_() {
            return underlying.func_230390_p_();
        }

        @Override
        public boolean hasPermissionLevel(int permissionLevel) {
            return underlying.hasPermissionLevel(permissionLevel);
        }
    }
}
