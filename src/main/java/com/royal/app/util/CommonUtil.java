package com.royal.app.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.BeanUtils;

public class CommonUtil {
	
	public static String nullRemove(String value) {
		return value != null ? value:"";
	}
	
	public static boolean valueTypeCheck(Object obj, String c) 
         throws ClassNotFoundException 
	{ 
      return Class.forName(c).isInstance(obj); 
    } 
	
	public static String valueTypeCheck(Object obj) throws ClassNotFoundException 
    {
      boolean result = false;
      result = Class.forName("java.lang.Integer").isInstance(obj);
      if (result) {
        return "INT";
      }
      result = Class.forName("java.lang.Double").isInstance(obj);
      if (result) {
        return "DOUBLE";
      }
      result = Class.forName("java.lang.Float").isInstance(obj);
      if (result) {
        return "FLOAT";
      }
      
      return "STRING";
    } 
	
	public static String nullRemove(Object value) {
		return value != null ? value.toString():"";
	}
	
	public static int IntNullRemove(String value) {
      return value != null ? Integer.parseInt(value):0;
    }
	
	public static Double DoubleNullRemove(String value) {
      return value != null ? Double.parseDouble(value):0.00;
    }
	
	public static String nullRemoveWithTrim(String value) {
		return value != null ? value.trim():"";
	}
	
	
	public static void copyProperties(Object source, Object target) {
		 BeanUtils.copyProperties(source, target);
	}
	
	public static boolean nullCheckBigInt(Object value) {
		boolean flag = false;
		if (value != null) {
			flag = true;
		}
		return flag;
	}
	
	public static void getMapResultObj(List<Object[]> resultObjList, Map<String, String> courAgencyNameMap) {
		if (resultObjList != null && !resultObjList.isEmpty()) {
			for (Object[] courAgencyName : resultObjList) {
				courAgencyNameMap.put(courAgencyName[0].toString(), courAgencyName[1].toString());
			}
		}
	}
	
    //Utility function
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    
    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
    {
      final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
       
      return t ->
      {
        final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
         
        return seen.putIfAbsent(keys, Boolean.TRUE) == null;
      };
    }
    
    public static Map<String, String> zipFiles(HashMap<String, String> filePaths) {
      String zipFileName = "";
      String tempFile = "";
      Map<String, String> zipFileMap = new HashMap<>();
      try {
        for (Entry<String,String> aFile : filePaths.entrySet()) {
          tempFile = aFile.getKey();
          filePaths.put(aFile.getKey(), aFile.getValue());
          break;
        }
        String currentDirectory = System.getProperty("user.dir");
        currentDirectory = currentDirectory + "/ratesheet/zippedFiles/";
        File directory = new File(currentDirectory);
        if (!directory.exists()) directory.mkdirs();
        zipFileName = currentDirectory+tempFile.concat(".zip");
        zipFileMap.put(tempFile, zipFileName);
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (Entry<String,String> aFile : filePaths.entrySet()) {
          zos.putNextEntry(new ZipEntry(new File(aFile.getValue()).getName()));
          byte[] bytes = Files.readAllBytes(Paths.get(aFile.getValue()));
          zos.write(bytes, 0, bytes.length);
          zos.closeEntry();
        }
        zos.close();
      } catch (FileNotFoundException ex) {
        System.err.println("A file does not exist: " + ex);
      } catch (IOException ex) {
        System.err.println("I/O error: " + ex);
      }
  
      return zipFileMap;
    }

}
