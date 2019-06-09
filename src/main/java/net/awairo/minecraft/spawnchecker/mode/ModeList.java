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

package net.awairo.minecraft.spawnchecker.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.api.PlayerPos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import lombok.var;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class ModeList {

    private final List<Mode.Selectable> selectableModes = new ArrayList<>();
    private final List<Mode.Conditional> conditionalModes = new ArrayList<>();

    private int index = 0;
    private int scheduledIndex = 0;
    private Mode previous = null;

    @Getter
    private Mode current;

    void add(Mode mode) {

        if (mode.isSelectable()) {
            selectableModes.add(mode.asSelectable());
            Collections.sort(selectableModes);
            return;
        }

        if (mode.isConditional()) {
            conditionalModes.add(mode.asConditional());
            Collections.sort(conditionalModes);
            return;
        }

        throw new IllegalArgumentException("unreachable code.");
    }

    void scheduleChangeNextMode() {
        scheduledIndex++;
    }

    void scheduleChangePrevMode() {
        scheduledIndex--;
    }

    Result updateList(PlayerPos playerPos, Mode.State state) {
        if (scheduledIndex != 0) {
            log.debug("index={}, scheduledIndex={}", index, scheduledIndex);
            var nextIndex = index;
            if (current.isConditional())
                popPreviousMode(state);

            // 進む方向に予定されてる回数だけ次に選択するindexを増加させる
            while (scheduledIndex > 0) {
                scheduledIndex--;
                if (nextIndex + 1 < selectableModes.size())
                    nextIndex++;
            }
            // 戻る方向に予定されてる回数だけ次に選択するindexを減少させる
            while (scheduledIndex < 0) {
                scheduledIndex++;
                if (nextIndex > 0)
                    nextIndex--;
            }
            // indexが変わる操作だった変更して終了
            if (index != nextIndex) {
                index = nextIndex;
                if (current.isActive())
                    current.deactivate(state);
                current = currentSelectableMode();
                return Result.CHANGED;
            }
        }

        if (current.isSelectable()) {
            val foundMode = findModeThatCanBeActivated(playerPos, state);
            if (foundMode.isPresent()) {
                pushConditionalMode(foundMode.get(), state);
                return Result.CHANGED;
            }
            return Result.NOT_CHANGED;
        }

        if (current.isConditional()) {
            if (((Mode.Conditional) current).canContinue(playerPos, state)) {
                return Result.NOT_CHANGED;
            }
            popPreviousMode(state);
            return Result.CHANGED;
        }

        return Result.NOT_CHANGED;
    }

    void selectBy(Mode.Name name) {
        // noinspection OptionalGetWithoutIsPresent
        val found = selectableModes.stream()
            .filter(mode -> mode.name().equals(name))
            .findFirst()
            .orElseGet(() -> selectableModes.stream()
                .filter(mode -> mode.name().equals(SpawnCheckMode.NAME))
                .findFirst()
                .get() // プリセットモードなのでかならず見つかる想定
            );

        current = found;
        index = selectableModes.indexOf(found);
    }

    private void pushConditionalMode(Mode.Conditional mode, Mode.State state) {
        if (current.isActive())
            current.deactivate(state);
        previous = current;
        current = mode;
    }

    private void popPreviousMode(Mode.State state) {
        if (current.isActive())
            current.deactivate(state);
        current = previous;
        previous = null;
    }

    private Mode.Selectable currentSelectableMode() {
        return selectableModes.get(index);
    }

    private Optional<Mode.Conditional> findModeThatCanBeActivated(PlayerPos playerPos, Mode.State state) {
        return conditionalModes.stream()
            .filter(mode -> mode.canActivate(playerPos, state))
            .findFirst();
    }

    @Getter
    @RequiredArgsConstructor
    enum Result {
        CHANGED(true),
        NOT_CHANGED(false);
        private final boolean changed;
    }
}
