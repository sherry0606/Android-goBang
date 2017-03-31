package com.uwindsor.gobang;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.uwindsor.lib.service.Client;
import com.uwindsor.lib.service.ISocketResponse;
import com.uwindsor.lib.service.Packet;

import static com.uwindsor.gobang.Constants.PORT;
import static com.uwindsor.gobang.Constants.Server_ADDR;

public class Activity_chess_main extends AppCompatActivity {
    private static Client chess=null;
    private ChessBoardView chessBoardView;
    private boolean isPlayer1 = false;
    Toast toast ;
    MotionEvent event;
    Point point=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_main);
        chess=new Client(this.getApplicationContext(),socketListener);

        chessBoardView = (ChessBoardView) findViewById(R.id.boardView);
        };

    @Override
    protected void onResume() {

        chess.open(Server_ADDR, PORT);
        //chess=new Client(this.getContext(),socketListener);
        super.onResume();
    }
    @Override
    protected void onPause() {
        chess.close();
        super.onPause();
    }

    private ISocketResponse socketListener=new ISocketResponse() {

        @Override
        public void onSocketResponse(final String txt) {
            runOnUiThread(new Runnable() {
                public void run() {
                    //chessBoardView.init();
                    System.out.println("result="+txt);
                    String head=ConvertData.readHead(txt);
                    String body=ConvertData.readData(txt);
                    switch(head) {
                        case "1":
                            isPlayer1 = true;
                            chessBoardView.setWhite(false);
                            chessBoardView.setGameStart(false);
                            //Toast.makeText(chessBoardView.getContext(), ConvertData.readData(txt), Toast.LENGTH_SHORT).show();
                            break;
                        case "2": //服务器传来本方选定的是白棋，还是黑棋？
                            System.out.println("result heeeeeeeeeeeeeeeeeee---");
                            if(!isPlayer1) {
                                chessBoardView.setWhite(true);
                            }
                            chessBoardView.setGameStart(true);
                            //Toast.makeText(chessBoardView.getContext(), ConvertData.readData(txt), Toast.LENGTH_SHORT).show();
                            break;
                        case "n":
                            point=new Point(ConvertXY.convertX(body),ConvertXY.convertY(body));//坐result解析坐标,f填进去
                            if(chessBoardView.isWhite()){
                                System.out.println("white-----");
                                chessBoardView.getmBlackArray().add(point);
                                chessBoardView.invalidate();
                                chessBoardView.setmIsWhite(!chessBoardView.ismIsWhite());
                            }
                            else{
                                System.out.println("black-----");
                                chessBoardView.getmWhiteArray().add(point);
                                chessBoardView.invalidate();
                                chessBoardView.setmIsWhite(!chessBoardView.ismIsWhite());
                            }
                            break;
                        case "w":
                            String win =  "You Win!" ;
                            toast=Toast.makeText(getApplicationContext(), win, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            chessBoardView.init();
                            break;
                        case "l":
                            String lose =  "You Lose!" ;
                            toast=Toast.makeText(getApplicationContext(), lose, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            chessBoardView.init();
                            break;
                        case "e":
                            String escape =  "Your opponent exited. Game over." ;
                            toast=Toast.makeText(getApplicationContext(), escape, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            chessBoardView.init();
                            break;
                        default:
                            System.out.println("result---" + 11);
                    }
                }
            });
        }
    };


    public static void sendServer(String string){
        Packet packet=new Packet();
        String tempStr=string;
        packet.pack(tempStr);
        chess.send(packet);
    }
}
