package com.superclient.android;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.net.Socket;

public class MainActivity extends Activity {

    EditText ipServer;
    EditText cmbBds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean getIpServer(){
        ipServer = (EditText) findViewById(R.id.ipServer);
        if (!ipServer.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    public void sendAlertaOk(View view) {
        sendMsg("alerta #OK#");
    }

    public void sendAbsCmd(View view){
        cmbBds = (EditText) findViewById(R.id.cmdAbs);
        String strMsg = cmbBds.getText().toString();
        if(!strMsg.isEmpty()){
            sendMsg(strMsg);
        }else{
            System.err.println("Sem Mensagem!");
        }
    }


    public void sendMsg(final String str){

        Thread thSendMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!getIpServer()){
                    System.err.println("Não encontrei o servidor!");
                }else{

                    Socket socket = null;
                    DataOutputStream strOut = null;

                    try{
                        socket = new Socket(ipServer.getText().toString(),4602);
                        strOut = new DataOutputStream(socket.getOutputStream());
                        strOut.writeUTF(str);
                        socket.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        try {
                            socket.close();
                            strOut.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thSendMsg.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
