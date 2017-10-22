package com.taslabs.apps.smartlamp;

import android.content.Context;
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

    static String MQTTHOST = "tcp://192.168.0.4:1883";  // IP e porta do servidor MQTT
    MqttAndroidClient client; // Instancia de um cliente MQTT
    String clientId = MqttClient.generateClientId(); // Gera um ID aleatório pro dispositivo
    Context context; // Contexto da classe Main

    public AjudanteMqtt(Context contex){  // Construtor que recebe o contexto

        context = contex; // Armazena a referência do contexto
    }


    public void connect(){

        client =
                new MqttAndroidClient(context, MQTTHOST, clientId); // Configura o cliente com o contexto, endereço do servidor
                                                                    // e ID único de cliente


        try {
            IMqttToken token = client.connect();  // Conecta com o broker
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) { // Se a conexão for bem sucedida
                    // We are connected
                    Toast toas = Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT); // Exibe um Toast informando
                    toas.show();;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {  //Se não for bem sucedida
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast toas = Toast.makeText(context, "Falha", Toast.LENGTH_SHORT);     // Exibe um Toast informando
                    toas.show();;

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
                client.publish(topic, message.getBytes(), 0, false); // Mensagem sendo postada com tópico, Qos e retained ou não
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        else{

            Toast toas = Toast.makeText(context, "oi", Toast.LENGTH_SHORT);  // Teste da reconexão
            toas.show();;
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






}
