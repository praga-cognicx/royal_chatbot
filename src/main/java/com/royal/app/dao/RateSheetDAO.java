package com.royal.app.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.royal.app.message.request.RSUpdatesCreateReq;
import com.royal.app.message.request.RateSheetMgtPriorityReqRes;
import com.royal.app.message.request.RatesheetUpdateViewRequest;
import com.royal.app.message.response.RSUpdatesResponse;
import com.royal.app.model.RatesheetAgentMap;
import com.royal.app.model.RatesheetMapping;
import com.royal.app.model.RatesheetMgt;
import com.royal.app.model.RatesheetUpdates;
import com.royal.app.shared.dto.RateSheetDto;
import com.royal.app.shared.dto.RateSheetUpdatesDto;
import com.royal.app.shared.dto.RatesheetMappingDto;
import com.royal.app.shared.dto.RatesheetMgtDto;

public interface RateSheetDAO {
  
  public Map<String, Object> getRateSheetTables(final RateSheetDto rateSheetDto) throws Exception;

  String getCurrencyCodeFromAgentMast(String agentCode) throws Exception;

  public List<List<String>> getMaximumOccupancyData(String type, String conPromoId, String partyCode) throws Exception;

  public List<Object[]> getdtMinNightsData(String hotelId, String contractId) throws Exception;

  List<Object[]> getWeekDaysData(String contractId) throws Exception;  
  
  public List<Object[]> getSpecialEventTables(final RateSheetDto rateSheetDto, final String rateType, final String ratePlanId) throws Exception;
    
  public List<Object[]> getCancelPolicyDetails(final RateSheetDto rateSheetDto, final String ratePlanId) throws Exception;

  public Map<String, Object> CheckInCheckOutPolicy(String partyCode, String contractId) throws Exception;

  List<Object[]> getCheckInCheckOutDateDetails(String contractId, String seasonCodes)
      throws Exception;

  List<Object[]> getCheckInCheckOutRoomDetails(String checkInoutPolicyId, String partyCode)
      throws Exception;

  List<Object[]> getCheckInCheckOutRestrictDetails(String checkInoutPolicyId) throws Exception;

  List<Object[]> getCheckInCheckOutEarlyCheckDetails(String checkInoutPolicyId) throws Exception;

  List<Object[]> getGeneralPolicyDetails(RateSheetDto rateSheetDto, String ratePlanId)
      throws Exception;

  List<Object[]> getHotelConstractionDetails(RateSheetDto rateSheetDto, String ratePlanId)
      throws Exception;

  RateSheetDto getRateSheetDet() throws Exception;

  String getEndContractDate(String partyCode, String indCode) throws Exception; 
  
  public boolean saveRateSheetTimeLog(RateSheetDto rateSheetDto, Date startTime, Date endTime) throws Exception; 
    
  public boolean saveRateSheetErrorLog(RateSheetDto rateSheetDto, String errorMsg, String fileName, byte[] fileContent, String status) throws Exception;

  List<Object[]> getRateSheetUpdatesDet(BigInteger rateSheetId, String schedularDate) throws Exception;

  Map<String, String> getRatesheetAppParamDet(String Type) throws Exception;
  
  public Object[] uploadRateSheetDet(List<List<String>> insertList) throws Exception;
  
  public RatesheetMapping saveRateSheetMapDetNew(RatesheetMapping rsMap) throws Exception; 
  
  public RatesheetMgt saveRateSheetDetNew(RatesheetMgt rsMgt) throws Exception;
  
  public Map<String, Object[]> getPartyMastDet() throws Exception;
  
  public List<RatesheetMgt> findRateSheetDet(String rName) throws Exception;

  public List<RatesheetMgt> getAllRateSheetDet() throws Exception;
  
  public List<RatesheetMgt> getRateSheetWithMaps(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  RatesheetMgtDto saveRateSheetDet(RatesheetMgtDto rateSheetMgtDto) throws Exception;

  Object[] getCountryCode(String agentCode) throws Exception;
  
  public List<Object[]> getMappedHotelDropdown(String partyCode) throws Exception;

  boolean updateRateSheetDet(RatesheetMgtDto rateSheetMgtDto) throws Exception;
  
  boolean updatePriorityRateSheetDet(List<RateSheetMgtPriorityReqRes> RateSheetMgtPriorityList) throws Exception;
  
  public List<Object[]> getRateSheetMapSearch(BigInteger rsId, String categoryCode, String sectorCode, String cityCode) throws Exception;
  
  public Object[] getRateSheetMgtDropdown(String type) throws Exception;
  
  public List<RatesheetMgt> findRateSheetDetByRsId(BigInteger id) throws Exception;
  
  public RatesheetMappingDto saveRatesheetMap(RatesheetMappingDto ratesheetMappingDto) throws Exception;
  
  public List<RatesheetUpdates> getRateSheetUpdates() throws Exception;
  
  public Object[] saveRateSheetUpdates(RSUpdatesCreateReq insertObj) throws Exception; 
  
  public Object[] updateRateSheetUpdates(RSUpdatesCreateReq insertObj) throws Exception ;
  
  public List<Object[]> searchRSUpdatesView(RatesheetUpdateViewRequest ratesheetViewRequest)
      throws Exception;
  public boolean ratesheetMapStatusChange(List<BigInteger> list, BigInteger rsId) throws Exception;

  List<RatesheetAgentMap> getRateSheetAgnetMap() throws Exception;
  
  public Map<?, ?> getRateSheetsDivByMap(String type) throws Exception;

boolean ratesheetMapUpdateStatus(BigInteger mapId, BigInteger rsId) throws Exception;

String checkRatesheetDate() throws Exception;

boolean updateRatesheetSchedularDate() throws Exception;
}
