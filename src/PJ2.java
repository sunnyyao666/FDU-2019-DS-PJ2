import javafx.application.Application;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class PJ2 extends Application {
    private static double height = Screen.getPrimary().getVisualBounds().getHeight();
    private static double width = Screen.getPrimary().getVisualBounds().getWidth();
    private static int coordinate[][] = new int[28][2];
    private static int collect[][] = new int[1000][3];
    private static TextArea textArea;
    private static GraphicsContext gc;
    private static Text warning = new Text();
    private static TextField textField1;
    private static TextField textField2;
    private static ComboBox comboBox;
    private static boolean mark = true;
    private static int way = 0, tail = 0;
    private static FileWriter fileWriter;
    private static ImageView imageView = new ImageView();
    private static Image star_full, star_empty;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Map");
        primaryStage.setResizable(false);
        Pane pane = new Pane();
        Scene scene = new Scene(pane, width - 500, height / 2);
        Canvas canvas = new Canvas(width / 2, height / 2);
        gc = canvas.getGraphicsContext2D();

        BackgroundImage image = new BackgroundImage(new Image("file:./data/map.png", width / 2, height / 2, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(image));

        Text text = new Text("Map");
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(254, 235, 66, 0.3));
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);
        blend.setBottomInput(ds);
        DropShadow ds1 = new DropShadow();
        ds1.setColor(Color.web("#f13a00"));
        ds1.setRadius(20);
        ds1.setSpread(0.2);
        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);
        InnerShadow is = new InnerShadow();
        is.setColor(Color.web("#feeb42"));
        is.setRadius(9);
        is.setChoke(0.8);
        blend2.setBottomInput(is);
        InnerShadow is1 = new InnerShadow();
        is1.setColor(Color.web("#f13a00"));
        is1.setRadius(5);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);
        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        blend.setTopInput(blend1);
        text.setEffect(blend);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 60));
        text.setLayoutX(width / 2 + 120);
        text.setLayoutY(height / 2 - 450);

        Label label1 = new Label("Origin:");
        label1.setLayoutX(width / 2 + 30);
        label1.setLayoutY(height / 2 - 420);
        label1.setFont(Font.font("Verdana", 20));
        textField1 = new TextField();
        textField1.setLayoutX(width / 2 + 160);
        textField1.setLayoutY(height / 2 - 420);

        Label label2 = new Label("Destination:");
        label2.setLayoutX(width / 2 + 30);
        label2.setLayoutY(height / 2 - 390);
        label2.setFont(Font.font("Verdana", 20));
        textField2 = new TextField();
        textField2.setLayoutX(width / 2 + 160);
        textField2.setLayoutY(height / 2 - 390);

        Label label3 = new Label("Favourites:");
        label3.setLayoutX(width / 2 + 30);
        label3.setLayoutY(height / 2 - 360);
        label3.setFont(Font.font("Verdana", 20));
        comboBox = new ComboBox();
        comboBox.setLayoutX(width / 2 + 160);
        comboBox.setLayoutY(height / 2 - 360);

        warning.setFont(Font.font("Verdana", 15));
        warning.setFill(Color.WHITE);
        warning.setLayoutX(width / 2 + 40);
        warning.setLayoutY(height / 2 - 310);

        Button btn1 = new Button("Reset");
        btn1.setFont(Font.font("Verdana", 15));
        btn1.setLayoutX(width / 2 + 280);
        btn1.setLayoutY(height / 2 - 320);

        Button btn2 = new Button("Walk");
        btn2.setTextFill(Color.ORANGE);
        btn2.setFont(Font.font("Verdana", 18));
        btn2.setLayoutX(width / 2 + 40);
        btn2.setLayoutY(height / 2 - 270);

        Button btn3 = new Button("Drive");
        btn3.setTextFill(Color.ORANGE);
        btn3.setFont(Font.font("Verdana", 18));
        btn3.setLayoutX(width / 2 + 150);
        btn3.setLayoutY(height / 2 - 270);

        Button btn4 = new Button("Bus");
        btn4.setTextFill(Color.ORANGE);
        btn4.setFont(Font.font("Verdana", 18));
        btn4.setLayoutX(width / 2 + 270);
        btn4.setLayoutY(height / 2 - 270);

        Button btn5 = new Button("Return");
        btn5.setFont(Font.font("Verdana", 15));
        btn5.setLayoutX(width / 2 + 190);
        btn5.setLayoutY(height / 2 - 320);

        textArea = new TextArea();
        textArea.setLayoutX(width / 2 + 10);
        textArea.setLayoutY(height / 2 - 220);
        textArea.setPrefWidth(330);
        textArea.setPrefHeight(200);
        textArea.setEditable(false);

        star_empty = new Image("file:./data/star_empty.jpg");
        star_full = new Image("file:./data/star_full.jpg");
        imageView.setImage(star_empty);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setLayoutX(width / 2 + 280);
        imageView.setLayoutY(height / 2 - 480);

        coordinate[0][0] = 60;
        coordinate[0][1] = 200;
        coordinate[1][0] = 100;
        coordinate[1][1] = 400;
        coordinate[2][0] = 120;
        coordinate[2][1] = 500;
        coordinate[3][0] = 200;
        coordinate[3][1] = 130;
        coordinate[4][0] = 225;
        coordinate[4][1] = 200;
        coordinate[5][0] = 225;
        coordinate[5][1] = 270;
        coordinate[6][0] = 240;
        coordinate[6][1] = 350;
        coordinate[7][0] = 250;
        coordinate[7][1] = 470;
        coordinate[8][0] = 340;
        coordinate[8][1] = 60;
        coordinate[9][0] = 350;
        coordinate[9][1] = 170;
        coordinate[10][0] = 370;
        coordinate[10][1] = 250;
        coordinate[11][0] = 365;
        coordinate[11][1] = 330;
        coordinate[12][0] = 370;
        coordinate[12][1] = 450;
        coordinate[13][0] = 450;
        coordinate[13][1] = 40;
        coordinate[14][0] = 480;
        coordinate[14][1] = 130;
        coordinate[15][0] = 495;
        coordinate[15][1] = 195;
        coordinate[16][0] = 545;
        coordinate[16][1] = 255;
        coordinate[17][0] = 490;
        coordinate[17][1] = 330;
        coordinate[18][0] = 590;
        coordinate[18][1] = 315;
        coordinate[19][0] = 560;
        coordinate[19][1] = 155;
        coordinate[20][0] = 640;
        coordinate[20][1] = 95;
        coordinate[21][0] = 720;
        coordinate[21][1] = 15;
        coordinate[22][0] = 790;
        coordinate[22][1] = 85;
        coordinate[23][0] = 725;
        coordinate[23][1] = 90;
        coordinate[24][0] = 755;
        coordinate[24][1] = 255;
        coordinate[25][0] = 640;
        coordinate[25][1] = 300;
        coordinate[26][0] = 640;
        coordinate[26][1] = 150;
        coordinate[27][0] = 780;
        coordinate[27][1] = 10;

        clear();
        try {
            File file = new File("./data/distance.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            Graph graphWalk = new Graph();
            Graph graphDrive = new Graph();
            Graph graphBus = new Graph();
            String line = br.readLine();
            while (line != null) {
                char[] arrayLine = line.toCharArray();
                double weight = (int) arrayLine[4] - 48 + ((int) arrayLine[6] - 48) * 0.1 + ((int) arrayLine[7] - 48) * 0.01;
                graphWalk.setEdge(arrayLine[0], arrayLine[2], weight);
                graphDrive.setEdge(arrayLine[0], arrayLine[2], weight);
                graphBus.setEdge(arrayLine[0], arrayLine[2], weight / 0.07);
                line = br.readLine();
            }
            file = new File("./data/special.txt");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            line = br.readLine();
            while (line != null) {
                char[] arrayLine = line.toCharArray();
                if (arrayLine[0] == '0') graphDrive.del(arrayLine[2], arrayLine[1]);
                else if (arrayLine[0] == '1') {
                    graphWalk.del(arrayLine[1], arrayLine[2]);
                    graphWalk.del(arrayLine[2], arrayLine[1]);
                    graphBus.del(arrayLine[1], arrayLine[2]);
                    graphBus.del(arrayLine[2], arrayLine[1]);
                } else {
                    graphDrive.del(arrayLine[1], arrayLine[2]);
                    graphDrive.del(arrayLine[2], arrayLine[1]);
                }
                line = br.readLine();
            }
            file = new File("./data/busline.txt");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            line = br.readLine();
            while (line != null) {
                char[] arrayLine = line.toCharArray();
                graphBus.setEdge(arrayLine[0], arrayLine[1], (int) arrayLine[3] - 48);
                graphBus.setEdge(arrayLine[1], arrayLine[0], (int) arrayLine[3] - 48);
                line = br.readLine();
            }
            file = new File("./data/collect.txt");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            line = br.readLine();
            while (line != null) {
                char[] arrayLine = line.toCharArray();
                collect[tail][0] = (int) arrayLine[0];
                collect[tail][1] = (int) arrayLine[1];
                collect[tail][2] = (int) arrayLine[2] - 48;
                tail++;
                line = br.readLine();
            }

            listCheck();

            btn1.setOnAction(event -> {
                clear();
                comboBox.setValue(null);
                textField1.clear();
                textField2.clear();
            });

            btn2.setOnAction(event -> {
                clear();
                comboBox.setValue(null);
                gc.setStroke(Color.BLUE);
                way = 1;
                act(graphWalk, textField1.getText(), textField2.getText());
            });

            btn3.setOnAction(event -> {
                clear();
                comboBox.setValue(null);
                gc.setStroke(Color.GREEN);
                way = 2;
                act(graphDrive, textField1.getText(), textField2.getText());
            });

            btn4.setOnAction(event -> {
                clear();
                comboBox.setValue(null);
                gc.setStroke(Color.BLUE);
                way = 3;
                act(graphBus, textField1.getText(), textField2.getText());
            });

            btn5.setOnAction(event -> {
                if ((textField1.getText().length() == 1) && (textField2.getText().length() == 1))
                    if (way == 1) {
                        clear();
                        comboBox.setValue(null);
                        gc.setStroke(Color.BLUE);
                        act(graphWalk, textField2.getText(), textField1.getText());
                    } else if (way == 2) {
                        clear();
                        comboBox.setValue(null);
                        gc.setStroke(Color.GREEN);
                        act(graphDrive, textField2.getText(), textField1.getText());
                    } else if (way == 3) {
                        clear();
                        comboBox.setValue(null);
                        way = 3;
                        gc.setStroke(Color.BLUE);
                        act(graphBus, textField2.getText(), textField1.getText());
                        way = 0;
                    }
            });

            textField1.setOnKeyTyped(event -> {
                clear();
                comboBox.setValue(null);
                char c = check(textField1);
                if (c != ' ') {
                    gc.setFill(Color.PINK);
                    drawVertex(c);
                }
                c = check(textField2);
                if (c != ' ') {
                    gc.setFill(Color.BLUEVIOLET);
                    drawVertex(c);
                }
            });

            textField2.setOnKeyTyped(event -> {
                clear();
                comboBox.setValue(null);
                char c = check(textField1);
                if (c != ' ') {
                    gc.setFill(Color.PINK);
                    drawVertex(c);
                }
                c = check(textField2);
                if (c != ' ') {
                    gc.setFill(Color.BLUEVIOLET);
                    drawVertex(c);
                }
            });

            comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (!comboBox.getSelectionModel().isEmpty()) {
                    clear();
                    String s = comboBox.getSelectionModel().getSelectedItem().toString();
                    char c1 = s.charAt(0);
                    char c2 = s.charAt(3);
                    textField1.setText(String.valueOf(c1));
                    textField2.setText(String.valueOf(c2));
                    if (s.charAt(1) == '=') {
                        way = 3;
                        gc.setStroke(Color.BLUE);
                        shortest(graphBus, c1, c2);
                    } else if (s.charAt(5) == 'o') {
                        way = 1;
                        gc.setStroke(Color.BLUE);
                        shortest(graphWalk, c1, c2);
                    } else {
                        way = 2;
                        gc.setStroke(Color.GREEN);
                        shortest(graphDrive, c1, c2);
                    }
                    imageView.setImage(star_full);
                }
            });

            imageView.setOnMouseClicked(event -> {
                if ((textField1.getText().length() == 1) && (textField2.getText().length() == 1) && (way != 0)) {
                    char c1 = textField1.getText().charAt(0);
                    char c2 = textField2.getText().charAt(0);
                    boolean f = false;
                    for (int i = 0; i < tail; i++)
                        if (f) {
                            collect[i - 1][0] = collect[i][0];
                            collect[i - 1][1] = collect[i][1];
                            collect[i - 1][2] = collect[i][2];
                        } else if ((c1 == (char) collect[i][0]) && (c2 == (char) collect[i][1]) && (way == collect[i][2]))
                            f = true;
                    if (f) {
                        tail--;
                        imageView.setImage(star_empty);
                    } else {
                        collect[tail][0] = (int) c1;
                        collect[tail][1] = (int) c2;
                        collect[tail][2] = way;
                        tail++;
                        imageView.setImage(star_full);
                    }
                    listCheck();
                    try {
                        fileWriter = new FileWriter("./data/collect.txt");
                        for (int i = 0; i < tail; i++)
                            fileWriter.write("" + (char) collect[i][0] + (char) collect[i][1] + (char) (collect[i][2] + 48) + "\r\n");
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (Exception e) {

                    }
                }
            });

            canvas.setOnMouseClicked(event -> {
                double x = event.getSceneX();
                double y = event.getSceneY();
                char c = ' ';
                for (int i = 0; i < 26; i++)
                    if ((x - coordinate[i][0] <= 10) && (x - coordinate[i][0] >= 0) && (y - coordinate[i][1] <= 10) && (y - coordinate[i][1] > 0)) {
                        c = (char) (i + 65);
                        break;
                    }
                if (c != ' ') if (mark) {
                    textField2.setText(String.valueOf(c));
                    clear();
                    comboBox.setValue(null);
                    char c1 = check(textField1);
                    if (c1 != ' ') {
                        gc.setFill(Color.PINK);
                        gc.fillOval(coordinate[(int) c1 - 65][0], coordinate[(int) c1 - 65][1], 10, 10);
                        gc.fillText(String.valueOf(c1), coordinate[(int) c1 - 65][0] - 20, coordinate[(int) c1 - 65][1]);
                    }
                    mark = false;
                    gc.setFill(Color.BLUEVIOLET);
                    gc.fillOval(coordinate[(int) c - 65][0], coordinate[(int) c - 65][1], 10, 10);
                    gc.fillText(String.valueOf(c), coordinate[(int) c - 65][0] - 20, coordinate[(int) c - 65][1]);
                } else {
                    textField1.setText(String.valueOf(c));
                    clear();
                    comboBox.setValue(null);
                    char c1 = check(textField2);
                    if (c1 != ' ') {
                        gc.setFill(Color.BLUEVIOLET);
                        gc.fillOval(coordinate[(int) c1 - 65][0], coordinate[(int) c1 - 65][1], 10, 10);
                        gc.fillText(String.valueOf(c1), coordinate[(int) c1 - 65][0] - 20, coordinate[(int) c1 - 65][1]);
                    }
                    mark = true;
                    gc.setFill(Color.PINK);
                    gc.fillOval(coordinate[(int) c - 65][0], coordinate[(int) c - 65][1], 10, 10);
                    gc.fillText(String.valueOf(c), coordinate[(int) c - 65][0] - 20, coordinate[(int) c - 65][1]);
                }
            });
        } catch (Exception e) {

        }
        pane.getChildren().addAll(canvas, text, label1, textField1, label2, textField2, btn1, btn2, textArea, btn3, warning, btn4, btn5, imageView, comboBox, label3);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void drawVertex(char c) {
        gc.setFont(Font.font("Verdana", 20));
        gc.fillOval(coordinate[(int) c - 65][0], coordinate[(int) c - 65][1], 10, 10);
        gc.fillText(String.valueOf(c), coordinate[(int) c - 65][0] - 20, coordinate[(int) c - 65][1]);
    }

    private static char check(TextField textField) {
        String s1 = textField.getText();
        char c1 = ' ';
        if (s1.length() > 0) c1 = s1.charAt(0);
        if ((s1.length() != 1) || ((int) c1 < 65) || ((int) c1 > 90)) c1 = ' ';
        return c1;
    }

    private static void clear() {
        way = 0;
        mark = true;
        textArea.clear();
        gc.clearRect(0, 0, width / 2, height / 2);
        gc.setFill(Color.RED);
        gc.setLineWidth(3);
        for (int i = 0; i < 26; i++) drawVertex((char) (i + 65));
        warning.setFill(Color.WHITE);
        imageView.setImage(star_empty);
    }

    private static void listCheck() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < tail; i++) {
            String s = "";
            if (collect[i][0] != 0)
                if (collect[i][2] == 1) s = (char) collect[i][0] + "->" + (char) collect[i][1] + " on foot";
                else if (collect[i][2] == 2) s = (char) collect[i][0] + "->" + (char) collect[i][1] + " by car";
                else s = (char) collect[i][0] + "=>" + (char) collect[i][1] + " by bus";
            list.add(s);
        }
        ObservableList<String> obList = FXCollections.observableArrayList(list);
        comboBox.setItems(obList);
    }

    private static void act(Graph g, String s1, String s2) {
        if ((s1.length() == 0) && (s2.length() == 0)) try {
            fileWriter = new FileWriter("./data/output.txt");
            for (int i = 0; i < 25; i++) shortest(g, (char) (i + 65), '1');
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
        }
        else if (s2.length() == 0) {
            warning.setText("D missing!");
            warning.setFill(Color.RED);
            textField2.requestFocus();
        } else if (check(textField2) == ' ') {
            warning.setText("Bad D input!");
            warning.setFill(Color.RED);
            textField2.requestFocus();
        } else if (s1.length() == 0) try {
            fileWriter = new FileWriter("./data/output.txt");
            if ((s2.charAt(0) == 'Q') && (way == 2)) textArea.appendText("Can't drive to Q!");
            else shortest(g, s2.charAt(0), '0');
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
        }
        else if (check(textField1) == ' ') {
            warning.setText("Bad O input!");
            warning.setFill(Color.RED);
            textField1.requestFocus();
        } else if ((way == 2) && ((s1.charAt(0) == 'Q') || (s2.charAt(0) == 'Q')))
            textArea.appendText("Can't drive to Q!");
        else {
            shortest(g, s1.charAt(0), s2.charAt(0));
            for (int i = 0; i < tail; i++)
                if ((s1.charAt(0) == (char) collect[i][0]) && (s2.charAt(0) == (char) collect[i][1]) && (way == collect[i][2])) {
                    imageView.setImage(star_full);
                    break;
                }
        }
    }

    private static void shortest(Graph g, char s, char d) {
        double[] len = new double[26];
        String[] path = new String[26];
        for (int i = 0; i < 26; i++) {
            len[i] = 100;
            g.setMark((char) (i + 65), false);
        }
        len[(int) s - 65] = 0;
        path[(int) s - 65] = "" + s;
        for (int i = 0; i < 26; i++) {
            char v = 'A';
            for (int j = 0; j < 26; j++) {
                v = (char) (j + 65);
                if (!g.getMark(v)) {
                    v = (char) (j + 65);
                    break;
                }
            }
            for (int j = (int) v - 64; j < 26; j++)
                if ((!g.getMark((char) (j + 65))) && (len[j] < len[(int) v - 65])) v = (char) (j + 65);
            g.setMark(v, true);
            for (Edge w = g.first(v); w != null; w = g.next(w))
                if (len[(int) v - 65] + w.weight < len[(int) w.vert2 - 65]) {
                    len[(int) w.vert2 - 65] = len[(int) v - 65] + w.weight;
                    if ((way == 3) && (w.weight <= 5)) path[(int) w.vert2 - 65] = path[(int) v - 65] + "=>" + w.vert2;
                    else path[(int) w.vert2 - 65] = path[(int) v - 65] + "->" + w.vert2;
                }
        }
        for (int i = 0; i < 26; i++) len[i] = ((int) (len[i] * 100)) / 100.0;
        if (d == '0')
            if (way != 3) {
                for (int i = 0; i < 26; i++)
                    if ((i != (int) s - 65) && (len[i] != 100)) {
                        textArea.appendText((char) (i + 65) + "->" + s + " : " + len[i] + "km\n");
                        try {
                            fileWriter.write((char) (i + 65) + "->" + s + " : " + len[i] + "km\r\n");
                        } catch (Exception e) {

                        }
                    }
            } else {
                for (int i = 0; i < 26; i++)
                    if (i != (int) s - 65) {
                        textArea.appendText((char) (i + 65) + "=>" + s + " : " + len[i] + "min\n");
                        try {
                            fileWriter.write((char) (i + 65) + "=>" + s + " : " + len[i] + "min\r\n");
                        } catch (Exception e) {

                        }
                    }
            }
        else if (d == '1')
            if (way != 3) {
                for (int i = (int) s - 64; i < 26; i++)
                    if (len[i] != 100) {
                        textArea.appendText(s + "->" + (char) (i + 65) + " : " + len[i] + "km\n");
                        try {
                            fileWriter.write(s + "->" + (char) (i + 65) + " : " + len[i] + "km\r\n");
                        } catch (Exception e) {

                        }
                    }
            } else {
                for (int i = (int) s - 64; i < 26; i++) {
                    textArea.appendText(s + "=>" + (char) (i + 65) + " : " + len[i] + "min\n");
                    try {
                        fileWriter.write(s + "=>" + (char) (i + 65) + " : " + len[i] + "min\r\n");
                    } catch (Exception e) {

                    }
                }
            }
        else {
            if (way != 3) textArea.appendText(path[(int) d - 65] + " : " + len[(int) d - 65] + "km\n");
            else textArea.appendText(path[(int) d - 65] + " : " + len[(int) d - 65] + "min\n");
            gc.setFill(Color.PINK);
            drawVertex(s);
            gc.setFill(Color.BLUEVIOLET);
            drawVertex(d);
            for (int i = 0; i < path[(int) d - 65].length() - 2; i += 3) {
                char v1 = path[(int) d - 65].charAt(i);
                char v2 = path[(int) d - 65].charAt(i + 3);
                if (path[(int) d - 65].charAt(i + 1) == '=') gc.setStroke(Color.ORANGE);
                if (((v1 == 'K') && (v2 == 'T')) || ((v1 == 'T') && (v2 == 'K'))) {
                    gc.strokeLine(coordinate[(int) 'K' - 65][0] + 5, coordinate[(int) 'K' - 65][1] + 5, coordinate[(int) 'P' - 65][0] + 5, coordinate[(int) 'P' - 65][1] + 5);
                    gc.strokeLine(coordinate[(int) 'P' - 65][0] + 5, coordinate[(int) 'P' - 65][1] + 5, coordinate[(int) 'O' - 65][0] + 5, coordinate[(int) 'O' - 65][1] + 5);
                    gc.strokeLine(coordinate[(int) 'O' - 65][0] + 5, coordinate[(int) 'O' - 65][1] + 5, coordinate[(int) 'U' - 65][0] + 5, coordinate[(int) 'U' - 65][1] + 5);
                    gc.strokeLine(coordinate[(int) 'U' - 65][0] + 5, coordinate[(int) 'U' - 65][1] + 5, coordinate[26][0] + 5, coordinate[26][1] + 5);
                    gc.strokeLine(coordinate[26][0] + 5, coordinate[26][1] + 5, coordinate[(int) 'T' - 65][0] + 5, coordinate[(int) 'T' - 65][1] + 5);
                } else if (((v1 == 'R') && (v2 == 'Z')) || ((v1 == 'Z') && (v2 == 'R'))) {
                    gc.strokeLine(coordinate[(int) 'R' - 65][0] + 5, coordinate[(int) 'R' - 65][1] + 5, coordinate[(int) 'S' - 65][0] + 5, coordinate[(int) 'S' - 65][1] + 5);
                    gc.strokeLine(coordinate[(int) 'S' - 65][0] + 5, coordinate[(int) 'S' - 65][1] + 5, coordinate[(int) 'Z' - 65][0] + 5, coordinate[(int) 'Z' - 65][1] + 5);
                } else if (((v1 == 'T') && (v2 == 'U')) || ((v1 == 'T') && (v2 == 'Z')) || ((v1 == 'U') && (v2 == 'T')) || ((v1 == 'Z') && (v2 == 'T'))) {
                    gc.strokeLine(coordinate[(int) v1 - 65][0] + 5, coordinate[(int) v1 - 65][1] + 5, coordinate[26][0] + 5, coordinate[26][1] + 5);
                    gc.strokeLine(coordinate[26][0] + 5, coordinate[26][1] + 5, coordinate[(int) v2 - 65][0] + 5, coordinate[(int) v2 - 65][1] + 5);
                } else if (((v1 == 'V') && (v2 == 'W')) || ((v1 == 'W') && (v2 == 'V'))) {
                    gc.strokeLine(coordinate[(int) v1 - 65][0] + 5, coordinate[(int) v1 - 65][1] + 5, coordinate[27][0] + 5, coordinate[27][1] + 5);
                    gc.strokeLine(coordinate[27][0] + 5, coordinate[27][1] + 5, coordinate[(int) v2 - 65][0] + 5, coordinate[(int) v2 - 65][1] + 5);
                } else
                    gc.strokeLine(coordinate[(int) v1 - 65][0] + 5, coordinate[(int) v1 - 65][1] + 5, coordinate[(int) v2 - 65][0] + 5, coordinate[(int) v2 - 65][1] + 5);
                if (way == 3) gc.setStroke(Color.BLUE);
            }
        }
    }
}

