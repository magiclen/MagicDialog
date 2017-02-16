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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Window;

/**
 * 一般對話框設定類別，若要建立出對話框必須使用create方法。
 *
 * @author Magic Len
 */
public class Dialogs implements Cloneable {

    // -----類別介面-----
    /**
     * 對話框的按鈕事件。
     */
    public static interface ButtonEvent {

        public void onClick();
    }

    // -----類別列舉-----
    /**
     * 對話框的類型。
     */
    public static enum Type {

        NONE, INFORMATION, WARNING, ERROR, QUESTION;
    }

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
    public static Dialogs create() {
        return new Dialogs();
    }

    // -----物件常數-----
    private final ArrayList<ButtonType> buttonList = new ArrayList<>();
    private final ArrayList<ButtonEvent> buttonEventList = new ArrayList<>();

    // -----物件變數-----
    private Window owner;
    private String title = "Title";
    private String message = "Message";
    private String header = null;
    private double fontSize = FONT_SIZE;
    private String fontFamily = FONT_FAMILY;
    private Type type = Type.INFORMATION;
    private DialogAudio audio = DialogAudio.DEFAULT;

    // -----建構子-----
    /**
     * 建構子，設為private，無法提供其他類別實體化。
     */
    private Dialogs() {

    }

    // -----物件方法-----
    /**
     * 建立對話框。
     *
     * @return 傳回對話框
     */
    public Dialog createDialog() {
        final Font font = new Font(fontFamily, fontSize);
        final AlertType alertType;
        switch (type) {
            case INFORMATION:
                alertType = AlertType.INFORMATION;
                break;
            case QUESTION:
                alertType = AlertType.CONFIRMATION;
                break;
            case ERROR:
                alertType = AlertType.ERROR;
                break;
            case WARNING:
                alertType = AlertType.WARNING;
                break;
            default:
                alertType = AlertType.NONE;
                break;
        }
        final Alert dialog = new Alert(alertType);
        dialog.initOwner(owner);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(message);

        if (!buttonList.isEmpty()) {
            dialog.getButtonTypes().setAll(buttonList);
            dialog.getDialogPane().getChildren().stream().filter(node -> node instanceof ButtonBar).forEach(node -> {
                final ButtonBar buttonBar = (ButtonBar) node;
                final List<Button> buttonBarList = new ArrayList<>();
                buttonBar.getButtons().stream().filter(insideNode -> insideNode instanceof Button).forEach(insideNode -> {
                    final Button button = (Button) insideNode;
                    button.setFont(font);
                    buttonBarList.add(button);
                });
                final int buttonCount = buttonBarList.size();
                for (int i = 0; i < buttonCount; ++i) {
                    final int index = i;
                    final ButtonEvent event = buttonEventList.get(index);
                    if (event != null) {
                        buttonBarList.get(index).setOnAction((e) -> {
                            Platform.runLater(() -> {
                                event.onClick();
                            });
                        });
                    }
                }
            });
        } else {
            dialog.getDialogPane().getChildren().stream().filter(node -> node instanceof ButtonBar).forEach(node -> {
                final ButtonBar buttonBar = (ButtonBar) node;
                buttonBar.getButtons().stream().filter(insideNode -> insideNode instanceof Button).forEach(insideNode -> {
                    final Button button = (Button) insideNode;
                    button.setFont(font);
                });
            });
        }

        //設定標籤文字大小與高度
        dialog.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> {
            final Label label = (Label) node;
            label.setFont(font);
            label.setMinHeight(Region.USE_PREF_SIZE);
        });

        //加入音效
        dialog.setOnShown(e -> {
            playAudio();
        });

