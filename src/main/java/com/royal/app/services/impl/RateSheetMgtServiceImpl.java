package com.royal.app.services.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.persistence.PersistenceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.royal.app.dao.RateSheetDAO;
import com.royal.app.message.request.RSUpdatesCreateReq;
import com.royal.app.message.request.RateSheetMgtPriorityReqRes;
import com.royal.app.message.request.RatesheetUpdateViewRequest;
import com.royal.app.message.response.KeyValuePair;
import com.royal.app.message.response.RSMapsDropdowns;
import com.royal.app.message.response.RSUpdatesDropdowns;
import com.royal.app.message.response.RSUpdatesResponse;
import com.royal.app.message.response.RateSheetMgtResponse;
import com.royal.app.message.response.RateSheetMgtViewResponse;
import com.royal.app.message.response.RatesheetMapResponse;
import com.royal.app.model.RatesheetMgt;
import com.royal.app.model.RatesheetUpdates;
import com.royal.app.services.RateSheetMgtService;
import com.royal.app.services.RateSheetService;
import com.royal.app.shared.dto.RatesheetMappingDto;
import com.royal.app.shared.dto.RatesheetMgtDto;
import com.royal.app.util.CommonUtil;
import com.royal.app.util.DateUtil;
import com.royal.app.util.ExcelUtil;

@Service
public class RateSheetMgtServiceImpl implements RateSheetMgtService {
  
  @Autowired
  RateSheetDAO rateSheetDAO;  
  
  @Autowired
  RateSheetService rateSheetService;
  
  private static final Logger logger = LoggerFactory.getLogger(RateSheetMgtServiceImpl.class);
  
  public RatesheetMgtDto saveRateSheet(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    try {
    ratesheetMgtDto.setStatus("Active");
     rateSheetDAO.saveRateSheetDet(ratesheetMgtDto);
    } catch(Exception exception) {
      if(exception instanceof PersistenceException) {
        if(exception.getCause() instanceof ConstraintViolationException)
      ratesheetMgtDto.setErrorMsg("Already exists.Please select different divison and ratesheet to add new.");
      } else {
        ratesheetMgtDto.setErrorMsg("Error.Please contact admin.");
      }
      logger.info("Exception :: RateSheetMgtServiceImpl :: saveRateSheet() : " + exception);
    }
    return ratesheetMgtDto;
  }
  
  public RatesheetMgtDto updateRateSheet(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    ratesheetMgtDto.setFlag(rateSheetDAO.updateRateSheetDet(ratesheetMgtDto));
    return ratesheetMgtDto;
  }
  
  public boolean updatePriorityRateSheet(List<RateSheetMgtPriorityReqRes> RateSheetMgtPriorityList) throws Exception {
    return rateSheetDAO.updatePriorityRateSheetDet(RateSheetMgtPriorityList);
  }
  
  public RatesheetMgtDto getAllRateSheet(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    List<RatesheetMgt> resultList = rateSheetDAO.getAllRateSheetDet();
    if (resultList != null && !resultList.isEmpty()) {
      List<RateSheetMgtResponse> rateSheetList = new ArrayList<>();
      resultList.stream().forEach(resultObj -> {
        RateSheetMgtResponse rsResponse = new RateSheetMgtResponse();
        BeanUtils.copyProperties(resultObj, rsResponse);
        rsResponse.setId(resultObj.getAutogenRsId());
        rateSheetList.add(rsResponse);
      });
      ratesheetMgtDto.setResult(rateSheetList);  
    }
    return ratesheetMgtDto;
  }
  
  public RatesheetMgtDto getRateSheetListForPrority (RatesheetMgtDto ratesheetMgtDto) throws Exception {
    List<RatesheetMgt> resultList = rateSheetDAO.getAllRateSheetDet();
    if (resultList != null && !resultList.isEmpty()) {
      List<RateSheetMgtPriorityReqRes> rateSheetList = new ArrayList<>();
      resultList.stream().forEach(resultObj -> {
        RateSheetMgtPriorityReqRes rsResponse = new RateSheetMgtPriorityReqRes();
        //BeanUtils.copyProperties(resultObj, rsResponse);
        rsResponse.setRsName(resultObj.getRsName());
        rsResponse.setRsDesc(resultObj.getRsDesc());
        if(resultObj.getPriority() != null) {
          rsResponse.setPriority(resultObj.getPriority());
        }
        rsResponse.setId(resultObj.getAutogenRsId());
        rateSheetList.add(rsResponse);
      });
      ratesheetMgtDto.setResult(rateSheetList);  
    }
    return ratesheetMgtDto;
  }
  
