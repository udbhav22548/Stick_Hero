package com.example.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.util.*;

public class StickHeroGame extends Application implements Serializable{
    private int cherries_Collected = 0;
    private double last_platform_middle;
    private boolean is_SpaceBar_Pressed = false;
    private double Plank_length = 0.0;
    private boolean is_Game_Running=true;
    private boolean is_Game_Over = false;
    private int Score=0;
    private boolean is_Mouse_Pressed = false;
    private boolean is_Plank_Save = false;
    private double screen_Position = 200;
    private double Vertical_Height = 0.0;
    private boolean rotation_complete = false;
    private int Plank_no=0;
    private List<Bridge> planks;
    private List<int[]> Ends_for_platform = new ArrayList<>();
    private StartScreen startScreen;
    private List<Cherry> cherries;
    private List<Platform> platforms;
    private int highest_Score = 0;
    private Canvas canvas;
    private GraphicsContext GC;
    private Player Hero;
    private Pane Root;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;


    public static void playSound(String filePath, float v) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGameState() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gameState.txt"))) {
            writer.println(Score);
            writer.println(highest_Score);
            writer.println(cherries_Collected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreGameState() {
        try (BufferedReader reader = new BufferedReader(new FileReader("gameState.txt"))) {
            Score = Integer.parseInt(reader.readLine());
            highest_Score = Integer.parseInt(reader.readLine());
            cherries_Collected = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stick Hero Game");
        Root = new Pane();
        Scene scene = new Scene(Root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);

        Image backgroundImage = new Image("file:background.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1000, 800, false, false, false, false)
        );
        Root.setBackground(new javafx.scene.layout.Background(background));

        canvas = new Canvas(WIDTH, HEIGHT);
        GC = canvas.getGraphicsContext2D();
        Root.getChildren().add(canvas);

        startScreen = new StartScreen(() -> startGame());
        Root.getChildren().add(startScreen);

        planks = new ArrayList<>();
        primaryStage.show();
    }
    private List<Cherry> generateRandomCherries() {
        List<Cherry> cherries = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < platforms.size() - 1; i++) {
            Platform currentPlatform = platforms.get(i);
            Platform nextPlatform = platforms.get(i + 1);
            if (random.nextInt(100) < 50) {
                double max = nextPlatform.getX()-21;
                double min = currentPlatform.getX()+currentPlatform.getWidth();
                double cherryX = random.nextDouble(max - min) + min;
                double cherryY = HEIGHT + 10 - currentPlatform.getHeight();
                cherries.add(new Cherry(cherryX, cherryY));
            }

        }
        return cherries;
    }

    private void collectCherry(Cherry cherry) {
        cherries_Collected++;
        Iterator<Cherry> iterator = cherries.iterator();
        while (iterator.hasNext()) {
            Cherry currentCherry = iterator.next();
            if (currentCherry == cherry) {
                iterator.remove();
                break;
            }
        }
        Score += 2;
    }

    private void startGame() {
        playSound("file:backgroundMusic.wav", 0.1f);
        Root.getChildren().remove(startScreen);

        platforms = generateRandomPlatforms(100);
        cherries=generateRandomCherries();
        Platform firstPlatform = platforms.get(0);
        Hero = new Player(firstPlatform.getX() + firstPlatform.getWidth() - 20, firstPlatform.getY() + 10);

        new AnimationTimer() {
            private static final double ROTATION_SPEED = 1;

            @Override
            public void handle(long now) {
                if(is_Game_Running) {
                    GC.clearRect(0, 0, WIDTH, HEIGHT);

                    // Display the score at the top
                    GC.setFill(Color.BLACK);
                    GC.setFont(Font.font(20));
                    GC.fillText("Score: " + Score, 10, 30);

                    GC.setFill(Color.RED);
                    GC.setFont(Font.font(20));
                    GC.fillText("Cherries: " + cherries_Collected, WIDTH - 150, 30);

                    // Check if the game is over
                    if (is_Game_Over) {
                        if(cherries_Collected>=3){
                            cherries_Collected-=3;
                            is_Game_Over=false;
                            Hero.setX(last_platform_middle);
                            planks.clear();
                        }
                        else {
                            displayEndScreen();
                            stop();
                        }
                    }

                    for (Platform platform : platforms) {
                        platform.generate(GC);
                    }

                    try {
                        for (Cherry cherry : new ArrayList<>(cherries)) {
                            cherry.generate(GC);

                            if (Hero.getFlipped() && (int) Hero.getX() == (int) cherry.getX()) {
                                collectCherry(cherry);
                            }
                        }
                    } catch (ConcurrentModificationException e) {

                    }

                    for (Bridge bridge : planks) {
                        rotation_complete = false;
                        bridge.update();
                        bridge.generate(GC);
                            if (bridge.getAngle() == 90 && bridge.getB_no() == Plank_no) {
                                rotation_complete = true;
                            }


                        boolean cT = isCollisonTrue();
                        if (Hero.getFlipped() && cT) {
                            is_Game_Over = true;
                        }
                        if (rotation_complete && bridge.getB_no() == Plank_no) {
                            moveNinjaAcrossBridge(bridge);
                        }
                    }

                    if (is_Mouse_Pressed) {

                        drawDynamicBridge();
                    }
                    Hero.generate(GC);

                    if (is_SpaceBar_Pressed) {
                        Hero.flip();
                        is_SpaceBar_Pressed = false;
                    }
                }
            }

            public boolean isCollisonTrue(){
                boolean isNinjaPlatformCollide = false;
                for(int[] x:Ends_for_platform){
                    if(Hero.getX()>x[0] && Hero.getX()<x[1]){
                        isNinjaPlatformCollide = true;
                    }
                }
                return isNinjaPlatformCollide;
            }

            private void moveNinjaAcrossBridge(Bridge bridge) {
                double ninjaX = Hero.getX();
                if (ninjaX < (bridge.getTopX() + Plank_length)) {
                    Hero.setX(Hero.getX() + 1);
                }
                else if(ninjaX == (bridge.getTopX() + Plank_length)){
                    boolean isOnPlatform = false;
                    for (int[] platformRange : Ends_for_platform) {
                        if (ninjaX >= platformRange[0] && ninjaX <= platformRange[1]) {
                            isOnPlatform = true;
                            Score++;
                            screen_Position = platformRange[1] - WIDTH / 2;
                            updateEntities();
                            Hero.setX(Hero.getX() + 1);
                            break;
                        }
                    }

                    if (!isOnPlatform) {
                        for (int[] platformRange : Ends_for_platform) {
                            if (ninjaX > platformRange[1]) {
                                last_platform_middle= (double) (platformRange[1] + platformRange[0]) /2;
                            }
                            else{
                                break;
                            }
                        }
                        is_Game_Over = true;

                    }
                }
            }
            private void updateEntities() {
                Hero.setX(Hero.getX() - screen_Position);
                for (Bridge bridge : planks) {
                    bridge.setX(bridge.getX() - screen_Position);
                }
                for (Platform platform : platforms) {
                    platform.setX(platform.getX() - screen_Position);
                }
                for (Cherry cherry : cherries) {
                    cherry.setX(cherry.getX() - screen_Position);
                }
                updatePlatformEnds();
            }
        }.start();

        Scene scene = canvas.getScene();
        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                is_Mouse_Pressed = true;
            }
        });

        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                is_Mouse_Pressed = false;
                saveBar();
            }
        });
        Scene scene2 = canvas.getScene();
        scene2.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                is_SpaceBar_Pressed=true;
            } else if (event.getCode() == KeyCode.S) {
                saveGameState();
            } else if (event.getCode()==KeyCode.R) {
                restoreGameState();
            }
        });
        scene2.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                is_SpaceBar_Pressed=false;
            }
        });
    }
    private void updatePlatformEnds() {
        Ends_for_platform.clear();
        for (Platform platform : platforms) {
            int startX = platform.getX_int();
            int endX = startX + (int)platform.getWidth();
            Ends_for_platform.add(new int[]{startX, endX});
        }
    }
    private void displayEndScreen() {
        if (Score > highest_Score) {
            highest_Score = Score;
        }
        EndScreen endScreen = new EndScreen(Score,cherries_Collected,highest_Score,() -> restartGame());
        Root.getChildren().add(endScreen);
    }
    private void restartGame() {
        is_Game_Over = false;
        is_Plank_Save = false;
        Score = 0;
        planks.clear();
        platforms.clear();
        Ends_for_platform.clear();
        screen_Position = 200;

        Root.getChildren().removeIf(node -> node instanceof EndScreen);

        startGame();
    }

    private void saveBar() {
        Plank_no+=1;
        Bridge newBridge = new Bridge(Hero.getX(), Vertical_Height, Color.BROWN,Plank_no);
        planks.add(newBridge);
        Plank_length=Vertical_Height;

        is_Plank_Save = true;

        Vertical_Height = 0.0;
    }

    private void drawDynamicBridge() {
        Vertical_Height += 5;
        GC.setFill(Color.BROWN);
        GC.fillRect(Hero.getX() - 5, HEIGHT - 100 - Vertical_Height, 10, Vertical_Height);

    }

    private List<Platform> generateRandomPlatforms(int count) {
        List<Platform> platforms = new ArrayList<>();
        int x = (int) screen_Position;

        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int platformWidth = random.nextInt(100) + 50;
            platforms.add(new Platform(x, platformWidth,100));
            Ends_for_platform.add(new int[]{x, x + platformWidth});
            x += platformWidth + random.nextInt(150) + 50;
        }
        return platforms;
    }
}