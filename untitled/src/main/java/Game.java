import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

public class Game {

    Screen screen;
    Arena arena;

    Game(){
        try {
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);

            screen.setCursorPosition(null);   // we don't need a cursor
            screen.startScreen();             // screens must be started
            screen.doResizeIfNecessary();     // resize screen if necessary

            arena = new Arena(40,20);

        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void draw() throws IOException {
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }


    private void processKey(KeyStroke key){
        arena.processKey(key);
    }

    public void run(){
        try{
            KeyStroke key;
            do{
                draw();
                key = screen.readInput();
                processKey(key);
                if(key.getKeyType() == KeyType.Character && key.getCharacter() == 'q' || arena.isGameOver())
                    screen.close();
            }while(key.getKeyType() != KeyType.EOF);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
