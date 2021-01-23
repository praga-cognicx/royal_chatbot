package com.royal.app.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.royal.app.constants.StatusCodeConstants;
import com.royal.app.message.response.GenericResponse;
import com.royal.app.services.RateSheetService;
import com.royal.app.shared.dto.RateSheetDto;

@RestController
@RequestMapping("/ratesheet")
public class RateSheetController {
  
  private Logger logger = Logger.getLogger(RateSheetController.class);

  @Autowired
  private RateSheetService rateSheetService;
  
  @PostMapping("/getRateSheetDet")
  public ResponseEntity<GenericResponse> getRateSheetDet(@RequestParam BigInteger rsId)
      throws JsonParseException, JsonMappingException, IOException, ParseException {
    GenericResponse gr = new GenericResponse();
    try {
      
      rateSheetService.generateSingleRateSheetWithOutMail(rsId);
      
     /* RateSheetDto rateSheetDto =
          new RateSheetDto();
      BeanUtils.copyProperties(rateSheetRequest, rateSheetDto);*/
   //   rateSheetService.getReteSheetExcel(rateSheetDto);
 /*     if (reportMonthSelectionRequestDto != null
          && reportMonthSelectionRequestDto.getResultObj() != null
          && reportMonthSelectionRequestDto.getResultObj() != "") {
        gr.setStatus(StatusCodeConstants.SUCCESS);
        gr.setError(StatusCodeConstants.SUCCESS_STR);
        gr.setMessage("Reports Found");
        gr.setHeader(reportMonthSelectionRequestDto.getHeader());
        gr.setValue(
            new GenericParameterWiseRptResponse(reportMonthSelectionRequestDto.getResultObj(),
                reportMonthSelectionRequestDto.getResultObj2()));
        gr.setGrandTotal(reportMonthSelectionRequestDto.getGrandTotal());
      } else {
        gr.setStatus(StatusCodeConstants.SUCCESS);
        gr.setError(StatusCodeConstants.SUCCESS_STR);
        gr.setMessage("Data Not Found.");
        gr.setValue(null);
      }
*/
      gr.setStatus(StatusCodeConstants.SUCCESS);
      gr.setError(StatusCodeConstants.SUCCESS_STR);
      gr.setMessage("Success");
      gr.setValue(null);
    } catch (Exception e) {
      gr.setStatus(StatusCodeConstants.FAILURE);
      gr.setError(StatusCodeConstants.FAILURE_STR);
      gr.setMessage("Unable to fetch report. Please contact admin.");
      gr.setValue(null);
      logger.error("Exception::CRMReportController.Class::getTLWiseScoreReport()", e);
    }

    return new ResponseEntity<>(new GenericResponse(gr), HttpStatus.OK);

  }
  
  @PostMapping("/getRateSheetDetNew")
  public ResponseEntity<GenericResponse> getRateSheetDetNew(@RequestParam BigInteger rsId)
      throws JsonParseException, JsonMappingException, IOException, ParseException {
    GenericResponse gr = new GenericResponse();
    try {
      rateSheetService.getReteSheetExcel(new RateSheetDto());
    } catch (Exception e) {
      gr.setStatus(StatusCodeConstants.FAILURE);
      gr.setError(StatusCodeConstants.FAILURE_STR);
      gr.setMessage("Unable to fetch report. Please contact admin.");
      gr.setValue(null);
      logger.error("Exception::CRMReportController.Class::getTLWiseScoreReport()", e);
    }

    return new ResponseEntity<>(new GenericResponse(gr), HttpStatus.OK);

  }
}
