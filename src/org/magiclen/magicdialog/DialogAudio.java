/*
 *
 * Copyright 2015-2017 magiclen.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.magiclen.magicdialog;

import java.util.Objects;
import org.magiclen.magicaudioplayer.AudioPlayer;

/**
 * 對話框的聲音類別。
 *
 * @author Magic Len
 */
public final class DialogAudio {

    // -----類別常數-----
    public static final DialogAudio NONE = new DialogAudio("none");
    public static final DialogAudio DEFAULT = new DialogAudio("default");
    public static final DialogAudio BUBUBU = new DialogAudio("bububu");
    public static final DialogAudio COCOCO = new DialogAudio("cococo");
    public static final DialogAudio DIN = new DialogAudio("din");
    public static final DialogAudio DING_H_DING_L = new DialogAudio("ding_h_ding_l");
    public static final DialogAudio DING_L_DING_H = new DialogAudio("ding_l_ding_h");
    public static final DialogAudio DINGDING = new DialogAudio("dingding");
    public static final DialogAudio DUDU = new DialogAudio("dudu");
    public static final DialogAudio DUDUDU = new DialogAudio("dududu");
    public static final DialogAudio EMERGE = new DialogAudio("emerge");
    public static final DialogAudio HASTY = new DialogAudio("hasty");
    public static final DialogAudio LOSE = new DialogAudio("lose");
    public static final DialogAudio MILD = new DialogAudio("mild");
    public static final DialogAudio SLUMP = new DialogAudio("slump");
    public static final DialogAudio VICTORY = new DialogAudio("victory");

    // -----類別方法-----
    /**
     * 播放音效。
     *
     * @param audio 傳入要播放的音效
     */
    public static void playAudio(final DialogAudio audio) {
        if (audio == null) {
            return;
        }
        final String audioName = audio.audio;
        final String audioFileName;
        switch (audioName) {
            case "none":
                return;
            case "default":
                audioFileName = EMERGE.audio.concat(".wav");
                break;
            default:
                audioFileName = audioName.concat(".wav");
                break;
        }
        try {
            final AudioPlayer ap = AudioPlayer.createPlayer(audio.getClass().getResource("/org/magiclen/magicdialog/".concat(audioFileName)));
            ap.setAutoClose(true);
            ap.play();
        } catch (final Exception ex) {
        }
    }

    // -----物件常數-----
    private final String audio;

    // -----建構子-----
    /**
     * 私有的建構子，將無法被外部實體化。
     */
    private DialogAudio(final String audio) {
        this.audio = audio;
    }

    // -----物件方法-----
    /**
     * 播放音效。
     *
     */
    public void playAudio() {
        DialogAudio.playAudio(this);
    }

    @Override
    public int hashCode() {
        return 43 * audio.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DialogAudio other = (DialogAudio) obj;
        return Objects.equals(this.audio, other.audio);
    }
}
