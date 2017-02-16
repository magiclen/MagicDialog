MagicDialog
=================================

# Introduction

**MagicDialog** is a Java library used for creating dialogs quickly in JavaFX applications. It supports alert(information, warning, confirmation, error) dialogs, input dialogs and choice dialogs. In addition, **MagicDialog** includes [MagicAudioPlayer](https://github.com/magiclen/MagicAudioPlayer "MagicAudioPlayer") to produce sound when the dialog is shown.

# Usage

## Dialogs class

**Dialogs** class is in the *org.magiclen.magicdialog* package. It can help you create an alert dialog quickly.

### Initialize

To create an **Dialog** instance, you can use the static method `create` in **Dialogs** class and make some settings before you show it.

Here is an example to create and show an alert dialog,

    Dialogs.create()
            .addButton("Yes", null)
            .addButton("No", () -> {
                Platform.exit(); // Exit this problem.
            })
            .audio(DialogAudio.COCOCO)
            .fontSize(15)
            .owner(primaryStage)
            .message("If you don't want to continue, this application will be stopped.")
            .header("Do you want to continue?")
            .title("Hint")
            .type(Dialogs.Type.QUESTION)
            .showAndWait();

You don't need to set up all the options.

## InputDialogs class

**InputDialogs** class is in the *org.magiclen.magicdialog* package. It can help you create an input dialog quickly.

### Initialize

To create an **Dialog** instance, you can use the static method `create` in **InputDialogs** class and make some settings before you show it.

Here is an example to create and show an input dialog,

    final String inputText = InputDialogs.create()
            .audio(DialogAudio.DIN)
            .fontSize(15)
            .owner(primaryStage)
            .message("Input your name:")
            .header("What is your name?")
            .title("Input")
            .text("Magic Len")
            .showAndWait();

Again, you don't need to set up all the options.

## DoubleInputDialogs class

**DoubleInputDialogs** class is in the *org.magiclen.magicdialog* package. It can help you create an input dialog quickly. In comparison with **InputDialogs** class, **DoubleInputDialogs** class can allow user to input two rows of data but **InputDialogs** class cannot.

### Initialize

To create an **Dialog** instance, you can use the static method `create` in **DoubleInputDialogs** class and make some settings before you show it.

Here is an example to create and show an input dialog,

    final String[] inputText = DoubleInputDialogs.create()
            .audio(DialogAudio.DIN)
            .fontSize(15)
            .fontFamily("Noto Sans TC Regular")
            .owner(primaryStage)
            .message("Input your name:", "Input your website:")
            .header("What is your name?")
            .title("Input")
            .text("Magic Len", "magiclen.org")
            .showAndWait();

Again, you don't need to set up all the options.

## ChoiceDialogs class

**ChoiceDialogs** class is in the *org.magiclen.magicdialog* package. It can help you create a choice dialog quickly.

### Initialize

To create an **Dialog** instance, you can use the static method `create` in **ChoiceDialogs** class and make some settings before you show it.

Here is an example to create and show a choice dialog,

    final String choose = ChoiceDialogs.create()
            .audio(DialogAudio.DEFAULT)
            .fontSize(15)
            .owner(primaryStage)
            .message(null)
            .header("How many people are in your family?")
            .title("Choose")
            .options("One", "Two", "Three", "Four", "Five")
            .defaultOption("Three")
            .showAndWait();

Again, you don't need to set up all the options.

## SimpleProgressDialogs class

**SimpleProgressDialogs** class is in the *org.magiclen.magicdialog* package. It can help you create a simple progress dialog quickly.

To create an **SimpleProgressDialog** instance, you can use the static method `create` in **SimpleProgressDialogs** class and make some settings before you show it.

Here is an example to create and show a simple progress dialog,

### Initialize

    SimpleProgressDialogs.create()
            .audio(DialogAudio.DEFAULT)
            .fontSize(20)
            .owner(primaryStage)
            .message("Please wait...")
            .showAndWait();

Again, you don't need to set up all the options.

# License

    Copyright 2015-2017 magiclen.org

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

# What's More?

Please check out our web page at

https://magiclen.org/javafx-dialog/
