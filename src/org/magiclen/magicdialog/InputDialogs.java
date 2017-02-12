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

import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Window;

/**
 * 輸入對話框設定類別，若要建立出對話框必須使用create方法。
 *
 * @author Magic Len
 */
public class InputDialogs implements Cloneable {

    // -----類別介面-----
    // -----類別列舉-----
    // -----類別常數-----
    private static final int FONT_SIZE = 16;

    // -----類別方法-----
    /**
     * 建立對話框的設定檔。
     *
     * @return 傳回新的對話框設定
     */
    public static InputDialogs create() {
        return new InputDialogs();
    }

    // -----物件變數-----
    private Window owner;
    private String title = "Title";
    private String message = null;
    private String header = null;
    private String text = null;
    private double fontSize = FONT_SIZE;
    private DialogAudio audio = DialogAudio.DEFAULT;

    // -----建構子-----
    /**
     * 建構子，設為private，無法提供其他類別實體化。
     */
    private InputDialogs() {

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
            } else if (node instanceof TextField) {
                ((TextField) node).setFont(font);
            } else if (node instanceof ButtonBar) {
                ((ButtonBar) node).getButtons().stream().filter(insideNode -> insideNode instanceof Button).forEach(insideNode -> {
                    final Button button = (Button) insideNode;
                    button.setFont(font);
                });
            }
        });
    }

    /**
     * 建立對話框。
     *
     * @return 傳回對話框
     */
    public Dialog createDialog() {
        final Font font = new Font(fontSize);

        final TextInputDialog dialog = new TextInputDialog(text);
        dialog.initOwner(owner);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(message);

        //設定標籤文字大小與高度
        changeFontSize(font, dialog.getDialogPane());

        //加入音效
        dialog.setOnShown(e -> {
            playAudio();
        });

        return dialog;
    }

    /**
     * 顯示對話框並播放出音效，此方法之後的程式將會暫時停止執行，如果要取得對話框物件的參考需使用createDialog方法。
     *
     * @return 傳回使用者輸入的文字，如果沒有輸入，回傳null
     */
    public String showAndWait() {
        final Dialog dialog = createDialog();
        final Optional<String> buttonTypeOpt = dialog.showAndWait();
        if (buttonTypeOpt.isPresent()) {
            return buttonTypeOpt.get();
        }
        return null;
    }

    /**
     * 設定擁有對話框的Window。
     *
     * @param owner 傳入擁有對話框的Window
     * @return 再把對話框設定傳回
     */
    public InputDialogs owner(final Window owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 設定對話框的標題。
     *
     * @param title 傳入對話框的標題
     * @return 再把對話框設定傳回
     */
    public InputDialogs title(final String title) {
        this.title = title;
        return this;
    }

    /**
     * 設定對話框的訊息。
     *
     * @param message 傳入對話框的訊息
     * @return 再把對話框設定傳回
     */
    public InputDialogs message(final String message) {
        this.message = message;
        return this;
    }

    /**
     * 設定對話框的訊息標題。
     *
     * @param header 傳入對話框的訊息標題
     * @return 再把對話框設定傳回
     */
    public InputDialogs header(final String header) {
        this.header = header;
        return this;
    }

    /**
     * 設定對話框輸入方塊的預設文字內容。
     *
     * @param text 傳入對話框輸入方塊的預設文字內容
     * @return 再把對話框設定傳回
     */
    public InputDialogs text(final String text) {
        this.text = text;
        return this;
    }

    /**
     * 設定對話框的字體大小。
     *
     * @param fontSize 傳入對話框的字體大小
     * @return 再把對話框設定傳回
     */
    public InputDialogs fontSize(final double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 設定對話框的音效，音效將在使用show或是showAndWait方法時播放出來。
     *
     * @param audio 傳入對話框的音效
     * @return 再把對話框設定傳回
     */
    public InputDialogs audio(final DialogAudio audio) {
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
        final InputDialogs dialogs = InputDialogs.create();
        dialogs.audio(audio).fontSize(fontSize).owner(owner).message(message).header(header).title(title).text(text);
        return dialogs;
    }

    /**
     * 播放音效。
     */
    private void playAudio() {
        DialogAudio actuallyAudio = audio;
        if (audio == DialogAudio.DEFAULT) {
            actuallyAudio = DialogAudio.SLUMP;
        }
        DialogAudio.playAudio(actuallyAudio);
    }

}
