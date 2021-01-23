package com.royal.app.services;

import java.math.BigInteger;
import java.util.List;
import com.royal.app.shared.dto.RateSheetDto;

public interface RateSheetService {
  
  public Object getReteSheetExcel(final RateSheetDto rateSheetDto) throws Exception;
  
  public void generateRateSheet() throws Exception ;
  
  public Object[] uploadRateSheetDet(List<List<String>> insertList) throws Exception;
  
  public void generateRateSheetNew(String schedularDate) throws Exception;
  
  public void execAsyncRatesheetGenerateTask(BigInteger rsId) throws Exception;
  
  public void generateSingleRateSheetWithOutMail(BigInteger rsId) throws Exception;
  
  public void sendRatesheetUpdates(String schedularDate) throws Exception; 
  
  String checkRatesheetDate() throws Exception;
  
  boolean updateRatesheetSchedularDate() throws Exception;

}
