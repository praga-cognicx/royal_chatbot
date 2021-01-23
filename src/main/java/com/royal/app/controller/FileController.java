package com.royal.app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.royal.app.constants.StatusCodeConstants;
import com.royal.app.message.response.GenericResponse;
import com.royal.app.model.RatesheetMapping;
import com.royal.app.model.RatesheetMgt;
import com.royal.app.services.RateSheetService;
import com.royal.app.services.RolesService;
import com.royal.app.services.UserService;
import com.royal.app.services.impl.UserDetailsServiceImpl;
import com.royal.app.util.DateUtil;
import com.royal.app.util.UserInfo;

@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileController {

  private static final Logger logger = LoggerFactory.getLogger(FileController.class);

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  RolesService rolesService;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  UserService userService;

  @Autowired
  UserInfo userInfo;
  
  @Autowired
  RateSheetService rateSheetService;

  @SuppressWarnings("unchecked")
  @PostMapping(path = "/ratesheet/uploadFile")
  public ResponseEntity<GenericResponse> uploadFile(HttpServletResponse response,
      @RequestParam("file") MultipartFile file) throws Exception {

    InputStream inputstreamfile = file.getInputStream();
    Object[] resultObject = getInsertObjectList(inputstreamfile);

    List<List<List<String>>> recList = null;
    List<List<String>> failureRecList = new ArrayList<>();
    List<List<String>> successRecList = new ArrayList<>();
    List<List<String>> headerData = new ArrayList<>();
    if (resultObject != null && resultObject[1] != null) {
      List<List<String>> insertList = null;
      recList = (List<List<List<String>>>) resultObject[1];
      if (recList != null && !recList.isEmpty()) {
        insertList = recList.get(0);
        headerData = recList.get(1);
        if (insertList != null && !insertList.isEmpty()) {
         Object[] result = rateSheetService.uploadRateSheetDet(insertList);
         if(result != null) {
           successRecList = (List<List<String>>) result[0];
           failureRecList = (List<List<String>>) result[1];
         }
        }
      }
    }

    String fileContent = "";
    fileContent = generateXLSFile(headerData.get(0), successRecList, failureRecList);
    GenericResponse genericResponse = new GenericResponse();
    if (fileContent != null && !fileContent.isEmpty()) {
      genericResponse.setStatus(StatusCodeConstants.SUCCESS);
      genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
      genericResponse.setMessage("RateSheet Details Uploaded Successfully.");
      genericResponse.setValue(fileContent);
    } else {
      genericResponse.setStatus(StatusCodeConstants.FAILURE);
      genericResponse.setError(StatusCodeConstants.FAILURE_STR);
      genericResponse.setMessage("RateSheet Details Upload Failed.");
      genericResponse.setValue(fileContent);
    }

    return ResponseEntity.ok(new GenericResponse(genericResponse));
  }

  private String generateXLSFile(List<String> headerData, List<List<String>> successRecList,
      List<List<String>> failureRecList) throws IOException {
    String fileContent = "";
    Workbook workbook = new XSSFWorkbook();
    try {
      Sheet successSheet = workbook.createSheet("Success");
      Sheet failureSheet = workbook.createSheet("failure");

      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 13);
      headerFont.setColor(IndexedColors.GREEN.getIndex());

      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFont(headerFont);

      Font headerFontFailure = workbook.createFont();
      headerFontFailure.setBold(true);
      headerFontFailure.setFontHeightInPoints((short) 13);
      headerFontFailure.setColor(IndexedColors.RED.getIndex());

      CellStyle headerCellStyleFailure = workbook.createCellStyle();
      headerCellStyleFailure.setFont(headerFontFailure);

      Row successHeaderRow = successSheet.createRow(0);
      Row failureHeaderRow = failureSheet.createRow(0);
      for (int i = 0; i < headerData.size(); i++) {
        Cell cell = successHeaderRow.createCell(i);
        cell.setCellValue(headerData.get(i));
        cell.setCellStyle(headerCellStyle);
        successSheet.autoSizeColumn(i);
      }

      for (int i = 0; i < headerData.size(); i++) {
        Cell cell = failureHeaderRow.createCell(i);
        cell.setCellValue(headerData.get(i));
        cell.setCellStyle(headerCellStyleFailure);
        failureSheet.autoSizeColumn(i);
      }

      int rowNum = 1;
      if (!successRecList.isEmpty()) {
        for (int i = 0; i < successRecList.size(); i++) {
          List<String> rowList = successRecList.get(i);
          Row row = successSheet.createRow(rowNum++);
          for (int coloumnIndex = 0; coloumnIndex < rowList.size(); coloumnIndex++) {
            row.createCell(coloumnIndex).setCellValue(rowList.get(coloumnIndex));
          }
        }
      }
      rowNum = 1;
      if (!failureRecList.isEmpty()) {
        for (int i = 0; i < failureRecList.size(); i++) {
          List<String> rowList = failureRecList.get(i);
          if(rowList.get(0) != null && !rowList.get(10).isEmpty()) {
          Row row = failureSheet.createRow(rowNum++);
          for (int coloumnIndex = 0; coloumnIndex < rowList.size(); coloumnIndex++) {
            row.createCell(coloumnIndex).setCellValue(rowList.get(coloumnIndex));
          }
          }
        }
      }
      
      String currentDirectory = System.getProperty("user.dir");
      currentDirectory = currentDirectory + "/upload/";
      File directory = new File(currentDirectory);
      if (!directory.exists()) directory.mkdirs();
      String updateFileName = currentDirectory + "\\" + "RatesheetUpload_"+"Mapping_"+DateUtil.getCurrentDate()+ ".xlsx";
      FileOutputStream outputStream = new FileOutputStream(updateFileName);
      workbook.write(outputStream);
    //  workbook.close();
      
    /*  byte[] resource = null;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      resource = out.toByteArray();
      fileContent = Base64.getEncoder().encodeToString(resource);*/
    } catch (Exception e) {
      logger.error("Error ::FileUploadUtil  :: generateXLSFile :: ", e);
    } finally {
      workbook.close();
    }

    return fileContent;
  }

  private Object[] getInsertObjectList(InputStream inputstreamfile) throws IOException {
    Object[] resultObj = new Object[2];
    Workbook workbook = null;
    String errMsg = "";
    List<Object> insertObject = new ArrayList<>();
    try {
      workbook = WorkbookFactory.create(inputstreamfile);
      Sheet sheet = workbook.getSheetAt(0);
      if (sheet != null && sheet.getLastRowNum() >= 0) {
        int rowCount = sheet.getPhysicalNumberOfRows();
        int coloumnCount = sheet.getRow(0).getLastCellNum();
        int headerListSize = 12;
        if (rowCount < 1) {
          errMsg = "Invalid - No data available for Upload";
        } else if (rowCount > 9999) {
          errMsg = "Invalid - maximum rows limit exceeded ";
        } else if (coloumnCount != headerListSize - 1) {
          errMsg = "Invalid - columns.";
        }

        List<List<String>> rowList = new ArrayList<>();
        List<List<String>> headerData = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        Iterator<Row> rowIterator = sheet.iterator();
        int worksheetRowCount = 0;
        RatesheetMgt rsMgt = new RatesheetMgt();
        List<RatesheetMapping> rsMaps = new ArrayList<>();
        while (rowIterator.hasNext()) {
          Row row = rowIterator.next();
          if (0 <= worksheetRowCount) {
            List<String> rowData = new ArrayList<>();
            RatesheetMapping rsMap = new RatesheetMapping();
            if(7 == worksheetRowCount) {
              for (int cn = 0; cn < headerListSize; cn++) {
                Cell cell = row.getCell(cn, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell != null) {
                  rowData.add(formatter.formatCellValue(cell).trim());
                } else {
                  rowData.add("");
                }
              }
            }
            if (1 == worksheetRowCount) {
              for (int cn = 0; cn < headerListSize; cn++) {
                Cell cell = row.getCell(cn, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell != null) {
                  if (1 == cn) {
                    rsMgt.setDivisionName(formatter.formatCellValue(cell).trim());
                    if ("Royal Park Tourism".equalsIgnoreCase(rsMgt.getDivisionName())) {
                      rsMgt.setDivisionId("001");
                    }
                    if ("Royal Gulf Tourism".equalsIgnoreCase(rsMgt.getDivisionName())) {
                      rsMgt.setDivisionId("002");
                    }
                  }
                  if (2 == cn) {
                    rsMgt.setRsName(formatter.formatCellValue(cell).trim());
                  }
                  if (3 == cn) {
                    rsMgt.setRsDesc(formatter.formatCellValue(cell).trim());
                  }
                  if (4 == cn) {
                    rsMgt.setEmailIds(formatter.formatCellValue(cell).trim());
                  }
                  if (5 == cn) {
                    rsMgt.setAgentName(formatter.formatCellValue(cell).trim());
                  }
                  if (6 == cn) {
                    rsMgt.setSendRatesheet(formatter.formatCellValue(cell).trim());
                  }
                  if (7 == cn) {
                    rsMgt.setSendUpdates(formatter.formatCellValue(cell).trim());
                  }
                  if (8 == cn) {
                    rsMgt.setSheetPassword(formatter.formatCellValue(cell).trim());
                  }
                  if (9 == cn) {
                    rsMap.setDisplayName(formatter.formatCellValue(cell).trim());
                  }
                  if (10 == cn) {
                    rsMap.setPartyName(formatter.formatCellValue(cell).trim());
                  }

                  rowData.add(formatter.formatCellValue(cell).trim());
                } else {
                  rowData.add("");
                }
              }
            } else {
              for (int cn = 0; cn < headerListSize; cn++) {
                Cell cell = row.getCell(cn, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell != null) {
                  if (9 == cn) {
                    rsMap.setDisplayName(formatter.formatCellValue(cell).trim());
                  }
                  if (10 == cn) {
                    rsMap.setPartyName(formatter.formatCellValue(cell).trim());
                  }
                  rowData.add(formatter.formatCellValue(cell).trim());
                } else {
                  rowData.add("");
                }
              }

            }
            rsMaps.add(rsMap);
            if(0 == worksheetRowCount) {
              headerData.add(rowData);
            } else {
            rowList.add(rowData);
            }
          }
          worksheetRowCount++;
        }
        insertObject.add(rowList);
        insertObject.add(headerData);
        insertObject.add(rsMgt);
        insertObject.add(rsMaps);
      }

    } catch (Exception e) {
      logger.error("Error ::FileUploadUtil  :: getInsertObjectList :: ", e);
    } finally {
      if (workbook != null) {
        workbook.close();
      }
    }

    resultObj[0] = errMsg;
    resultObj[1] = insertObject;
    return resultObj;

  }

}
