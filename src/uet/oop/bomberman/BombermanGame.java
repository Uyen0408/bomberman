package uet.oop.bomberman;

import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map.Map;

public class BombermanGame extends Application {

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();

    Entity bomberman = new Bomber(Sprite.x_bomber, Sprite.y_bomber, Sprite.player_right.getFxImage());
    Entity nextBomber = new Bomber(Sprite.x_bomber, Sprite.y_bomber, Sprite.player_right.getFxImage());
    protected static List<String> map = new Map(1).getMap();
    public static final int WIDTH = map.get(0).length();
    public static final int HEIGHT = map.size();

    private static int speed = 3; // toc do cua nhan vat

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
            }
        };
        timer.start();

        createMap();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            // System.out.println(bomberman.getX() + " " + bomberman.getY());

            if (key.getCode() == KeyCode.DOWN && nextBomber.getY() <= (HEIGHT - 2) * Sprite.SCALED_SIZE) {
                bomberman.setImg(Sprite.player_down.getFxImage());
                if (bomberman.getY() < (HEIGHT - 2) * Sprite.SCALED_SIZE)
                    nextBomber.setY(bomberman.getY() + Sprite.SCALED_SIZE / speed);
                if (checkCollision(nextBomber, stillObjects))
                    bomberman.setY(nextBomber.getY());
                else
                    nextBomber.setY(bomberman.getY());
                bomberman.setInput("DOWN");
            }
            if (key.getCode() == KeyCode.UP && nextBomber.getY() >= 1 * Sprite.SCALED_SIZE) {

                bomberman.setImg(Sprite.player_up.getFxImage());
                if (bomberman.getY() > 1 * Sprite.SCALED_SIZE)
                    nextBomber.setY(bomberman.getY() - Sprite.SCALED_SIZE / speed);
                if (checkCollision(nextBomber, stillObjects))
                    bomberman.setY(nextBomber.getY());
                else
                    nextBomber.setY(bomberman.getY());
                bomberman.setInput("UP");
            }
            if (key.getCode() == KeyCode.RIGHT & nextBomber.getX() <= (WIDTH - 2) * Sprite.SCALED_SIZE) {
                bomberman.setImg(Sprite.player_right.getFxImage());
                if (bomberman.getX() < (WIDTH - 2) * Sprite.SCALED_SIZE)
                    nextBomber.setX(bomberman.getX() + Sprite.SCALED_SIZE / speed);
                if (checkCollision(nextBomber, stillObjects))
                    bomberman.setX(nextBomber.getX());
                else
                    nextBomber.setX(bomberman.getX());
                bomberman.setInput("RIGHT");
            }
            if (key.getCode() == KeyCode.LEFT && nextBomber.getX() >= 1 * Sprite.SCALED_SIZE) {
                bomberman.setImg(Sprite.player_left.getFxImage());
                if (bomberman.getX() > 1 * Sprite.SCALED_SIZE)
                    nextBomber.setX(bomberman.getX() - Sprite.SCALED_SIZE / speed);
                if (checkCollision(nextBomber, stillObjects))
                    bomberman.setX(nextBomber.getX());
                else
                    nextBomber.setX(bomberman.getX());
                bomberman.setInput("LEFT");
            }
        });

    }

    public boolean checkCollision(Entity bomber, List<Entity> stillObjects) {
        Rectangle Obj1 = new Rectangle(bomber.getX(), bomber.getY(), Sprite.SCALED_SIZE - 1, Sprite.SCALED_SIZE - 1);
        for (int i = 0; i < stillObjects.size(); i++) {
            Rectangle Obj2 = new Rectangle(stillObjects.get(i).getX(), stillObjects.get(i).getY(),
                    Sprite.SCALED_SIZE - 1,
                    Sprite.SCALED_SIZE - 1);
            if (Obj1.intersects(Obj2) && !(stillObjects.get(i) instanceof Grass)) {
                System.out.println("va cham");
                return false;
            }
        }
        return true;
    }

    public void createMap() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Entity object;
                if (map.get(i).charAt(j) == '#') {
                    object = new Wall(j, i, Sprite.wall.getFxImage());

                } else if (map.get(i).charAt(j) == '*') {
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else {
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                }
                stillObjects.add(object);

            }
        }
    }

    public void update() {
        entities.forEach(Entity::update);
        bomberman.update();

    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bomberman.render(gc);
    }
}