  public RatesheetMgtDto getAllRateSheetView(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    List<RatesheetMgt> resultList = rateSheetDAO.getRateSheetWithMaps(ratesheetMgtDto);
    if (resultList != null && !resultList.isEmpty()) {
      RateSheetMgtViewResponse rsResponseView = new RateSheetMgtViewResponse();
      resultList.stream().forEach(resultObj -> {
        rsResponseView.setId(resultObj.getAutogenRsId());
        rsResponseView.setAgentCode(resultObj.getAgentCode());
        rsResponseView.setAgentName(resultObj.getAgentName());
        rsResponseView.setDivisionName(resultObj.getDivisionName());
        rsResponseView.setDivisionId(resultObj.getDivisionId());
        rsResponseView.setEmailIds(resultObj.getEmailIds());
        rsResponseView.setRsDesc(resultObj.getRsDesc());
        rsResponseView.setRsName(resultObj.getRsName());
        rsResponseView.setSendRatesheet(resultObj.getSendRatesheet());
        rsResponseView.setSendUpdates(resultObj.getSendUpdates());
        rsResponseView.setStatus(resultObj.getStatus());
        List<RatesheetMapResponse> ratesheetMaps = new ArrayList<>();
        if(!resultObj.getRatesheetMapping().isEmpty()) {
          resultObj.getRatesheetMapping().forEach(mapObj -> {
            RatesheetMapResponse tempObj = new RatesheetMapResponse();
            if(mapObj.getAutogenRsMapId() != null) {
            tempObj.setId(mapObj.getAutogenRsMapId());
            }
            tempObj.setCategoryCode(mapObj.getCategoryCode());
            tempObj.setCategoryName(mapObj.getCategoryName());
            tempObj.setCityCode(mapObj.getCityCode());
            tempObj.setCityName(mapObj.getCityName());
            tempObj.setDisplayName(mapObj.getDisplayName());
            tempObj.setMapped(true);
            tempObj.setPartyCode(mapObj.getPartyCode());
            tempObj.setPartyName(mapObj.getPartyName());
            tempObj.setSectorCode(mapObj.getSectorCode());
            tempObj.setSectorName(mapObj.getSectorName());    
            ratesheetMaps.add(tempObj);
            
          });
          rsResponseView.setRatesheetMaps(ratesheetMaps);
        }
        
      });
      ratesheetMgtDto.setResult(rsResponseView);  
    }
    return ratesheetMgtDto;
  }
  
