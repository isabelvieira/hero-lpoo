import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {

    Hero hero;

    private int width;
    private int height;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;
    private boolean finished;

    Arena(int width, int height){

        hero = new Hero(10,10);
        this.width = width;
        this.height = height;

        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
        this.finished = false;
    }

    private List<Wall> createWalls(){
        List<Wall> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }

        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }

        return walls;
    }

    private List<Coin> createCoins(){
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            while(true) {
                boolean invalid = false;
                Position coinPosition = new Position(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
                for (Coin coin: coins) {
                    if (coin.getPosition().equals(coinPosition)){
                        invalid = true;
                        break;
                    }
                }
                if (hero.getPosition().equals(coinPosition)) invalid = true;
                if (!invalid) {
                    coins.add(new Coin(coinPosition.getX(), coinPosition.getY()));
                    break;
                }
            }
        }
        return coins;
    }

    private List<Monster> createMonsters(){
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            while(true) {
                boolean invalid = false;
                Position monsterPosition = new Position(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
                for (Monster monster: monsters) {
                    if (monster.getPosition().equals(monsterPosition)){
                        invalid = true;
                        break;
                    }
                }
                if (hero.getPosition().equals(monsterPosition)) invalid = true;
                if (!invalid) {
                    monsters.add(new Monster(monsterPosition.getX(), monsterPosition.getY()));
                    break;
                }
            }
        }
        return monsters;
    }

    private boolean canMove(Position position){
        if(position.getX() < width && position.getY() < height &&
                position.getX() >= 0 && position.getY() >= 0) {
            for (Wall wall : walls) {
                if (wall.getPosition().equals(position))
                    return false;
            }
            return true;
        } else return false;
    }

    private boolean verifyMonsterCollision(){
        for(Monster monster: monsters){
            if (monster.getPosition().equals(hero.getPosition())) return true;
        }
        return false;
    }

    private void retrieveCoins(Position position){

        Coin coinCatched = null;

        for (Coin coin: coins){
            if (coin.getPosition().equals(position)) {
                coinCatched = coin;
                break;
            }
        }

        coins.remove(coinCatched);
    }

    public boolean isGameOver(){
        return finished;
    }

    private void gameOver(){
        System.out.println("Game Over!");
        finished = true;
    }

    private void moveHero(Position position){
        moveMonsters();
        if(verifyMonsterCollision()) gameOver();
        if (canMove(position)) {
            retrieveCoins(position);
            hero.setPosition(position);
            if(verifyMonsterCollision()) gameOver();
        }
    }

    private void moveMonsters(){
        for(Monster monster: monsters){
            Position newPosition = monster.move();
            if(canMove(newPosition)){
                retrieveCoins(newPosition);
                monster.setPosition(newPosition);
            }
        }
    }

    public void processKey(KeyStroke key){
        switch (key.getKeyType()){
            case ArrowUp:
                moveHero(hero.moveUp());
                break;
            case ArrowDown:
                moveHero(hero.moveDown());
                break;
            case ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case ArrowRight:
                moveHero(hero.moveRight());
                break;
            default:
                break;
        }
    }

    public void draw(TextGraphics graphics){
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        hero.draw(graphics);
        for (Wall wall: walls){
            wall.draw(graphics);
        }
        for (Coin coin: coins){
            coin.draw(graphics);
        }
        for (Monster monster: monsters){
            monster.draw(graphics);
        }

    }

}
