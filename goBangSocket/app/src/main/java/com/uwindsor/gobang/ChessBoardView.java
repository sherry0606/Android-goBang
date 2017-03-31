package com.uwindsor.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static com.uwindsor.gobang.Activity_chess_main.sendServer;

/**
 * Created by Sherry on 2017/3/5.
 */

public class ChessBoardView extends View {


    //The width of the chess board
    private int mViewWidth;
    //The height of each line
    private float maxLineHeight;
    private Paint paint = new Paint();
    //Define the Bitmap of the chess
    private Bitmap mWhitePiece, mBlackPiece;
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    public boolean isGameStart() {
        return isGameStart;
    }

    public void setGameStart(boolean gameStart) {
        isGameStart = gameStart;
    }

    private boolean isGameStart=false;


    public boolean ismIsWhite() {
        return mIsWhite;
    }

    public void setmIsWhite(boolean mIsWhite) {
        this.mIsWhite = mIsWhite;
    }

    //If the color of the chess is white
    private boolean mIsWhite = false;

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    //设置本方的棋子颜色
    private boolean isWhite =false;


    public ArrayList<Point> getmWhiteArray() {
        return mWhiteArray;
    }

    public void setmWhiteArray(ArrayList<Point> mWhiteArray) {
        this.mWhiteArray = mWhiteArray;
    }

    //Record the position of the chess
    private ArrayList<Point> mWhiteArray = new ArrayList<>();

    public ArrayList<Point> getmBlackArray() {
        return mBlackArray;
    }

    public void setmBlackArray(ArrayList<Point> mBlackArray) {
        this.mBlackArray = mBlackArray;
    }

    private ArrayList<Point> mBlackArray = new ArrayList<>();

    //If the game is over
    private boolean mIsGameOver;

    //If the white win
    private boolean mIsWhiteWinner;


    private  Context context;

    public ChessBoardView(Context context, AttributeSet attributeset){
        super(context,  attributeset);
        this.context=context;

        init();
    }
    public void init(){
        paint.setColor(0x88FF9900);
        paint.setStrokeWidth(3.0f);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.black);


    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }
        else if(heightMode == MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        maxLineHeight = mViewWidth * 1.0f /Constants.MAX_LINE;

        int pieceWidth = (int) (maxLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(mIsGameOver){
            return false;
        }
        System.out.println("isWhite="+isWhite+"              mIsWhite="+mIsWhite+ "                isGameStart="+isGameStart);
        if (isGameStart) {
             if(!(isWhite^mIsWhite)) {
                 int action = event.getAction();
                 if (action == MotionEvent.ACTION_UP) {
                     int x = (int) event.getX();
                     int y = (int) event.getY();

                     Point point = getValidPoint(x, y);

                     if (mWhiteArray.contains(point) || mBlackArray.contains(point)) {
                         return false;
                     }
                     if (mIsWhite) {
                         mWhiteArray.add(point);
                     } else {
                         mBlackArray.add(point);
                     }

                     //String tempStr =  point.x + "," + point.y;
                     String tempStr = ConvertXY.convertCordinate(point.x, point.y);
                     String Info = ConvertData.writeTotal("n", tempStr, 30);
                     //System.out.println("tempstr-----------" + tempStr);
                     sendServer(Info);

                     invalidate();
                     mIsWhite = !mIsWhite;
                 }
             }
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        int validX = (int) (x / maxLineHeight);
        int validY = (int) (y / maxLineHeight);

        return new Point(validX, validY);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //Drwa the grid
        drawBoard(canvas);
        //Draw the white and black chess
        drawPieces(canvas);
        //If the game over
        checkGameOver();
    }

    private void checkGameOver() {
        CheckWinner checkWinner = new CheckWinner();
        boolean whiteWin = checkWinner.checkWinner(mWhiteArray);
        boolean blackWin = checkWinner.checkWinner(mBlackArray);

        if (whiteWin || blackWin){
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner ? "White Win!" : "Black Win!";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++){
            System.out.println("i=--------------"+i);
            Point whitePoint = mWhiteArray.get(i);
            float left = (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;
            float top = (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;

            canvas.drawBitmap(mWhitePiece, left, top, null);
        }

        for (int i = 0, n = mBlackArray.size(); i < n; i++){
            Point blackPoint = mBlackArray.get(i);
            float left = (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;
            float top = (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;

            canvas.drawBitmap(mBlackPiece, left, top, null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mViewWidth;
        float lineHeight = maxLineHeight;

        for (int i=0; i<Constants.MAX_LINE; i++){
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i ) * lineHeight);

            canvas.drawLine(startX, y, endX, y, paint);
            canvas.drawLine(y, startX, y, endX, paint);
        }

    }

    public void start(){

    }
}

