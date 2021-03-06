/*
 * Copyright (C) 2020 The Android Ice Cold Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package james.dsp.tile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import james.dsp.activity.DSPManager;

import static james.dsp.service.HeadsetService.getAudioOutputRouting;

public class JamesTile extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        boolean active = isActive();

        getQsTile().setState(active ? Tile.STATE_INACTIVE : Tile.STATE_ACTIVE);
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        boolean active = isActive();

        getQsTile().setState(active ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        String mode = getAudioOutputRouting();
        SharedPreferences sharedPrefs = getSharedPreferences(DSPManager.SHARED_PREFERENCES_BASENAME + "." + mode, 0);

        boolean active = isActive();

        sharedPrefs.edit().putBoolean("dsp.masterswitch.enable", !active).apply();
        sendBroadcast(new Intent(DSPManager.ACTION_UPDATE_PREFERENCES));

        getQsTile().setState(active ? Tile.STATE_INACTIVE : Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }

    private boolean isActive() {
        String mode = getAudioOutputRouting();
        SharedPreferences sharedPrefs = getSharedPreferences(DSPManager.SHARED_PREFERENCES_BASENAME + "." + mode, 0);

        return sharedPrefs.getBoolean("dsp.masterswitch.enable", false);
    }
}
