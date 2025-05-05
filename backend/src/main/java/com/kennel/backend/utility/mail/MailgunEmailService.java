package com.kennel.backend.utility.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
//import lombok.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {

    private final WebClient.Builder webClientBuilder;
    private final MailgunProperties mailgunProperties;


    public void sendOtpEmail(String to, long otp) {
        String fromEmail= mailgunProperties.getFromEmail();;
        String domain= mailgunProperties.getDomain();
        String apiKey= mailgunProperties.getApiKey();

        System.out.println(apiKey+" - "+ domain+" - "+ fromEmail);

        String url = String.format("https://api.mailgun.net/v3/%s/messages", domain);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("from", fromEmail);
        formData.add("to", to);
        formData.add("subject", "Your OTP Code");
        formData.add("text", "Your OTP is: " + otp + ". It will expire in 10 minutes.");

//        try {
            webClientBuilder.build()
                    .post()
                    .uri(url)
                    .headers(headers -> headers.setBasicAuth("api", apiKey))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // Or async: .subscribe();
//        }catch (WebClientException e){
//            e.printStackTrace();
//        }
    }

}
