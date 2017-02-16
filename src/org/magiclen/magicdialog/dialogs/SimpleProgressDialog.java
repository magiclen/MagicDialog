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
package org.magiclen.magicdialog.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * 簡易的進度對話框。
 *
 * @author Magic Len
 */
public class SimpleProgressDialog extends Stage {

    // -----類別常數-----
    private static final int WIDTH = 480;
    private static final int HEIGHT = 220;
    private static final int GAP = 8;
    private static final double DOUBLE_GAP = GAP * 2;
    private static final int SHADOW_SIZE = 50;
    private static final double PADDING = DOUBLE_GAP + SHADOW_SIZE;

    // -----物件常數-----
    private final Insets insets, insetsPadding;
    private final Scene scene;
    private final StackPane stackPane;
    private final FlowPane fpMain;
    private final ProgressIndicator piLoading;
    private final Label lMessage;

    // -----建構子-----
    public SimpleProgressDialog() {
        insets = new Insets(GAP, GAP, GAP, GAP);
        insetsPadding = new Insets(PADDING, PADDING, PADDING, PADDING);

        piLoading = new ProgressIndicator();
        piLoading.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);

        lMessage = new Label();
        lMessage.setAlignment(Pos.CENTER_LEFT);
        lMessage.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);

        FlowPane.setMargin(piLoading, insets);
        FlowPane.setMargin(lMessage, insets);
        fpMain = new FlowPane();
        fpMain.getChildren().addAll(piLoading, lMessage);
        fpMain.setAlignment(Pos.CENTER);
        fpMain.setPadding(insetsPadding);
        fpMain.setStyle(String.format("-fx-background-color: white; -fx-effect: dropshadow(gaussian, gray, %d, 0, 0, 0); -fx-background-insets: %d;", SHADOW_SIZE, SHADOW_SIZE));

        stackPane = new StackPane(fpMain);
        stackPane.setStyle(String.format("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-insets: %d;", SHADOW_SIZE));

        scene = new Scene(stackPane, WIDTH, HEIGHT, Color.TRANSPARENT);

        setResizable(false);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.WINDOW_MODAL);

        centerToOwner();
    }

    // -----物件方法-----
    /**
     * 設定訊息。
     *
     * @param message 傳入要顯示的訊息
     */
    public void setMessage(final String message) {
        lMessage.setText(message);
    }

    /**
     * 取得對話框的窗格物件。
     *
     * @return 傳回對話框的窗格物件
     */
    public Pane getDialogPane() {
        return fpMain;
    }

    /**
     * 置中。
     */
    public final void centerToOwner() {
        final Window owner = getOwner();
        if (owner != null) {
            final double width = WIDTH;
            final double height = HEIGHT;
            final double ownerWidth = owner.getWidth();
            final double ownerHeight = owner.getHeight();
            final double ownerX = owner.getX();
            final double ownerY = owner.getY();
            double x = 0, y = 0;
            x = ownerX + (ownerWidth - width) / 2f;
            y = ownerY + (ownerHeight - height) / 2f;

            setX(x);
            setY(y);
        } else {
            //螢幕置中
            final Rectangle2D screen = Screen.getPrimary().getVisualBounds();
            setX((screen.getWidth() - WIDTH) / 2);
            setY((screen.getHeight() - HEIGHT) / 2);
        }
    }
}
