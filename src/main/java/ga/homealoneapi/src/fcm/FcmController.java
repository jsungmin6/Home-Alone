package ga.homealoneapi.src.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessagingException;
import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    /**
     * push알림 전송 API
     * [GET] /push-alarms
     */
    @ResponseBody
    @GetMapping("/push-alarms")
    public void pushMulitMessage() throws BaseException {
        System.out.println("pushMulitMessage start");
        List<String> tokens = null;

        try{
            tokens = firebaseCloudMessageService.getTokens();
        }catch (BaseException exception){
            System.out.println(exception.getStatus());
            return;
        }

        String title = "나홀로 집콕";
        String body = "집콕 챌린지 잊지마세요!";

        try{
            firebaseCloudMessageService.sendMessageTo(tokens);
        } catch (FirebaseMessagingException e){
            System.out.println("error :"+e);
            return;
        }
    }

    @PostConstruct
    private FirebaseApp createFirebaseApp() {
        String firebaseConfigPath = "/firebase/homealone-9ff45-firebase-adminsdk-qyxwr-b923961bd5.json";
        FirebaseOptions options = null;
        ClassPathResource resource =
                new ClassPathResource(firebaseConfigPath);
        try (InputStream is = resource.getInputStream()) {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    .setDatabaseUrl("https://homealone-9ff45.firebaseio.com")
                            .build();
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            log.error(e.getMessage());
        }

        return FirebaseApp.initializeApp(options);
    }
}
