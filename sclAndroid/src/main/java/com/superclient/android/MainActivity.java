package com.superclient.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.Socket;
import android.os.Handler;

public class MainActivity extends Activity {

    EditText ipServer;
    EditText cmbBds;
    Handler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
    }

    public boolean getIpServer(){
        ipServer = (EditText) findViewById(R.id.ipServer);
        String strIpServer = ipServer.getText().toString();
        if (!strIpServer.isEmpty()){
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
            showToast("Sem Mensagem!");
        }
    }


    public void sendMsg(final String str){

        Thread thSendMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!getIpServer()){
                    showToast("Não encontrei o Servidor!");
                }else{

                    Socket socket = null;
                    DataOutputStream strOut = null;

                    try{
                        socket = new Socket(ipServer.getText().toString(),4602);
                        strOut = new DataOutputStream(socket.getOutputStream());
                        strOut.writeUTF(str);
                        socket.close();
                        showToast("Comando Enviado com Sucesso! :) ");
                    }catch (Exception e){
                        e.printStackTrace();
                        showToast("Falha ao enviar Comando! :( ");
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


    public void showToast(final String strMsg){
        Thread thToast = new Thread( new Runnable() {
            @Override
            public void run() {
                handler.post(
                        new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        thToast.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
