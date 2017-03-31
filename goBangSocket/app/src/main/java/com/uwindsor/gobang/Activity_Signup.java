package com.uwindsor.gobang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uwindsor.lib.service.Client;
import com.uwindsor.lib.service.ISocketResponse;
import com.uwindsor.lib.service.Packet;

import static com.uwindsor.gobang.Constants.PORT;
import static com.uwindsor.gobang.Constants.Server_ADDR;

public class Activity_Signup extends AppCompatActivity {
    private Client user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__signup);

        user=new Client(this.getApplicationContext(),socketListener);
        findViewById(R.id.btnSubmit).setOnClickListener(listener);
    }
    @Override
    protected void onResume() {
        user.open(Server_ADDR, PORT);
        TextView textView_errorInfo = (TextView) findViewById(R.id.textView_errorInfo);
        textView_errorInfo.setText("");
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
                    System.out.println("result="+txt);
                    if(txt.equals("success")){
                        startActivity(new Intent(Activity_Signup.this, Activity_chess_main.class));
                    }
                    else if(txt.equals("failure")){
                        TextView textView_errorInfo = (TextView) findViewById(R.id.textView_errorInfo);
                        textView_errorInfo.setText("Error: userID is existed!!!");
                    }
                }
            });
        }
    };

    private View.OnClickListener listener=new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnSubmit:
                    submit();
                    break;
            }
        }
    };

    private void submit(){
        String userID ="";
        String passWord="";
        String confirmPassword="";

        EditText editText_userID = (EditText) findViewById(R.id.editText_userID);
        userID = editText_userID.getText().toString();

        EditText editText_password = (EditText) findViewById(R.id.editText_password);
        passWord = editText_password.getText().toString();

        EditText editText_confirmPassword = (EditText) findViewById(R.id.editText_confirmPassword);
        confirmPassword = editText_confirmPassword.getText().toString();

        TextView textView_errorInfo = (TextView) findViewById(R.id.textView_errorInfo);

        if (userID.equals("") || passWord.equals("")||confirmPassword.equals("") ){

            textView_errorInfo.setText("Error: userID or Password is Null!!!");
        }
        else if(!(passWord.equals(confirmPassword))){
            textView_errorInfo.setText("two Password is not same!!");
        }
        else {
            Packet packet=new Packet();
            String tempStr="UserID="+userID+ "||passWord="+passWord;
            packet.pack(tempStr);
            user.send(packet);
            //To do Something
        }
    }

}
