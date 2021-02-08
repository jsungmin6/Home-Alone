package ga.homealoneapi.src.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import ga.homealoneapi.config.BaseException;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseService {

    private FirebaseApp createFirebaseApp() throws IOException {
        String path = "../../../../../resources/firebase/homealone-9ff45-firebase-adminsdk-qyxwr-b923961bd5.json";

        FileInputStream serviceAccount =
                new FileInputStream(path);

        FirebaseOptions options = null;
        try{
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
        }catch (IOException exception){

        }
        return FirebaseApp.initializeApp(options);
    }

    private void sendToToken(Push push) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("테스트 광고")
                        .setBody("테스트 내용")
                        .build())
                // Device를 특정할 수 있는 토큰.
                .setToken(push.getRegistrationToken())
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
//        log.debug("Successfully sent message: " + response);
    }
}
