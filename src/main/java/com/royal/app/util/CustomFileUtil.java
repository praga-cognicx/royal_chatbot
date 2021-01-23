package com.royal.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

public class CustomFileUtil {

  private CustomFileUtil() {
    throw new IllegalStateException("File Utility class");
  }

  public static void copyFileUsingStream(File source, File dest) {
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
      ex.printStackTrace();
    } finally {
      try {
        is.close();
        os.close();
      } catch (Exception ex) {
      }
    }
  }

  public static void deleteFiles(File fSource) {
    if (fSource.exists()) {
      try {
        FileUtils.forceDelete(fSource);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