class Graph {
    ArrayList<Edge>[] vertex = new ArrayList[26];
    private boolean[] mark = new boolean[26];

    Graph() {
        for (int i = 0; i < 26; i++) vertex[i] = new ArrayList<Edge>();
    }

    void setEdge(char i, char j, double weight) {
        Edge edge1 = new Edge(i, j, weight);
        vertex[(int) i - 65].add(edge1);
        Edge edge2 = new Edge(j, i, weight);
        vertex[(int) j - 65].add(edge2);
    }

    Edge first(char v) {
        if (vertex[(int) v - 65].size() != 0) return vertex[(int) v - 65].get(0);
        else return null;
    }

    Edge next(Edge e) {
        if (e == null) return null;
        if (vertex[(int) e.vert1 - 65].indexOf(e) + 1 < vertex[(int) e.vert1 - 65].size())
            return vertex[(int) e.vert1 - 65].get(vertex[(int) e.vert1 - 65].indexOf(e) + 1);
        else return null;
    }

    void setMark(char v, boolean f) {
        mark[(int) v - 65] = f;
    }

    boolean getMark(char v) {
        return mark[(int) v - 65];
    }

    void del(char vt1, char vt2) {
        for (int i = 0; i < vertex[(int) vt1 - 65].size(); i++)
            if (vertex[(int) vt1 - 65].get(i).vert2 == vt2) {
                vertex[(int) vt1 - 65].remove(i);
                break;
            }
    }
}

class Edge {
    char vert1, vert2;
    double weight;

    Edge(char vt1, char vt2, double weight) {
        vert1 = vt1;
        vert2 = vt2;
        this.weight = weight;
    }
}