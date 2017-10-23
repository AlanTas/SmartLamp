package com.taslabs.apps.smartlamp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;



/**
 * Created by alantas on 22/10/17.
 */

public class AjudanteMqtt {

   TextView upper;
   TextView downer;

    static String MQTTHOST = "tcp://192.168.0.4:1883";  // IP e porta do servidor MQTT
    MqttAndroidClient client; // Instancia de um cliente MQTT
    String clientId = MqttClient.generateClientId(); // Gera um ID aleatório pro dispositivo
    Context context; // Contexto da classe Main

    public AjudanteMqtt(Context contex, TextView up, TextView down){  // Construtor que recebe o contexto

        context = contex; // Armazena a referência do contexto
        upper = up;
        downer = down;
    }


    public void connect(){
        downer.setText("Conectando...");
        downer.setTextColor(ContextCompat.getColor(context, R.color.red));

        client =
                new MqttAndroidClient(context, MQTTHOST, clientId); // Configura o cliente com o contexto, endereço do servidor
                                                                    // e ID único de cliente


        try {
            IMqttToken token = client.connect();  // Conecta com o broker
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) { // Se a conexão for bem sucedida
                    // We are connected
                    upper.setText("CONECTADO (" + MQTTHOST + ")");
                    upper.setTextColor(ContextCompat.getColor(context, R.color.green));
                    downer.setText("");
                    //Toast toas = Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT); // Exibe um Toast informando
                    //toas.show();;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {  //Se não for bem sucedida
                    // Something went wrong e.g. connection timeout or firewall problems
                    upper.setText("DESCONECTADO");
                    upper.setTextColor(ContextCompat.getColor(context, R.color.red));
                    downer.setText("Falha na conexão");
                    downer.setTextColor(ContextCompat.getColor(context, R.color.red));
                    //Toast toas = Toast.makeText(context, "Falha", Toast.LENGTH_SHORT);     // Exibe um Toast informando
                    //toas.show();;

                }
                

            });

        } catch (MqttException e) {
            e.printStackTrace();
        }


    }


    public void publish(String mensagem){

        if (client.isConnected()) {  // Posta mensagem apenas se o cliente estiver conectado
            String topic = "test";   // Tópico em que a mensagem vai ser postada
            String message = mensagem; // Mensagem a ser postada
            try {
                client.publish(topic, message.getBytes(), 1, false); // Mensagem sendo postada com tópico, Qos e retained ou não
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        else{

            connect();

        }
    }

    public boolean isConnected(){  // Método que retorna se o cliente MQTT está conectado, não usada até agora
                                   // mas talvez eu venha a usar

        boolean truth = false;

        if (client.isConnected()){

            truth = true;
        }

        return truth;
    }

    public void disconnect() {

        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }






}
