package gcs.capture;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import gcs.hud.HudViewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CaptureController implements Initializable{
	
	public static CaptureController instance;
	
	@FXML private WebView webViewCapture;
	
	
	private volatile static boolean running = false;
	static Thread captureThread;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		WebEngine engine = webViewCapture.getEngine();
        engine.load("http://localhost:8080/Capture/content.jsp");
        
		

		
	}
	
	
	
	public static void captureImage() {
		//WritableImage 객체를 생성하고 사이즈를 조정합니다. 그리고 snapshot 메소드를 부를때 인자로 넘겨주게 되면 캡쳐된 이미지가 쓰여지게 됩니다.
		//그럼 이제 새로 생성한 File 객체에다가 png 포맷인 이미지 파일로 저장을 할 수 있게 되는 형식입니다.
/*	    //파일 이름지정해서 저장.
	    FileChooser fileChooser = new FileChooser();
	    
	    //Set extension filter
	    FileChooser.ExtensionFilter extFilter = 
	            new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
	    fileChooser.getExtensionFilters().add(extFilter);
	   
	    //Show save file dialog
	    Window mainStage = null;
	    File file = fileChooser.showSaveDialog(mainStage);
	     
	    if(file != null){
	        try {
	        	Canvas CameraSize = HudViewController.instance.canvasCamera;
	        	WritableImage snapImage = new WritableImage(Integer.parseInt(String.valueOf(Math.round(CameraSize.getWidth()))), Integer.parseInt(String.valueOf(Math.round(CameraSize.getHeight()))));
	        	CameraSize.snapshot(null, snapImage);
	            RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapImage, null);
	            ImageIO.write(renderedImage, "png", file);
	        } catch (IOException ex) {
	        }
	    }*/
		
		
		//자동저장
	    Calendar cal = Calendar.getInstance();
	    String dateString;

	    dateString = String.format("%04d-%02d-%02d %02d：%02d：%02d.%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
	    //dateString = String.format("%04d-%02d-%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY));
	    //dateString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

	    File f = new File("C:\\Users\\Administrator\\Desktop", dateString + ".png");
	    System.out.println(dateString);
	     
	    if(f != null){
	        try {
	        	Canvas CameraSize = HudViewController.instance.canvasCamera;
	        	WritableImage snapImage = new WritableImage(Integer.parseInt(String.valueOf(Math.round(CameraSize.getWidth()))), Integer.parseInt(String.valueOf(Math.round(CameraSize.getHeight()))));
	        	CameraSize.snapshot(null, snapImage);
	            RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapImage, null);
	            ImageIO.write(renderedImage, "png", f);
	        } catch (IOException ex) {
	        }
	    }
	}

	     
	    
	    
        //display in new window
//      ImageView snapView = new ImageView();
//      snapView.setImage(snapImage);
//       
//      StackPane snapLayout = new StackPane();
//      snapLayout.getChildren().add(snapView);
//      
//      Scene snapScene = new Scene(snapLayout, 590, 485);
//
//      Stage snapStage = new Stage();
//      snapStage.setTitle("Snapshot");
//      snapStage.setScene(snapScene);
//
//      snapStage.show();
	
	
	
	/*@Override                                                    
	protected void onCreate(Bundle savedInstanceState) {          
	    super.onCreate(savedInstanceState);                       
	    setContentView(R.layout.activity_main);    
	 
	    mRecorder = new MediaRecorder();                          
	} 
	
	void initVideoRecorder() {
	    mCamera = Camera.open();
	    mCamera.setDisplayOrientation(90);
	    mSurfaceHolder = mSurface.getHolder();
	    mSurfaceHolder.addCallback(this);
	    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	void startVideoRecorder() {
	    if(isRecording) {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;

	        mCamera.lock();
	        isRecording = false;

	        mBtCamcording.setText("Start Camcording");
	    }
	    else {
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                mRecorder = new MediaRecorder();
	                mCamera.unlock();
	                mRecorder.setCamera(mCamera);
	                mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
	                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
	                mRecorder.setOrientationHint(90);

	                mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.mp4";
	                Log.d(TAG, "file path is " + mPath);
	                mRecorder.setOutputFile(mPath);

	                mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
	                try {
	                    mRecorder.prepare();
	                }catch(Exception e){
	                    e.printStackTrace();
	                }
	                mRecorder.start();
	                isRecording = true;

	                mBtCamcording.setText("Stop Camcording");
	            }
	        });
	    }
	}*/
	
	/*public static void startRecoding() {
		running = true;
		captureThread = new Thread() {
            @Override
            public void run() {
            	Calendar cal = Calendar.getInstance();
        	    String dateString;
        	    dateString = String.format("%04d-%02d-%02d %02d：%02d：%02d.%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
            	File file = new File("C:\\Users\\Administrator\\Desktop", dateString + ".mp4");
                //File file = new File("녹화파일.ts");
                IMediaWriter writer = ToolFactory.makeWriter(file.getName());
                //Dimension size = WebcamResolution.VGA.getSize();
                Canvas CameraSize = HudViewController.instance.canvasCamera;
                writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 320,240);

                //writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, Integer.parseInt(String.valueOf(Math.round(CameraSize.getWidth()))), Integer.parseInt(String.valueOf(Math.round(CameraSize.getHeight()))));
                //webcam.open();
                long start = System.currentTimeMillis();
                System.out.println("녹화시작");
                try {
                    while (running) {
                        // System.out.println("Capture frame " + i);
                    	//BufferedImage snapImage = new BufferedImage(Integer.parseInt(String.valueOf(Math.round(CameraSize.getWidth()))), Integer.parseInt(String.valueOf(Math.round(CameraSize.getHeight()))), );
        	        	WritableImage snapImage = new WritableImage(320, 240);

                    	//WritableImage snapImage = new WritableImage(Integer.parseInt(String.valueOf(Math.round(CameraSize.getWidth()))), Integer.parseInt(String.valueOf(Math.round(CameraSize.getHeight()))));
        	            BufferedImage bufferimage = SwingFXUtils.fromFXImage(snapImage, null);
                        IConverter converter = ConverterFactory.createConverter(bufferimage, IPixelFormat.Type.YUV420P);
                        IVideoPicture frame = converter.toPicture(bufferimage,
                                (System.currentTimeMillis() - start) * 1000);
                        // frame.setKeyFrame(i == 0);
                        frame.setQuality(0);
                        writer.encodeVideo(0, frame);
                        // 10 FPS
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            // sarxos: in case of interrupted exception,
                            // just break,
                            // return or throw, it's not severe exception,
                            // but you shall not
                            // ignore it because your application can hang
                            // in some rare
                            // cases, especially when it's multithreaded
                            // LOG.log(Level.SEVERE, null, ex);
                            break;
                        }
                    }
                } finally {
                    writer.close();// sarxos: don't forgot to close writer
                    // when done with recording
                }
                System.out.println("Video recorded in file: " + file.getAbsolutePath());
            }
        };
        // sarxos: if you are using worker threads (threads which perform
        // some
        // abstract parallel tasks) make sure to use daemon threads,
        // otherwise,
        // your app can be blocked when there are more than one non-daemon
        // thread
        // running which does not want to join
        captureThread.setDaemon(true);
        captureThread.start();
		
		
		
		
	}
	
	public static void stopRecoding() {
		running = false;
        System.out.println("녹화 끝");
        try {
            captureThread.join();
        } catch (InterruptedException ex) {
        }
		
	}*/
	
}
