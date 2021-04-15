package ga.homealoneapi.src.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponseStatus;
import ga.homealoneapi.src.user.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final ObjectMapper objectMapper;

    private static final String PROJECT_ID = "homealone-9ff45";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private final String API_URL = BASE_URL+FCM_SEND_ENDPOINT;
    private final UserInfoRepository userInfoRepository;


    public void sendMessageTo(List<String> targetTokens) throws FirebaseMessagingException {
//        String message = makeMessage(targetTokens,title,body);
        System.out.println("sendMessageTo start");

//        List<String> registrationTokens = Arrays.asList(
//                "YOUR_REGISTRATION_TOKEN_1",
//                // ...
//                "YOUR_REGISTRATION_TOKEN_n"
//        );

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle("[많은 양의 메세지] 알람입니다.")
                        .setBody("많은 알람에 당황하지 마세요.")
                        .build())
                .addAllTokens(targetTokens)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        // See the BatchResponse reference documentation
        // for the contents of response.
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
        // [END send_multicast]

    }

    private String makeMessage(List<String> registrationTokens, String title, String body) throws JsonProcessingException {

        System.out.println("registrationTokens"+registrationTokens);

        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .tokens(registrationTokens)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();

        System.out.println("fcmMessage"+fcmMessage);

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private  String getAccessToken() throws IOException{
        String firebaseConfigPath = "/firebase/homealone-9ff45-firebase-adminsdk-qyxwr-b923961bd5.json";

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new
                ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public List<String> getTokens() throws BaseException {
        List<UserDeviceTokenDto> userDeviceTokenDtos;
        List<String> result;

        userDeviceTokenDtos = userInfoRepository.findAllUserDeviceToken();

        System.out.println("userDeviceTokenDtos :"+userDeviceTokenDtos.size());
        if(userDeviceTokenDtos.size() < 1){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USERDEVICETOKEN);
        }

//        System.out.println("userDeviceTokenDtos :"+userDeviceTokenDtos);

        result = userDeviceTokenDtos.stream()
                .map(o -> o.getUserDeviceToken())
                .collect(toList());

        for (String s : result) {
            System.out.println("result :"+s);
        }

        return result;
    }



}
