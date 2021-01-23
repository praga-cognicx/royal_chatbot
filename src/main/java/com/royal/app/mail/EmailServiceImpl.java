package com.royal.app.mail;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

  @Value("${spring.mail.fromMail}")
  private String fromMail;

  @Autowired
  public JavaMailSender emailSender;

  @Value("${spring.mail.supportuser}")
  private String supportuser;

  @Value("${spring.mail.supportpass}")
  private String supportpass;

  @Value("${spring.mail.supporthost}")
  private String supporthost;

  @Value("${spring.mail.supportport}")
  private String supportport;

 
  public Session supportMailConfig() {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", supporthost);
    props.put("mail.smtp.port", supportport);

    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(supportuser, supportpass);
      }
    });

    return session;
  }

  public void sendSimpleMessage(String to, String subject, String text) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(fromMail);
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);

      emailSender.send(message);
    } catch (MailException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void sendSimpleMessageUsingTemplate(String to, String subject, SimpleMailMessage template,
      String... templateArgs) {
    String text = String.format(template.getText(), templateArgs);
    sendSimpleMessage(to, subject, text);
  }

  @Override
  public void sendMessageWithAttachment(String to, String subject, String text,
      String pathToAttachment) {
    try {
      MimeMessage message = emailSender.createMimeMessage();
      // pass 'true' to the constructor to create a multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(fromMail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text);

      FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
      helper.addAttachment("Invoice", file);

      emailSender.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String sendMultipleMessageWithAttachment(String[] to, String subject, String text,
      HashMap<String, String> filesList) {

    String errorMsg = "";
    try {
      MimeMessage message = new MimeMessage(supportMailConfig());
      // pass 'true' to the constructor to create a multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(supportuser);
      if (to.length > 0) {
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        if (filesList != null) {
          for (Entry<String, String> filePath : filesList.entrySet()) {
            FileSystemResource file = new FileSystemResource(new File(filePath.getValue()));
            helper.addAttachment(filePath.getKey(), file);
          }
        }
        //emailSender.send(message);
        Transport.send(message);
        errorMsg = "SUCCESS";
      } else {
        errorMsg = "MAILADDRESS-MISSING";
      }
    } catch (MessagingException exception) {
      errorMsg = exception.getMessage();
      exception.printStackTrace();
    }

    return errorMsg;
  }

  @Override
  public String sendMultipleMessageWithAttachment(String[] to, String subject, String text,
      HashMap<String, String> filesList, String scheduleDate) {

    String errorMsg = "";
    
    try {
      MimeMessage message = new MimeMessage(supportMailConfig());
      // pass 'true' to the constructor to create a multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(supportuser);
      if (to.length > 0) {
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        if (filesList != null) {
          for (Entry<String, String> filePath : filesList.entrySet()) {
            FileSystemResource file = new FileSystemResource(new File(filePath.getValue()));
            String fName = filePath.getKey().substring(0, filePath.getKey().length() - 5) + "-"
                + scheduleDate + ".xlsx";
            helper.addAttachment(fName, file);
          }
        }
        //emailSender.send(message);
        Transport.send(message);
        errorMsg = "SUCCESS";
      } else {
        errorMsg = "MAILADDRESS-MISSING";
      }
    } catch (MessagingException exception) {
      errorMsg = exception.getMessage();
      exception.printStackTrace();
    }

    return errorMsg;
  }

  @Override
  public String sendMailWithAttachment(String[] to, String[] cc, String subject, String text,
      HashMap<String, String> filesList, String scheduleDate) {

    String errorMsg = "";
    try {
      MimeMessage message = emailSender.createMimeMessage();
      // pass 'true' to the constructor to create a multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(fromMail);
      if (to.length > 0) {
        helper.setTo(to);
        if (cc.length > 0) {
          helper.setCc(cc);
        }
        helper.setSubject(subject);
        helper.setText(text, true);
        System.out.println("mail attachment loop starts");
        if (filesList != null) {
          for (Entry<String, String> filePath : filesList.entrySet()) {
            System.out.println("attachment loop inside");
            FileSystemResource file = new FileSystemResource(new File(filePath.getValue()));
            String fName = filePath.getKey().substring(0, filePath.getKey().length() - 5) + "-"
                + scheduleDate + ".xlsx";
            System.out.println("file name-->" + fName);
            helper.addAttachment(fName, file);
          }
        }
        System.out.println("mail attachment loop starts");
        emailSender.send(message);
        errorMsg = "SUCCESS";
      } else {
        errorMsg = "MAILADDRESS-MISSING";
      }
    } catch (MessagingException exception) {
      errorMsg = exception.getMessage();
      exception.printStackTrace();
    }

    return errorMsg;
  }

  @Override
  public String sendMultipleMessageWithZipAttach(String[] to, String[] cc, String subject,
      String text, String fileName, String filePath, String scheduleDate) {

    String errorMsg = "";
    try {
      MimeMessage message = emailSender.createMimeMessage();
      // pass 'true' to the constructor to create a multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(fromMail);
      if (to.length > 0) {
        helper.setTo(to);
        helper.setCc(cc);
        helper.setSubject(subject);
        helper.setText(text);
        if (filePath != null) {
          FileSystemResource file = new FileSystemResource(new File(filePath));
          helper.addAttachment(fileName, file);
        }
        emailSender.send(message);
        errorMsg = "SUCCESS";
      } else {
        errorMsg = "MAILADDRESS-MISSING";
      }
    } catch (MessagingException exception) {
      errorMsg = exception.getMessage();
      exception.printStackTrace();
    }

    return errorMsg;
  }
  
  public static void main(String[] a) {
    EmailServiceImpl ss = new EmailServiceImpl();
    String[] mail = {"balaji.1992cs@gmail.com"};
    ss.sendMultipleMessageWithAttachment(mail, "Testing Mail",
        "Testing GGGG", null);
  }

}
