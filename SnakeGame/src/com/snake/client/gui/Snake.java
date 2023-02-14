package com.snake.client.gui;

import java.util.LinkedList;

public class Snake {

    // public int x[];
    // public int y[];
    public String playerName;
    public int bodyParts;
    public int applesEaten;
    public char direction;

    private LinkedList<int[]> body;

    // test
    public Snake(String playerName, int GAME_UNIT, int bodyParts, char direction) {
        this.playerName = playerName;
        body = new LinkedList<int[]>();
        // this.x = new int[GAME_UNIT];
        // this.y = new int[GAME_UNIT];
        this.bodyParts = bodyParts;
        this.applesEaten = 0;
        this.direction = direction;
        initBody();
        // for (int[] item : body) {
        // System.out.println("snake body: " + item[0] + " " + item[1]);
        // }
    }

    //
    public Snake() {
        body = new LinkedList<int[]>();
        this.bodyParts = 3;
        this.applesEaten = 0;
        this.direction = 'L';
        initBody();
    }

    public Snake(int GAME_UNIT, int bodyParts, char direction) {
        body = new LinkedList<int[]>();
        this.bodyParts = bodyParts;
        this.applesEaten = 0;
        this.direction = direction;
        initBody();

    }

    public Snake(Snake snake) {
        this.bodyParts = snake.bodyParts;
        this.applesEaten = snake.applesEaten;
        this.direction = snake.direction;
        this.body = new LinkedList<int[]>(snake.getBody());
    }

    public void pushHead(int x, int y) {
        // System.out.println("push head call " + x + " " + y);
        body.addFirst(new int[] { x, y });
    }

    public int[] pop() {
        // System.out.println("pop last call:");
        return body.removeLast();
    }

    private void initBody() {
        for (int i = 0; i < bodyParts; i++) {
            this.pushHead(0, 0);
        }
    }

    public void initBody(int x, int y) {
        for (int i = 0; i < bodyParts; i++) {
            this.getBody().set(i, new int[] { x, y });

        }
    }

    public int[] getHead() {
        // System.out.println("get head call :" + body.getFirst()[0] + " " +
        // body.getFirst()[1]);
        return body.getFirst();
    }

    public LinkedList<int[]> getBody() {
        return body;
    }

    public void pushTail(int x, int y) {
        body.addLast(new int[] { x, y });
    }

    public void popHead() {
        // System.out.println("pop head call:");
        body.removeFirst();
    }

    public void setHead(int x, int y) {
        body.set(0, new int[] { x, y });
    }

    public void setBody(LinkedList<int[]> bd) {
        this.body = new LinkedList<int[]>(bd);
    }
}
