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

import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Window;
import org.magiclen.magicdialog.dialogs.SimpleProgressDialog;

/**
 * 進度對話框設定類別，若要建立出對話框必須使用create方法。
 *
 * @author Magic Len
 */
public class SimpleProgressDialogs implements Cloneable {

    // -----類別介面-----
    // -----類別列舉-----
    // -----類別常數-----
    private static final Font DEFAULT_FONT = Font.getDefault();
    private static final double FONT_SIZE = DEFAULT_FONT.getSize();
    private static final String FONT_FAMILY = Font.getDefault().getFamily();

    // -----類別方法-----
    /**
     * 建立對話框的設定檔。
     *
     * @return 傳回新的對話框設定
     */
    public static SimpleProgressDialogs create() {
        return new SimpleProgressDialogs();
    }

    // -----物件變數-----
    private Window owner;
    private String message = "Loading...";
    private double fontSize = FONT_SIZE;
    private String fontFamily = FONT_FAMILY;
    private DialogAudio audio = DialogAudio.DEFAULT;

    // -----建構子-----
    /**
     * 建構子，設為private，無法提供其他類別實體化。
     */
    private SimpleProgressDialogs() {

    }

    // -----物件方法-----
    /**
     * 更改文字大小。
     *
     * @param font 傳入字型
     * @param pane 傳入容器
     */
    private void changeFontSize(final Font font, final Pane pane) {
        pane.getChildren().stream().forEach(node -> {
            if (node instanceof Pane) {
                changeFontSize(font, (Pane) node);
            } else if (node instanceof Labeled) {
                ((Labeled) node).setFont(font);
            }
        });
    }

    /**
     * 建立對話框。
     *
     * @return 傳回對話框
     */
    public SimpleProgressDialog createDialog() {
        final Font font = new Font(fontFamily, fontSize);

        final SimpleProgressDialog dialog = new SimpleProgressDialog();
        dialog.initOwner(owner);
        dialog.setMessage(message);

        dialog.centerToOwner();

        //設定標籤文字大小與高度
        changeFontSize(font, dialog.getDialogPane());

        //加入音效
        dialog.setOnShown(e -> {
            playAudio();
        });

        return dialog;
    }

    /**
     * 顯示對話框並播放出音效，此方法之後的程式將會繼續執行，如果要取得對話框物件的參考需使用createDialog方法。
     */
    private void show() {
        final SimpleProgressDialog dialog = createDialog();
        dialog.show();
    }

    /**
     * 顯示對話框並播放出音效，此方法之後的程式將會暫時停止執行，如果要取得對話框物件的參考需使用createDialog方法。
     *
     */
    public void showAndWait() {
        final SimpleProgressDialog dialog = createDialog();
        dialog.showAndWait();
    }

    /**
     * 設定擁有對話框的Window。
     *
     * @param owner 傳入擁有對話框的Window
     * @return 再把對話框設定傳回
     */
    public SimpleProgressDialogs owner(final Window owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 設定對話框的訊息。
     *
     * @param message 傳入對話框的訊息
     * @return 再把對話框設定傳回
     */
    public SimpleProgressDialogs message(final String message) {
        this.message = message;
        return this;
    }

    /**
     * 設定對話框的字體大小。
     *
     * @param fontSize 傳入對話框的字體大小
     * @return 再把對話框設定傳回
     */
    public SimpleProgressDialogs fontSize(final double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 設定對話框的字體樣式。
     *
     * @param fontFamily 傳入對話框的字體樣式
     * @return 再把對話框設定傳回
     */
    public SimpleProgressDialogs fontFamily(final String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * 設定對話框的音效，音效將在使用show或是showAndWait方法時播放出來。
     *
     * @param audio 傳入對話框的音效
     * @return 再把對話框設定傳回
     */
    public SimpleProgressDialogs audio(final DialogAudio audio) {
        if (audio == null) {
            this.audio = DialogAudio.NONE;
        } else {
            this.audio = audio;
        }
        return this;
    }

    /**
     * 複製對話框設定。
     *
     * @return 傳回設定值一樣但是為不同物件的對話框設定
     */
    @Override
    public Object clone() {
        final SimpleProgressDialogs dialogs = SimpleProgressDialogs.create();
        dialogs.audio(audio).fontSize(fontSize).fontFamily(fontFamily).owner(owner).message(message);
        return dialogs;
    }

    /**
     * 播放音效。
     */
    private void playAudio() {
        DialogAudio.playAudio(audio);
    }
}
