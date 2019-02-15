package gcs.lowercam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import gcs.network.Network;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MjpgStreamBottom {
    private Canvas canvas;
    private GraphicsContext gc;
    private MqttClient mqttClient;

    public MjpgStreamBottom(Canvas canvas) throws Exception {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }   
    
    public void stop() {
    	try {
    		mqttClient.disconnect();
    		mqttClient.close();
    	} catch(Exception e) {}
    }
    
    public void start() {
        Thread thread = new Thread() {
            boolean connected;
            @Override
            public void run() {
                while(!connected) {
                    try {
                    	mqttClient = new MqttClient("tcp://" + Network.mqttIp + ":" + Network.mqttPort, MqttClient.generateClientId(), null);
                        mqttClient.setCallback(new MqttCallback() {
                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                            	MqttMessage nextMessage = new MqttMessage("next".getBytes());
            					mqttClient.publish(Network.uavCameraBottomSubTopic, nextMessage);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                        	byte[] imageBytes = message.getPayload();
                                        	BufferedImage bufferdImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                                            Image imgFx = SwingFXUtils.toFXImage(bufferdImage, null);
                                            gc.drawImage(imgFx, 0, 0, imgFx.getWidth(), imgFx.getHeight(), 0, 0, canvas.getWidth(), canvas.getHeight());
                                        } catch (Exception ex) {
                                            //ex.printStackTrace();
                                        }
                                    }
                                });
                            }
                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {
                            }
                            @Override
                            public void connectionLost(Throwable cause) {
                            }
                        });
                        MqttConnectOptions options = new MqttConnectOptions();
                        options.setConnectionTimeout(1000);
                        mqttClient.connect(options);
                        mqttClient.subscribe(Network.uavCameraBottomPubTopic);
                        connected = true;
                        mqttClient.publish(Network.uavCameraBottomSubTopic, new byte[0], 0, true); // MQTT Broker의 영상버퍼 제거
                        MqttMessage nextMessage = new MqttMessage("next".getBytes()); // next Message 제작
    					mqttClient.publish(Network.uavCameraBottomSubTopic, nextMessage.getPayload(), 0, true); // next Message 발송
                    } catch(Exception e) {
                        try {
                            MjpgStreamBottom.this.stop();
                        } catch(Exception e2) {}
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