  public String generateRSMgtDownload(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    String fileContent = "";
    Workbook workbook = new XSSFWorkbook();
    CellStyle cellStyle = workbook.createCellStyle();
    try {
      ratesheetMgtDto = getAllRateSheetView(ratesheetMgtDto);
      if (ratesheetMgtDto.getResult() != null) {
        RateSheetMgtViewResponse ratesheetMgt =
            (RateSheetMgtViewResponse) ratesheetMgtDto.getResult();
        Sheet sheetIndex = workbook.createSheet("Ratesheet View");
        XSSFColor rptBlue = new XSSFColor(new Color(0, 112, 192));
        String logoName = "RoyalPark.png";
        if (ratesheetMgt.getDivisionId() != null && !ratesheetMgt.getDivisionId().isEmpty()
            && "02".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
          rptBlue = new XSSFColor(new Color(198, 89, 17));
          logoName = "RoyalGulf.png";
        }
        int lastLine = 8;
        XSSFColor rptLightGray = new XSSFColor(new Color(214, 220, 228));
        XSSFColor color9 = new XSSFColor(new Color(255, 255, 255));
        Row sheetIndexrow0 = sheetIndex.createRow(lastLine);
        sheetIndexrow0.setHeight((short) 500);
        Cell cell = sheetIndexrow0.createCell((short) 0);
        sheetIndex.setColumnWidth(0, (short) 1000);

        sheetIndex.setDisplayGridlines(false);
        CreationHelper helper = workbook.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(2);
        anchor.setCol2(7);
        anchor.setRow1(0);
        anchor.setRow2(7);
        Drawing<?> drawing = sheetIndex.createDrawingPatriarch();
        drawing.createPicture(anchor, ExcelUtil.getLogo((Workbook) workbook, sheetIndex, logoName,
            Workbook.PICTURE_TYPE_PNG));

        lastLine = lastLine + 1;
        sheetIndexrow0 = sheetIndex.createRow(lastLine);
        cell = sheetIndexrow0.createCell((short)1);
        cell.setCellValue("Rate Sheet");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
       
        cell = sheetIndexrow0.createCell((short)2);
        cell.setCellValue(ratesheetMgt.getRsName());
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 3), sheetIndex, workbook);
              
        cell = sheetIndexrow0.createCell((short)4);
        cell.setCellValue("Division Name");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
      
        cell = sheetIndexrow0.createCell((short)5);
        cell.setCellValue(ratesheetMgt.getDivisionName());
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 5, 6), sheetIndex, workbook);
       
        cell = sheetIndexrow0.createCell((short)7);
        cell.setCellValue("Agent");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
       
        cell = sheetIndexrow0.createCell((short)8);
        cell.setCellValue(ratesheetMgt.getAgentName());
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 8, 9), sheetIndex, workbook);
       
        
        lastLine = lastLine + 1;
        sheetIndexrow0 = sheetIndex.createRow(lastLine);
        cell = sheetIndexrow0.createCell((short)1);
        cell.setCellValue("Email ID's");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
       
        cell = sheetIndexrow0.createCell((short)2);
        cell.setCellValue(ratesheetMgt.getEmailIds());
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 3), sheetIndex, workbook);
              
        cell = sheetIndexrow0.createCell((short)4);
        cell.setCellValue("Desc");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
      
        cell = sheetIndexrow0.createCell((short)5);
        cell.setCellValue(ratesheetMgt.getRsDesc());
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 5, 6), sheetIndex, workbook);
       
        cell = sheetIndexrow0.createCell((short)7);
        cell.setCellValue("Send/Updates");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
       
        cell = sheetIndexrow0.createCell((short)8);
        cell.setCellValue(ratesheetMgt.getSendRatesheet()+"/"+ratesheetMgt.getSendUpdates());
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 8, 9), sheetIndex, workbook);
       
        lastLine = lastLine + 2;
        sheetIndexrow0 = sheetIndex.createRow(lastLine);
        cell = sheetIndexrow0.createCell((short)1);
        cell.setCellValue("Hotel Name");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 3), sheetIndex, workbook);
        
        cell = sheetIndexrow0.createCell((short)4);
        cell.setCellValue("Display Name");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 4, 5), sheetIndex, workbook);
              
        cell = sheetIndexrow0.createCell((short)6);
        cell.setCellValue("Category");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
        
        cell = sheetIndexrow0.createCell((short)7);
        cell.setCellValue("City");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
       
        cell = sheetIndexrow0.createCell((short)8);
        cell.setCellValue("Sector");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, true, false, (short)14, "Calibri"));
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 8, 9), sheetIndex, workbook);
        
        if(ratesheetMgt.getRatesheetMaps() != null && !ratesheetMgt.getRatesheetMaps().isEmpty()) {
          for(RatesheetMapResponse obj:ratesheetMgt.getRatesheetMaps()){
            lastLine = lastLine + 1;
            sheetIndexrow0 = sheetIndex.createRow(lastLine);
            cell = sheetIndexrow0.createCell((short)1);
            cell.setCellValue(obj.getPartyName());
            cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, false, (short)12, "Calibri"));
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 3), sheetIndex, workbook);
            
            cell = sheetIndexrow0.createCell((short)4);
            cell.setCellValue(obj.getDisplayName());
            cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, false, (short)12, "Calibri"));
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 4, 5), sheetIndex, workbook);
                  
            cell = sheetIndexrow0.createCell((short)6);
            cell.setCellValue(obj.getCategoryName());
            cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, false, (short)12, "Calibri"));
            
            cell = sheetIndexrow0.createCell((short)7);
            cell.setCellValue(obj.getCityName());
            cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, false, (short)12, "Calibri"));
           
            cell = sheetIndexrow0.createCell((short)8);
            cell.setCellValue(obj.getSectorName());
            cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, false, (short)12, "Calibri"));
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 8, 9), sheetIndex, workbook);
            
          }
        }
        
        sheetIndex.setColumnWidth(1, (short)5000);
        sheetIndex.setColumnWidth(2, (short)5000);
        sheetIndex.setColumnWidth(3, (short)5000);
        sheetIndex.setColumnWidth(4, (short)5000);
        sheetIndex.setColumnWidth(5, (short)5000);
        sheetIndex.setColumnWidth(6, (short)5000);
        sheetIndex.setColumnWidth(7, (short)4000);  
        sheetIndex.setColumnWidth(8, (short)4000);
        sheetIndex.setColumnWidth(9, (short)5000);
        
        
       /* String currentDirectory = System.getProperty("user.dir");
        currentDirectory = currentDirectory + "/view/";
        File directory = new File(currentDirectory);
        if (!directory.exists()) directory.mkdirs();
        String updateFileName = currentDirectory + "\\" + ratesheetMgt.getRsName().trim() +"_View"+ ".xlsx";
        *//** File Writing Process *//*
        FileOutputStream outputStream = new FileOutputStream(updateFileName);
        workbook.write(outputStream);
        workbook.close();*/
        
        byte[] resource = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        resource = out.toByteArray();
        fileContent = Base64.getEncoder().encodeToString(resource);
      }
    } catch (Exception e) {
    logger.error("Error ::RateSheetMgtServiceImpl  :: generateRSMgtDownload :: ", e);
  } finally {
    workbook.close();
  }
    
    return fileContent;
  }
  
 public String generateRSUpdatesDownload(RatesheetUpdateViewRequest ratesheetViewRequest) throws Exception {
    String fileContent = "";
    Workbook workbook = new XSSFWorkbook();
    CellStyle cellStyle = workbook.createCellStyle();
    try {
      List<RSUpdatesResponse> updateResult = searchRatesheetUpdateView(ratesheetViewRequest);
      if (updateResult != null && !updateResult.isEmpty()) {
        Sheet sheetIndex = workbook.createSheet("Ratesheet Updates View");
        XSSFColor rptBlue = new XSSFColor(new Color(0, 112, 192));
        XSSFColor rptLightGray = new XSSFColor(new Color(214, 220, 228));
        int lastLine = 0;
        Row sheetIndexrow0 = null;
        Cell cell = null;
        sheetIndexrow0 = sheetIndex.createRow(lastLine);
        cell = sheetIndexrow0.createCell((short)1);
        cell.setCellValue("Created By");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
        
        cell = sheetIndexrow0.createCell((short)2);
        cell.setCellValue("Update Type");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
              
        cell = sheetIndexrow0.createCell((short)3);
        cell.setCellValue("Hotel");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
        
        cell = sheetIndexrow0.createCell((short)4);
        cell.setCellValue("Update Details");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
       
        cell = sheetIndexrow0.createCell((short)5);
        cell.setCellValue("Validation Period");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
        
        cell = sheetIndexrow0.createCell((short)6);
        cell.setCellValue("Ratesheet Name");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
      
        cell = sheetIndexrow0.createCell((short)7);
        cell.setCellValue("Internal Note");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
      
        cell = sheetIndexrow0.createCell((short)8);
        cell.setCellValue("Updates Created On");
        cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, true, (short)14, "Calibri"));
      
        
         for(RSUpdatesResponse obj:updateResult){ 
          lastLine = lastLine + 1;
          sheetIndexrow0 = sheetIndex.createRow(lastLine);
          cell = sheetIndexrow0.createCell((short)1);
          cell.setCellValue(CommonUtil.nullRemove(obj.getCreatedBy()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
          
          cell = sheetIndexrow0.createCell((short)2);
          cell.setCellValue(CommonUtil.nullRemove(obj.getUpdateName()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
                
          cell = sheetIndexrow0.createCell((short)3);
          cell.setCellValue(CommonUtil.nullRemove(obj.getPartyName()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
          
          cell = sheetIndexrow0.createCell((short)4);
          cell.setCellValue(CommonUtil.nullRemove(obj.getUpdateDetails()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
         
          cell = sheetIndexrow0.createCell((short)5);
          cell.setCellValue(CommonUtil.nullRemove(obj.getValidFrom())+" - "+CommonUtil.nullRemove(obj.getValidTill()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
          
          cell = sheetIndexrow0.createCell((short)6);
          cell.setCellValue(CommonUtil.nullRemove(obj.getRsName()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
        
          cell = sheetIndexrow0.createCell((short)7);
          cell.setCellValue(CommonUtil.nullRemove(obj.getInternalNote()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
        
          cell = sheetIndexrow0.createCell((short)8);
          cell.setCellValue(CommonUtil.nullRemove(obj.getUpdatedDate()));
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(workbook, cellStyle, IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, false, true, (short)12, "Calibri"));
        }
        
        sheetIndex.setColumnWidth(0, (short)500);
        sheetIndex.setColumnWidth(1, (short)4000);
        sheetIndex.setColumnWidth(2, (short)5000);
        sheetIndex.setColumnWidth(3, (short)9000);
        sheetIndex.setColumnWidth(4, (short)5000);
        sheetIndex.setColumnWidth(5, (short)4000);
        sheetIndex.setColumnWidth(6, (short)5000);
        sheetIndex.setColumnWidth(7, (short)5000);  
        sheetIndex.setColumnWidth(8, (short)4000);
                
        byte[] resource = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        resource = out.toByteArray();
        fileContent = Base64.getEncoder().encodeToString(resource);
      }
    } catch (Exception e) {
    logger.error("Error ::RateSheetMgtServiceImpl  :: generateRSMgtDownload :: ", e);
  } finally {
    workbook.close();
  }
    
    return fileContent;
  }
  
  public RatesheetMgtDto getRateSheetMapsDet(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    
    List<Object[]> result = rateSheetDAO.getRateSheetMapSearch(ratesheetMgtDto.getAutogenRsId(), ratesheetMgtDto.getCategoryCode(), ratesheetMgtDto.getSectorCode(), ratesheetMgtDto.getCityCode());
    List<RatesheetMapResponse> responseList = new ArrayList<>();
    List<RatesheetMapResponse> trueList = new ArrayList<>();
    List<RatesheetMapResponse> falseList = new ArrayList<>();
    if(result != null && !result.isEmpty()) {
    result.stream().forEach(obj -> {
      RatesheetMapResponse rmr = new RatesheetMapResponse();
      rmr.setId(new BigInteger(CommonUtil.nullRemove(obj[1])));
      rmr.setPartyCode(CommonUtil.nullRemove(obj[2]));
      rmr.setPartyName(CommonUtil.nullRemove(obj[3]));
      rmr.setDisplayName(CommonUtil.nullRemove(obj[4]));
      rmr.setCategoryCode(CommonUtil.nullRemove(obj[5]));
      rmr.setCategoryName(CommonUtil.nullRemove(obj[6]));
      rmr.setSectorCode(CommonUtil.nullRemove(obj[7]));
      rmr.setSectorName(CommonUtil.nullRemove(obj[8]));
      rmr.setCityCode(CommonUtil.nullRemove(obj[9]));
      rmr.setCityName(CommonUtil.nullRemove(obj[10]));
      if("true".equalsIgnoreCase(CommonUtil.nullRemove(obj[0]))) {
        rmr.setMapped(true);
        trueList.add(rmr);
        } else {
          rmr.setMapped(false);
          falseList.add(rmr);
        }
    });
    responseList.addAll(trueList);
    responseList.addAll(falseList);
    }
    ratesheetMgtDto.setResult(responseList);
    return ratesheetMgtDto;
    
  }
  
  public RatesheetMgtDto getRSMapsDropDowns(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    Object[] result = rateSheetDAO.getRateSheetMgtDropdown("RSMAPS_DROPDOWN");
    List<Object[]> list = null;
    if(result != null)
    {
      RSMapsDropdowns rsMapsDD = new RSMapsDropdowns();
      if(result[0] != null) {
         list = (List<Object[]>)result[0];
         List<KeyValuePair> kv = new ArrayList<>();
         list.stream().forEach(obj -> {
           kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
         });
         rsMapsDD.setRatesheets(kv);
      }
      
      if(result[1] != null) {
         list = (List<Object[]>)result[1];
         List<KeyValuePair> kv = new ArrayList<>();
         list.stream().forEach(obj -> {
           kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
         });
         rsMapsDD.setSectors(kv);
      }
      
      if(result[2] != null) {
        list = (List<Object[]>)result[2];
        List<KeyValuePair> kv = new ArrayList<>();
        list.stream().forEach(obj -> {
          kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
        });
        rsMapsDD.setCities(kv);
     }
      
      if(result[3] != null) {
        list = (List<Object[]>)result[3];
        List<KeyValuePair> kv = new ArrayList<>();
        list.stream().forEach(obj -> {
          kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
        });
        rsMapsDD.setCategories(kv);
     }
      ratesheetMgtDto.setResult(rsMapsDD);
    }
    
    return ratesheetMgtDto;
  }
  
  public RatesheetMgtDto getRSAgentDropDown(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    Object[] result = rateSheetDAO.getRateSheetMgtDropdown("RS_AGENT_DROPDOWN");
    List<Object[]> list = null;
    if(result != null)
    {
      if(result[4] != null) {
         list = (List<Object[]>)result[4];
         List<KeyValuePair> kv = new ArrayList<>();
         list.stream().forEach(obj -> {
           kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1]), CommonUtil.nullRemove(obj[2]), CommonUtil.nullRemove(obj[3])));
         });
         ratesheetMgtDto.setResult(kv);
      }
    }
    
    return ratesheetMgtDto;
  }
  
  public RatesheetMgtDto getRSUpdateTypeDropDown(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    Object[] result = rateSheetDAO.getRateSheetMgtDropdown("UPDATE_DROPDOWN");
    List<Object[]> list = null;
    if(result != null)
    {
      RSUpdatesDropdowns rsdropdowns = new RSUpdatesDropdowns();
      if(result[5] != null) {
         list = (List<Object[]>)result[5];
         List<KeyValuePair> kv = new ArrayList<>();
         list.stream().forEach(obj -> {
           kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[1]), CommonUtil.nullRemove(obj[1])));
         });
         rsdropdowns.setUpdateTypes(kv);
      }
      
      if(result[6] != null) {
         list = (List<Object[]>)result[6];
         List<KeyValuePair> kv = new ArrayList<>();
         list.stream().forEach(obj -> {
           kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
         });
         rsdropdowns.setRatesheets(kv);
      }
      
      if(result[7] != null) {
        list = (List<Object[]>)result[7];
        List<KeyValuePair> kv = new ArrayList<>();
        list.stream().forEach(obj -> {
          kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
        });
        rsdropdowns.setParties(kv);
     }
      
      if(result[8] != null) {
        list = (List<Object[]>)result[8];
        List<KeyValuePair> kv = new ArrayList<>();
        list.stream().forEach(obj -> {
          kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
        });
        rsdropdowns.setUsers(kv);
     }
      ratesheetMgtDto.setResult(rsdropdowns);
    }
    
    return ratesheetMgtDto;
  
  }
  
  public List<KeyValuePair> getMappedHotelDropDown(String partyCode) throws Exception {
    List<Object[]> result = rateSheetDAO.getMappedHotelDropdown(partyCode);
      if(result != null && !result.isEmpty()) {
        List<KeyValuePair> kv = new ArrayList<>();
         result.stream().forEach(obj -> {
           kv.add(new KeyValuePair(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
         });
        return kv;
      }
    
    return null;
  }
  
  public RatesheetMappingDto saveRSMapping(RatesheetMappingDto ratesheetMappingDto) throws Exception {
   /* rateSheetDAO.saveRatesheetMap(ratesheetMappingDto);
    if(ratesheetMappingDto != null && ratesheetMappingDto.isStatus()) {
      rateSheetService.execAsyncRatesheetGenerateTask(ratesheetMappingDto.getAutogenRsId());
    }*/
    return rateSheetDAO.saveRatesheetMap(ratesheetMappingDto);
  }
  
  public List<RSUpdatesResponse> getRateSheetUpdates() throws Exception {
    List<RSUpdatesResponse> responseList = new ArrayList<>();
    List<RatesheetUpdates> resultList = rateSheetDAO.getRateSheetUpdates();
    if(resultList != null && !resultList.isEmpty()) {
      resultList.stream().forEach(rsObj->{
        RSUpdatesResponse rsUpdateRes = new RSUpdatesResponse();
        BeanUtils.copyProperties(rsObj, rsUpdateRes);
        if(rsObj.getValidFrom() != null)
          try {
            rsUpdateRes.setValidFrom(DateUtil.convertDatetoString(rsObj.getValidFrom(),"dd/MM/yyyy"));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        if(rsObj.getValidTill() != null)
          try {
            rsUpdateRes.setValidTill(DateUtil.convertDatetoString(rsObj.getValidTill(),"dd/MM/yyyy"));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        if(rsObj.getRecAddDt() != null)
          try {
            rsUpdateRes.setCreatedDate(DateUtil.convertDatetoString(rsObj.getRecAddDt(),"dd/MM/yyyy"));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        if(rsObj.getRecUpdateDt() != null)
          try {
            rsUpdateRes.setUpdatedDate(DateUtil.convertDatetoString(rsObj.getRecUpdateDt(),"dd/MM/yyyy"));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        
        rsUpdateRes.setRsUpdatesId(rsObj.getAutogenRsUpdatesId());
        rsUpdateRes.setRsId(rsObj.getAutogenRsId());
        rsUpdateRes.setRsName(rsObj.getRsName());
        responseList.add(rsUpdateRes);
      });
    }
    return responseList;
    
  }
  
  public Object[] saveRateSheetUpdates(List<RSUpdatesCreateReq> rsUpdatesCreateReq) throws Exception {
    Object[] result = new Object[2];
    result[0] = true;
      if(rsUpdatesCreateReq != null && !rsUpdatesCreateReq.isEmpty()) {
        for (RSUpdatesCreateReq insertObj : rsUpdatesCreateReq) {
          try {
          rateSheetDAO.saveRateSheetUpdates(insertObj);
          }catch (Exception e) {
            result[0] = false;
            if(e instanceof PersistenceException) {
              if(e.getCause() instanceof ConstraintViolationException)
                result[1] = insertObj.getRsName()+":Already exist. Please use edit option to update.";
            } else {
              result[1] = "Error.Please contact admin.";
            }
          }
        }
       }
      return result;
  }
  
  public Object[]  updateRateSheetUpdates(RSUpdatesCreateReq rsUpdatesCreateReq) throws Exception {
    Object[] result = new Object[2];
    result[0] = true;
      if(rsUpdatesCreateReq != null) {
          try {
          rateSheetDAO.updateRateSheetUpdates(rsUpdatesCreateReq);
          }catch (Exception e) {
            result[0] = false;
            if(e instanceof PersistenceException) {
              if(e.getCause() instanceof ConstraintViolationException)
                result[1] = rsUpdatesCreateReq.getRsName()+":Already exist. Please use edit option to update.";
            } else {
              result[1] = "Error.Please contact admin.";
            }
          }
        }
      return result;
  }
  
  public List<RSUpdatesResponse> searchRatesheetUpdateView(RatesheetUpdateViewRequest ratesheetViewRequest) throws Exception {
    List<RSUpdatesResponse> responseList = new ArrayList<>();
    List<Object[]> resultRes = rateSheetDAO.searchRSUpdatesView(ratesheetViewRequest);
    if(resultRes != null && !resultRes.isEmpty()) {
      resultRes.stream().forEach(obj -> {
        RSUpdatesResponse rsUpdateRes = new RSUpdatesResponse();
        /**AUTOGEN_RS_UPDATES_ID, UPDATE_NAME, PARTY_CODE, PARTY_NAME, UPDATE_DETAILS,
        VALID_FROM, VALID_TILL, INTERNAL_NOTE, AUTOGEN_RS_ID, RS_NAME, CREATED_BY, UPDATED_BY */
        rsUpdateRes.setRsUpdatesId(new BigInteger(CommonUtil.nullRemove(obj[0])));
        rsUpdateRes.setUpdateName(CommonUtil.nullRemove(obj[1]));
        rsUpdateRes.setPartyCode(CommonUtil.nullRemove(obj[2]));
        rsUpdateRes.setPartyName(CommonUtil.nullRemove(obj[3]));
        rsUpdateRes.setUpdateDetails(CommonUtil.nullRemove(obj[4]));
        try {
          rsUpdateRes.setValidFrom(DateUtil.getFormattedDate(CommonUtil.nullRemove(obj[5]),"yyyy-MM-dd","dd/MM/yyyy"));
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
        try {
          rsUpdateRes.setValidTill(DateUtil.getFormattedDate(CommonUtil.nullRemove(obj[6]),"yyyy-MM-dd","dd/MM/yyyy"));
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        rsUpdateRes.setInternalNote(CommonUtil.nullRemove(obj[7]));
        rsUpdateRes.setRsId(new BigInteger(CommonUtil.nullRemove(obj[8])));
        rsUpdateRes.setRsName(CommonUtil.nullRemove(obj[9]));
        rsUpdateRes.setCreatedBy(CommonUtil.nullRemove(obj[10]));
        rsUpdateRes.setUpdatedBy(CommonUtil.nullRemove(obj[11]));
        responseList.add(rsUpdateRes);
      });
    }
    
    return responseList;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
