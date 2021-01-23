package com.royal.app.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.royal.app.constants.StatusCodeConstants;
import com.royal.app.message.request.RSUpdatesCreateReq;
import com.royal.app.message.request.RateSheetMgtPriorityReqRes;
import com.royal.app.message.request.RateSheetMgtRequest;
import com.royal.app.message.request.RatesheetMapCreateRequest;
import com.royal.app.message.request.RatesheetMapsSearchRequest;
import com.royal.app.message.request.RatesheetMgtUpdateRequest;
import com.royal.app.message.request.RatesheetUpdateViewRequest;
import com.royal.app.message.request.RatesheetViewRequest;
import com.royal.app.message.response.GenericResponse;
import com.royal.app.message.response.KeyValuePair;
import com.royal.app.message.response.RSUpdatesResponse;
import com.royal.app.message.response.RateSheetMgtResponse;
import com.royal.app.services.RateSheetMgtService;
import com.royal.app.shared.dto.RatesheetMappingDto;
import com.royal.app.shared.dto.RatesheetMgtDto;
import com.royal.app.util.UserInfo;

@RestController
@CrossOrigin
@RequestMapping("/ratesheet")
public class RatesheetMgtController {
  private static final Logger logger = LoggerFactory.getLogger(RatesheetMgtController.class);

  @Autowired
  PasswordEncoder encoder;
  
  @Autowired
  UserInfo userInfo;

  @Autowired
  RateSheetMgtService rateSheetMgtService;

