package org.apache.poi.ss;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

public class SendEmailOffice365 {
  
  public static void main(String[] args) throws IOException {
    
     String sourceFolder = "C:\\folderA";
    String targetFolder = "C:\\folderA/folderA20200420/";
   
    File srcDir = new File(targetFolder);
    srcDir.mkdirs();
    deleteFiles(srcDir);
   /* File sFile = new File(sourceFolder);
    File[] sourceFiles = sFile.listFiles();
    for (File fSource : sourceFiles) {
      if(fSource.getName().contains("xlsx")) {
        File fTarget = new File(new File(srcDir.getAbsolutePath()), fSource.getName());
        copyFileUsingStream(fSource, fTarget);
      }
       // deleteFiles(fSource);
    }*/
       
  }
  
  private static void copyFileUsingStream(File source, File dest) {
    InputStream is = null;
    OutputStream os = null;
    try {
        is = new FileInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } catch (Exception ex) {
        System.out.println("Unable to copy file:" + ex.getMessage());
    } finally {
        try {
            is.close();
            os.close();
        } catch (Exception ex) {
        }
    }
}
  
  private static void deleteFiles(File fSource) {
    if(fSource.exists()) {
        try {
            FileUtils.forceDelete(fSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
  
}
  /*

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private static final String SERVIDOR_SMTP = "smtp.office365.com";
    private static final int PORTA_SERVIDOR_SMTP = 587;
    private static final String CONTA_PADRAO = "online-rpt@royalpark.net";
    private static final String SENHA_CONTA_PADRAO = "Lafu2206";

    private final String from = "online-rpt@royalpark.net";
    private final String to = "balaji.1992cs@gmail.com";

    private final String subject = "Teste";
    private final String messageContent = "Dear Partner,\r\n" + 
        "\r\n" + 
        "\r\n" + 
        "Greetings from Royal Gulf Tourism L.L.C.!\r\n" + 
        " \r\n" + 
        "\r\n" + 
        "Kindly find the updated RateSheet for your reference.\r\n" + 
        " \r\n" + 
        "\r\n" + 
        "Thanks & Regards,\r\n" + 
        "\r\n" + 
        "\r\n" + 
        "Royal Gulf Tourism L.L.C. LLC.";

    public void sendEmail() {
        final Session session = Session.getInstance(this.getEmailProperties(), new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONTA_PADRAO, SENHA_CONTA_PADRAO);
            }

        });

        try {
            final Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(messageContent);
            message.setSentDate(new Date());
            Transport.send(message);
        } catch (final MessagingException ex) {
            LOGGER.log(Level.WARNING, "Erro ao enviar mensagem: " + ex.getMessage(), ex);
        }
    }

    public Properties getEmailProperties() {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", SERVIDOR_SMTP);
        config.put("mail.smtp.port", PORTA_SERVIDOR_SMTP);
        return config;
    }


    public static void main(final String[] args) {
        new SendEmailOffice365().sendEmail();
      
      Map<String,BigInteger> processMap  = new TreeMap<String, BigInteger>(String.CASE_INSENSITIVE_ORDER);
      processMap.put("Hai", new BigInteger("1"));
      processMap.put("aaaai", new BigInteger("123"));
      
      System.out.println(processMap.get("AaaAI"));
      
    }
    
    
    
  public String zipFiles(String... filePaths) {
    String zipFileName = "";
    try {
      File firstFile = new File(filePaths[0]);
      zipFileName = firstFile.getName().concat(".zip");
      FileOutputStream fos = new FileOutputStream(zipFileName);
      ZipOutputStream zos = new ZipOutputStream(fos);
      for (String aFile : filePaths) {
        zos.putNextEntry(new ZipEntry(new File(aFile).getName()));
        byte[] bytes = Files.readAllBytes(Paths.get(aFile));
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
      }
      zos.close();
    } catch (FileNotFoundException ex) {
      System.err.println("A file does not exist: " + ex);
    } catch (IOException ex) {
      System.err.println("I/O error: " + ex);
    }
    
    return zipFileName;
  }

}*/