package com.uwindsor.gobang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uwindsor.lib.service.Client;
import com.uwindsor.lib.service.ISocketResponse;
import com.uwindsor.lib.service.Packet;

import static com.uwindsor.gobang.Constants.PORT2;
import static com.uwindsor.gobang.Constants.Server_ADDR;

public class Activity_login extends AppCompatActivity {
    private Client user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user=new Client(this.getApplicationContext(),socketListener);
        findViewById(R.id.btnLogin).setOnClickListener(listener);
        findViewById(R.id.btnSignUp).setOnClickListener(listener);

    }

    @Override
    protected void onResume() {
        user.open(Server_ADDR, PORT2);
        super.onResume();
    }

    @Override
    protected void onPause() {
        user.close();
        super.onPause();
    }

    private ISocketResponse socketListener=new ISocketResponse() {

        @Override
        public void onSocketResponse(final String txt) {
            runOnUiThread(new Runnable() {
                public void run() {
                    //recContent.getText().append(txt).append("\r\n");
                    String head=ConvertData.readHead(txt);
                    System.out.println("result="+txt);
                    if(head.equals("s")){
                        startActivity(new Intent(Activity_login.this, Activity_chess_main.class));
                    }
                    else{
                        TextView textView_errorInfo = (TextView) findViewById(R.id.textView_errorInfo);
                        textView_errorInfo.setText("Error: userID or Password is incorrect!!!");
                    }
                }
            });
        }
    };

    private View.OnClickListener listener=new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnLogin:
                    login();
                break;
                case R.id.btnSignUp:
                    startActivity(new Intent(Activity_login.this, Activity_Signup.class));
                break;
            }
        }
    };

private void login(){
    String userID = "";
    EditText login_account = (EditText) findViewById(R.id.login_account);
    userID = login_account.getText().toString();

    String passWord = "";
    EditText login_password = (EditText) findViewById(R.id.login_password);
    passWord = login_password.getText().toString();

    TextView textView_errorInfo = (TextView) findViewById(R.id.textView_errorInfo);

    if (userID.equals("") || passWord.equals("")) {

        textView_errorInfo.setText("Error: userID or Password is Null!!!");
    } else {
        Packet packet=new Packet();
        Packet packet1=new Packet();
        String userName=ConvertData.writeTotal("i",userID,30);
        String userPassword=ConvertData.writeTotal("i",passWord,30);
        //String tempStr="UserID="+userID+ "||passWord="+passWord;
        packet.pack(userName);
        packet1.pack(userPassword);
        //startActivity(new Intent(Activity_login.this, Activity_chess_main.class));
        user.send(packet);
        user.send(packet1);
    }
}





}
