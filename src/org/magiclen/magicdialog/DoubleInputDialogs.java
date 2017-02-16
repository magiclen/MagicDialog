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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Window;

/**
 * 輸入對話框設定類別，若要建立出對話框必須使用create方法。
 *
 * @author Magic Len
 */
public class DoubleInputDialogs implements Cloneable {

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
    public static DoubleInputDialogs create() {
        return new DoubleInputDialogs();
    }

    // -----物件常數-----
    // -----物件變數-----
    private Window owner;
    private String title = "Title";
    private String message1 = null;
    private String message2 = null;
    private String header = null;
    private String text1 = null;
    private String text2 = null;
    private double fontSize = FONT_SIZE;
    private String fontFamily = FONT_FAMILY;
    private DialogAudio audio = DialogAudio.DEFAULT;

    // -----建構子-----
    /**
     * 建構子，設為private，無法提供其他類別實體化。
     */
    private DoubleInputDialogs() {

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
    public Dialog<String[]> createDialog() {
        final Font font = new Font(fontFamily, fontSize);

        final Dialog<String[]> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setGraphic(new ImageView(this.getClass().getResource("/org/magiclen/magicdialog/did.png").toString()));

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        final TextField tf1 = new TextField();
        tf1.setPromptText(text1);
        tf1.setMaxWidth(Integer.MAX_VALUE);
        final TextField tf2 = new TextField();
        tf2.setPromptText(text2);
        tf2.setMaxWidth(Integer.MAX_VALUE);

        grid.add(new Label(message1), 0, 0);
        grid.add(tf1, 1, 0);
        grid.add(new Label(message2), 0, 1);
        grid.add(tf2, 1, 1);

        dialog.getDialogPane().setContent(grid);

        tf1.requestFocus();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new String[]{tf1.getText(), tf2.getText()};
            }
            return null;
        });

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
    public String[] showAndWait() {
        final Dialog dialog = createDialog();
        final Optional<String[]> buttonTypeOpt = dialog.showAndWait();
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
    public DoubleInputDialogs owner(final Window owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 設定對話框的標題。
     *
     * @param title 傳入對話框的標題
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs title(final String title) {
        this.title = title;
        return this;
    }

    /**
     * 設定對話框的訊息。
     *
     * @param message1 傳入對話框第一個訊息
     * @param message2 傳入對話框第二個訊息
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs message(final String message1, final String message2) {
        this.message1 = message1;
        this.message2 = message2;
        return this;
    }

    /**
     * 設定對話框的訊息標題。
     *
     * @param header 傳入對話框的訊息標題
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs header(final String header) {
        this.header = header;
        return this;
    }

    /**
     * 設定對話框輸入方塊的預設文字內容。
     *
     * @param text1 傳入對話框輸入方塊的預設文字內容
     * @param text2 傳入對話框輸入方塊的預設文字內容
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs text(final String text1, final String text2) {
        this.text1 = text1;
        this.text2 = text2;
        return this;
    }

    /**
     * 設定對話框的字體大小。
     *
     * @param fontSize 傳入對話框的字體大小
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs fontSize(final double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 設定對話框的字體樣式。
     *
     * @param fontFamily 傳入對話框的字體樣式
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs fontFamily(final String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * 設定對話框的音效，音效將在使用show或是showAndWait方法時播放出來。
     *
     * @param audio 傳入對話框的音效
     * @return 再把對話框設定傳回
     */
    public DoubleInputDialogs audio(final DialogAudio audio) {
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
        final DoubleInputDialogs dialogs = DoubleInputDialogs.create();
        dialogs.audio(audio).fontSize(fontSize).fontFamily(fontFamily).owner(owner).message(message1, message2).header(header).title(title).text(text1, text2);
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
