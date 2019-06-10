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

package net.awairo.minecraft.spawnchecker.api;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import lombok.NonNull;
import lombok.Value;

public interface Mode {
    Name name();
    ResourceLocation icon();
    default Priority priority() { return Priority.DEFAULT; }

    boolean isActive();
    default boolean isInactive() { return !isActive(); }

    default void proceedNextOption(State modeState) { }
    default void proceedPrevOption(State modeState) { }

    default void activate(State modeState) {
        modeState.hudDataRegistry().accept(new HudData.ModeActivated(this, modeState.hudShowDuration()));
    }
    default void deactivate(State modeState) { }

    Stream<Marker> update(State modeState, PlayerPos playerPos);

    default boolean isConditional() { return this instanceof Conditional; }
    default boolean isSelectable() { return this instanceof Selectable; }
    default Conditional asConditional() { return (Conditional) this; }
    default Selectable asSelectable() { return (Selectable) this; }

    interface Conditional extends Mode, Comparable<Conditional> {
        boolean canActivate(PlayerPos playerPos, Mode.State state);
        boolean canContinue(PlayerPos playerPos, Mode.State state);

        @Override
        default int compareTo(@Nonnull Conditional o) {
            return COMPARATOR.compare(this, o);
        }

        Comparator<Conditional> COMPARATOR = Comparator
            .comparing(Conditional::priority)
            .thenComparing(Conditional::name)
            .thenComparingInt(Object::hashCode);
    }

    interface Selectable extends Mode, Comparable<Selectable> {

        @Override
        default int compareTo(@Nonnull Selectable o) {
            return COMPARATOR.compare(this, o);
        }

        Comparator<Selectable> COMPARATOR = Comparator
            .comparing(Selectable::priority)
            .thenComparing(Selectable::name)
            .thenComparingInt(Object::hashCode);
    }

    interface State {
        ClientWorld worldClient();
        int tickCount();
        ScanRange.Horizontal horizontalRange();
        ScanRange.Vertical verticalRange();
        Brightness brightness();
        HudData.ShowDuration hudShowDuration();
        Consumer<HudData> hudDataRegistry();
    }

    final class Name implements Comparable<Name> {

        private final TranslationTextComponent textComponent;

        public ITextComponent textComponent() {
            return textComponent;
        }

        public String translationKey() {
            return textComponent.getKey();
        }

        public Name(@NonNull String translationKey) {
            this.textComponent = new TranslationTextComponent(translationKey);
        }

        @Override
        public int compareTo(@NonNull Name o) {
            return textComponent.getKey().compareTo(o.textComponent.getKey());
        }

        @Override
        public int hashCode() {
            return textComponent.getKey().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this ||
                obj instanceof Name && ((Name) obj).textComponent.getKey().equals(textComponent.getKey());
        }

        @Override
        public String toString() {
            return "Mode.Name(translationKey=" + textComponent.getKey() + ")";
        }
    }

    @Value
    final class Priority implements Comparable<Priority> {
        public static final int MIN = 0;
        public static final Priority DEFAULT = new Priority(0);

        private final int value;

        public Priority(int value) {
            if (value < MIN)
                throw new IllegalArgumentException("priority must be greater than or equal zero. (" + value + ")");
            this.value = value;
        }

        @Override
        public int compareTo(@NonNull Priority o) {
            // 数値の降順
            return Integer.compare(o.value, value);
        }
    }
}