        return dialog;
    }

    /**
     * 顯示對話框並播放出音效，此方法之後的程式將會繼續執行，如果要取得對話框物件的參考需使用createDialog方法。
     */
    public void show() {
        final Dialog dialog = createDialog();
        dialog.show();
    }

    /**
     * 顯示對話框並播放出音效，此方法之後的程式將會暫時停止執行，如果要取得對話框物件的參考需使用createDialog方法。
     *
     * @return 傳回使用者按下的按鈕，如果沒有按，回傳null
     */
    public ButtonType showAndWait() {
        final Dialog dialog = createDialog();
        final Optional<ButtonType> buttonTypeOpt = dialog.showAndWait();
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
    public Dialogs owner(final Window owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 設定對話框的標題。
     *
     * @param title 傳入對話框的標題
     * @return 再把對話框設定傳回
     */
    public Dialogs title(final String title) {
        this.title = title;
        return this;
    }

    /**
     * 設定對話框的訊息。
     *
     * @param message 傳入對話框的訊息
     * @return 再把對話框設定傳回
     */
    public Dialogs message(final String message) {
        this.message = message;
        return this;
    }

    /**
     * 設定對話框的訊息標題。
     *
     * @param header 傳入對話框的訊息標題
     * @return 再把對話框設定傳回
     */
    public Dialogs header(final String header) {
        this.header = header;
        return this;
    }

    /**
     * 設定對話框的字體大小。
     *
     * @param fontSize 傳入對話框的字體大小
     * @return 再把對話框設定傳回
     */
    public Dialogs fontSize(final double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 設定對話框的字體樣式。
     *
     * @param fontFamily 傳入對話框的字體樣式
     * @return 再把對話框設定傳回
     */
    public Dialogs fontFamily(final String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * 設定對話框的類型，將會影響到對話框顯示出來的圖片。
     *
     * @param type 傳入對話框的類型
     * @return 再把對話框設定傳回
     */
    public Dialogs type(final Type type) {
        if (type == null) {
            this.type = Type.NONE;
        } else {
            this.type = type;
        }
        return this;
    }

    /**
     * 設定對話框的音效，音效將在使用show或是showAndWait方法時播放出來。
     *
     * @param audio 傳入對話框的音效
     * @return 再把對話框設定傳回
     */
    public Dialogs audio(final DialogAudio audio) {
        if (audio == null) {
            this.audio = DialogAudio.NONE;
        } else {
            this.audio = audio;
        }
        return this;
    }

    /**
     * 加入按鈕至對話框中。
     *
     * @param button 傳入按鈕
     * @param event 傳入按鈕的事件
     * @return 再把對話框設定傳回
     */
    public Dialogs addButton(final ButtonType button, final ButtonEvent event) {
        buttonList.add(button);
        buttonEventList.add(event);
        return this;
    }

    /**
     * 加入按鈕至對話框中。
     *
     * @param text 傳入按鈕的文字
     * @param event 傳入按鈕的事件
     * @return 再把對話框設定傳回
     */
    public Dialogs addButton(final String text, final ButtonEvent event) {
        return addButton(new ButtonType(text), event);
    }

    /**
     * 加入按鈕至對話框中。
     *
     * @param text 傳入按鈕的文字
     * @param type 傳入按鈕的類型
     * @param event 傳入按鈕的事件
     * @return 再把對話框設定傳回
     */
    public Dialogs addButton(final String text, final ButtonData type, final ButtonEvent event) {
        return addButton(new ButtonType(text, type), event);
    }

    /**
     * 複製對話框設定。
     *
     * @return 傳回設定值一樣但是為不同物件的對話框設定
     */
    @Override
    public Object clone() {
        final Dialogs dialogs = Dialogs.create();
        dialogs.audio(audio).fontSize(fontSize).fontFamily(fontFamily).owner(owner).message(message).header(header).title(title).type(type);
        final int buttonCount = buttonList.size();
        for (int i = 0; i < buttonCount; ++i) {
            dialogs.addButton(buttonList.get(i), buttonEventList.get(i));
        }
        return dialogs;
    }

    /**
     * 播放音效。
     */
    private void playAudio() {
        DialogAudio actuallyAudio = audio;
        if (audio == DialogAudio.DEFAULT) {
            switch (type) {
                case INFORMATION:
                    actuallyAudio = DialogAudio.MILD;
                    break;
                case QUESTION:
                    actuallyAudio = DialogAudio.DUDUDU;
                    break;
                case ERROR:
                    actuallyAudio = DialogAudio.HASTY;
                    break;
                case WARNING:
                    actuallyAudio = DialogAudio.DIN;
                    break;
            }
        }
        DialogAudio.playAudio(actuallyAudio);
    }
}
