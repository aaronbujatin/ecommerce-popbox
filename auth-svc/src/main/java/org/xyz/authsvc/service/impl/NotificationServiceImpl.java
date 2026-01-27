package org.xyz.authsvc.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.xyz.authsvc.client.UserClient;
import org.xyz.authsvc.client.dto.UserResp;
import org.xyz.authsvc.dto.AuthTokenResp;
import org.xyz.authsvc.dto.AuthUserDetails;
import org.xyz.authsvc.service.NotificationService;
import org.xyz.authsvc.service.enums.RecipientType;
import org.xyz.authsvc.service.jwt.JwtService;
import org.xyz.authsvc.service.util.OtpUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final OtpService otpService;
    private final JwtService jwtService;
    private final UserClient userClient;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long OTP_EXPIRY_FIVE_MIN = 300;

    @Override
    public String sendOtp(String recipient) {
        return switch (validateRecipient(recipient)) {
            case OTP_EMAIL -> sendOtpEmail(recipient);
            case OTP_SMS -> sendOtpSms(recipient);
            default -> throw new IllegalArgumentException("invalid email or phone number");
        };
    }

    @Override
    public AuthTokenResp verifyOtp(String recipient,
                                   String otp) {

        otpService.validateOtp(recipient, otp);

        var token = jwtService.generateToken(new AuthUserDetails());

//        var userResp = userClient.getUserByEmail(recipient);
        UserResp userResp = null;
        return (userResp != null)
                ? new AuthTokenResp(token, OTP_EXPIRY_FIVE_MIN, "userResp.username()", "userResp.imageUrl()")
                : new AuthTokenResp(token, OTP_EXPIRY_FIVE_MIN, null, null);


    }

    private RecipientType validateRecipient(String recipient) {
        var emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        var phonePatterns = List.of(
                Pattern.compile("^09\\d{9}$"),        // 09XXXXXXXXX (11 digits)
                Pattern.compile("^\\+639\\d{9}$"),    // +639XXXXXXXXX
                Pattern.compile("^639\\d{9}$")        // 639XXXXXXXXX
        );

        Matcher matcher = emailPattern.matcher(recipient.trim());

        if (matcher.matches()) {
            return RecipientType.OTP_EMAIL;
        }

        String cleaned = recipient.replaceAll("[\\s\\-\\(\\)]", "");

        for (Pattern pattern : phonePatterns) {
            if (pattern.matcher(cleaned).matches()) {
                return RecipientType.OTP_SMS;
            }
        }
        return RecipientType.UNKNOWN;
    }

    private String sendOtpEmail(String recipient) {
        String otp = OtpUtil.generateOtp();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\nIt will expire in 5 minutes.");

        javaMailSender.send(message);

        otpService.cacheOtp(recipient, otp);

        log.info("Email successfully send to: {}", recipient);
        return otp;
    }

    private String sendOtpSms(String recipient){
        String otp = OtpUtil.generateOtp();
        log.info("Text Message successfully send to: {}", recipient);
        return otp;
    }

}