  @PostMapping(path = "/mgt/create")
  public ResponseEntity<GenericResponse> createRateSheet(
      @Valid @RequestBody RateSheetMgtRequest rateSheetMgtRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto();
      BeanUtils.copyProperties(rateSheetMgtRequest, ratesheetMgtDto);
      rateSheetMgtService.saveRateSheet(ratesheetMgtDto);
      if ((ratesheetMgtDto.getErrorMsg() == null || ratesheetMgtDto.getErrorMsg().isEmpty()) && ratesheetMgtDto.getAutogenRsId() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet created successfully.");
        genericResponse.setValue(new RateSheetMgtResponse(ratesheetMgtDto));
      } else if(ratesheetMgtDto.getErrorMsg() != null  && !ratesheetMgtDto.getErrorMsg().isEmpty()) {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage(ratesheetMgtDto.getErrorMsg());
        genericResponse.setValue(null);
      }else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Unable to create ratesheet. Please contact admin.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to create ratesheet. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::createRateSheet()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/mgt/update")
  public ResponseEntity<GenericResponse> updateRateSheet(
      @Valid @RequestBody RatesheetMgtUpdateRequest ratesheetMgtUpdateRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto();
      BeanUtils.copyProperties(ratesheetMgtUpdateRequest, ratesheetMgtDto);
      ratesheetMgtDto.setAutogenRsId(ratesheetMgtUpdateRequest.getId());
      rateSheetMgtService.updateRateSheet(ratesheetMgtDto);
      if (ratesheetMgtDto.isFlag()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet updated successfully.");
        genericResponse.setValue(new RateSheetMgtResponse(ratesheetMgtDto));
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Unable to update ratesheet. Please contact admin.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to update ratesheet. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::updateRateSheet()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/mgt/list")
  public ResponseEntity<GenericResponse> rateSheetList() throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto(); 
      ratesheetMgtDto = rateSheetMgtService.getAllRateSheet(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheets Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheets. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetList()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/mgt/priority/list")
  public ResponseEntity<GenericResponse> rateSheetPriorityList() throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto(); 
      ratesheetMgtDto = rateSheetMgtService.getRateSheetListForPrority(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheets Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheets. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetPriorityList()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/mgt/priority/update")
  public ResponseEntity<GenericResponse> updatePriorityRateSheet(
      @Valid @RequestBody List<RateSheetMgtPriorityReqRes> RateSheetMgtPriorityList) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto();
      if (rateSheetMgtService.updatePriorityRateSheet(RateSheetMgtPriorityList)) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet priority updated successfully.");
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Unable to update ratesheet priority. Please contact admin.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to update ratesheet. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::updateRateSheet()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/maps/list")
  public ResponseEntity<GenericResponse> rateSheetMapList(@RequestBody RatesheetMapsSearchRequest rsMapsRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto(); 
      ratesheetMgtDto.setAutogenRsId(rsMapsRequest.getRatesheetId());
      ratesheetMgtDto.setCategoryCode(rsMapsRequest.getCategoryCode());
      ratesheetMgtDto.setSectorCode(rsMapsRequest.getSectorCode());
      ratesheetMgtDto.setCityCode(rsMapsRequest.getCityCode());
      ratesheetMgtDto = rateSheetMgtService.getRateSheetMapsDet(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Maps Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Maps Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheet Maps. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetList()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/maps/dropdowns")
  public ResponseEntity<GenericResponse> rsMapsDropdowns() throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto(); 
      ratesheetMgtDto = rateSheetMgtService.getRSMapsDropDowns(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Maps Dropdown Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Maps Dropdown Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheet Maps dropdowns. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rsMapsDropdowns()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/agentdropdown")
  public ResponseEntity<GenericResponse> rsAgentDropdown() throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto(); 
      ratesheetMgtDto = rateSheetMgtService.getRSAgentDropDown(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Agent Dropdown Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Agent Dropdown Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch Agent dropdown. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rsAgentDropdown()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/Maps/create")
  public ResponseEntity<GenericResponse> createRateSheetMap(
      @Valid @RequestBody RatesheetMapCreateRequest rsMapCreateRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMappingDto ratesheetMappingDto = new RatesheetMappingDto();
      ratesheetMappingDto.setAutogenRsId(rsMapCreateRequest.getRatesheetId());
      ratesheetMappingDto.setRatesheetMappingList(rsMapCreateRequest.getRatesheetMappingList());
      ratesheetMappingDto = rateSheetMgtService.saveRSMapping(ratesheetMappingDto);
      if (ratesheetMappingDto.isStatus()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Mapped successfully.");
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Unable to Map Ratesheet. Please contact admin.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Error::Unable to Map Ratesheet. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::createRateSheetMap()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/mgt/view")
  public ResponseEntity<GenericResponse> rateSheetMgtView(
      @Valid @RequestBody RatesheetViewRequest ratesheetViewRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto();
      ratesheetMgtDto.setAutogenRsId(ratesheetViewRequest.getRatesheetId());
      ratesheetMgtDto = rateSheetMgtService.getAllRateSheetView(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet view Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets View Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheet view. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetMgtView()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/mgt/download")
  public ResponseEntity<GenericResponse> rateSheetMgtViewDownload(
      @Valid @RequestBody RatesheetViewRequest ratesheetViewRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto();
      ratesheetMgtDto.setAutogenRsId(ratesheetViewRequest.getRatesheetId());
      String content  = rateSheetMgtService.generateRSMgtDownload(ratesheetMgtDto);
      if (content != null && !content.isEmpty()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet view download found");
        genericResponse.setValue(content);
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets View Download Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to download ratesheet view. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetMgtViewDownload()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/view/download")
  public ResponseEntity<GenericResponse> rateSheetUpdatesViewDownload(
      @Valid @RequestBody RatesheetUpdateViewRequest ratesheetViewRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      String content  = rateSheetMgtService.generateRSUpdatesDownload(ratesheetViewRequest);
      if (content != null && !content.isEmpty()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Updates view download found");
        genericResponse.setValue(content);
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Updates View Download Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to download ratesheet updates view. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetUpdatesViewDownload()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/list")
  public ResponseEntity<GenericResponse> rateSheetUpdateList() throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      List<RSUpdatesResponse> response = rateSheetMgtService.getRateSheetUpdates();
      if (response != null && !response.isEmpty()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Update List Found");
        genericResponse.setValue(response);
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Update List Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheet update list. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheetUpdateList()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/saveRatesheetUpdates")
  public ResponseEntity<GenericResponse> saveRateSheetUpdateList(@Valid @RequestBody List<RSUpdatesCreateReq> rsUpdatesCreateReq) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      Object[] response = rateSheetMgtService.saveRateSheetUpdates(rsUpdatesCreateReq);
      if(response != null) {
      if ((boolean)response[0]) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet updates created");
        genericResponse.setValue(response);
      } else if (!(boolean)response[0]) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet updates partiality created.\n "+response[1]);
        genericResponse.setValue(null);
      }
      }else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Update Not created.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to create ratesheet updates. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::saveRateSheetUpdateList()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/updateRatesheetUpdates")
  public ResponseEntity<GenericResponse> updateRateSheetUpdate(@Valid @RequestBody RSUpdatesCreateReq rsUpdatesCreateReq) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    rsUpdatesCreateReq.setCreatedBy(userInfo.getEmployeeId());
    try {
      Object[] response = rateSheetMgtService.updateRateSheetUpdates(rsUpdatesCreateReq);
      if (response != null) {
        if ((boolean) response[0]) {
          genericResponse.setStatus(StatusCodeConstants.SUCCESS);
          genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
          genericResponse.setMessage("Ratesheet updates updated.");
          genericResponse.setValue(response);
        } else if (!(boolean) response[0]) {
          genericResponse.setStatus(StatusCodeConstants.FAILURE);
          genericResponse.setError(StatusCodeConstants.FAILURE_STR);
          genericResponse.setMessage("RateSheets update not updated.");
          genericResponse.setValue(null);
        }
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to update ratesheet updates. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::updateRateSheetUpdate()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/mappedhoteldropdown")
  public ResponseEntity<GenericResponse> getMappedHotelDropDown(@RequestParam @NotEmpty String partyCode) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      List<KeyValuePair> kvPair = new ArrayList<>(); 
      kvPair = rateSheetMgtService.getMappedHotelDropDown(partyCode);
      if (kvPair != null && !kvPair.isEmpty()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Mapped hotel dropdown Found");
        genericResponse.setValue(kvPair);
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Mapped hotel dropdown not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch mapped hotel dropdown. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::getMappedHotelDropDown()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/view")
  public ResponseEntity<GenericResponse> rateSheeUpdatesView(
      @Valid @RequestBody RatesheetUpdateViewRequest ratesheetViewRequest) throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
     
      List<?> response = rateSheetMgtService.searchRatesheetUpdateView(ratesheetViewRequest);
      if (response != null && !response.isEmpty()) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Updates view Found");
        genericResponse.setValue(response);
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("RateSheets Updates View Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch ratesheet updates view. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rateSheeUpdatesView()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
  @PostMapping(path = "/update/dropdowns")
  public ResponseEntity<GenericResponse> rsUpdateDropdowns() throws Exception {
    GenericResponse genericResponse = new GenericResponse();
    try {
      RatesheetMgtDto ratesheetMgtDto = new RatesheetMgtDto(); 
      ratesheetMgtDto = rateSheetMgtService.getRSUpdateTypeDropDown(ratesheetMgtDto);
      if (ratesheetMgtDto != null && ratesheetMgtDto.getResult() != null) {
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Ratesheet Updates Dropdowns Found");
        genericResponse.setValue(ratesheetMgtDto.getResult());
      } else {
        genericResponse.setStatus(StatusCodeConstants.FAILURE);
        genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        genericResponse.setMessage("Ratesheet Updates Dropdowns Not Found.");
        genericResponse.setValue(null);
      }
    } catch (Exception e) {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("Unable to fetch Ratesheet updates dropdowns. Please contact admin.");
      genericResponse.setValue(null);
      logger.error("Exception::RatesheetMgtController.Class::rsUpdateDropdowns()", e);
    }
    return ResponseEntity.ok().body(new GenericResponse(genericResponse));
  }
  
}
