package com.royal.app.services;

import java.util.List;
import com.royal.app.message.request.RSUpdatesCreateReq;
import com.royal.app.message.request.RateSheetMgtPriorityReqRes;
import com.royal.app.message.request.RatesheetUpdateViewRequest;
import com.royal.app.message.response.KeyValuePair;
import com.royal.app.message.response.RSUpdatesResponse;
import com.royal.app.shared.dto.RatesheetMappingDto;
import com.royal.app.shared.dto.RatesheetMgtDto;

public interface RateSheetMgtService {
  
  public RatesheetMgtDto saveRateSheet(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public RatesheetMgtDto updateRateSheet(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public boolean updatePriorityRateSheet(List<RateSheetMgtPriorityReqRes> RateSheetMgtPriorityList) throws Exception;
  
  public RatesheetMgtDto getAllRateSheet(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public RatesheetMgtDto getRateSheetListForPrority (RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public RatesheetMgtDto getRateSheetMapsDet(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public RatesheetMgtDto getRSMapsDropDowns(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public RatesheetMgtDto getRSAgentDropDown(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public RatesheetMgtDto getRSUpdateTypeDropDown(RatesheetMgtDto ratesheetMgtDto) throws Exception; 
  
  public List<KeyValuePair> getMappedHotelDropDown(String partyCode) throws Exception; 
  
  public RatesheetMappingDto saveRSMapping(RatesheetMappingDto ratesheetMappingDto) throws Exception;
  
  public RatesheetMgtDto getAllRateSheetView(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public List<RSUpdatesResponse> getRateSheetUpdates() throws Exception;
  
  public Object[] saveRateSheetUpdates(List<RSUpdatesCreateReq> rsUpdatesCreateReq) throws Exception;
  
  public Object[] updateRateSheetUpdates(RSUpdatesCreateReq rsUpdatesCreateReq) throws Exception;
  
  public List<RSUpdatesResponse> searchRatesheetUpdateView(RatesheetUpdateViewRequest ratesheetViewRequest) throws Exception;

  public String generateRSMgtDownload(RatesheetMgtDto ratesheetMgtDto) throws Exception;
  
  public String generateRSUpdatesDownload(RatesheetUpdateViewRequest ratesheetViewRequest) throws Exception; 
}