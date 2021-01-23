package com.royal.app.mail;
import java.util.HashMap;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

	void sendSimpleMessage(String to, String subject, String text);

	void sendSimpleMessageUsingTemplate(String to, String subject, SimpleMailMessage template, String... templateArgs);

	void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

  String sendMultipleMessageWithAttachment(String[] to, String subject, String text,  HashMap<String, String> filesList);

  String sendMultipleMessageWithAttachment(String[] to, String subject, String text,
      HashMap<String, String> filesList, String scheduleDate);

  String sendMultipleMessageWithZipAttach(String[] to, String[] cc, String subject, String text,
      String fileName, String FilePath, String scheduleDate);

  String sendMailWithAttachment(String[] to, String[] cc, String subject, String text,
      HashMap<String, String> filesList, String scheduleDate);

}
