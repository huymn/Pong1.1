package com.example.huy.pongnguyenhu21;

import android.graphics.*;
import android.view.MotionEvent;

import java.util.*;

/**
 * This class animates the behavior of the ball
 *
 * @author Huy Nguyen
 * @version March 2018
 */

public class BallAnimator implements Animator {
    private final int BALL_RADIUS = 30; //Set radius of ball
    private final String CONTINUE = "Tap the screen to continue"; //Text to let user know to tap to continue

    private Random ranNum = new Random(); //Random number generator
    //Instance variables of x position, y position, the change in x, change in y and the speed
    private float xPos, yPos, changeInY, changeInX,speedX, speedY;
    private Paint paint = new Paint(); //paint for the ball, walls, and paddle
    //Set the ball to be on the field
    private boolean isBallOut = false;
    //Set the paddle to be the small paddle
    private boolean isBigPaddle = false;

    public BallAnimator(){
        //Pick a random starting position for the ball
        xPos = ranNum.nextInt(1000) + 500;
        yPos = ranNum.nextInt(500) + 500;
        //Pick a random direction and speed of the ball
        randomize();
        //set paint color to white
        paint.setColor(Color.WHITE);
    }

    /**
     * Randomize direction and speed of the ball
     */
    public void randomize(){

        //Pick a random initial speed
        speedX = 1 + ranNum.nextInt(4) + (float)Math.random();
        speedY = 1 + ranNum.nextInt(4) + (float)Math.random();

        //Pick random Y direction
        if(ranNum.nextInt(2) == 1){
            changeInY = 10 * speedY;
        }
        else {
            changeInY = -10 * speedY;
        }


        //Pick random X direction
        if(ranNum.nextInt(2) == 1){
            changeInX = 10 * speedX;
        }
        else {
            changeInX = -10 * speedX;
        }
    }

    /**
     * Draw the the three walls of the game
     * @param canvas  canvas to be drawn on
     */
    public void drawWalls(Canvas canvas) {
        //Draw top wall
        canvas.drawRect(0.0f, 0.0f, (float)canvas.getWidth(), 60.0f, paint);
        //Draw left side wall
        canvas.drawRect(0.0f, 0.0f, 60.0f, (float)canvas.getHeight(), paint);
        //Draw bottom wall
        canvas.drawRect(0.0f, (float)(canvas.getHeight() - 60), canvas.getWidth(), canvas.getHeight(), paint);
    }

    /**
     * Draw the correct paddle for the game
     * @param canvas canvas to be drawn on
     */
    public void drawPaddle(Canvas canvas) {
        //Check to see if it's the big paddle that has to be drawn, draw the small one otherwise
        if(isBigPaddle) {
            canvas.drawRect((float)(canvas.getWidth() - 150), (float)((canvas.getHeight() / 2) - 300),
                    (float)(canvas.getWidth()), (float)((canvas.getHeight() / 2) + 300), paint);
        }
        else {
            canvas.drawRect((float)(canvas.getWidth() - 60), (float)((canvas.getHeight() / 2) - 120),
                    (float)(canvas.getWidth()), (float)((canvas.getHeight() / 2) + 120), paint);
        }
    }

    /**
     * Animates the ball
     * Check collisions of the ball and change direction appropriately
     * Note: Every time the ball hit the wall, it slows down hence the 0.99
     * @param canvas canvas to be drawn on
     */
    public void ballAnimation(Canvas canvas) {
        //If the ball hit the top wall, move it down
        if((yPos - BALL_RADIUS) < 60) {
            changeInY = 10 * 0.99f;
        }
        //If the ball hit the bottom wall, move it up
        if((yPos + BALL_RADIUS) > canvas.getHeight()- 60) {
            changeInY = -10 * 0.99f;
        }
        //If the ball hit the left wall, move it to the right
        if((xPos - BALL_RADIUS) < 60) {
            changeInX = 10 * 0.99f;
        }
        /**
         * Check if it's the big or small paddle
         * Check if it hits, move it in the other direction
         */
        if(isBigPaddle) {
            if((xPos + BALL_RADIUS) > canvas.getWidth() - 150){
                if((yPos >= (canvas.getHeight()/2) - 300) && (yPos <= (canvas.getHeight()/2) + 300)) {
                    changeInX = -10 * 0.99f;
                }
            }
        }
        else if(isBigPaddle == false) {
            if((xPos + BALL_RADIUS) > canvas.getWidth() - 60 ){
                if((yPos >= (canvas.getHeight()/2) - 120) && (yPos <= (canvas.getHeight()/2) + 120)) {
                    changeInX = -10 * 0.99f;
                }
            }
        }
        //If the ball goes out of bound, put it back in the frame with a random position, direction, and speed
        if(xPos > canvas.getWidth()){
            isBallOut = true;
            xPos = ranNum.nextInt(1000) + 500;
            yPos = ranNum.nextInt(500) + 500;
            randomize();
        }

        //Move the ball by changing the positions
        yPos += changeInY;
        xPos += changeInX;
        //Check if the ball is out of bound
        if(isBallOut) {
            /**
              External Citation
                Date:    March 22, 2018
                Problem: Don't know how to draw text on SurfaceView
                Resource:
                    https://developer.android.com/reference/android/graphics/Canvas.html
                Solution: Search up the the android developer and use it as a reference
             */
            paint.setTextSize(50.0f);
            //The the "Tap the screen to continue" text
            canvas.drawText(CONTINUE, (canvas.getWidth()/2) - 200, canvas.getHeight()/2, paint);
        }
        //Continue drawing the ball if it's still in bound
        else if (isBallOut == false) {
            canvas.drawCircle(xPos, yPos, BALL_RADIUS, paint);
        }
    }

    /**
     * Set the paddle to big
     */
    public void setPaddleBig() {
        isBigPaddle = true;
    }

    /**
     * Set paddle to small
     */
    public void setPaddleSmall() {
        isBigPaddle = false;
    }

    /**
     * Getter of frame rate
     * @return frame rate
     */
    @Override
    public int interval() {
        return 20;
    }

    /**
     * Get the background color
     * @return background color
     */
    @Override
    public int backgroundColor() {
        return Color.BLACK;
    }

    /**
     * See if the game is pause(it never does)
     * @return true if pause, false otherwise
     */
    @Override
    public boolean doPause() {
        return false;
    }

    /**
     * Method to quit the game
     * @return true to quit, false to not
     */
    @Override
    public boolean doQuit() {
        return false;
    }

    /**
     * Draw the walls
     * Draw the paddles
     * Draw the ball along with its animation
     * @param canvas canvas to be drawn on
     */
    @Override
    public void tick(Canvas canvas) {

        //Draw the walls
        drawWalls(canvas);

        //draw the paddle
        drawPaddle(canvas);

        //Animation of the ball
        ballAnimation(canvas);




    }

    /**
     * Touch events, when the ball goes out of bound, touch the screen to continue
     * @param event a MotionEvent describing the touch
     */
    @Override
    public void onTouch(MotionEvent event) {
        isBallOut = false;
    }


}
