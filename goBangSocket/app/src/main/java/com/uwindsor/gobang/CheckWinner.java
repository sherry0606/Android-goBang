package com.uwindsor.gobang;

import android.graphics.Point;

import java.util.List;

/**
 * Created by Sherry on 2017/3/5.
 */

public class CheckWinner {
    private Point point1, point2;
    private int checkMode = -1;

    private boolean check(int x, int y, List<Point> points, int checkOrientation){
        int count = 1;
        for (int i = 1; i < Constants.MAX_COUNT_IN_LINE; i++){
            switch (checkOrientation){
                case Constants.HORIZONTAL:
                     point1 = new Point(x - i, y);
                     break;
                case Constants.VERTICAL:
                    point1 = new Point(x, y - i);
                    break;
                case Constants.LEFT_DIAGONAL:
                    point1 = new Point(x - i, y + i);
                    break;
                case Constants.RIGHT_DIAGONAL:
                    point1 = new Point(x + i, y + i);
                    break;
            }
            if (points.contains(point1)){
                count++;
            }
            else {
                break;
            }
        }
        for (int i = 1; i < Constants.MAX_COUNT_IN_LINE; i++){
            switch (checkOrientation){
                case Constants.HORIZONTAL:
                    point2 = new Point(x + i, y);
                    break;
                case Constants.VERTICAL:
                    point2 = new Point(x, y + i);
                    break;
                case Constants.LEFT_DIAGONAL:
                    point2 = new Point(x + i, y - i);
                    break;
                case Constants.RIGHT_DIAGONAL:
                    point2 = new Point(x - i, y - i);
                    break;
            }
            if (points.contains(point2)){
                count++;
            }
            else {
                break;
            }
        }
        if (count == Constants.MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points){
        checkMode = Constants.HORIZONTAL;
        return check(x, y, points, checkMode);
    }

    private boolean checkVertical(int x, int y, List<Point> points){
        checkMode = Constants.VERTICAL;
        return check(x, y, points, checkMode);
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points){
        checkMode = Constants.LEFT_DIAGONAL;
        return check(x, y, points,checkMode);
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points){
        checkMode = Constants.RIGHT_DIAGONAL;
        return check(x,y, points, checkMode);
    }

    public boolean checkWinner(List<Point> points){
        for (Point point : points){
            int x = point.x;
            int y = point.y;

            if (checkHorizontal(x,y, points)){
                return true;
            }else if(checkVertical(x, y, points)){
                return true;
            }else if (checkLeftDiagonal(x, y, points)){
                return true;
            }else if (checkRightDiagonal(x, y, points)){
                return true;
            }
        }
        return false;
    }
}
