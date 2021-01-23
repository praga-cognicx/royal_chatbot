package com.royal.app.services.impl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.royal.app.dao.RateSheetDAO;
import com.royal.app.mail.EmailService;
import com.royal.app.model.RatesheetAgentMap;
import com.royal.app.model.RatesheetMapping;
import com.royal.app.model.RatesheetMgt;
import com.royal.app.services.RateSheetService;
import com.royal.app.shared.dto.RateSheetDto;
import com.royal.app.util.CommonUtil;
import com.royal.app.util.CustomFileUtil;
import com.royal.app.util.DateUtil;
import com.royal.app.util.ExcelUtil;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import net.bytebuddy.utility.RandomString;

@Service
public class RateSheetServiceImpl implements RateSheetService {

  @Autowired
  RateSheetDAO rateSheetDAO;

  @Autowired
  EmailService emailService;
  
  @Value("${gulf.mail.content}")
  private String gulfMailContent;
  
  @Value("${park.mail.content}")
  private String parkMailContent;
  
  @Value("${gulf.mail.subject}")
  private String gulfMailSubject;
  
  @Value("${park.mail.subject}")
  private String parkMailSubject;
  
  @Value("${parkgulf.mail.subject}")
  private String parkGulfMailSubject;
  
  @Value("${parkgulf.mail.content}")
  private String parkGulfMailContent;
  
//  @Value("${filemoving.server.username}")
//  private String filemovingServerUser;
//  
//  @Value("${filemoving.server.password}")
//  private String filemovingServerPass;
//  
//  @Value("${filemoving.server.domain}")
//  private String filemovingServerDomain;
//  
//  @Value("${filemoving.server.networkfolder}")
//  private String filemovingServerFoldName;
	@Value("${filemoving.server.ratesheetfolder}")
	private String filemovingFoldName;
  
  @Value("${fileremoving.days}")
  private String fileremovingDays;

  private static final Logger logger = LoggerFactory.getLogger(RateSheetServiceImpl.class);

  private RandomString randomString = new RandomString(5);

  CellStyle hColor4C = null;
  CellStyle hColor4L = null;
  CellStyle hWColor5C = null;
  CellStyle hWColor5L = null;
  CellStyle hDBColor2C = null;
  CellStyle ratesColumnStyle = null;
  CellStyle tacticalHeaderStyle = null;
  CellStyle tacticalvalueStyle = null;
  CellStyle remarksColor5Style = null;
  CellStyle remarksColor9Style = null;
  CellStyle taxNotablelightStyleL = null;
  CellStyle taxNotableColor3StyleC = null;
  CellStyle taxNotableColor2StyleC = null;
  CellStyle taxNotableColor5StyleC = null;
  CellStyle maxOccColor9StyleC = null;
  CellStyle minNightColor2StyleC = null;
  CellStyle minNightColor5StyleC = null;
  CellStyle minNightColor9ValStyleC = null;
  CellStyle minNightWeekDayColor9StyleC = null;
  CellStyle specialEvColor5StyleC = null;
  CellStyle specialEvColor2HeaderStyleL = null;
  CellStyle specialEvColor3HeaderStyleL = null;
  CellStyle specialEvColor5ValStyleC = null;
  CellStyle specialEvColor2ValStyleC = null;
  CellStyle specialEvColor9ValStyleC = null;
  CellStyle cancelColor5HeaderStyleC = null;
  CellStyle cancelColor2SubHeaderStyleC = null;
  CellStyle cancelColor2SubHeaderStyleL = null;
  CellStyle cancelColor9ValStyleC = null;
  CellStyle cancelColor9ValStyleL = null;
  CellStyle checkInColor5HeaderStyleC = null;
  CellStyle checkInTimeStyle = null;
  CellStyle checkInTimeStyle1 = null;
  CellStyle checkOutTimeStyle = null;
  CellStyle checkOutTimeStyle1 = null;
  CellStyle checkOutPeriodTimeStyle = null;
  CellStyle checkOutFromToStyle = null;
  CellStyle checkOutRoomTypeStyle = null;
  CellStyle checkOutDateValStyle = null;
  CellStyle checkOutRoomValStyle = null;
  CellStyle checkOutRestictStyle = null;
  CellStyle checkOutRestictValStyle = null;
  CellStyle pEarlyCIHeadStyle = null;
  CellStyle pEarlyCISubHeadStyle1 = null;
  CellStyle pEarlyCISubHeadStyle2 = null;
  CellStyle pEarlyCIDateValStyle = null;
  CellStyle pEarlyCIRoomValStyle = null;
  CellStyle earlyCIHeaderStyle = null;
  CellStyle earlyCIValStyle = null;
  CellStyle earlyCIValStyleDB = null;
  CellStyle generalPolHeaderStyle = null;
  CellStyle generalPolSubHeaderStyle = null;
  CellStyle generalPolValStyle1 = null;
  CellStyle generalPolValStyle2 = null;
  CellStyle hotelConstHeadStyle = null;
  CellStyle hotelConstSubHeadStyle = null;
  CellStyle hotelConstValStyle1 = null;
  CellStyle hotelConstValStyle2 = null;
  CellStyle backIndexStyle = null;
  CellStyle mainHeaderStyle = null;
  CellStyle mainSubHeaderStyle = null;
  CellStyle row2CellStyle = null;
  CellStyle indexHeaderStyle = null;
  CellStyle indexHeaderStyle1 = null;
  CellStyle indexHeaderStyle2 = null;
  CellStyle updatesheaderStyle = null;
  CellStyle updsatesSubHeaderStyle = null;
  CellStyle updsatesSubHeaderStyle1 = null;
  CellStyle updsatesValStyle = null;
  CellStyle updsatesValStyle1 = null;
  CellStyle ratesValStyle = null;
  String rateSheetName = "";

  public Object getReteSheetExcel(RateSheetDto rateSheetDto) throws Exception {
    generateRateSheetNew("");
    return null;
  }

  // My Asynchronous task
  public void execAsyncRatesheetGenerateTask(BigInteger rsId) throws Exception {
    // An Async task always executes in new thread
    (new Thread(new Runnable() {
      public void run() {
        try {
          generateSingleRateSheetWithOutMail(rsId);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    })).start();

  }

  @SuppressWarnings("resource")
  public void generateSingleRateSheetWithOutMail(BigInteger rsId) throws Exception {/*

    *//** Get Application Param Support Mail Details *//*
    Map<String, String> supportMailMaps = rateSheetDAO.getRatesheetAppParamDet("'SUPPORT_MAIL'");
    String sptMails = "";
    if (supportMailMaps.entrySet().size() > 0) {
      for (Entry<String, String> mail : supportMailMaps.entrySet()) {
        if (!sptMails.isEmpty()) {
          sptMails = sptMails + ";" + mail.getValue();
        } else {
          sptMails = mail.getValue();
        }
      }
    }
    String[] sptMailsArr = sptMails.split(";");

    
     * String subjectDemo = "Ratesheet generation Exception:: ";
     * emailService.sendMultipleMessageWithAttachment(sptMailsArr,subjectDemo, "Error", null);
     
    *//** Get All RateSheet With Mapped Hotel Details *//*
    // RateSheetDto rateSheetDto = rateSheetDAO.findRateSheetDet(rsName);
    List<RatesheetMgt> ratesheetMgtList = rateSheetDAO.findRateSheetDetByRsId(rsId);
    RateSheetDto rateSheetDto = new RateSheetDto();
    try {

      String currentDirectory = System.getProperty("user.dir");
      currentDirectory = currentDirectory + "/ratesheet/";
      File directory = new File(currentDirectory);
      if (!directory.exists())
        directory.mkdirs();
      String displayName = "";
      String schedularDate = DateUtil.getCurrentDateWithDotFormat();
      *//** RateSheet MGT Loop *//*
      for (RatesheetMgt ratesheetMgt : ratesheetMgtList) {
        rateSheetDto = new RateSheetDto();
        rateSheetDto.setAutogenRsId(ratesheetMgt.getAutogenRsId());
        rateSheetDto.setRsName(ratesheetMgt.getRsName());
        rateSheetDto.setAgentCode(CommonUtil.nullRemove(ratesheetMgt.getAgentCode()).trim());
        rateSheetDto
            .setSheetPassword(CommonUtil.nullRemove(ratesheetMgt.getSheetPassword()).trim());
        String fileName = "";
        HashMap<String, String> fileList = new HashMap<>();
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        CellStyle cellStyle = xSSFWorkbook.createCellStyle();
        *//** All Cell Style *//*
        XSSFColor color1 = new XSSFColor(new Color(51, 63, 79));
        XSSFColor color2 = new XSSFColor(new Color(221, 235, 247));
        XSSFColor color3 = new XSSFColor(new Color(252, 228, 214));
        XSSFColor color4 = new XSSFColor(new Color(237, 237, 237));
        XSSFColor color5 = new XSSFColor(new Color(31, 78, 120));
        // XSSFColor color6 = new XSSFColor(new Color(252, 228, 214));
        XSSFColor color7 = new XSSFColor(new Color(224, 224, 224));
        XSSFColor color8 = new XSSFColor(new Color(250, 191, 143));
        XSSFColor color9 = new XSSFColor(new Color(255, 255, 255));
        XSSFColor lightGray = new XSSFColor(new Color(211, 211, 211));
        XSSFColor rptBlue = new XSSFColor(new Color(0, 112, 192));
        String logoName = "RoyalPark.png";
        if (ratesheetMgt.getDivisionId() != null && !ratesheetMgt.getDivisionId().isEmpty()
            && "02".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
          rptBlue = new XSSFColor(new Color(198, 89, 17));
          logoName = "RoyalGulf.png";
        }

        hColor4C = null;
        hColor4L = null;
        hWColor5C = null;
        hWColor5L = null;
        hDBColor2C = null;
        ratesColumnStyle = null;
        tacticalHeaderStyle = null;
        tacticalvalueStyle = null;
        remarksColor5Style = null;
        remarksColor9Style = null;
        taxNotablelightStyleL = null;
        taxNotableColor3StyleC = null;
        taxNotableColor2StyleC = null;
        taxNotableColor5StyleC = null;
        maxOccColor9StyleC = null;
        minNightColor2StyleC = null;
        minNightColor5StyleC = null;
        minNightColor9ValStyleC = null;
        minNightWeekDayColor9StyleC = null;
        specialEvColor5StyleC = null;
        specialEvColor2HeaderStyleL = null;
        specialEvColor3HeaderStyleL = null;
        specialEvColor5ValStyleC = null;
        specialEvColor2ValStyleC = null;
        specialEvColor9ValStyleC = null;
        cancelColor5HeaderStyleC = null;
        cancelColor2SubHeaderStyleC = null;
        cancelColor2SubHeaderStyleL = null;
        cancelColor9ValStyleC = null;
        cancelColor9ValStyleL = null;
        checkInColor5HeaderStyleC = null;
        checkInTimeStyle = null;
        checkInTimeStyle1 = null;
        checkOutTimeStyle = null;
        checkOutTimeStyle1 = null;
        checkOutPeriodTimeStyle = null;
        checkOutFromToStyle = null;
        checkOutRoomTypeStyle = null;
        checkOutDateValStyle = null;
        checkOutRoomValStyle = null;
        checkOutRestictStyle = null;
        checkOutRestictValStyle = null;
        pEarlyCIHeadStyle = null;
        pEarlyCISubHeadStyle1 = null;
        pEarlyCISubHeadStyle2 = null;
        pEarlyCIDateValStyle = null;
        pEarlyCIRoomValStyle = null;
        earlyCIHeaderStyle = null;
        earlyCIValStyle = null;
        earlyCIValStyleDB = null;
        generalPolHeaderStyle = null;
        generalPolSubHeaderStyle = null;
        generalPolValStyle1 = null;
        generalPolValStyle2 = null;
        hotelConstHeadStyle = null;
        hotelConstSubHeadStyle = null;
        hotelConstValStyle1 = null;
        hotelConstValStyle2 = null;
        backIndexStyle = null;
        mainHeaderStyle = null;
        mainSubHeaderStyle = null;
        row2CellStyle = null;
        indexHeaderStyle = null;
        indexHeaderStyle1 = null;
        indexHeaderStyle2 = null;
        updatesheaderStyle = null;
        updsatesSubHeaderStyle = null;
        updsatesSubHeaderStyle1 = null;
        updsatesValStyle = null;
        updsatesValStyle1 = null;
        ratesValStyle = null;
        mainHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color1, HorizontalAlignment.CENTER, true, false,
            (short) 18);
        mainSubHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true);
        backIndexStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color8, HorizontalAlignment.CENTER, true, false,
            (short) 12);
        row2CellStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color7, HorizontalAlignment.CENTER, true);
        indexHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, true, false,
            (short) 10, "Arial");
        indexHeaderStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 10, "Arial");
        indexHeaderStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false,
            (short) 10, "Arial");
        hColor4C = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_BLUE.getIndex(), color4, HorizontalAlignment.CENTER, true);
        hColor4L = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_BLUE.getIndex(), color4, HorizontalAlignment.LEFT, true);
        hWColor5C = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true);
        hWColor5L = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.LEFT, true);
        hDBColor2C = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_BLUE.getIndex(), color2, HorizontalAlignment.CENTER, true);
        ratesColumnStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true, true,
            (short) 11);
        tacticalHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.CENTER, true, true,
            (short) 11);
        tacticalvalueStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 11);
        remarksColor5Style = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        remarksColor9Style = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, true,
            (short) 11);
        taxNotablelightStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), lightGray, HorizontalAlignment.LEFT, true, true,
            (short) 10);
        taxNotableColor3StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        taxNotableColor2StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        taxNotableColor5StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        maxOccColor9StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        minNightColor2StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        minNightColor5StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        minNightColor9ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        minNightWeekDayColor9StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        specialEvColor5StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        specialEvColor2HeaderStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.LEFT, true, false,
            (short) 11);
        specialEvColor3HeaderStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.LEFT, true, false,
            (short) 11);
        specialEvColor5ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, true,
            (short) 11, "Calibri");
        specialEvColor2ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true, true,
            (short) 11, "Calibri");
        specialEvColor9ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        cancelColor5HeaderStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        cancelColor2SubHeaderStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        cancelColor2SubHeaderStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.LEFT, true, false,
            (short) 11);
        cancelColor9ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 10);
        cancelColor9ValStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, true,
            (short) 10);
        checkInColor5HeaderStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        checkInTimeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkInTimeStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(50, 205, 50)),
            HorizontalAlignment.CENTER, true, false, (short) 11);
        checkOutTimeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkOutTimeStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(205, 92, 92)),
            HorizontalAlignment.CENTER, true, false, (short) 11);
        checkOutPeriodTimeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkOutFromToStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        checkOutRoomTypeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkOutDateValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        checkOutRoomValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        checkOutRestictStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        checkOutRestictValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        pEarlyCIHeadStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(50, 205, 50)),
            HorizontalAlignment.CENTER, true, false, (short) 11, "Arial");
        pEarlyCISubHeadStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        pEarlyCISubHeadStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(180, 182, 233)),
            HorizontalAlignment.CENTER, true, false, (short) 11, "Arial");
        pEarlyCIDateValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        pEarlyCIRoomValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(240, 230, 198)),
            HorizontalAlignment.CENTER, false, false, (short) 11, "Calibri");
        earlyCIHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        earlyCIValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        earlyCIValStyleDB = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_RED.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Calibri");
        generalPolHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        generalPolSubHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        generalPolValStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        generalPolValStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, true,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        hotelConstHeadStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        hotelConstSubHeadStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        hotelConstValStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        hotelConstValStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, true,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        ratesValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        try {
          if ("YES".equalsIgnoreCase(ratesheetMgt.getSendRatesheet())) {
            rateSheetName = ratesheetMgt.getRsName().trim();
            fileName = currentDirectory + "\\" + rateSheetName + ".xlsx";
            fileList.put(rateSheetName + ".xlsx", fileName);
            CreationHelper createHelper = xSSFWorkbook.getCreationHelper();
            XSSFColor rptYello = new XSSFColor(new Color(255, 192, 0));
            XSSFColor rptLightYellow = new XSSFColor(new Color(254, 244, 236));
            int lastLine = 0;
            Sheet sheetIndex = xSSFWorkbook.createSheet("Index");
            *//** Index Header Part *//*
            lastLine = addIndexHeaderDetails(lastLine, sheetIndex, xSSFWorkbook, cellStyle, rptBlue,
                logoName, ratesheetMgt);
            String sName = "";
            Row sheetIndexrow0 = null;
            Cell cell;
            Set<String> dispUniName = new HashSet<>();

            *//** RateSheet Mapping Loop *//*
            for (RatesheetMapping ratesheetMap : ratesheetMgt.getRatesheetMapping()) {
              Date startTime = new Date();
              rateSheetDto.setPartyName(CommonUtil.nullRemove(ratesheetMap.getPartyName()).trim());
              rateSheetDto
                  .setCountryCode(CommonUtil.nullRemove(ratesheetMap.getCountryCode()).trim());
              rateSheetDto.setPartyCode(CommonUtil.nullRemove(ratesheetMap.getPartyCode()).trim());
              rateSheetDto.setFromDate(DateUtil.getTodayDate());
              rateSheetDto.setDisplayName(CommonUtil.nullRemove(ratesheetMap.getDisplayName())
                  .trim().replace(" ", "_").replace("  ", "_").replace("&", "_"));
              if (rateSheetDto.getDisplayName() != null
                  && rateSheetDto.getDisplayName().length() > 0) {
                if (rateSheetDto.getDisplayName().length() > 31) {
                  displayName = rateSheetDto.getDisplayName().substring(0, 30);
                } else {
                  displayName = rateSheetDto.getDisplayName().substring(0,
                      rateSheetDto.getDisplayName().length());
                }

                if (!dispUniName.contains(displayName)) {
                  dispUniName.add(displayName);
                  rateSheetDto.setDisplayName(displayName);
                } else {
                  if (!rateSheetDto.getDisplayName().isEmpty()) {
                    displayName = rateSheetDto.getDisplayName().substring(0,
                        rateSheetDto.getDisplayName().length()) + randomString.nextString();
                  } else {
                    if (rateSheetDto.getPartyName().length() > 31) {
                      displayName = rateSheetDto.getPartyName().substring(0, 30);
                    } else {
                      displayName = rateSheetDto.getPartyName().substring(0,
                          rateSheetDto.getPartyName().length());
                    }
                  }
                  dispUniName.add(displayName);
                  rateSheetDto.setDisplayName(displayName);
                }
              } else {
                if (rateSheetDto.getPartyName().length() > 31) {
                  displayName = rateSheetDto.getPartyName().substring(0, 30);
                } else {
                  displayName = rateSheetDto.getPartyName().substring(0,
                      rateSheetDto.getPartyName().length());
                }
                dispUniName.add(displayName);
                rateSheetDto.setDisplayName(displayName);
              }

              String toDate = rateSheetDAO.getEndContractDate(
                  CommonUtil.nullRemove(ratesheetMap.getPartyCode()).trim(),
                  CommonUtil.nullRemove(ratesheetMap.getCountryCode()).trim());
              rateSheetDto.setToDate(DateUtil.getFormattedDate(toDate, "yyyy-MM-dd", "yyyy/MM/dd"));
              if (rateSheetDto.getToDate() == null || rateSheetDto.getToDate().isEmpty()) {
                rateSheetDto.setToDate(DateUtil.getContractEndDateAfterSixMonth());
              }
              lastLine = lastLine + 1;
              sheetIndexrow0 = sheetIndex.createRow(lastLine);
              // String hotelCode = ratesheetMap.getPartyCode();
              cell = sheetIndexrow0.createCell((short) 1);
              cell.setCellValue(ratesheetMap.getPartyName());
              // cell.setCellValue(ratesheetMap.getPartyName() + " (" + hotelCode + ") ");
              cell.setCellStyle(indexHeaderStyle);

              sName = "'" + rateSheetDto.getDisplayName() + "'";
              Hyperlink hyperLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
              hyperLink.setAddress(sName + "!A1");
              cell.setHyperlink(hyperLink);

              cell = sheetIndexrow0.createCell((short) 2);
              cell.setCellValue(ratesheetMap.getCategoryName());
              cell.setCellStyle(indexHeaderStyle1);

              cell = sheetIndexrow0.createCell((short) 3);
              cell.setCellValue(ratesheetMap.getSectorName());
              cell.setCellStyle(indexHeaderStyle1);

              cell = sheetIndexrow0.createCell((short) 4);
              cell.setCellValue(DateUtil.getFormattedDate(rateSheetDto.getFromDate(), "yyyy/MM/dd",
                  "dd/MM/yyyy"));
              cell.setCellStyle(indexHeaderStyle2);

              cell = sheetIndexrow0.createCell((short) 5);
              cell.setCellValue(
                  DateUtil.getFormattedDate(rateSheetDto.getToDate(), "yyyy/MM/dd", "dd/MM/yyyy"));
              cell.setCellStyle(indexHeaderStyle2);

              try {
                *//** RATESHEET SHEET FOR SINGLE HOTAL *//*
                generateRateSheetByParty(xSSFWorkbook, cellStyle, rateSheetDto,
                    rateSheetDto.getDisplayName(), lastLine);

              } catch (Exception e) {
                logger.error(
                    "Exception Nested Try2::RateSheetServiceImpl.Class::generateSingleRateSheetWithOutMail()",
                    e);
                rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, e.fillInStackTrace().toString(),
                    fileName, null, "RSH_EXCEPTION");
                *//** Support Mail Logic *//*
                String subject = "Ratesheet generation Exception:: " + rateSheetDto.getRsName()
                    + "::" + rateSheetDto.getPartyName() + "::" + rateSheetDto.getPartyName();
                emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject,
                    e.fillInStackTrace().toString(), null);
              }

              *//** RATESHEET TIME LOGGER *//*
              Date endTime = new Date();
              rateSheetDAO.saveRateSheetTimeLog(rateSheetDto, startTime, endTime);
            }

            lastLine = addIndexFooterDetails(lastLine, sheetIndex, xSSFWorkbook, cellStyle,
                rptYello, rptLightYellow, sName, createHelper);
            sheetIndex.protectSheet(rateSheetDto.getSheetPassword());

            *//** File Writing Process *//*
            FileOutputStream outputStream = new FileOutputStream(fileName);
            xSSFWorkbook.write(outputStream);
            xSSFWorkbook.close();
            System.out.println("RateSheet Excel Downloaded!!!");
          }
          
           * if ("YES".equalsIgnoreCase(ratesheetMgt.getSendUpdates())) { List<Object[]>
           * rsUpdateResults = rateSheetDAO.getRateSheetUpdatesDet(rateSheetDto.getAutogenRsId());
           * if(rsUpdateResults != null && !rsUpdateResults.isEmpty()) { String updateFileName = "";
           * XSSFWorkbook updateWorkbook = new XSSFWorkbook(); XSSFColor rptLightGray = new
           * XSSFColor(new Color(214, 220, 228)); CellStyle cellStyleUpdate =
           * updateWorkbook.createCellStyle(); updatesheaderStyle =
           * ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
           * IndexedColors.WHITE.getIndex(), rptBlue, HorizontalAlignment.CENTER, false, false,
           * (short)16, "Arial Black"); updsatesSubHeaderStyle =
           * ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
           * IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.CENTER, true, false,
           * (short)10, "Arial"); updsatesSubHeaderStyle1 =
           * ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
           * IndexedColors.BLACK.getIndex(), rptLightGray, HorizontalAlignment.LEFT, true, true,
           * (short)10, "Arial"); updsatesValStyle =
           * ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
           * IndexedColors.BLACK.getIndex(),color9, HorizontalAlignment.CENTER, true, false, (short)
           * 10, "Arial"); updsatesValStyle1 = ExcelUtil.getCellStyleForContent(updateWorkbook,
           * cellStyleUpdate, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT,
           * true, true, (short) 10, "Arial"); rateSheetName =
           * ratesheetMgt.getRsName().trim()+"_Updates" ; updateFileName = currentDirectory +
           * "\\" + rateSheetName+ ".xlsx"; fileList.put(rateSheetName+ ".xlsx", updateFileName);
           * int lastLine = 0; Sheet sheetUpdates = updateWorkbook.createSheet("Updates");
           * sheetUpdates.setColumnWidth(0, (short)1000); sheetUpdates.setColumnWidth(1,
           * (short)6000); sheetUpdates.setColumnWidth(2, (short)12000);
           * sheetUpdates.setColumnWidth(3, (short)17000); sheetUpdates.setColumnWidth(4,
           * (short)4000); sheetUpdates.setColumnWidth(5, (short)4000);
           * sheetUpdates.setDisplayGridlines(false); Row row = null; Cell cell = null; lastLine =
           * lastLine + 1; row = sheetUpdates.createRow(lastLine); row.setHeight((short)500); cell =
           * row.createCell((short)1); cell.setCellValue("UPDATES FOR THE DATE "+
           * DateUtil.getFormattedDate(DateUtil.getTodayDate(), "yyyy/MM/dd", "dd.MM.yyyy"));
           * cell.setCellStyle(updatesheaderStyle); ExcelUtil.frameMergedWithThinBorder(new
           * CellRangeAddress(lastLine, lastLine, 1, 5), sheetUpdates, xSSFWorkbook);
           * 
           * lastLine = lastLine + 1; row = sheetUpdates.createRow(lastLine); row.setHeight((short)
           * 600); sheetUpdates.setAutoFilter(new CellRangeAddress(lastLine, lastLine, 1, 5)); cell
           * = row.createCell((short)1); cell.setCellValue("UPDATE TYPE");
           * cell.setCellStyle(updsatesSubHeaderStyle);
           * 
           * cell = row.createCell((short)2); cell.setCellValue("HOTEL NAME");
           * cell.setCellStyle(updsatesSubHeaderStyle);
           * 
           * cell = row.createCell((short)3); cell.setCellValue("UPDATE DETAILS");
           * cell.setCellStyle(updsatesSubHeaderStyle1);
           * 
           * cell = row.createCell((short)4); cell.setCellValue("VALID FROM");
           * cell.setCellStyle(updsatesSubHeaderStyle);
           * 
           * cell = row.createCell((short)5); cell.setCellValue("VALID TILL");
           * cell.setCellStyle(updsatesSubHeaderStyle);
           * 
           * for (Object[] rsUpdateObj : rsUpdateResults) { lastLine = lastLine + 1; row =
           * sheetUpdates.createRow(lastLine); cell = row.createCell((short) 1);
           * cell.setCellValue(CommonUtil.nullRemove(rsUpdateObj[0]));
           * cell.setCellStyle(updsatesValStyle);
           * 
           * cell = row.createCell((short) 2);
           * cell.setCellValue(CommonUtil.nullRemove(rsUpdateObj[2]));
           * cell.setCellStyle(updsatesValStyle);
           * 
           * cell = row.createCell((short) 3);
           * cell.setCellValue(CommonUtil.nullRemove(rsUpdateObj[3])); cell.setCellStyle(
           * ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
           * IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, true, true, (short)
           * 10, "Arial"));
           * 
           * cell = row.createCell((short) 4);
           * cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(rsUpdateObj[4]),
           * "yyyy-MM-dd", "dd-MMM-yyyy")); cell.setCellStyle(updsatesValStyle);
           * 
           * cell = row.createCell((short) 5);
           * cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(rsUpdateObj[5]),
           * "yyyy-MM-dd", "dd-MMM-yyyy")); cell.setCellStyle(updsatesValStyle);
           * 
           * BufferedReader hName = new BufferedReader(new
           * StringReader(CommonUtil.nullRemove(rsUpdateObj[2]))); BufferedReader updateDetail = new
           * BufferedReader(new StringReader(CommonUtil.nullRemove(rsUpdateObj[3]))); int
           * hNameHeight = 0; int updateDetHeight = 0; while(hName.readLine() != null ){
           * hNameHeight++; } while(updateDetail.readLine() != null ){ updateDetHeight++; }
           * if(hNameHeight < updateDetHeight) { hNameHeight = updateDetHeight; }
           * row.setHeight((short) (short)(hNameHeight * 750)); }
           * 
           *//** File Writing Process *//*
          
           * FileOutputStream outputStream = new FileOutputStream(updateFileName);
           * updateWorkbook.write(outputStream); updateWorkbook.close();
           * System.out.println("Update Excel Downloaded!!!"); } }
           * 
           *//** Email Started *//*
          
           * if(fileList != null && !fileList.isEmpty()) { if (ratesheetMgt.getEmailIds() != null &&
           * !ratesheetMgt.getEmailIds().isEmpty()) { String[] mailList =
           * ratesheetMgt.getEmailIds().split(";"); String message = "Dear Partner,\r\n" + "\r\n" +
           * "\r\n" + "Greetings from "+ratesheetMgt.getDivisionName()+"!\r\n" + " \r\n" + "\r\n" +
           * "Kindly find the updated RateSheet for your reference.\r\n" + " \r\n" + "\r\n" +
           * "Thanks & Regards,\r\n" + "\r\n" + "\r\n" + ratesheetMgt.getDivisionName()+" LLC.";
           * 
           * String errMsg = emailService.sendMultipleMessageWithAttachment(mailList,
           * "Royal RateSheet::"+rateSheetDto.getRsName()+"::"+DateUtil.getSystemDate(), message,
           * fileList, schedularDate); if (!errMsg.equalsIgnoreCase("SUCCESS")) {
           *//** Error Log Writing *//*
                                      * FileInputStream inputStream = new FileInputStream(fileName);
                                      * byte fileContent[] = new byte[(int) fileName.length()];
                                      * inputStream.read(fileContent);
                                      * rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, errMsg,
                                      * fileName, fileContent, "ML_NOTSENT");
                                      * 
                                      * String subject =
                                      * "Mail Not Sent - "+rateSheetDto.getRsName();
                                      * emailService.sendMultipleMessageWithAttachment(sptMailsArr,
                                      * subject, message, fileList); } else {
                                      * rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, errMsg,
                                      * fileName, null, "ML_SENT"); } } }
                                      
          *//** Email Ended *//*

        } catch (Exception e) {
          logger.error(
              "Exception Nested Try1::RateSheetServiceImpl.Class::generateSingleRateSheetWithOutMail()",
              e);
          rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, e.fillInStackTrace().toString(),
              fileName, null, "RS_EXCEPTION");
          *//** Support Mail Logic *//*
          String subject = "Ratesheet generation Exception:: " + rateSheetDto.getRsName();
          emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject,
              e.fillInStackTrace().toString(), null);
        }
      }
    } catch (Exception e) {
      logger.error("Exception::RateSheetServiceImpl.Class::generateSingleRateSheetWithOutMail()",
          e);
      rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, e.fillInStackTrace().toString(), "", null,
          "EXCEPTION");
      *//** Support Mail Logic *//*
      String subject = "RateSheet Generation Exception::" + rateSheetDto.getRsName();
      emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject,
          e.fillInStackTrace().toString(), null);
      System.out.println(e);

    }
  */}

  /** Currently Not Using */
  @SuppressWarnings("resource")
  public void generateRateSheet()
      throws Exception {}

  @SuppressWarnings({"resource", "unchecked"})
  public void generateRateSheetNew(String fileMovingSchedularDate) throws Exception {

    /** Get Application Param Support Mail Details */
    Map<String, String> supportMailMaps = rateSheetDAO.getRatesheetAppParamDet("'SUPPORT_MAIL'");
    String sptMails = "";
    if (supportMailMaps.entrySet().size() > 0) {
      for (Entry<String, String> mail : supportMailMaps.entrySet()) {
        if (!sptMails.isEmpty()) {
          sptMails = sptMails + ";" + mail.getValue();
        } else {
          sptMails = mail.getValue();
        }
      }
    }
    String[] sptMailsArr = sptMails.split(";");

    /*
     * String subjectDemo = "Ratesheet generation Exception:: ";
     * emailService.sendMultipleMessageWithAttachment(sptMailsArr,subjectDemo, "Error", null);
     */
    /** Get All RateSheet With Mapped Hotel Details */
    RateSheetDto rateSheetDto = rateSheetDAO.getRateSheetDet();
    List<RatesheetMgt> ratesheetMgtList = (List<RatesheetMgt>) rateSheetDto.getObject();
    Map<String, String> sectorMap = (Map<String, String>) rateSheetDAO.getRateSheetsDivByMap("");
 /*   if (!ratesheetMgtList.isEmpty()) {
      *//** Copy Existing File to Write Seperate Folder by Date *//*
      String currentDirectory = System.getProperty("user.dir");
      String sourceFolder = currentDirectory + "/ratesheet/";
      currentDirectory =
          currentDirectory + "/ratesheet/" + DateUtil.getPreviousDateWithoutFormat(DateUtil.getOldDate(0)) + "/";
      File directory = new File(currentDirectory);
      if (!directory.exists())
        directory.mkdirs();
      String targetFolder = currentDirectory;
      File srcDir = new File(targetFolder);
      if (!srcDir.exists())
        srcDir.mkdirs();
      File sFile = new File(sourceFolder);
      File[] sourceFiles = sFile.listFiles();
      for (File fSource : sourceFiles) {
        if (fSource.getName().contains("xlsx")) {
          File fTarget = new File(new File(srcDir.getAbsolutePath()), fSource.getName());
          CustomFileUtil.copyFileUsingStream(fSource, fTarget);
        }
      }
      
      *//**Remove 7 Day Previous Folder *//*
      String dSource = sourceFolder + DateUtil.getPreviousDateWithoutFormat(DateUtil.getOldDate(7));
      File dFileSrc = new File(dSource);
      if(dFileSrc != null) {
        CustomFileUtil.deleteFiles(dFileSrc);
      }
    }*/
    

    /** Copy Existing File to Write Seperate Folder by Date */
    String folderDirectory = System.getProperty("user.dir");
    String sourceFolder = folderDirectory + "/ratesheet/";
    folderDirectory =
        folderDirectory + "/ratesheet/" + DateUtil.getFormattedDate(fileMovingSchedularDate, "dd.MM.yyyy", "ddMMyyyy") + "/";
    File newDirectory = new File(folderDirectory);
    if (!newDirectory.exists())
      newDirectory.mkdirs();
    String targetFolder = folderDirectory;
    File srcDir = new File(targetFolder);
    if (!srcDir.exists())
      srcDir.mkdirs();
    
    //folder creation for ratesheet outside tomcat
    Date date = new Date();
    String folderDirectory1 = filemovingFoldName+"/"+DateUtil.getFormattedDate(fileMovingSchedularDate, "dd.MM.yyyy", "ddMMyyyy")+"/";
    logger.info("Ratesheet folder path::"+folderDirectory1);
    File newDirectory1 = new File(folderDirectory1);
    if (!newDirectory1.exists())
      newDirectory1.mkdirs();
    String targetFolder1 = folderDirectory1;
    File srcDir1 = new File(targetFolder1);
    if (!srcDir1.exists())
      srcDir1.mkdirs();
    
    File mapDriveDirectory = new File("Z:/ratesheet/");
    String mappedDrivePath =  "/"+DateUtil.getFormattedDate(fileMovingSchedularDate, "dd.MM.yyyy", "ddMMyyyy") + "/";
    
/*    File sFile = new File(sourceFolder);
    File[] sourceFiles = sFile.listFiles();
    for (File fSource : sourceFiles) {
      if (fSource.getName().contains("xlsx")) {
        File fTarget = new File(new File(srcDir.getAbsolutePath()), fSource.getName());
        CustomFileUtil.copyFileUsingStream(fSource, fTarget);
      }
    }*/
    
 /*   File mapDriveDirectory = new File("Z:\ratesheet");
    String mappedDrivePath =  mapDriveDirectory.getAbsolutePath()+"/"+DateUtil.getPreviousDateWithoutFormat(DateUtil.getOldDate(0)) + "/";
    File mapDriveDirectory1 = new File(mappedDrivePath);
    if (!mapDriveDirectory1.exists())
      mapDriveDirectory1.mkdirs();
    File[] sourceFiles = srcDir.listFiles();
    for (File fSource : sourceFiles) {
      if (fSource.getName().contains("xlsx")) {
        File fTarget = new File(new File(mapDriveDirectory1.getAbsolutePath()), fSource.getName());
        CustomFileUtil.copyFileUsingStream(fSource, fTarget);
      }
    }*/

    try {

      String currentDirectory = System.getProperty("user.dir");
      currentDirectory = currentDirectory + "/ratesheet/";
      File directory = new File(currentDirectory);
      if (!directory.exists())
        directory.mkdirs();
      String displayName = "";
      //String schedularDate = DateUtil.getCurrentDateWithDotFormat();
      /** RateSheet MGT Loop */
      for (RatesheetMgt ratesheetMgt : ratesheetMgtList) {
       //List<BigInteger> updateList = new ArrayList<>();
        rateSheetDto = new RateSheetDto();
        rateSheetDto.setAutogenRsId(ratesheetMgt.getAutogenRsId());
        rateSheetDto.setRsName(ratesheetMgt.getRsName());
        rateSheetDto.setAgentCode(CommonUtil.nullRemove(ratesheetMgt.getAgentCode()).trim());
        rateSheetDto
            .setSheetPassword(CommonUtil.nullRemove(ratesheetMgt.getSheetPassword()).trim());
        String fileName = "";
        HashMap<String, String> fileList = new HashMap<>();

        rateSheetName = ratesheetMgt.getRsName().trim();
        fileName = currentDirectory + "\\" + rateSheetName + ".xlsx";
        fileList.put(rateSheetName + ".xlsx", fileName);

        /** File Reading Process */
        XSSFWorkbook xSSFWorkbook = null;
        File f = new File(fileName);
        if (f.isFile() && f.canRead()) {
          FileInputStream readInputStream = new FileInputStream(fileName);
          xSSFWorkbook = (XSSFWorkbook) WorkbookFactory.create(readInputStream);
        }
        if (xSSFWorkbook == null) {
          xSSFWorkbook = new XSSFWorkbook();
        }

        CellStyle cellStyle = xSSFWorkbook.createCellStyle();
        /** All Cell Style */
        XSSFColor color1 = new XSSFColor(new Color(51, 63, 79));
        XSSFColor color2 = new XSSFColor(new Color(221, 235, 247));
        XSSFColor color3 = new XSSFColor(new Color(252, 228, 214));
        XSSFColor color4 = new XSSFColor(new Color(237, 237, 237));
        XSSFColor color5 = new XSSFColor(new Color(31, 78, 120));
        // XSSFColor color6 = new XSSFColor(new Color(252, 228, 214));
        XSSFColor color7 = new XSSFColor(new Color(224, 224, 224));
        XSSFColor color8 = new XSSFColor(new Color(250, 191, 143));
        XSSFColor color9 = new XSSFColor(new Color(255, 255, 255));
        XSSFColor lightGray = new XSSFColor(new Color(211, 211, 211));
        XSSFColor rptBlue = new XSSFColor(new Color(0, 112, 192));
        String logoName = "RoyalPark.png";
        if (ratesheetMgt.getDivisionId() != null && !ratesheetMgt.getDivisionId().isEmpty()
            && "02".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
          rptBlue = new XSSFColor(new Color(198, 89, 17));
          logoName = "RoyalGulf.png";
        }

        hColor4C = null;
        hColor4L = null;
        hWColor5C = null;
        hWColor5L = null;
        hDBColor2C = null;
        ratesColumnStyle = null;
        tacticalHeaderStyle = null;
        tacticalvalueStyle = null;
        remarksColor5Style = null;
        remarksColor9Style = null;
        taxNotablelightStyleL = null;
        taxNotableColor3StyleC = null;
        taxNotableColor2StyleC = null;
        taxNotableColor5StyleC = null;
        maxOccColor9StyleC = null;
        minNightColor2StyleC = null;
        minNightColor5StyleC = null;
        minNightColor9ValStyleC = null;
        minNightWeekDayColor9StyleC = null;
        specialEvColor5StyleC = null;
        specialEvColor2HeaderStyleL = null;
        specialEvColor3HeaderStyleL = null;
        specialEvColor5ValStyleC = null;
        specialEvColor2ValStyleC = null;
        specialEvColor9ValStyleC = null;
        cancelColor5HeaderStyleC = null;
        cancelColor2SubHeaderStyleC = null;
        cancelColor2SubHeaderStyleL = null;
        cancelColor9ValStyleC = null;
        cancelColor9ValStyleL = null;
        checkInColor5HeaderStyleC = null;
        checkInTimeStyle = null;
        checkInTimeStyle1 = null;
        checkOutTimeStyle = null;
        checkOutTimeStyle1 = null;
        checkOutPeriodTimeStyle = null;
        checkOutFromToStyle = null;
        checkOutRoomTypeStyle = null;
        checkOutDateValStyle = null;
        checkOutRoomValStyle = null;
        checkOutRestictStyle = null;
        checkOutRestictValStyle = null;
        pEarlyCIHeadStyle = null;
        pEarlyCISubHeadStyle1 = null;
        pEarlyCISubHeadStyle2 = null;
        pEarlyCIDateValStyle = null;
        pEarlyCIRoomValStyle = null;
        earlyCIHeaderStyle = null;
        earlyCIValStyle = null;
        earlyCIValStyleDB = null;
        generalPolHeaderStyle = null;
        generalPolSubHeaderStyle = null;
        generalPolValStyle1 = null;
        generalPolValStyle2 = null;
        hotelConstHeadStyle = null;
        hotelConstSubHeadStyle = null;
        hotelConstValStyle1 = null;
        hotelConstValStyle2 = null;
        backIndexStyle = null;
        mainHeaderStyle = null;
        mainSubHeaderStyle = null;
        row2CellStyle = null;
        indexHeaderStyle = null;
        indexHeaderStyle1 = null;
        indexHeaderStyle2 = null;
        updatesheaderStyle = null;
        updsatesSubHeaderStyle = null;
        updsatesSubHeaderStyle1 = null;
        updsatesValStyle = null;
        updsatesValStyle1 = null;
        ratesValStyle = null;
        mainHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color1, HorizontalAlignment.CENTER, true, false,
            (short) 18);
        mainSubHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true);
        backIndexStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color8, HorizontalAlignment.CENTER, true, false,
            (short) 12);
        row2CellStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color7, HorizontalAlignment.CENTER, true);
        indexHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, true, false,
            (short) 10, "Arial");
        indexHeaderStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 10, "Arial");
        indexHeaderStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), rptBlue, HorizontalAlignment.CENTER, true, false,
            (short) 10, "Arial");
        hColor4C = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_BLUE.getIndex(), color4, HorizontalAlignment.CENTER, true);
        hColor4L = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_BLUE.getIndex(), color4, HorizontalAlignment.LEFT, true);
        hWColor5C = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true);
        hWColor5L = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.LEFT, true);
        hDBColor2C = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_BLUE.getIndex(), color2, HorizontalAlignment.CENTER, true);
        ratesColumnStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true, true,
            (short) 11);
        tacticalHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.CENTER, true, true,
            (short) 11);
        tacticalvalueStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 11);
        remarksColor5Style = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        remarksColor9Style = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, true,
            (short) 11);
        taxNotablelightStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), lightGray, HorizontalAlignment.LEFT, true, true,
            (short) 10);
        taxNotableColor3StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        taxNotableColor2StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        taxNotableColor5StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        maxOccColor9StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        minNightColor2StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        minNightColor5StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        minNightColor9ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        minNightWeekDayColor9StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        specialEvColor5StyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        specialEvColor2HeaderStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.LEFT, true, false,
            (short) 11);
        specialEvColor3HeaderStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.LEFT, true, false,
            (short) 11);
        specialEvColor5ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, true,
            (short) 11, "Calibri");
        specialEvColor2ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true, true,
            (short) 11, "Calibri");
        specialEvColor9ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        cancelColor5HeaderStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        cancelColor2SubHeaderStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        cancelColor2SubHeaderStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color2, HorizontalAlignment.LEFT, true, false,
            (short) 11);
        cancelColor9ValStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 10);
        cancelColor9ValStyleL = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, true,
            (short) 10);
        checkInColor5HeaderStyleC = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.WHITE.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        checkInTimeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkInTimeStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(50, 205, 50)),
            HorizontalAlignment.CENTER, true, false, (short) 11);
        checkOutTimeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkOutTimeStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(205, 92, 92)),
            HorizontalAlignment.CENTER, true, false, (short) 11);
        checkOutPeriodTimeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkOutFromToStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11);
        checkOutRoomTypeStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.RIGHT, true, false,
            (short) 11, "Arial", false);
        checkOutDateValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        checkOutRoomValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        checkOutRestictStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color3, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        checkOutRestictValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        pEarlyCIHeadStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(50, 205, 50)),
            HorizontalAlignment.CENTER, true, false, (short) 11, "Arial");
        pEarlyCISubHeadStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        pEarlyCISubHeadStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(180, 182, 233)),
            HorizontalAlignment.CENTER, true, false, (short) 11, "Arial");
        pEarlyCIDateValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        pEarlyCIRoomValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(240, 230, 198)),
            HorizontalAlignment.CENTER, false, false, (short) 11, "Calibri");
        earlyCIHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        earlyCIValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Calibri");
        earlyCIValStyleDB = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.DARK_RED.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Calibri");
        generalPolHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        generalPolSubHeaderStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        generalPolValStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        generalPolValStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, true,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        hotelConstHeadStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color5, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        hotelConstSubHeadStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
            (short) 11, "Arial");
        hotelConstValStyle1 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, false,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        hotelConstValStyle2 = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, false, true,
            (short) 11, "Arial", true, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        ratesValStyle = ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
            IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, false, true,
            (short) 11, "Calibri");
        try {
          if ("YES".equalsIgnoreCase(ratesheetMgt.getSendRatesheet())) {
            CreationHelper createHelper = xSSFWorkbook.getCreationHelper();
            XSSFColor rptYello = new XSSFColor(new Color(255, 192, 0));
            XSSFColor rptLightYellow = new XSSFColor(new Color(254, 244, 236));
            int lastLine = 0;
            boolean isNew = false;
            if (xSSFWorkbook.getSheet("Index") != null) {
              xSSFWorkbook
                  .removeSheetAt(xSSFWorkbook.getSheetIndex(xSSFWorkbook.getSheet("Index")));
            } else {
              isNew = true;
            }
            Sheet sheetIndex = xSSFWorkbook.getSheet("Index");
            if (sheetIndex == null) {
              sheetIndex = xSSFWorkbook.createSheet("Index");
            }
            /** Index Header Part */
            lastLine = addIndexHeaderDetails(lastLine, sheetIndex, xSSFWorkbook, cellStyle, rptBlue,
                logoName, ratesheetMgt);

            String sName = "";
            Row sheetIndexrow0 = null;
            Cell cell;
            Set<String> dispUniName = new HashSet<>();

            /** RateSheet Mapping Loop */
            for (RatesheetMapping ratesheetMap : ratesheetMgt.getRatesheetMapping()) {
              Date startTime = new Date();
              rateSheetDto.setPartyName(CommonUtil.nullRemove(ratesheetMap.getPartyName()).trim());
              rateSheetDto
                  .setCountryCode(CommonUtil.nullRemove(ratesheetMap.getCountryCode()).trim());
              rateSheetDto.setPartyCode(CommonUtil.nullRemove(ratesheetMap.getPartyCode()).trim());
              rateSheetDto.setFromDate(DateUtil.getTodayDate());
              rateSheetDto.setDisplayName(CommonUtil.nullRemove(ratesheetMap.getDisplayName())
                  .trim().replace(" ", "_").replace("  ", "_").replace("   ", "_").replace("*", "_")
                  .replace("&", "_").replace(",", "_"));
              if (rateSheetDto.getDisplayName() != null
                  && rateSheetDto.getDisplayName().length() > 0) {
                if (rateSheetDto.getDisplayName().length() > 31) {
                  displayName = rateSheetDto.getDisplayName().substring(0, 30);
                } else {
                  displayName = rateSheetDto.getDisplayName().substring(0,
                      rateSheetDto.getDisplayName().length());
                }

                if (!dispUniName.contains(displayName)) {
                  displayName = CommonUtil.nullRemove(displayName).trim().replace(" ", "_")
                      .replace("  ", "_").replace("   ", "_").replace("*", "_").replace("&", "_")
                      .replace(",", "_");
                  if (!displayName.isEmpty() && displayName.length() > 31) {
                    displayName = displayName.substring(0, 30);
                  } else {
                    displayName = displayName.substring(0, displayName.length());
                  }
                  dispUniName.add(displayName);
                  rateSheetDto.setDisplayName(displayName);
                } else {
                  if (!rateSheetDto.getDisplayName().isEmpty()) {
                    displayName = rateSheetDto.getDisplayName().substring(0,
                        rateSheetDto.getDisplayName().length()) + randomString.nextString();
                  } else {
                    if (rateSheetDto.getPartyName().length() > 31) {
                      displayName = rateSheetDto.getPartyName().substring(0, 30);
                    } else {
                      displayName = rateSheetDto.getPartyName().substring(0,
                          rateSheetDto.getPartyName().length());
                    }
                  }
                  displayName = CommonUtil.nullRemove(displayName).trim().replace(" ", "_")
                      .replace("  ", "_").replace("   ", "_").replace("*", "_").replace("&", "_")
                      .replace(",", "_");
                  if (!displayName.isEmpty() && displayName.length() > 31) {
                    displayName = displayName.substring(0, 30);
                  } else {
                    displayName = displayName.substring(0, displayName.length());
                  }
                  dispUniName.add(displayName);
                  rateSheetDto.setDisplayName(displayName);
                }
              } else {
                if (rateSheetDto.getPartyName().length() > 31) {
                  displayName = rateSheetDto.getPartyName().substring(0, 30);
                } else {
                  displayName = rateSheetDto.getPartyName().substring(0,
                      rateSheetDto.getPartyName().length());
                }
                displayName =
                    CommonUtil.nullRemove(displayName).trim().replace(" ", "_").replace("  ", "_")
                        .replace("   ", "_").replace("*", "_").replace("&", "_").replace(",", "_");
                if (!displayName.isEmpty() && displayName.length() > 31) {
                  displayName = displayName.substring(0, 30);
                } else {
                  displayName = displayName.substring(0, displayName.length());
                }
                dispUniName.add(displayName);
                rateSheetDto.setDisplayName(displayName);
              }

              String toDate = rateSheetDAO.getEndContractDate(
                  CommonUtil.nullRemove(ratesheetMap.getPartyCode()).trim(),
                  CommonUtil.nullRemove(ratesheetMap.getCountryCode()).trim());
              rateSheetDto.setToDate(DateUtil.getFormattedDate(toDate, "yyyy-MM-dd", "yyyy/MM/dd"));
                if (rateSheetDto.getToDate() != null && !rateSheetDto.getToDate().isEmpty()) {
                /*  rateSheetDto.setToDate(DateUtil.getContractEndDateAfterSixMonth());
                }*/
  
                lastLine = lastLine + 1;
                sheetIndexrow0 = sheetIndex.createRow(lastLine);
                cell = sheetIndexrow0.createCell((short) 1);
                cell.setCellValue(ratesheetMap.getPartyName());
                cell.setCellStyle(indexHeaderStyle);
  
                sName = "'" + rateSheetDto.getDisplayName() + "'";
                Hyperlink hyperLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
                hyperLink.setAddress(sName + "!A1");
                cell.setHyperlink(hyperLink);
  
                cell = sheetIndexrow0.createCell((short) 2);
                cell.setCellValue(ratesheetMap.getCategoryName());
                cell.setCellStyle(indexHeaderStyle1);
  
                cell = sheetIndexrow0.createCell((short) 3);
                cell.setCellValue(sectorMap.get(ratesheetMap.getPartyCode()));
                cell.setCellStyle(indexHeaderStyle1);
  
                cell = sheetIndexrow0.createCell((short) 4);
                cell.setCellValue(DateUtil.getFormattedDate(rateSheetDto.getFromDate(), "yyyy/MM/dd",
                    "dd/MM/yyyy"));
                cell.setCellStyle(indexHeaderStyle2);
  
                cell = sheetIndexrow0.createCell((short) 5);
                cell.setCellValue(
                    DateUtil.getFormattedDate(rateSheetDto.getToDate(), "yyyy/MM/dd", "dd/MM/yyyy"));
                cell.setCellStyle(indexHeaderStyle2);
  
                try {
                  String newHotalflag = "NOTUPDATE";
                  /** RATESHEET SHEET HOTEL CHECK */
                  if (xSSFWorkbook
                      .getSheetIndex(CommonUtil.nullRemove(rateSheetDto.getDisplayName())) >= 0) {
                    if (ratesheetMap.getCurrentUpdate() != null
                        && "Y".equalsIgnoreCase(ratesheetMap.getCurrentUpdate().trim())) {
                      xSSFWorkbook.removeSheetAt(xSSFWorkbook
                          .getSheetIndex(CommonUtil.nullRemove(rateSheetDto.getDisplayName())));
                      newHotalflag = "UPDATE";
                    } else {
                      newHotalflag = "NOTUPDATE";
                    }
                  } else {
                    newHotalflag = "UPDATE";
                  }
  
                  if ((!isNew && ratesheetMap.getCurrentUpdate() != null
                      && "Y".equalsIgnoreCase(ratesheetMap.getCurrentUpdate().trim()))) {
                    generateRateSheetByPartyNew(xSSFWorkbook, cellStyle, rateSheetDto,
                        rateSheetDto.getDisplayName(), lastLine);
                    newHotalflag = "NOTUPDATE";
                  }
  
                  if (isNew) {
                    generateRateSheetByPartyNew(xSSFWorkbook, cellStyle, rateSheetDto,
                        rateSheetDto.getDisplayName(), lastLine);
                    newHotalflag = "NOTUPDATE";
                  }
  
                  if ("UPDATE".equalsIgnoreCase(newHotalflag)) {
                    generateRateSheetByPartyNew(xSSFWorkbook, cellStyle, rateSheetDto,
                        rateSheetDto.getDisplayName(), lastLine);
                  }
                  //updateList.add(ratesheetMap.getAutogenRsMapId());
                  rateSheetDAO.ratesheetMapUpdateStatus(ratesheetMap.getAutogenRsMapId(), ratesheetMgt.getAutogenRsId());
                } catch (Exception e) {
                  logger.error(
                      "Exception Nested Try2::RateSheetServiceImpl.Class::getReteSheetExcel()", e);
                  rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, e.fillInStackTrace().toString(),
                      fileName, null, "RSH_EXCEPTION");
                  /** Support Mail Logic */
                  String subject = "Ratesheet generation Exception:: " + rateSheetDto.getRsName()
                      + "::" + rateSheetDto.getPartyName() + "::" + rateSheetDto.getPartyName();
                  emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject,
                      e.fillInStackTrace().toString(), null);
                }
  
                /** RATESHEET TIME LOGGER */
                Date endTime = new Date();
                rateSheetDAO.saveRateSheetTimeLog(rateSheetDto, startTime, endTime);
                
              } else {
                dispUniName.remove(displayName);
              }
            }

            /** Ratesheet Footer */
            lastLine = addIndexFooterDetails(lastLine, sheetIndex, xSSFWorkbook, cellStyle,
                rptYello, rptLightYellow, sName, createHelper);
            sheetIndex.protectSheet(rateSheetDto.getSheetPassword());

            /** Ratesheet Sheet Order */
            xSSFWorkbook.setSheetOrder("Index", 0);
            xSSFWorkbook.setActiveSheet(0);

            /** REMOVE UNUSED SHEETS */
            Set<String> sheetNames = new HashSet<>();
            for (int i = 1; i < xSSFWorkbook.getNumberOfSheets(); i++) {
              sheetNames.add(xSSFWorkbook.getSheetName(i));
            }
            sheetNames.removeAll(dispUniName);
            for (String unUsedSheet : sheetNames) {
              xSSFWorkbook
                  .removeSheetAt(xSSFWorkbook.getSheetIndex(CommonUtil.nullRemove(unUsedSheet)));
            }

            /** File Writing Process Start*/
            FileOutputStream outputStream = new FileOutputStream(fileName);
            xSSFWorkbook.write(outputStream);
            xSSFWorkbook.close();
            System.out.println("RateSheet Excel Downloaded!!!");
            /** File Writing Process End*/
            rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, "File Generated", fileName, null, "GENERATED");
            
            /**File Moving Start*/
            File fSource = new File(fileName);
            File fTarget = new File(new File(srcDir.getAbsolutePath()), fSource.getName());
            CustomFileUtil.copyFileUsingStream(fSource, fTarget);
            /**File Moving End*/
            
            /**File Moving to outside Ratesheet folder Start*/
            File fSource1 = new File(fileName);
            File fTarget1 = new File(new File(filemovingFoldName+"/"+DateUtil.getFormattedDate(fileMovingSchedularDate, "dd.MM.yyyy", "ddMMyyyy"))+"/", fSource1.getName());
            CustomFileUtil.copyFileUsingStream(fSource1, fTarget1);
            /**File Moving End*/
            
          }

          if ("YES".equalsIgnoreCase(ratesheetMgt.getSendUpdates())) {
            List<Object[]> rsUpdateResults =
                rateSheetDAO.getRateSheetUpdatesDet(rateSheetDto.getAutogenRsId(), fileMovingSchedularDate);
            if (rsUpdateResults != null && !rsUpdateResults.isEmpty()) {
              String updateFileName = "";
              XSSFWorkbook updateWorkbook = new XSSFWorkbook();
              XSSFColor rptLightGray = new XSSFColor(new Color(214, 220, 228));
              CellStyle cellStyleUpdate = updateWorkbook.createCellStyle();
              updatesheaderStyle = ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
                  IndexedColors.WHITE.getIndex(), rptBlue, HorizontalAlignment.CENTER, false, false,
                  (short) 16, "Arial Black");
              updsatesSubHeaderStyle = ExcelUtil.getCellStyleForContent(updateWorkbook,
                  cellStyleUpdate, IndexedColors.BLACK.getIndex(), rptLightGray,
                  HorizontalAlignment.CENTER, true, false, (short) 10, "Arial");
              updsatesSubHeaderStyle1 = ExcelUtil.getCellStyleForContent(updateWorkbook,
                  cellStyleUpdate, IndexedColors.BLACK.getIndex(), rptLightGray,
                  HorizontalAlignment.LEFT, true, true, (short) 10, "Arial");
              updsatesValStyle = ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
                  IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true, false,
                  (short) 10, "Arial");
              updsatesValStyle1 = ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
                  IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, true, true,
                  (short) 10, "Arial");
              rateSheetName = ratesheetMgt.getRsName().trim() + "_Updates";
              updateFileName = currentDirectory + "\\" + rateSheetName + ".xlsx";
              fileList.put(rateSheetName + ".xlsx", updateFileName);
              int lastLine = 0;
              Sheet sheetUpdates = updateWorkbook.createSheet("Updates");
              sheetUpdates.setColumnWidth(0, (short) 1000);
              sheetUpdates.setColumnWidth(1, (short) 6000);
              sheetUpdates.setColumnWidth(2, (short) 12000);
              sheetUpdates.setColumnWidth(3, (short) 17000);
              sheetUpdates.setColumnWidth(4, (short) 4000);
              sheetUpdates.setColumnWidth(5, (short) 4000);
              sheetUpdates.setDisplayGridlines(false);
              Row row = null;
              Cell cell = null;
              lastLine = lastLine + 1;
              row = sheetUpdates.createRow(lastLine);
              row.setHeight((short) 500);
              cell = row.createCell((short) 1);
              cell.setCellValue("UPDATES FOR THE DATE "+ fileMovingSchedularDate);
              cell.setCellStyle(updatesheaderStyle);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5),
                  sheetUpdates, xSSFWorkbook);

              lastLine = lastLine + 1;
              row = sheetUpdates.createRow(lastLine);
              row.setHeight((short) 600);
              sheetUpdates.setAutoFilter(new CellRangeAddress(lastLine, lastLine, 1, 5));
              cell = row.createCell((short) 1);
              cell.setCellValue("UPDATE TYPE");
              cell.setCellStyle(updsatesSubHeaderStyle);

              cell = row.createCell((short) 2);
              cell.setCellValue("HOTEL NAME");
              cell.setCellStyle(updsatesSubHeaderStyle);

              cell = row.createCell((short) 3);
              cell.setCellValue("UPDATE DETAILS");
              cell.setCellStyle(updsatesSubHeaderStyle1);

              cell = row.createCell((short) 4);
              cell.setCellValue("VALID FROM");
              cell.setCellStyle(updsatesSubHeaderStyle);

              cell = row.createCell((short) 5);
              cell.setCellValue("VALID TILL");
              cell.setCellStyle(updsatesSubHeaderStyle);

              for (Object[] rsUpdateObj : rsUpdateResults) {
                lastLine = lastLine + 1;
                row = sheetUpdates.createRow(lastLine);
                cell = row.createCell((short) 1);
                cell.setCellValue(CommonUtil.nullRemove(rsUpdateObj[0]));
                cell.setCellStyle(updsatesValStyle);

                cell = row.createCell((short) 2);
                cell.setCellValue(CommonUtil.nullRemove(rsUpdateObj[2]));
                cell.setCellStyle(updsatesValStyle);

                cell = row.createCell((short) 3);
                cell.setCellValue(CommonUtil.nullRemove(rsUpdateObj[3]));
                cell.setCellStyle(ExcelUtil.getCellStyleForContent(updateWorkbook, cellStyleUpdate,
                    IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.LEFT, true, true,
                    (short) 10, "Arial"));

                cell = row.createCell((short) 4);
                cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(rsUpdateObj[4]),
                    "yyyy-MM-dd", "dd-MMM-yyyy"));
                cell.setCellStyle(updsatesValStyle);

                cell = row.createCell((short) 5);
                cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(rsUpdateObj[5]),
                    "yyyy-MM-dd", "dd-MMM-yyyy"));
                cell.setCellStyle(updsatesValStyle);

                BufferedReader hName =
                    new BufferedReader(new StringReader(CommonUtil.nullRemove(rsUpdateObj[2])));
                BufferedReader updateDetail =
                    new BufferedReader(new StringReader(CommonUtil.nullRemove(rsUpdateObj[3])));
                int hNameHeight = 0;
                int updateDetHeight = 0;
                while (hName.readLine() != null) {
                  hNameHeight++;
                }
                while (updateDetail.readLine() != null) {
                  updateDetHeight++;
                }
                if (hNameHeight < updateDetHeight) {
                  hNameHeight = updateDetHeight;
                }
                row.setHeight((short) (short) (hNameHeight * 750));
              }

              /** File Writing Process */
              FileOutputStream outputStream = new FileOutputStream(updateFileName);
              updateWorkbook.write(outputStream);
              updateWorkbook.close();
              System.out.println("Update Excel Downloaded!!!");
            }
          }

          /** Email Started */


          /** Email Started */
         /* if (fileList != null && !fileList.isEmpty()) {
            if (ratesheetMgt.getEmailIds() != null && !ratesheetMgt.getEmailIds().isEmpty()) {
              String[] mailList = ratesheetMgt.getEmailIds().split(";");
              String message = "Dear Partner,\r\n" + "\r\n" + "\r\n" + "Greetings from "
                  + ratesheetMgt.getDivisionName() + "!\r\n" + " \r\n" + "\r\n"
                  + "Please find attached the Updated rate sheet for the Day. Also along with the Rate sheet is the Update List ( Changes done for the day ). \r\n"
                  + " \r\n" + "\r\n" + "Thanks & Regards,\r\n" + "\r\n" + "\r\n"
                  + ratesheetMgt.getDivisionName() + " LLC.";

              String errMsg = emailService.sendMultipleMessageWithAttachment(mailList,
                  rateSheetDto.getRsName() + "::" + DateUtil.getSystemDate(), message, fileList,
                  schedularDate);
              if (!errMsg.equalsIgnoreCase("SUCCESS")) {
                *//** Error Log Writing *//*
                FileInputStream inputStream = new FileInputStream(fileName);
                byte fileContent[] = new byte[(int) fileName.length()];
                inputStream.read(fileContent);
                rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, errMsg, fileName, fileContent,
                    "ML_NOTSENT");

                String subject = "Mail Not Sent - " + rateSheetDto.getRsName();
                emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject, message,
                    fileList, schedularDate);
              } else {
                rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, errMsg, fileName, null, "ML_SENT");
              }
            }
          }*/
          /** Email Ended */

        } catch (Exception e) {
          logger.error("Exception Nested Try1::RateSheetServiceImpl.Class::getReteSheetExcel()", e);
          rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, e.fillInStackTrace().toString(),
              fileName, null, "RS_EXCEPTION");
          /** Support Mail Logic */
          String subject = "Ratesheet generation Exception:: " + rateSheetDto.getRsName();
          emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject,
              e.fillInStackTrace().toString(), null);
        }

        /** Updated Ratesheet Hotels Status to 'N' */
        //commented by pragadees rateSheetDAO.ratesheetMapStatusChange(updateList, ratesheetMgt.getAutogenRsId());
      }
      //commented by pragadeeswar on oct 10th 2020 for disable network folder
//      if((filemovingServerUser == null && filemovingServerUser.isEmpty()) 
//          && filemovingServerPass == null && filemovingServerPass.isEmpty()
//          && filemovingServerDomain == null && filemovingServerDomain.isEmpty()
//          && filemovingServerFoldName == null && filemovingServerFoldName.isEmpty()) {
//        emailService.sendMultipleMessageWithAttachment(sptMailsArr, "File Moving Failure Date:"+schedularDate,
//            "Failure Due to File moving parameter missing in application.properties file.<br> Add missing parameter and restart server.", null);
//      } else {
//        try {
//          NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(filemovingServerDomain,
//              filemovingServerUser, filemovingServerPass);
//          System.out.println("Path: " + filemovingServerFoldName);
//          SmbFile testFile = new SmbFile(filemovingServerFoldName, auth);
//          System.out.println("Test Path:" + testFile.getName());
//          SmbFile sFile = new SmbFile(testFile.getPath() + "/ratesheet/" + mappedDrivePath, auth);
//          if (!sFile.exists())
//            sFile.mkdirs();
//          System.out.println("SMB Path:" + sFile.getPath());
//          File driveDir = new File(targetFolder);
//          File[] sourceFiles = driveDir.listFiles();
//          for (File fSource : sourceFiles) {
//            Path source = Paths.get(fSource.getAbsolutePath());
//            SmbFile sFileWrite = new SmbFile(
//                filemovingServerFoldName + "/ratesheet/" + mappedDrivePath + fSource.getName(),
//                auth);
//            SmbFileOutputStream sfos = new SmbFileOutputStream(sFileWrite);
//            Files.copy(source, sfos);
//            sfos.close();
//          }
//          logger.info("File moving Successful");
//          emailService.sendMultipleMessageWithAttachment(sptMailsArr,
//              "File Moving Successful Date:" + schedularDate,
//              filemovingServerFoldName + "/ratesheet/" + mappedDrivePath, null);
//        } catch (Exception e) {
//          e.printStackTrace();
//          System.out.println("Network Error::" + e);
//          logger.error("Network Error::" + e);
//          emailService.sendMultipleMessageWithAttachment(sptMailsArr,
//              "File Moving Failure Date:" + schedularDate, "", null);
//        }
//      }
      
      /**Remove 7 Day Previous Folder */
      String dSource = sourceFolder + DateUtil.getPreviousDateWithoutFormat(DateUtil.getOldDate(fileremovingDays != null ? Integer.valueOf(fileremovingDays):7));
      File dFileSrc = new File(dSource);
      if(dFileSrc != null) {
        CustomFileUtil.deleteFiles(dFileSrc);
      }
      
    } catch (Exception e) {
      logger.error("Exception::RateSheetServiceImpl.Class::getReteSheetExcel()", e);
      rateSheetDAO.saveRateSheetErrorLog(rateSheetDto, e.fillInStackTrace().toString(), "", null,
          "EXCEPTION");
      /** Support Mail Logic */
      String subject = "RateSheet Generation Exception::" + rateSheetDto.getRsName();
      emailService.sendMultipleMessageWithAttachment(sptMailsArr, subject,
          e.fillInStackTrace().toString(), null);
      System.out.println(e);

    }
  }

  public int addIndexHeaderDetails(int lastLine, Sheet sheetIndex, Workbook xSSFWorkbook,
      CellStyle cellStyle, XSSFColor rptBlue, String logoName, RatesheetMgt ratesheetMgt)
      throws Exception {
    
    Object[] currCode = rateSheetDAO.getCountryCode(ratesheetMgt.getAgentCode());
    XSSFColor rptYello = new XSSFColor(new Color(255, 192, 0));
    XSSFColor rptLightGray = new XSSFColor(new Color(214, 220, 228));
    XSSFColor color9 = new XSSFColor(new Color(255, 255, 255));
    Row sheetIndexrow0 = sheetIndex.createRow(lastLine);
    sheetIndexrow0.setHeight((short) 500);
    Cell cell = sheetIndexrow0.createCell((short) 0);
    sheetIndex.setColumnWidth(0, (short) 1000);
    sheetIndex.setColumnWidth(1, (short) 22000);
    sheetIndex.setColumnWidth(2, (short) 4000);
    sheetIndex.setColumnWidth(3, (short) 6000);
    sheetIndex.setColumnWidth(4, (short) 4000);
    sheetIndex.setColumnWidth(5, (short) 4000);

    sheetIndex.setDisplayGridlines(false);
    CreationHelper helper = xSSFWorkbook.getCreationHelper();
    ClientAnchor anchor = helper.createClientAnchor();

    anchor.setCol1(0);
    anchor.setCol2(5);
    anchor.setRow1(0);
    anchor.setRow2(7);
    Drawing<?> drawing = sheetIndex.createDrawingPatriarch();
    drawing.createPicture(anchor, ExcelUtil.getLogo((Workbook) xSSFWorkbook, sheetIndex, logoName,
        Workbook.PICTURE_TYPE_PNG));

    lastLine = lastLine + 7;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    if ("01".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
      cell.setCellValue("ROYAL PARK TOURISM L.L.C, DUBAI");
    } else if ("02".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
      cell.setCellValue("ROYAL GULF TOURISM L.L.C, DUBAI");
    } else {
      cell.setCellValue(
          CommonUtil.nullRemove(ratesheetMgt.getDivisionName()).toUpperCase() + " L.L.C, DUBAI");
    }
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.WHITE.getIndex(),
            rptBlue, HorizontalAlignment.CENTER, false, false, (short) 16, "Arial Black"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    if(currCode != null && currCode.length > 0) {
      cell.setCellValue("ALL HOTEL RATES ARE QUOTED IN "+ CommonUtil.nullRemove(currCode[2]).trim());
    } else {
      cell.setCellValue("ALL HOTEL RATES ARE QUOTED IN AED");
    }
   
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            color9, HorizontalAlignment.CENTER, false, false, (short) 14, "Calibri"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("PLEASE CLICK HERE FOR DETAILS OF TOURISM DIRHAM CHARGES");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptYello, HorizontalAlignment.CENTER, true, false, (short) 12, "Bookman Old Style"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    if(getIndexNameMap().get(ratesheetMgt.getRsName()) != null && !getIndexNameMap().get(ratesheetMgt.getRsName()).isEmpty()) {
      cell.setCellValue(getIndexNameMap().get(ratesheetMgt.getRsName()).toUpperCase());
    }
    
    /*if ("001".equalsIgnoreCase(ratesheetMgt.getDivisionId())
        || "01".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
      cell.setCellValue("JUMEIRAH BEACH SECTOR");
    } else if ("002".equalsIgnoreCase(ratesheetMgt.getDivisionId())
        || "02".equalsIgnoreCase(ratesheetMgt.getDivisionId())) {
      cell.setCellValue("JUMEIRAH BEACH SECTOR");
    } else {
      cell.setCellValue("OTHER SECTOR");
    }*/

    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.WHITE.getIndex(),
            rptBlue, HorizontalAlignment.CENTER, false, false, (short) 12, "Arial Black"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 3), sheetIndex,
        xSSFWorkbook);

    cell = sheetIndexrow0.createCell((short) 4);
    cell.setCellValue("VALIDITY");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.WHITE.getIndex(),
            rptBlue, HorizontalAlignment.CENTER, false, false, (short) 11, "Arial Black"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 4, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    CellStyle hederStyle =
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptLightGray, HorizontalAlignment.CENTER, true, false, (short) 10, "Arial");
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Hotel Name");
    cell.setCellStyle(hederStyle);
    sheetIndex.setAutoFilter(new CellRangeAddress(lastLine, lastLine, 1, 5));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("Star Category");
    cell.setCellStyle(hederStyle);

    cell = sheetIndexrow0.createCell((short) 3);
    cell.setCellValue("Location");
    cell.setCellStyle(hederStyle);

    cell = sheetIndexrow0.createCell((short) 4);
    cell.setCellValue("From");
    cell.setCellStyle(hederStyle);

    cell = sheetIndexrow0.createCell((short) 5);
    cell.setCellValue("To");
    cell.setCellStyle(hederStyle);

    return lastLine;
  }

  public int addIndexFooterDetails(int lastLine, Sheet sheetIndex, Workbook xSSFWorkbook,
      CellStyle cellStyle, XSSFColor rptYello, XSSFColor rptLightYellow, String sName,
      CreationHelper createHelper) {

    lastLine = lastLine + 2;
    Row sheetIndexrow0 = sheetIndex.createRow(lastLine);
    Cell cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("TOURISM DIRHAM (DUBAI & RAS AL KHAIMAH)");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptYello, HorizontalAlignment.CENTER, true, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue(
        "The Tourism Dirham is a minimal charge to be applied to hotel guests, staying in any type of hotel establishment, including hotels, hotel apartments.");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), rptLightYellow, HorizontalAlignment.LEFT, false, true,
        (short) 11, "Arial Rounded MT Bold"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue(
        "This fee will be levied per occupied room night, and will be represented as a separate line item in the bill.");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptLightYellow, HorizontalAlignment.LEFT, false, true, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("The details of the Tourim Dirham fee structure as per below.");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptLightYellow, HorizontalAlignment.LEFT, false, true, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Hotel Establishment Classification");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(155, 194, 230)),
        HorizontalAlignment.CENTER, true, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("Fee Per Occupied Bedroom Per Night");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(155, 194, 230)),
        HorizontalAlignment.CENTER, true, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Five Star / Higher Hotel Or Resort");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 20  ($5.50)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Four Star Hotel Or Resort");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 15  ($4.10)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Three / Two Star Hotel Or Resort");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 10  ($2.75)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("One Star Hotel / Budget Hotel");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 7  ($2.00)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Deluxe Hotel Apartment");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 20  ($5.50)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Superior Hotel Apartment");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 15  ($4.10)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Standard Hotel Apartment");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 10  ($2.75)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    sheetIndexrow0.setHeight((short) 300);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("ABU DHABI MUNICIPALITY FEE");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptYello, HorizontalAlignment.CENTER, true, false, (short) 13, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    sheetIndexrow0.setHeight((short) 700);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue(
        "Municipality Room fee will be applied for any stay. This is in accordance to the resolution number 15 from the Executive Council of Abu Dhabi for 2016. The fee will be collected from the guest directly at the hotel.");
    cell.setCellStyle(
        ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle, IndexedColors.BLACK.getIndex(),
            rptLightYellow, HorizontalAlignment.LEFT, false, true, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);
    /*
     * int formatindex = 0; String obj = "10.00 AED"; int formatCnt = obj.length(); int pos = 0;
     * boolean status = true; do { formatindex = cell.getStringCellValue().indexOf(obj, pos);
     * if(formatindex >= 0) { Font font = xSSFWorkbook.createFont(); font.setBold(true);
     * font.setColor(IndexedColors.RED.getIndex());
     * cell.getRichStringCellValue().applyFont(formatindex, formatindex+formatCnt, font); pos =
     * formatindex + formatCnt; status = true; } else { status = false; break; } } while(status);
     */

    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("All Hotel / Apartments - Fee Per Room Per Night");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));

    cell = sheetIndexrow0.createCell((short) 2);
    cell.setCellValue("AED 10  ($2.75)");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.BLACK.getIndex(), new XSSFColor(new Color(242, 242, 242)),
        HorizontalAlignment.CENTER, false, false, (short) 12, "Arial"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 5), sheetIndex,
        xSSFWorkbook);


    lastLine = lastLine + 1;
    sheetIndexrow0 = sheetIndex.createRow(lastLine);
    cell = sheetIndexrow0.createCell((short) 1);
    cell.setCellValue("Back to top");
    cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
        IndexedColors.YELLOW.getIndex(), new XSSFColor(new Color(192, 0, 0)),
        HorizontalAlignment.CENTER, true, false, (short) 22, "Bookman Old Style"));
    ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 5), sheetIndex,
        xSSFWorkbook);
    sName = "'Index'";
    Hyperlink hyperLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
    hyperLink.setAddress(sName + "!A1");
    cell.setHyperlink(hyperLink);

    return lastLine;
  }

  /** Currently Not Using */
  @SuppressWarnings({"unchecked", "unused", "unlikely-arg-type"})
  private void generateRateSheetByParty(XSSFWorkbook xSSFWorkbook, CellStyle cellStyle,
      final RateSheetDto rateSheetDto, String sName, int rowNum)
      throws Exception {}

  @SuppressWarnings({"unchecked", "unused", "unlikely-arg-type"})
  private void generateRateSheetByPartyNew(XSSFWorkbook xSSFWorkbook, CellStyle cellStyle,
      final RateSheetDto rateSheetDto, String sName, int rowNum) throws Exception {

    String payMode = "Selling Rates";
    CreationHelper createHelper = xSSFWorkbook.getCreationHelper();
    XSSFColor color1 = new XSSFColor(new Color(51, 63, 79));
    XSSFColor color2 = new XSSFColor(new Color(221, 235, 247));
    XSSFColor color3 = new XSSFColor(new Color(252, 228, 214));
    XSSFColor color4 = new XSSFColor(new Color(237, 237, 237));
    XSSFColor color5 = new XSSFColor(new Color(31, 78, 120));
    // XSSFColor color6 = new XSSFColor(new Color(252, 228, 214));
    XSSFColor color7 = new XSSFColor(new Color(224, 224, 224));
    XSSFColor color8 = new XSSFColor(new Color(250, 191, 143));
    XSSFColor color9 = new XSSFColor(new Color(255, 255, 255));
    XSSFColor lightGray = new XSSFColor(new Color(211, 211, 211));

    Map<String, Object> resultSetMap = rateSheetDAO.getRateSheetTables(rateSheetDto);
    int lastLine = 0;
    Object dtHotelDetails = resultSetMap.get("9");
    Object dtContarctPromoDetails = resultSetMap.get("10");
    Object dtRatesAll = resultSetMap.get("11");
    Object dtTacticalOffers = resultSetMap.get("12");
    Sheet sheet = null;

    String sheetName = CommonUtil.nullRemove(sName);
    sheet = xSSFWorkbook.createSheet(sheetName);
    sheet.setDisplayGridlines(false);
    /** Row 0 Start **/
    Row row0 = sheet.createRow(lastLine);
    row0.setHeight((short) 500);
    Cell cell = row0.createCell((short) 0);
    cell.setCellValue("BACK TO INDEX");
    Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
    link.setAddress("'Index'!A1");
    cell.setHyperlink(link);
    cell.setCellStyle(backIndexStyle);
    if (dtHotelDetails != null) {
      List<List<String>> dtHotelDetailsList = (List<List<String>>) dtHotelDetails;
      if (dtHotelDetailsList != null && !dtHotelDetailsList.isEmpty()
          && dtHotelDetailsList.size() > 1) {
        List<String> dtHotelDetailsHeadArr = dtHotelDetailsList.get(1);
        if (dtHotelDetailsHeadArr != null && !dtHotelDetailsHeadArr.isEmpty()) {

          cell = row0.createCell((short) 1);
          cell.setCellValue(dtHotelDetailsHeadArr.get(1).trim().toUpperCase());
          cell.setCellStyle(mainHeaderStyle);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 10),
              sheet, xSSFWorkbook);

          /** Row 0 End **/
          /** Row 1 Start **/
          lastLine = lastLine + 1;
          Row row1 = sheet.createRow(lastLine);
          cell = row1.createCell((short) 0);
          row0.setHeight((short) 500);
          cell.setCellValue("BACK TO TOP");
          link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
          link.setAddress("'" + sheetName + "'!A" + 3);
          cell.setHyperlink(link);
          cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
              IndexedColors.YELLOW.getIndex(), new XSSFColor(new Color(192, 0, 0)),
              HorizontalAlignment.CENTER, true, false, (short) 12, "Arial"));

          cell = row1.createCell((short) 1);
          cell.setCellValue("     STAR CATEGORY: "
              + CommonUtil.nullRemove(dtHotelDetailsHeadArr.get(3)).trim() + "          LOCATION: "
              + CommonUtil.nullRemove(dtHotelDetailsHeadArr.get(5)).trim() + ", "
              + CommonUtil.nullRemove(dtHotelDetailsHeadArr.get(7)).trim() + "            WEBSITE: "
              + CommonUtil.nullRemove(dtHotelDetailsHeadArr.get(8)).trim()
              + "                 TEL: "
              + CommonUtil.nullRemove(dtHotelDetailsHeadArr.get(9)).trim());
          cell.setCellStyle(mainSubHeaderStyle);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 10),
              sheet, xSSFWorkbook);
          sheet.createFreezePane(0, 2);
          /** Row 1 End **/

          /** Row 2 Start **/
          lastLine = lastLine + 1;
          Row row2 = sheet.createRow(lastLine);
          cell = row2.createCell((short) 1);
          cell.setCellValue("STAY VALIDTITY");
          cell.setCellStyle(row2CellStyle);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 2), sheet,
              xSSFWorkbook);
          cell = row2.createCell((short) 3);
          cell.setCellValue("CONTRACTED (CO) & SPECIAL PROMOTIONS (SPO)");
          cell.setCellStyle(row2CellStyle);
          /** Row 2 End **/

          /** Row 3 Start **/
          lastLine = lastLine + 1;
          Row row3 = sheet.createRow(lastLine);
          cell = row3.createCell((short) 1);
          cell.setCellValue("FROM");
          cell.setCellStyle(row2CellStyle);
          cell = row3.createCell((short) 2);
          cell.setCellValue("TO");
          cell.setCellStyle(row2CellStyle);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(2, 3, 3, 10), sheet,
              xSSFWorkbook);

        }
      }
      dtHotelDetails = null;
      /** Row 3 End **/
    }
    /** Dynamic Row 4 Start **/
    List<List<String>> dtContarctPromoDetailsList = null;
    Map<String, Integer> offerHyperLink = new HashMap<>();
    Set<String> offerUniqueLink = new HashSet<>();
    int offerRandomNum = 1;
    int offerUniqueNum = 1;
    if (dtContarctPromoDetails != null) {
      dtContarctPromoDetailsList = (List<List<String>>) dtContarctPromoDetails;
      if (dtContarctPromoDetailsList != null && !dtContarctPromoDetailsList.isEmpty()) {
        CellStyle dtContarctPromoDetailsListStyle4 = ExcelUtil.getCellStyleForContent(xSSFWorkbook,
            cellStyle, IndexedColors.BLACK.getIndex(), color4, HorizontalAlignment.CENTER, true);
        CellStyle dtContarctPromoDetailsListStyle9 = ExcelUtil.getCellStyleForContent(xSSFWorkbook,
            cellStyle, IndexedColors.BLACK.getIndex(), color9, HorizontalAlignment.CENTER, true);
        for (int i = 1; i <= dtContarctPromoDetailsList.size() - 1; i++) {
          List<String> dtHotelDetailsArr = dtContarctPromoDetailsList.get(i);
          lastLine = lastLine + 1;
          Row dynamicrow = sheet.createRow(lastLine);
          cell = dynamicrow.createCell((short) 1);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(1)).trim());
          cell.setCellStyle(dtContarctPromoDetailsListStyle4);
          cell = dynamicrow.createCell((short) 2);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(2)).trim());
          cell.setCellStyle(dtContarctPromoDetailsListStyle4);
          cell = dynamicrow.createCell((short) 3);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase());
          cell.setCellStyle(dtContarctPromoDetailsListStyle9);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 3, 10),
              sheet, xSSFWorkbook);
          if (offerHyperLink
              .containsKey(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase())) {
            offerHyperLink.put(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase()
                + "-" + offerRandomNum, lastLine);
            offerRandomNum++;
          } else {
            offerHyperLink.put(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase(),
                lastLine);
          }
        }
      }
      dtContarctPromoDetails = null;
    }

    int columnSize_1 = 0;
    /** Dynamic Table 1 Start **/
    lastLine = lastLine + 1;
    if (dtRatesAll != null && (List<List<String>>) dtRatesAll != null) {
      if (dtContarctPromoDetailsList != null && !dtContarctPromoDetailsList.isEmpty()) {
        for (int proIndex = 1; proIndex <= dtContarctPromoDetailsList.size() - 1; proIndex++) {
          List<Integer> hideList = new ArrayList<>();
          int contractLink = lastLine + 3;
          int specialLink = lastLine + 4;
          int cancelLink = lastLine + 5;
          int checkInOutLink = lastLine + 6;
          int generalLink = lastLine + 7;
          int linkLastLine = lastLine + 7;

          List<List<String>> dtRatesAllList = (List<List<String>>) dtRatesAll;
          List<List<String>> dvRates = new ArrayList<>();
          List<String> dtColumns = new ArrayList<>();
          for (int dtRates = 0; dtRates < dtRatesAllList.size(); dtRates++) {
            List<String> obj = dtRatesAllList.get(dtRates);
            if (dtRates == 0) {
              dtColumns.addAll(obj);
            }

            if (dtRates > 0) {
              List<String> dtHotelDetailsArr = dtContarctPromoDetailsList.get(proIndex);
              if (CommonUtil.nullRemove(obj.get(0)).equalsIgnoreCase(dtHotelDetailsArr.get(6))) {
                dvRates.add(obj);
              }
            }

          }

          List<List<String>> dtTacticalOffersList = (List<List<String>>) dtTacticalOffers;
          List<List<String>> dvTacticalList = new ArrayList<>();
          for (int dtRates = 1; dtRates <= dtTacticalOffersList.size() - 1; dtRates++) {
            List<String> obj = dtTacticalOffersList.get(dtRates);
            List<String> dtHotelDetailsArrCP = dtContarctPromoDetailsList.get(proIndex);
            if (CommonUtil.nullRemove(obj.get(0)).equalsIgnoreCase(dtHotelDetailsArrCP.get(6))) {
              dvTacticalList.add(obj);
            }
          }

          lastLine = lastLine + 1;

          int iNoOfColumn = dtColumns.size();
          int iNoOfColumn_R = 0;
          int iNoOfColumn_OC = 0;
          int iNoOfColumn_CS = 0;
          int iNoOfRColumn_CE = 0;

          for (String columnName : dtColumns) {
            if (columnName.contains("R@")) {
              iNoOfColumn_R = iNoOfColumn_R + 1;
            }
            if (columnName.contains("OC@")) {
              iNoOfColumn_OC = iNoOfColumn_OC + 1;
            }
            if (columnName.contains("CS@")) {
              iNoOfColumn_CS = iNoOfColumn_CS + 1;
            }
            if (columnName.contains("CE@")) {
              iNoOfRColumn_CE = iNoOfRColumn_CE + 1;
            }
          }
          lastLine = lastLine + 1;
          /** Static column Start */

          int offerLineSize = iNoOfColumn - 4;
          offerLineSize = Math.round(offerLineSize / 2);

          Row dynamicrow1 = sheet.createRow(lastLine);
          cell = dynamicrow1.createCell((short) 0);
          cell.setCellValue("STAY FROM (MM/DD/YYYY)");
          cell.setCellStyle(ExcelUtil.getCellStyleForContentGrayWithBlue(xSSFWorkbook));
          cell.setCellStyle(hColor4C);

          cell = dynamicrow1.createCell((short) 1);
          cell.setCellValue("STAY TO (MM/DD/YYYY)");
          cell.setCellStyle(hColor4C);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 2), sheet,
              xSSFWorkbook);

          cell = dynamicrow1.createCell((short) 3);
          cell.setCellValue("OFFER TITTLE");
          cell.setCellStyle(hColor4C);
          ExcelUtil.frameMergedWithThinBorder(
              new CellRangeAddress(lastLine, lastLine, 3, offerLineSize), sheet, xSSFWorkbook);

          cell = dynamicrow1.createCell((short) offerLineSize + 1);
          cell.setCellValue("MARKETS APPLICABLE");
          cell.setCellStyle(hColor4L);
          ExcelUtil.frameMergedWithThinBorder(
              new CellRangeAddress(lastLine, lastLine, offerLineSize + 1, (iNoOfColumn - 2)), sheet,
              xSSFWorkbook);

          /** Dynamic column Start */
          lastLine = lastLine + 1;
          List<String> dtHotelDetailsArr = dtContarctPromoDetailsList.get(proIndex);
          Row dynamicrow2 = sheet.createRow(lastLine);
          dynamicrow2.setHeight((short) 600);
          cell = dynamicrow2.createCell((short) 0);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(1)));
          // cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(dtHotelDetailsArr.get(1)),
          // "dd/MM/yyyy", DateUtil.DATE_MONTH_STRING_YEAR_SLASH_PATTERN));
          cell.setCellStyle(hWColor5C);

          cell = dynamicrow2.createCell((short) 1);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(2)));
          // cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(dtHotelDetailsArr.get(2)),
          // "dd/MM/yyyy", DateUtil.DATE_MONTH_STRING_YEAR_SLASH_PATTERN));
          cell.setCellStyle(hWColor5C);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 2), sheet,
              xSSFWorkbook);

          cell = dynamicrow2.createCell((short) 3);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).toUpperCase());
          cell.setCellStyle(hWColor5C);
          ExcelUtil.frameMergedWithThinBorder(
              new CellRangeAddress(lastLine, lastLine, 3, offerLineSize), sheet, xSSFWorkbook);

          cell = dynamicrow2.createCell((short) offerLineSize + 1);
          cell.setCellValue(CommonUtil.nullRemove(dtHotelDetailsArr.get(5)));
          cell.setCellStyle(hWColor5L);
          ExcelUtil.frameMergedWithThinBorder(
              new CellRangeAddress(lastLine, lastLine, offerLineSize + 1, (iNoOfColumn - 2)), sheet,
              xSSFWorkbook);

          /** Offer hyperLink Set */
          if (offerUniqueLink
              .contains(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase())) {
            if (offerHyperLink
                .containsKey(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase()
                    + "-" + offerUniqueNum)) {
              int offerLine = offerHyperLink
                  .get(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase() + "-"
                      + offerUniqueNum);
              link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
              link.setFirstColumn(0);
              link.setLastColumn(22);
              link.setFirstRow(lastLine + 1);
              link.setLastRow(lastLine + 1);
              link.setAddress("'" + sheetName + "'!A" + (lastLine + 1) + ":V" + (lastLine + 1));
              sheet.getRow(offerLine).getCell(3).setHyperlink(link);
              offerUniqueLink
                  .add(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase() + "-"
                      + offerUniqueNum);
              offerUniqueNum++;
            } else {
              int offerLine = offerHyperLink
                  .get(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase());
              link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
              link.setFirstColumn(0);
              link.setLastColumn(22);
              link.setFirstRow(lastLine + 1);
              link.setLastRow(lastLine + 1);
              link.setAddress("'" + sheetName + "'!A" + (lastLine + 1) + ":V" + (lastLine + 1));
              sheet.getRow(offerLine).getCell(3).setHyperlink(link);
              offerUniqueLink
                  .add(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase());

            }
          } else {
            int offerLine = offerHyperLink
                .get(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase());
            link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
            link.setFirstColumn(0);
            link.setLastColumn(22);
            link.setFirstRow(lastLine + 1);
            link.setLastRow(lastLine + 1);
            link.setAddress("'" + sheetName + "'!A" + (lastLine + 1) + ":V" + (lastLine + 1));
            sheet.getRow(offerLine).getCell(3).setHyperlink(link);
            offerUniqueLink
                .add(CommonUtil.nullRemove(dtHotelDetailsArr.get(0)).trim().toUpperCase());
          }

          /** dynamic Rate column declare */
          if (dvRates != null && !dvRates.isEmpty()) {
            lastLine = lastLine + 1;
            Row dynamicrow3 = sheet.createRow(lastLine);
            cell = dynamicrow3.createCell((short) 0);
            cell.setCellValue(CommonUtil.nullRemove("PERIOD & ROOM DETAILS").toUpperCase());
            cell.setCellStyle(hDBColor2C);
            ExcelUtil.frameMergedWithThinBorder(
                new CellRangeAddress(lastLine, lastLine, 0, iNoOfColumn_R - 1), sheet,
                xSSFWorkbook);
            cell = dynamicrow3.createCell((short) iNoOfColumn_R);
            cell.setCellValue(CommonUtil.nullRemove("ADULT OCCUPANCY RATES").trim().toUpperCase());
            cell.setCellStyle(hDBColor2C);
            if (iNoOfColumn_R < iNoOfColumn_R + iNoOfColumn_OC - 1) {
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine,
                  iNoOfColumn_R, iNoOfColumn_R + iNoOfColumn_OC - 1), sheet, xSSFWorkbook);
            }
            cell = dynamicrow3.createCell((short) iNoOfColumn_R + iNoOfColumn_OC);
            cell.setCellValue(CommonUtil.nullRemove("CHILD SHARING").trim().toUpperCase());
            cell.setCellStyle(hDBColor2C);
            if (iNoOfColumn_R + iNoOfColumn_OC < iNoOfColumn_R + iNoOfColumn_OC + iNoOfColumn_CS
                - 1) {
              ExcelUtil
                  .frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, iNoOfColumn_R + iNoOfColumn_OC,
                          iNoOfColumn_R + iNoOfColumn_OC + iNoOfColumn_CS - 1),
                      sheet, xSSFWorkbook);
            }

            cell = dynamicrow3.createCell((short) iNoOfColumn_R + iNoOfColumn_OC + iNoOfColumn_CS);
            cell.setCellValue(CommonUtil.nullRemove("CHILD ON EXTRA BED").trim().toUpperCase());
            cell.setCellStyle(hDBColor2C);
            if (iNoOfColumn_R + iNoOfColumn_OC + iNoOfColumn_CS < iNoOfColumn_R + iNoOfColumn_OC
                + iNoOfColumn_CS + iNoOfRColumn_CE - 1) {
              ExcelUtil.frameMergedWithThinBorder(
                  new CellRangeAddress(lastLine, lastLine,
                      iNoOfColumn_R + iNoOfColumn_OC + iNoOfColumn_CS,
                      iNoOfColumn_R + iNoOfColumn_OC + iNoOfColumn_CS + iNoOfRColumn_CE - 1),
                  sheet, xSSFWorkbook);
            }
            String columnColorChange = "";
            lastLine = lastLine + 1;
            Row dynamicrow4 = sheet.createRow(lastLine);
            dynamicrow4.setHeight((short) 500);
            int columnIndex = 0;
            for (String columnName : dtColumns) {
              if (columnIndex > 0) {
                String columnNameRates =
                    CommonUtil.nullRemove(columnName).replace("R@", "").replace("OC@", "")
                        .replace("CS@", "").replace("CE@", "").replace("_", " ").toUpperCase();


                if ("STAY FROM".equalsIgnoreCase(columnNameRates)
                    || "STAY TO".equalsIgnoreCase(columnNameRates)) {
                  columnNameRates = columnNameRates + " (DD/MM/YYYY)";
                }
                cell = dynamicrow4.createCell((short) columnIndex - 1);
                cell.setCellValue(columnNameRates);
                cell.setCellStyle(ratesColumnStyle);
              }
              columnIndex++;
            }

            XSSFColor colorOddEven = color3;
            XSSFColor colorOddEven1 = color2;
            XSSFColor colorOddEven2 = color3;
            String existRoomName = "";
            boolean condColor = false;
            for (List<String> dvRatesList : dvRates) {
              lastLine = lastLine + 1;

              if (!existRoomName
                  .equalsIgnoreCase(CommonUtil.nullRemove(dvRatesList.get(1)).toUpperCase()
                      + CommonUtil.nullRemove(dvRatesList.get(2)).toUpperCase())) {
                colorOddEven = colorOddEven1;
                if (!condColor) {
                  colorOddEven = colorOddEven2;
                }
                condColor = true;
              } else {
                if (condColor) {
                  if (colorOddEven != colorOddEven1) {
                    colorOddEven = colorOddEven2;
                  } else {
                    condColor = false;
                    colorOddEven = colorOddEven1;
                  }
                }
              }

              Row dynamicrow5 = sheet.createRow(lastLine);
              for (int i = 1; i <= dvRatesList.size() - 1; i++) {
                cell = dynamicrow5.createCell((short) i - 1);
                cell.getCellStyle().setWrapText(true);
                String value = "";
                value = CommonUtil.nullRemove(dvRatesList.get(i));
                if (3 == i || 4 == i) {
                  if (CommonUtil.nullRemove(dvRatesList.get(i)).contains("-")) {
                    value = DateUtil.getFormattedDate(CommonUtil.nullRemove(dvRatesList.get(i)),
                        DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy");
                  }
                  if (CommonUtil.nullRemove(dvRatesList.get(i)).contains("/")) {
                    value = DateUtil.getFormattedDate(CommonUtil.nullRemove(dvRatesList.get(i)),
                        DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN, "dd/MM/yyyy");
                  }
                }

                if (value != null && !value.isEmpty()) {
                  if ("".equalsIgnoreCase(value)) {
                    value = "NA";
                  }
                  if ("-3.00".equalsIgnoreCase(value)) {
                    value = "Free";
                  }
                  if ("-1.00".equalsIgnoreCase(value)) {
                    value = "Inc";
                  }
                  if ("-4.00".equalsIgnoreCase(value) || "-7.00".equalsIgnoreCase(value)
                      || "-12.00".equalsIgnoreCase(value)) {
                    value = "NA";
                  }
                  if ("0.00".equalsIgnoreCase(value)) {
                    value = "NA";
                  }

                  if (6 < i) {
                    if (!value.equalsIgnoreCase("NA") && !value.equalsIgnoreCase("Free")
                        && !value.equalsIgnoreCase("Inc")) {
                      cell.setCellValue(Math.round(Double.parseDouble(value)));
                    } else {
                      cell.setCellValue(value);
                    }
                  } else {
                    cell.setCellValue(value);
                  }
                  if (columnSize_1 < value.length()) {
                    columnSize_1 = value.length();
                  }
                } else {
                  cell.setCellValue("NA");
                }

                if (0 == i - 1 || 1 == i - 1) {
                  HorizontalAlignment alignCode = HorizontalAlignment.CENTER;
                  if (0 == i - 1) {
                    alignCode = HorizontalAlignment.LEFT;
                    cell.getCellStyle().setAlignment(alignCode);
                    /*
                     * cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
                     * IndexedColors.BLACK.getIndex(), colorOddEven, alignCode, false, false,
                     * (short)11, "Calibri"));
                     */
                  }
                  if (1 == i - 1) {
                    alignCode = HorizontalAlignment.CENTER;
                    cell.getCellStyle().setAlignment(alignCode);
                    /*
                     * cell.setCellStyle(ExcelUtil.getCellStyleForContent(xSSFWorkbook, cellStyle,
                     * IndexedColors.BLACK.getIndex(), colorOddEven, alignCode, false, true,
                     * (short)11, "Calibri"));
                     */
                  }
                  existRoomName = CommonUtil.nullRemove(dvRatesList.get(1)).toUpperCase()
                      + CommonUtil.nullRemove(dvRatesList.get(2)).toUpperCase();
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) i - 1, (short) i - 1),
                      sheet);
                } else {
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) i - 1, (short) i - 1),
                      sheet);
                  cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                  // cell.setCellStyle(ratesValStyle);
                }

                // if(dvRatesList.size()-1)
              }

            }
          }


          /** Tactical Start */

          if (dvTacticalList != null && !dvTacticalList.isEmpty()) {
            lastLine = lastLine + 1;
            Row dynamicrow6 = sheet.createRow(lastLine);

            cell = dynamicrow6.createCell((short) 0);
            cell.setCellValue("Booking Validity Option");
            cell.setCellStyle(tacticalHeaderStyle);

            cell = dynamicrow6.createCell((short) 1);
            cell.setCellValue("Minimum Length of Stay");
            cell.setCellStyle(tacticalHeaderStyle);

            cell = dynamicrow6.createCell((short) 2);
            cell.setCellValue("Booking Code");
            cell.setCellStyle(tacticalHeaderStyle);

            cell = dynamicrow6.createCell((short) 3);
            cell.setCellValue("Room Types");
            cell.setCellStyle(tacticalHeaderStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 3, 5),
                sheet, xSSFWorkbook);

            cell = dynamicrow6.createCell((short) 6);
            cell.setCellValue("Meal Plan");
            cell.setCellStyle(tacticalHeaderStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 6, 7),
                sheet, xSSFWorkbook);

            cell = dynamicrow6.createCell((short) 8);
            cell.setCellValue("Accomodation Category");
            cell.setCellStyle(tacticalHeaderStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 8, 9),
                sheet, xSSFWorkbook);

            cell = dynamicrow6.createCell((short) 10);
            cell.setCellValue("Apply on");
            cell.setCellStyle(tacticalHeaderStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 10, 12),
                sheet, xSSFWorkbook);

            if (CommonUtil.nullRemove(dvTacticalList.get(0).get(19)).contains("Free Nights")) {
              cell = dynamicrow6.createCell((short) 13);
              cell.setCellValue("Stay For");
              cell.setCellStyle(tacticalHeaderStyle);

              cell = dynamicrow6.createCell((short) 14);
              cell.setCellValue("Pay For");
              cell.setCellStyle(tacticalHeaderStyle);

              cell = dynamicrow6.createCell((short) 15);
              cell.setCellValue("Max Free Nights");
              cell.setCellStyle(tacticalHeaderStyle);

              cell = dynamicrow6.createCell((short) 16);
              cell.setCellValue("Free Nights Apply to");
              cell.setCellStyle(tacticalHeaderStyle);

              cell = dynamicrow6.createCell((short) 17);
              cell.setCellValue(" Multiple Stay");
              cell.setCellStyle(tacticalHeaderStyle);

              cell = dynamicrow6.createCell((short) 18);
              cell.setCellValue("Max Multiples");
              cell.setCellStyle(tacticalHeaderStyle);

              cell = dynamicrow6.createCell((short) 19);
              cell.setCellValue("Remarks");
              cell.setCellStyle(tacticalHeaderStyle);
            } else {
              cell = dynamicrow6.createCell((short) 13);
              cell.setCellValue("Remarks");
              cell.setCellStyle(tacticalHeaderStyle);
            }


            for (int tacIndex = 0; tacIndex < dvTacticalList.size(); tacIndex++) {
              List<String> dvTactical = dvTacticalList.get(tacIndex);
              if (dvTactical != null && !dvTactical.isEmpty()) {
                lastLine = lastLine + 1;
                Row dynamicrow7 = sheet.createRow(lastLine);
                cell = dynamicrow7.createCell((short) 0);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(5)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 0, (short) 0), sheet);
                // cell.setCellStyle(tacticalvalueStyle);

                cell = dynamicrow7.createCell((short) 1);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(6)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 1, (short) 1), sheet);
                // cell.setCellStyle(tacticalvalueStyle);

                cell = dynamicrow7.createCell((short) 2);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(7)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 2, (short) 2), sheet);
                // cell.setCellStyle(tacticalvalueStyle);

                cell = dynamicrow7.createCell((short) 3);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(8)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 3, (short) 3), sheet);
                // cell.setCellStyle(tacticalvalueStyle);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 3, 5),
                    sheet, xSSFWorkbook);

                cell = dynamicrow7.createCell((short) 6);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(9)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 6, (short) 6), sheet);
                // cell.setCellStyle(tacticalvalueStyle);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 6, 7),
                    sheet, xSSFWorkbook);

                cell = dynamicrow7.createCell((short) 8);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(10)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 8, (short) 8), sheet);
                // cell.setCellStyle(tacticalvalueStyle);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 8, 9),
                    sheet, xSSFWorkbook);

                cell = dynamicrow7.createCell((short) 10);
                cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(17)));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 10, (short) 10), sheet);
                // cell.setCellStyle(tacticalvalueStyle);
                ExcelUtil.frameMergedWithThinBorder(
                    new CellRangeAddress(lastLine, lastLine, 10, 12), sheet, xSSFWorkbook);

                if (CommonUtil.nullRemove(dvTacticalList.get(tacIndex).get(19))
                    .contains("Free Nights")) {
                  cell = dynamicrow7.createCell((short) 13);
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 13, (short) 13), sheet);
                  // cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(11)));
                  cell.setCellStyle(tacticalvalueStyle);

                  cell = dynamicrow7.createCell((short) 14);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(12)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 14, (short) 14), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);

                  cell = dynamicrow7.createCell((short) 15);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(13)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 15, (short) 15), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);

                  cell = dynamicrow7.createCell((short) 16);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(14)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 16, (short) 16), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);

                  cell = dynamicrow7.createCell((short) 17);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(15)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 17, (short) 17), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);

                  cell = dynamicrow7.createCell((short) 18);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(16)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 18, (short) 18), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);

                  cell = dynamicrow7.createCell((short) 19);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(18)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 19, (short) 19), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);
                } else {
                  cell = dynamicrow7.createCell((short) 13);
                  cell.setCellValue(CommonUtil.nullRemove(dvTactical.get(18)));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(lastLine, lastLine, (short) 13, (short) 13), sheet);
                  // cell.setCellStyle(tacticalvalueStyle);
                }

              }

            }

          }
          /** remarks */
          String remarks = CommonUtil.nullRemove(dtHotelDetailsArr.get(9));
          if (!remarks.isEmpty()) {
            lastLine = lastLine + 2;
            Row dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("Remarks");
            cell.setCellStyle(remarksColor5Style);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 12),
                sheet, xSSFWorkbook);

            lastLine = lastLine + 1;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue(remarks);
            cell.setCellStyle(remarksColor9Style);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 12),
                sheet, xSSFWorkbook);
            dynamicrowRemarks.setHeight((short) 1000);

          }

          /** ########### TaxNoteTable ########### */

          lastLine = lastLine + 1;
          Row dynamicrowRemarks = sheet.createRow(lastLine);
          cell = dynamicrowRemarks.createCell((short) 0);
          cell.setCellValue("ALL RATES ARE IN "
              + rateSheetDAO.getCurrencyCodeFromAgentMast(rateSheetDto.getAgentCode()));
          cell.setCellStyle(taxNotablelightStyleL);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 11),
              sheet, xSSFWorkbook);

          lastLine = lastLine + 1;
          dynamicrowRemarks = sheet.createRow(lastLine);
          cell = dynamicrowRemarks.createCell((short) 0);
          cell.setCellValue("RATES ARE INCLUSIVE OF ALL TAXES INCLUDING VAT");
          cell.setCellStyle(taxNotablelightStyleL);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 11),
              sheet, xSSFWorkbook);

          lastLine = lastLine + 1;
          dynamicrowRemarks = sheet.createRow(lastLine);
          cell = dynamicrowRemarks.createCell((short) 0);
          cell.setCellValue(
              "RATES DOES NOT INCLUDE TOURISM DIRHAM FEE WHICH IS TO BE PAID BY THE GUEST DIRECTLY AT THE HOTEL");
          cell.setCellStyle(taxNotablelightStyleL);
          ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 10),
              sheet, xSSFWorkbook);

          /** ##################### Maximum Occupancy ##################### */
          List<List<String>> dtMaxOccupancy =
              rateSheetDAO.getMaximumOccupancyData(CommonUtil.nullRemove(dtHotelDetailsArr.get(7)),
                  CommonUtil.nullRemove(dtHotelDetailsArr.get(6)), rateSheetDto.getPartyCode());
          if (dtMaxOccupancy != null && dtMaxOccupancy.size() > 1) {
            lastLine = lastLine + 2;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("MAX OCCUPANCY");
            cell.setCellStyle(taxNotableColor5StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 8),
                sheet, xSSFWorkbook);

            lastLine = lastLine + 1;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("ROOM CATEGORY");
            cell.setCellStyle(taxNotableColor2StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine + 1, 0, 2),
                sheet, xSSFWorkbook);

            cell = dynamicrowRemarks.createCell((short) 3);
            cell.setCellValue("Max Occupancy");
            cell.setCellStyle(taxNotableColor2StyleC);

            cell = dynamicrowRemarks.createCell((short) 4);
            cell.setCellValue("Existing Bed Occupancy");
            cell.setCellStyle(taxNotableColor2StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 4, 6),
                sheet, xSSFWorkbook);

            cell = dynamicrowRemarks.createCell((short) 7);
            cell.setCellValue("Additional Pax");
            cell.setCellStyle(taxNotableColor2StyleC);

            cell = dynamicrowRemarks.createCell((short) 8);
            cell.setCellValue("Allowed Combinations(Adult + child)");
            cell.setCellStyle(taxNotableColor2StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine + 1, 8, 8),
                sheet, xSSFWorkbook);

            lastLine = lastLine + 1;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 3);
            cell.setCellValue("Total Pax allowed in Room");
            cell.setCellStyle(taxNotableColor3StyleC);

            cell = dynamicrowRemarks.createCell((short) 4);
            cell.setCellValue("No. of Adults");
            cell.setCellStyle(taxNotableColor3StyleC);

            cell = dynamicrowRemarks.createCell((short) 5);
            cell.setCellValue("No. of Child");
            cell.setCellStyle(taxNotableColor3StyleC);

            cell = dynamicrowRemarks.createCell((short) 6);
            cell.setCellValue("Unit Room");
            cell.setCellStyle(taxNotableColor3StyleC);

            cell = dynamicrowRemarks.createCell((short) 7);
            cell.setCellValue("Extra Bed Allowed");
            cell.setCellStyle(taxNotableColor3StyleC);

            for (int i = 1; i <= dtMaxOccupancy.size() - 1; i++) {
              List<String> dtMaxOccupanObj = dtMaxOccupancy.get(i);
              if (!dtMaxOccupanObj.isEmpty()) {
                lastLine = lastLine + 1;
                dynamicrowRemarks = sheet.createRow(lastLine);
                cell = dynamicrowRemarks.createCell((short) 0);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(4)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 0, (short) 0), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 2),
                    sheet, xSSFWorkbook);

                cell = dynamicrowRemarks.createCell((short) 3);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(5)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 3, (short) 3), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);

                cell = dynamicrowRemarks.createCell((short) 4);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(6)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 4, (short) 4), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);

                cell = dynamicrowRemarks.createCell((short) 5);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(7)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 5, (short) 5), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);

                cell = dynamicrowRemarks.createCell((short) 6);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(8)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 6, (short) 6), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);

                cell = dynamicrowRemarks.createCell((short) 7);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(9)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 7, (short) 7), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);

                cell = dynamicrowRemarks.createCell((short) 8);
                cell.setCellValue(
                    CommonUtil.nullRemove(dtMaxOccupanObj.get(10)).trim().toUpperCase());
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 8, (short) 8), sheet);
                // cell.setCellStyle(maxOccColor9StyleC);
              }
            }
            dtMaxOccupancy = null;
          }


          /** ##################### Minimum Nights ##################### */
          List<Object[]> dtMinNightsList = rateSheetDAO.getdtMinNightsData(
              rateSheetDto.getPartyCode(), CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
          if (dtMinNightsList != null && !dtMinNightsList.isEmpty()) {
            lastLine = lastLine + 2;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("MINIMUM NIGHTS");
            cell.setCellStyle(minNightColor5StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 9),
                sheet, xSSFWorkbook);

            lastLine = lastLine + 1;
            int iMinNightLine = lastLine + 1;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("Room Type");
            cell.setCellStyle(minNightColor2StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 2),
                sheet, xSSFWorkbook);

            cell = dynamicrowRemarks.createCell((short) 3);
            cell.setCellValue("Meal Plan");
            cell.setCellStyle(minNightColor2StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 3, 4),
                sheet, xSSFWorkbook);

            cell = dynamicrowRemarks.createCell((short) 5);
            cell.setCellValue("From Date");
            cell.setCellStyle(minNightColor2StyleC);

            cell = dynamicrowRemarks.createCell((short) 6);
            cell.setCellValue("To Date");
            cell.setCellStyle(minNightColor2StyleC);

            cell = dynamicrowRemarks.createCell((short) 7);
            cell.setCellValue("Min.Nights");
            cell.setCellStyle(minNightColor2StyleC);

            cell = dynamicrowRemarks.createCell((short) 8);
            cell.setCellValue("Options");
            cell.setCellStyle(minNightColor2StyleC);

            cell = dynamicrowRemarks.createCell((short) 9);
            cell.setCellValue("Week Days");
            cell.setCellStyle(minNightColor2StyleC);


            for (Object[] obj : dtMinNightsList) {
              lastLine = lastLine + 1;
              dynamicrowRemarks = sheet.createRow(lastLine);
              cell = dynamicrowRemarks.createCell((short) 0);
              cell.setCellValue(CommonUtil.nullRemove(obj[0]));
              ExcelUtil.setCellBorder(
                  new CellRangeAddress(lastLine, lastLine, (short) 0, (short) 0), sheet);
              // cell.setCellStyle(minNightColor9ValStyleC);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 2),
                  sheet, xSSFWorkbook);

              cell = dynamicrowRemarks.createCell((short) 3);
              cell.setCellValue(CommonUtil.nullRemove(obj[1]));
              ExcelUtil.setCellBorder(
                  new CellRangeAddress(lastLine, lastLine, (short) 4, (short) 4), sheet);
              // cell.setCellStyle(minNightColor9ValStyleC);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 3, 4),
                  sheet, xSSFWorkbook);

              cell = dynamicrowRemarks.createCell((short) 5);
              cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(obj[2]),
                  DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
              ExcelUtil.setCellBorder(
                  new CellRangeAddress(lastLine, lastLine, (short) 5, (short) 5), sheet);
              // cell.setCellStyle(minNightColor9ValStyleC);

              cell = dynamicrowRemarks.createCell((short) 6);
              cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(obj[3]),
                  DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
              ExcelUtil.setCellBorder(
                  new CellRangeAddress(lastLine, lastLine, (short) 6, (short) 6), sheet);
              // cell.setCellStyle(minNightColor9ValStyleC);

              cell = dynamicrowRemarks.createCell((short) 7);
              cell.setCellValue(CommonUtil.nullRemove(obj[4]));
              ExcelUtil.setCellBorder(
                  new CellRangeAddress(lastLine, lastLine, (short) 7, (short) 7), sheet);
              // cell.setCellStyle(minNightColor9ValStyleC);

              cell = dynamicrowRemarks.createCell((short) 8);
              cell.setCellValue(CommonUtil.nullRemove(obj[5]));
              ExcelUtil.setCellBorder(
                  new CellRangeAddress(lastLine, lastLine, (short) 8, (short) 8), sheet);
              // cell.setCellStyle(minNightColor9ValStyleC);
            }

            List<Object[]> weekDaysList =
                rateSheetDAO.getWeekDaysData(CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
            if (weekDaysList != null && !weekDaysList.isEmpty()) {
              int weekCount = 1;
              for (Object objects : weekDaysList) {
                if (weekCount > dtMinNightsList.size()) {
                  dynamicrowRemarks = sheet.createRow(iMinNightLine);
                  cell = dynamicrowRemarks.createCell((short) 9);
                } else {
                  cell = sheet.getRow(iMinNightLine).createCell((short) 9);
                }
                cell.setCellValue(CommonUtil.nullRemove(objects));
                // cell.setCellStyle(minNightWeekDayColor9StyleC);
                iMinNightLine++;
                weekCount++;
              }
              weekDaysList = null;
            }

            if (iMinNightLine > lastLine) {
              lastLine = iMinNightLine;
            }
            dtMinNightsList = null;
          }

          /** ##################### Special Events ##################### */

          List<Object[]> specialEventResultList = rateSheetDAO.getSpecialEventTables(rateSheetDto,
              payMode, CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
          if (!specialEventResultList.isEmpty()) {
            lastLine = lastLine + 2;
            lastLine = lastLine + 3;
            /** '--------c------- Tax inclusive Note ---------------------- */

            /** '---------------------- End -------------------- */
            int specialFirstRow = lastLine;
            dynamicrowRemarks = sheet.createRow(lastLine);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("SPECIAL EVENTS LIST");
            cell.setCellStyle(specialEvColor5StyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 12),
                sheet, xSSFWorkbook);

            Set<String> distinctSeDt = new TreeSet<>();
            specialEventResultList.stream()
                .forEach(obj -> distinctSeDt.add(CommonUtil.nullRemove(obj[2])));
            List<Integer> colList = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
              colList.add(i);
            }
            int ColNo = 6;
            while (colList.size() < 9) {
              if (hideList.indexOf(ColNo) < 0) {
                colList.add(ColNo);
              }
              ColNo++;
            }
            List<String> seEvtTitle = new ArrayList<>();
            seEvtTitle.add("Special Events Name");
            seEvtTitle.add("Special Events Date");
            seEvtTitle.add("Room Types");
            seEvtTitle.add("Meal Plans");
            seEvtTitle.add("Room Occupancy");
            seEvtTitle.add("Adult Rate");
            seEvtTitle.add("Age From");
            seEvtTitle.add("Age To");
            seEvtTitle.add("Rate");
            Iterator<String> distinctSeDtIt = distinctSeDt.iterator();
            while (distinctSeDtIt.hasNext()) {
              List<Object[]> filterDr = new ArrayList<>();
              List<Object[]> filterDt = new ArrayList<>();
              String distinctVal = distinctSeDtIt.next();
              filterDr = specialEventResultList.stream()
                  .filter(line -> distinctVal.equalsIgnoreCase(CommonUtil.nullRemove(line[2])))
                  .collect(Collectors.toList());
              filterDr.stream().forEach(obj -> filterDt.add(obj));


              lastLine = lastLine + 2;
              dynamicrowRemarks = sheet.createRow(lastLine);
              cell = dynamicrowRemarks.createCell((short) 0);
              cell.setCellValue("Compulsory Options");
              cell.setCellStyle(specialEvColor2HeaderStyleL);

              cell = dynamicrowRemarks.createCell((short) 1);
              cell.setCellValue(distinctVal);
              cell.setCellStyle(specialEvColor3HeaderStyleL);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 2),
                  sheet, xSSFWorkbook);

              lastLine = lastLine + 2;
              dynamicrowRemarks = sheet.createRow(lastLine);
              Row dynamicrowRemarks1 = sheet.createRow(lastLine + 1);
              for (int i = 1; i <= colList.size() + 1; i++) {
                if (i <= 6) {
                  cell = dynamicrowRemarks.createCell((short) i - 1);
                  cell.setCellValue(seEvtTitle.get(i - 1));
                  cell.setCellStyle(specialEvColor5ValStyleC);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine + 1, i - 1, i - 1), sheet,
                      xSSFWorkbook);
                } else if (7 == i) {
                  cell = dynamicrowRemarks.createCell((short) i - 1);
                  cell.setCellValue("Child Rate");
                  cell.setCellStyle(specialEvColor5ValStyleC);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, i - 1, i + 1), sheet, xSSFWorkbook);
                } else {
                  cell = dynamicrowRemarks1.createCell((short) i - 2);
                  cell.setCellValue(seEvtTitle.get(i - 2));
                  cell.setCellStyle(specialEvColor2ValStyleC);
                }
              }

              lastLine = lastLine + 1;
              int sEHeight = 0;
              for (Object[] obj : filterDt) {
                lastLine = lastLine + 1;
                dynamicrowRemarks = sheet.createRow(lastLine);
                cell = dynamicrowRemarks.createCell((short) 0);
                cell.setCellValue(CommonUtil.nullRemove(obj[3]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 0, (short) 0), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 1);
                cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(obj[4]),
                    DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 1, (short) 1), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 2);
                cell.setCellValue(CommonUtil.nullRemove(obj[5]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 2, (short) 2), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);
                BufferedReader bufReader =
                    new BufferedReader(new StringReader(CommonUtil.nullRemove(obj[5])));
                int genPolHeight = 0;
                while (bufReader.readLine() != null) {
                  genPolHeight++;
                }
                if (sEHeight < genPolHeight) {
                  sEHeight = genPolHeight;
                }
                dynamicrowRemarks.setHeight((short) (sEHeight * 3700));

                cell = dynamicrowRemarks.createCell((short) 3);
                cell.setCellValue(CommonUtil.nullRemove(obj[6]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 3, (short) 3), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 4);
                cell.setCellValue(CommonUtil.nullRemove(obj[7]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 4, (short) 4), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 5);
                cell.setCellValue(CommonUtil.nullRemove(obj[8]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 5, (short) 5), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 6);
                cell.setCellValue(CommonUtil.nullRemove(obj[9]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 6, (short) 6), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 7);
                cell.setCellValue(CommonUtil.nullRemove(obj[10]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 7, (short) 7), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

                cell = dynamicrowRemarks.createCell((short) 8);
                cell.setCellValue(CommonUtil.nullRemove(obj[11]));
                ExcelUtil.setCellBorder(
                    new CellRangeAddress(lastLine, lastLine, (short) 8, (short) 8), sheet);
                // cell.setCellStyle(specialEvColor9ValStyleC);

              }
            }
            specialEventResultList = null;
          }

          /**
           * ######################### CANCELLATION / NO SHOW / EARLY CHECKOUT - POLICY
           * ############################
           */
          List<Object[]> cancelPocResultList = rateSheetDAO.getCancelPolicyDetails(rateSheetDto,
              CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
          if (!cancelPocResultList.isEmpty() && cancelPocResultList.size() > 1) {
            int cnt = 0;
            cnt = lastLine + 2;
            dynamicrowRemarks = sheet.createRow(cnt);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("CANCELLATION / NO SHOW / EARLY CHECKOUT - POLICY");
            cell.setCellStyle(cancelColor5HeaderStyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 0, 16), sheet,
                xSSFWorkbook);

            cnt = cnt + 1;
            dynamicrowRemarks = sheet.createRow(cnt);
            cell = dynamicrowRemarks.createCell((short) 0);
            cell.setCellValue("Season");
            cell.setCellStyle(cancelColor2SubHeaderStyleC);


            cell = dynamicrowRemarks.createCell((short) 1);
            cell.setCellValue("Period");
            cell.setCellStyle(cancelColor2SubHeaderStyleC);

            cell = dynamicrowRemarks.createCell((short) 2);
            cell.setCellValue("Applicable Room Types");
            cell.setCellStyle(cancelColor2SubHeaderStyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 2, 4), sheet,
                xSSFWorkbook);

            cell = dynamicrowRemarks.createCell((short) 5);
            cell.setCellValue("Meal Plan");
            cell.setCellStyle(cancelColor2SubHeaderStyleC);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 5, 6), sheet,
                xSSFWorkbook);

            cell = dynamicrowRemarks.createCell((short) 7);
            cell.setCellValue("Cancellation / No Show / Early Checkout - Policy");
            cell.setCellStyle(cancelColor2SubHeaderStyleL);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 7, 16), sheet,
                xSSFWorkbook);

            int pstartRow = cnt + 1;
            int sEHeight = 0;
            for (int i = 1; i <= cancelPocResultList.size() - 1; i++) {
              cnt = cnt + 1;
              Object[] rs = cancelPocResultList.get(i);
              dynamicrowRemarks = sheet.createRow(cnt);

              cell = dynamicrowRemarks.createCell((short) 0);
              cell.setCellValue(CommonUtil.nullRemove(rs[0]));
              ExcelUtil.setCellBorder(new CellRangeAddress(cnt, cnt, (short) 0, (short) 0), sheet);
              // cell.setCellStyle(cancelColor9ValStyleC);

              cell = dynamicrowRemarks.createCell((short) 1);
              cell.setCellValue(CommonUtil.nullRemove(rs[1]).replace("\"", ""));
              ExcelUtil.setCellBorder(new CellRangeAddress(cnt, cnt, (short) 1, (short) 1), sheet);
              cell.getCellStyle().setWrapText(true);
              // cell.setCellStyle(cancelColor9ValStyleC);
              int periodEHeight = ExcelUtil.getColumnHeight(CommonUtil.nullRemove(rs[1]).replace("\"", ""));
              if (sEHeight < periodEHeight) {
                sEHeight = periodEHeight;
              }
              dynamicrowRemarks.setHeight((short) (sEHeight * 1200));
              

              cell = dynamicrowRemarks.createCell((short) 2);
              cell.setCellValue(CommonUtil.nullRemove(rs[2]));
              ExcelUtil.setCellBorder(new CellRangeAddress(cnt, cnt, (short) 2, (short) 2), sheet);
              cell.getCellStyle().setWrapText(true);
              // cell.setCellStyle(cancelColor9ValStyleC);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 2, 4), sheet,
                  xSSFWorkbook);

              cell = dynamicrowRemarks.createCell((short) 5);
              cell.setCellValue(CommonUtil.nullRemove(rs[3]));
              ExcelUtil.setCellBorder(new CellRangeAddress(cnt, cnt, (short) 5, (short) 5), sheet);
              // cell.setCellStyle(cancelColor9ValStyleC);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 5, 6), sheet,
                  xSSFWorkbook);

              cell = dynamicrowRemarks.createCell((short) 7);
              cell.setCellValue(CommonUtil.nullRemove(rs[4]).replace("\"", ""));
              List<String> formatSpecificString = new ArrayList<>();
              formatSpecificString.add("Cancellation Charges");
              formatSpecificString.add("No Show Charges");
              formatSpecificString.add("Early Checkout Charges");
              if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                int formatindex = 0;
                for (String obj : formatSpecificString) {
                  int formatCnt = obj.length();
                  int pos = 0;
                  boolean status = true;
                  do {
                    formatindex = cell.getStringCellValue().indexOf(obj, pos);
                    if (formatindex >= 0) {
                      Font font = xSSFWorkbook.createFont();
                      font.setBold(true);
                      font.setColor(IndexedColors.RED.getIndex());
                      cell.getRichStringCellValue().applyFont(formatindex, formatindex + formatCnt,
                          font);
                      pos = formatindex + formatCnt;
                      status = true;
                    } else {
                      status = false;
                      break;
                    }
                  } while (status);
                }
              }
              cell.getCellStyle().setWrapText(true);
              cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(cnt, cnt, 7, 16), sheet,
                  xSSFWorkbook);
              int genPolHeight = ExcelUtil.getColumnHeight(CommonUtil.nullRemove(rs[1]).replace("\"", ""));
              if (sEHeight < genPolHeight) {
                sEHeight = genPolHeight;
              }
              dynamicrowRemarks.setHeight((short) (sEHeight * 1200));
            }
            lastLine = cnt;
            cancelPocResultList = null;
          }

          /** #################### CheckInCheckOutPolicy ##################### */
          int OfferLastLine = lastLine;
          Map<String, Object> ds = rateSheetDAO.CheckInCheckOutPolicy(rateSheetDto.getPartyCode(),
              CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
          List<List<String>> dt = (List<List<String>>) ds.get("0");
          List<List<String>> secDt = (List<List<String>>) ds.get("1");
          if (dt != null && !dt.isEmpty()) {
            lastLine = lastLine + 2;
            for (int index = 1; index < dt.size(); index++) {
              List<String> valObj = dt.get(index);
              lastLine = lastLine + 1;
              int tableborder = lastLine;
              dynamicrowRemarks = sheet.createRow(lastLine);
              cell = dynamicrowRemarks.createCell((short) 0);
              cell.setCellValue("POLICY - CHECK-IN / CHECK-OUT");
              cell.setCellStyle(checkInColor5HeaderStyleC);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 9),
                  sheet, xSSFWorkbook);

              lastLine = lastLine + 2;
              dynamicrowRemarks = sheet.createRow(lastLine);
              cell = dynamicrowRemarks.createCell((short) 0);
              cell.setCellValue("CHECK IN TIME");
              cell.setCellStyle(checkInTimeStyle);

              cell = dynamicrowRemarks.createCell((short) 1);
              cell.setCellValue(CommonUtil.nullRemove(valObj.get(6)) + "HRS");
              cell.setCellStyle(checkInTimeStyle1);
              sheet.addMergedRegion(new CellRangeAddress(lastLine, lastLine, 1, 2));

              cell = dynamicrowRemarks.createCell((short) 3);
              cell.setCellValue("CHECK OUT TIME");
              cell.setCellStyle(checkOutTimeStyle);
              sheet.addMergedRegion(new CellRangeAddress(lastLine, lastLine, 3, 4));
              cell = dynamicrowRemarks.createCell((short) 5);
              cell.setCellValue(CommonUtil.nullRemove(valObj.get(8)) + "HRS");
              cell.setCellStyle(checkOutTimeStyle1);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 5, 6),
                  sheet, xSSFWorkbook);


              lastLine = lastLine + 2;
              int checkInRowNum = lastLine;
              Row dynamicrow = sheet.createRow(lastLine);
              cell = dynamicrow.createCell((short) 0);
              cell.setCellValue("PERIOD Applicable");
              cell.setCellStyle(checkOutPeriodTimeStyle);

              cell = dynamicrow.createCell((short) 1);
              cell.setCellValue("From");
              cell.setCellStyle(checkOutFromToStyle);

              cell = dynamicrow.createCell((short) 2);
              cell.setCellValue("Till");
              cell.setCellStyle(checkOutFromToStyle);

              cell = dynamicrow.createCell((short) 3);
              cell.setCellValue("ROOM TYPE Applicable");
              cell.setCellStyle(checkOutRoomTypeStyle);
              sheet.addMergedRegion(new CellRangeAddress(lastLine, lastLine, 3, 4));
              StringBuilder strCond = new StringBuilder();
              String strSeason = CommonUtil.nullRemove(valObj.get(2));
              if (strSeason != null && !strSeason.isEmpty()) {
                String[] ssString = strSeason.split(",");
                int ssCnt = 0;
                for (String obj : ssString) {
                  strCond.append("'" + CommonUtil.nullRemove(obj.trim()) + "'");
                  if (!(ssString.length - 1 == ssCnt)) {
                    strCond.append(", ");
                  }
                  ssCnt++;
                }
              }
              int sDate = lastLine + 1;
              List<Object[]> dateDetails = rateSheetDAO.getCheckInCheckOutDateDetails(
                  CommonUtil.nullRemove(valObj.get(3)), strCond.toString());
              if (dateDetails != null && !dateDetails.isEmpty()) {
                for (int sIndex = 0; sIndex < dateDetails.size(); sIndex++) {
                  Object[] secVal = dateDetails.get(sIndex);
                  Row sdynamicrow = sheet.createRow(sDate);
                  cell = sdynamicrow.createCell((short) 1);
                  cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(secVal[3]),
                      DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
                  ExcelUtil.setCellBorder(new CellRangeAddress(sDate, sDate, (short) 1, (short) 1),
                      sheet);
                  // cell.setCellStyle(checkOutDateValStyle);

                  cell = sdynamicrow.createCell((short) 2);
                  cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(secVal[4]),
                      DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
                  ExcelUtil.setCellBorder(new CellRangeAddress(sDate, sDate, (short) 2, (short) 2),
                      sheet);
                  // cell.setCellStyle(checkOutDateValStyle);
                  sDate = sDate + 1;
                }               
              }
              List<Object[]> roomDetails = rateSheetDAO.getCheckInCheckOutRoomDetails(
                  CommonUtil.nullRemove(valObj.get(0)), rateSheetDto.getPartyCode());
              int sCnt = lastLine;
              if (roomDetails != null && !roomDetails.isEmpty()) {
                for (int sIndex = 0; sIndex < roomDetails.size(); sIndex++) {
                  Object[] secVal = roomDetails.get(sIndex);
                  if (sDate <= sCnt) {
                    cell = sheet.createRow(checkInRowNum + sIndex).createCell((short) 5);
                  } else {
                    cell = sheet.getRow(checkInRowNum + sIndex).createCell((short) 5);
                  }
                  cell.setCellValue(CommonUtil.nullRemove(secVal[2]));
                  ExcelUtil.setCellBorder(new CellRangeAddress(checkInRowNum + sIndex,
                      checkInRowNum + sIndex, (short) 5, (short) 5), sheet);
                  // cell.setCellStyle(checkOutRoomValStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(checkInRowNum + sIndex, checkInRowNum + sIndex, 5, 9),
                      sheet, xSSFWorkbook);
                  sCnt = sCnt + 1;
                }
              }
              int dateSize = dateDetails.size();
              int roomSize = roomDetails.size();
              if (dateSize > roomSize) {
                lastLine = lastLine + dateSize;
              } else {
                lastLine = lastLine + roomSize;
              }
              lastLine = lastLine + 1;
              List<Object[]> dtRestrictDetails = rateSheetDAO
                  .getCheckInCheckOutRestrictDetails(CommonUtil.nullRemove(valObj.get(0)));
              if (dtRestrictDetails != null && !dtRestrictDetails.isEmpty()) {
                lastLine = lastLine + 1;
                dynamicrowRemarks = sheet.createRow(lastLine);
                cell = dynamicrowRemarks.createCell((short) 0);
                cell.setCellValue("RESTRICTED CHECK-IN DATES");
                cell.setCellStyle(checkOutRestictStyle);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 1),
                    sheet, xSSFWorkbook);

                cell = dynamicrowRemarks.createCell((short) 2);
                cell.setCellValue("RESTRICTED CHECK-OUT DATES");
                cell.setCellStyle(checkOutRestictStyle);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 4),
                    sheet, xSSFWorkbook);

                List<Object[]> drCheckIn = dtRestrictDetails.stream()
                    .filter(line -> "Checkin".equalsIgnoreCase(CommonUtil.nullRemove(line[2]))
                        || "Both".equalsIgnoreCase(CommonUtil.nullRemove(line[2])))
                    .collect(Collectors.toList());
                List<Object[]> drCheckOut = dtRestrictDetails.stream()
                    .filter(line -> "Checkout".equalsIgnoreCase(CommonUtil.nullRemove(line[2]))
                        || "Both".equalsIgnoreCase(CommonUtil.nullRemove(line[2])))
                    .collect(Collectors.toList());
                int resCheckInCnt = 0;
                resCheckInCnt = lastLine;
                for (Object[] obj : drCheckIn) {
                  lastLine = lastLine + 1;
                  cell = sheet.createRow(lastLine).createCell((short) 0);
                  cell.setCellValue(CommonUtil.nullRemove(obj[3]));
                  // cell.setCellStyle(checkOutRestictValStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 0, 1), sheet, xSSFWorkbook);
                }

                for (Object[] obj : drCheckOut) {
                  resCheckInCnt++;
                  if (resCheckInCnt < lastLine) {
                    cell = sheet.getRow(resCheckInCnt).createCell((short) 2);
                  } else {
                    cell = sheet.createRow(resCheckInCnt).createCell((short) 2);
                  }
                  cell.setCellValue(CommonUtil.nullRemove(obj[3]));
                  ExcelUtil.setCellBorder(
                      new CellRangeAddress(resCheckInCnt, resCheckInCnt, (short) 2, (short) 2),
                      sheet);
                  // cell.setCellStyle(checkOutRestictValStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(resCheckInCnt, resCheckInCnt, 2, 4), sheet,
                      xSSFWorkbook);
                }

                lastLine = resCheckInCnt + 1;
                dtRestrictDetails = null;
              }


              ExcelUtil.setTableBorder(new CellRangeAddress(tableborder, lastLine, 0, 9), sheet);

              lastLine = lastLine + 2;
              List<Object[]> dtEarlyCheckedDetails = rateSheetDAO
                  .getCheckInCheckOutEarlyCheckDetails(CommonUtil.nullRemove(valObj.get(0)));
              if (dtEarlyCheckedDetails != null && !dtEarlyCheckedDetails.isEmpty()) {
                tableborder = lastLine;
                dynamicrowRemarks = sheet.createRow(lastLine);
                cell = dynamicrowRemarks.createCell((short) 0);
                cell.setCellValue("POLICY - EARLY CHECK-IN / LATE CHECK-OUT");
                cell.setCellStyle(pEarlyCIHeadStyle);
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 9),
                    sheet, xSSFWorkbook);

                lastLine = lastLine + 1;
                Row ecDynamicRow = sheet.createRow(lastLine);
                cell = ecDynamicRow.createCell((short) 0);
                cell.setCellValue("Applicable Period");
                cell.setCellStyle(pEarlyCISubHeadStyle1);

                cell = ecDynamicRow.createCell((short) 1);
                cell.setCellValue("From");
                cell.setCellStyle(pEarlyCISubHeadStyle2);

                cell = ecDynamicRow.createCell((short) 2);
                cell.setCellValue("Till");
                cell.setCellStyle(pEarlyCISubHeadStyle2);

                cell = ecDynamicRow.createCell((short) 3);
                cell.setCellValue("Applicable Room Types");
                cell.setCellStyle(pEarlyCISubHeadStyle1);

                int checkInRowNumEc = lastLine + 1;
                int eDate = lastLine + 1;
                if (dateDetails != null && !dateDetails.isEmpty()) {
                  for (int sIndex = 0; sIndex < dateDetails.size(); sIndex++) {
                    Object[] secVal = dateDetails.get(sIndex);
                    Row sdynamicrow = sheet.createRow(eDate);
                    cell = sdynamicrow.createCell((short) 1);
                    cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(secVal[3]),
                        DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
                    ExcelUtil.setCellBorder(
                        new CellRangeAddress(eDate, eDate, (short) 1, (short) 1), sheet);
                    // cell.setCellStyle(pEarlyCIDateValStyle);

                    cell = sdynamicrow.createCell((short) 2);
                    cell.setCellValue(DateUtil.getFormattedDate(CommonUtil.nullRemove(secVal[4]),
                        DateUtil.DATE_MONTH_YEAR_HYPHEN_PATTERN, "dd/MM/yyyy"));
                    ExcelUtil.setCellBorder(
                        new CellRangeAddress(eDate, eDate, (short) 2, (short) 2), sheet);
                    // cell.setCellStyle(pEarlyCIDateValStyle);
                    eDate = eDate + 1;
                  }
                }

                if (roomDetails != null && !roomDetails.isEmpty()) {
                  for (int sIndex = 0; sIndex < roomDetails.size(); sIndex++) {
                    Object[] secVal = roomDetails.get(sIndex);
                    if (checkInRowNumEc < eDate) {
                      cell = sheet.getRow(checkInRowNumEc - 1).createCell((short) 5);
                    } else {
                      cell = sheet.createRow(checkInRowNumEc - 1).createCell((short) 5);
                    }
                    cell.setCellValue(CommonUtil.nullRemove(secVal[2]));
                    ExcelUtil.setCellBorder(new CellRangeAddress(checkInRowNumEc - 1,
                        checkInRowNumEc - 1, (short) 5, (short) 5), sheet);
                    // cell.setCellStyle(pEarlyCIRoomValStyle);
                    ExcelUtil.frameMergedWithThinBorder(
                        new CellRangeAddress(checkInRowNumEc - 1, checkInRowNumEc - 1, 5, 9), sheet,
                        xSSFWorkbook);
                    checkInRowNumEc++;
                  }
                }

                if (dateSize > roomSize) {
                  lastLine = lastLine + dateSize;
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(tableborder + 1, lastLine, 0, 0), sheet, xSSFWorkbook);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(tableborder + 1, lastLine, 3, 4), sheet, xSSFWorkbook);
                } else {
                  lastLine = lastLine + roomSize;
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(tableborder + 1, lastLine, 0, 0), sheet, xSSFWorkbook);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(tableborder + 1, lastLine, 3, 4), sheet, xSSFWorkbook);
                }
                lastLine = lastLine + 1;
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 9),
                    sheet, xSSFWorkbook);

                List<Object[]> drEIn = dtEarlyCheckedDetails.stream()
                    .filter(
                        line -> "Early CheckIn".equalsIgnoreCase(CommonUtil.nullRemove(line[2])))
                    .collect(Collectors.toList());
                List<Object[]> drEOut = dtEarlyCheckedDetails.stream()
                    .filter(
                        line -> "Late CheckOut".equalsIgnoreCase(CommonUtil.nullRemove(line[2])))
                    .collect(Collectors.toList());

                if (drEIn != null && !drEIn.isEmpty()) {
                  lastLine = lastLine + 1;
                  Row edynamicrow = sheet.createRow(lastLine);
                  cell = edynamicrow.createCell((short) 0);
                  cell.setCellValue("Early Check-In Time (From)");
                  cell.setCellStyle(earlyCIHeaderStyle);

                  cell = edynamicrow.createCell((short) 1);
                  cell.setCellValue("Early check-in time (Till)");
                  cell.setCellStyle(earlyCIHeaderStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 1, 2), sheet, xSSFWorkbook);

                  cell = edynamicrow.createCell((short) 3);
                  cell.setCellValue("Charge applicable");
                  cell.setCellStyle(earlyCIHeaderStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 3, 5), sheet, xSSFWorkbook);

                  cell = edynamicrow.createCell((short) 6);
                  cell.setCellValue("Meal plan");
                  cell.setCellStyle(earlyCIHeaderStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 6, 9), sheet, xSSFWorkbook);

                  for (Object[] obj : drEIn) {

                    if (100 != CommonUtil.DoubleNullRemove(CommonUtil.nullRemove(obj[7]))) {
                      lastLine = lastLine + 1;
                      Row esynamicrow = sheet.createRow(lastLine);
                      cell = esynamicrow.createCell((short) 0);
                      if (!CommonUtil.nullRemove(obj[3]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[3]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);

                      cell = esynamicrow.createCell((short) 1);
                      if (!CommonUtil.nullRemove(obj[4]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[4]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 1, 2), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 3);
                      cell.setCellValue(CommonUtil.nullRemove(obj[7]) + "% of 1 night charge");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 3, 5), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 6);
                      cell.setCellValue("Basic / Booked meal plan");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 6, 9), sheet, xSSFWorkbook);
                    } else {
                      lastLine = lastLine + 1;
                      Row esynamicrow = sheet.createRow(lastLine);
                      cell = esynamicrow.createCell((short) 0);
                      if (!CommonUtil.nullRemove(obj[3]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[3]) + " hrs");
                      }
                      cell.setCellStyle(earlyCIValStyle);

                      cell = esynamicrow.createCell((short) 1);
                      if (!CommonUtil.nullRemove(obj[4]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[4]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 1, 2), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 3);
                      cell.setCellValue(CommonUtil.nullRemove(obj[7]) + "% of 1 night charge");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 3, 5), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 6);
                      cell.setCellValue("Basic / Booked meal plan");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 6, 9), sheet, xSSFWorkbook);

                      lastLine = lastLine + 1;
                      Row esynamicrow1 = sheet.createRow(lastLine);
                      cell = esynamicrow1.createCell((short) 0);
                      cell.setCellValue("100% charge applies for early check-In before : "
                          + CommonUtil.nullRemove(obj[3]) + " hrs");
                      cell.setCellStyle(earlyCIValStyleDB);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 0, 9), sheet, xSSFWorkbook);
                    }
                  }
                }

                lastLine = lastLine + 1;
                ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 9),
                    sheet, xSSFWorkbook);

                if (drEOut != null && !drEOut.isEmpty()) {
                  lastLine = lastLine + 1;
                  Row edynamicrow = sheet.createRow(lastLine);
                  cell = edynamicrow.createCell((short) 0);
                  cell.setCellValue("Late Check-Out Time (From)");
                  cell.setCellStyle(earlyCIHeaderStyle);

                  cell = edynamicrow.createCell((short) 1);
                  cell.setCellValue("Late Check-Out time (Till)");
                  cell.setCellStyle(earlyCIHeaderStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 1, 2), sheet, xSSFWorkbook);

                  cell = edynamicrow.createCell((short) 3);
                  cell.setCellValue("Charge applicable");
                  cell.setCellStyle(earlyCIHeaderStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 3, 5), sheet, xSSFWorkbook);

                  cell = edynamicrow.createCell((short) 6);
                  cell.setCellValue("Meal plan");
                  cell.setCellStyle(earlyCIHeaderStyle);
                  ExcelUtil.frameMergedWithThinBorder(
                      new CellRangeAddress(lastLine, lastLine, 6, 9), sheet, xSSFWorkbook);

                  for (Object[] obj : drEOut) {

                    if (100 != CommonUtil.DoubleNullRemove(CommonUtil.nullRemove(obj[7]))) {
                      lastLine = lastLine + 1;
                      Row esynamicrow = sheet.createRow(lastLine);

                      cell = esynamicrow.createCell((short) 0);
                      if (!CommonUtil.nullRemove(obj[3]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[3]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);

                      cell = esynamicrow.createCell((short) 1);
                      if (!CommonUtil.nullRemove(obj[4]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[4]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 1, 2), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 3);
                      cell.setCellValue(CommonUtil.nullRemove(obj[7]) + "% of 1 night charge");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 3, 5), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 6);
                      cell.setCellValue("Basic / Booked meal plan");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 6, 9), sheet, xSSFWorkbook);
                    } else {
                      lastLine = lastLine + 1;
                      Row esynamicrow = sheet.createRow(lastLine);

                      cell = esynamicrow.createCell((short) 0);
                      if (!CommonUtil.nullRemove(obj[3]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[3]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);

                      cell = esynamicrow.createCell((short) 1);
                      if (!CommonUtil.nullRemove(obj[4]).isEmpty()) {
                        cell.setCellValue(CommonUtil.nullRemove(obj[4]) + " hrs");
                      }
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 1, 2), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 3);
                      cell.setCellValue(CommonUtil.nullRemove(obj[7]) + "% of 1 night charge");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 3, 5), sheet, xSSFWorkbook);

                      cell = esynamicrow.createCell((short) 6);
                      cell.setCellValue("Basic / Booked meal plan");
                      // cell.setCellStyle(earlyCIValStyle);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 6, 9), sheet, xSSFWorkbook);

                      lastLine = lastLine + 1;
                      Row esynamicrow1 = sheet.createRow(lastLine);
                      cell = esynamicrow1.createCell((short) 0);
                      cell.setCellValue("100% charge applies for Late Check-Out after : "
                          + CommonUtil.nullRemove(obj[3]) + " hrs");
                      cell.setCellStyle(earlyCIValStyleDB);
                      ExcelUtil.frameMergedWithThinBorder(
                          new CellRangeAddress(lastLine, lastLine, 0, 9), sheet, xSSFWorkbook);
                    }
                  }
                }
                lastLine = lastLine + 1;
                ExcelUtil.setTableBorder(new CellRangeAddress(tableborder, lastLine, 0, 9), sheet);
                dtEarlyCheckedDetails = null;
              }

            }
          }

          /** #################### General Policy ##################### */
          List<Object[]> generalPolicyDet = rateSheetDAO.getGeneralPolicyDetails(rateSheetDto,
              CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
          lastLine = lastLine + 1;
          if (generalPolicyDet != null && !generalPolicyDet.isEmpty()) {
            lastLine = lastLine + 1;
            int tableborder = lastLine;
            Row gpDynamicRow = sheet.createRow(lastLine);
            cell = gpDynamicRow.createCell((short) 0);
            cell.setCellValue("GENERAL POLICY");
            cell.setCellStyle(generalPolHeaderStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 12),
                sheet, xSSFWorkbook);

            lastLine = lastLine + 1;
            gpDynamicRow = sheet.createRow(lastLine);
            cell = gpDynamicRow.createCell((short) 0);
            cell.setCellValue("From Date");
            cell.setCellStyle(generalPolSubHeaderStyle);

            cell = gpDynamicRow.createCell((short) 1);
            cell.setCellValue("To Date");
            cell.setCellStyle(generalPolSubHeaderStyle);

            cell = gpDynamicRow.createCell((short) 2);
            cell.setCellValue("Policy");
            cell.setCellStyle(generalPolSubHeaderStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 12),
                sheet, xSSFWorkbook);

            for (Object[] genPolicy : generalPolicyDet) {
              lastLine = lastLine + 1;
              gpDynamicRow = sheet.createRow(lastLine);
              cell = gpDynamicRow.createCell((short) 0);
              cell.setCellValue(CommonUtil.nullRemove(genPolicy[1]));
              cell.getCellStyle()
                  .setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
              // cell.setCellStyle(generalPolValStyle1);

              cell = gpDynamicRow.createCell((short) 1);
              cell.setCellValue(CommonUtil.nullRemove(genPolicy[2]));
              cell.getCellStyle()
                  .setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
              // cell.setCellStyle(generalPolValStyle1);

              cell = gpDynamicRow.createCell((short) 2);
              BufferedReader bufReader =
                  new BufferedReader(new StringReader(CommonUtil.nullRemove(genPolicy[3])));
              int genPolHeight = 0;
              while (bufReader.readLine() != null) {
                genPolHeight++;
              }
              gpDynamicRow.setHeight((short) (genPolHeight * 1500));
              cell.setCellValue(CommonUtil.nullRemove(genPolicy[3]));
              cell.setCellStyle(generalPolValStyle2);
              // sheet.showInPane(lastLine-1, (short)2-1);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 12),
                  sheet, xSSFWorkbook);

            }
            ExcelUtil.setTableBorder(new CellRangeAddress(tableborder, lastLine, 0, 12), sheet);
            generalPolicyDet = null;
          }
          lastLine = lastLine + 1;
          /** ############### Hotel Construction ##################### */
          List<Object[]> hotelConstractDet = rateSheetDAO.getHotelConstractionDetails(rateSheetDto,
              CommonUtil.nullRemove(dtHotelDetailsArr.get(6)));
          lastLine = lastLine + 1;
          if (hotelConstractDet != null && !hotelConstractDet.isEmpty()) {
            lastLine = lastLine + 1;
            int tableborder = lastLine;
            Row hcDynamicRow = sheet.createRow(lastLine);
            cell = hcDynamicRow.createCell((short) 0);
            cell.setCellValue("HOTEL CONSTRUCTION");
            cell.setCellStyle(hotelConstHeadStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 0, 12),
                sheet, xSSFWorkbook);

            lastLine = lastLine + 1;
            hcDynamicRow = sheet.createRow(lastLine);
            cell = hcDynamicRow.createCell((short) 0);
            cell.setCellValue("From Date");
            cell.setCellStyle(hotelConstSubHeadStyle);

            cell = hcDynamicRow.createCell((short) 1);
            cell.setCellValue("To Date");
            cell.setCellStyle(hotelConstSubHeadStyle);

            cell = hcDynamicRow.createCell((short) 2);
            cell.setCellValue("Construction");
            cell.setCellStyle(hotelConstSubHeadStyle);
            ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 12),
                sheet, xSSFWorkbook);

            for (Object[] genPolicy : hotelConstractDet) {
              lastLine = lastLine + 1;
              hcDynamicRow = sheet.createRow(lastLine);
              cell = hcDynamicRow.createCell((short) 0);
              cell.setCellValue(CommonUtil.nullRemove(genPolicy[1]));
              // cell.setCellStyle(hotelConstValStyle1);

              cell = hcDynamicRow.createCell((short) 1);
              cell.setCellValue(CommonUtil.nullRemove(genPolicy[2]));
              // cell.setCellStyle(hotelConstValStyle1);

              cell = hcDynamicRow.createCell((short) 2);
              BufferedReader bufReader =
                  new BufferedReader(new StringReader(CommonUtil.nullRemove(genPolicy[3])));
              int genPolHeight = 0;
              while (bufReader.readLine() != null) {
                genPolHeight++;
              }
              hcDynamicRow.setHeight((short) (genPolHeight * 1200));
              cell.setCellValue(CommonUtil.nullRemove(genPolicy[3]));
              // cell.setCellStyle(hotelConstValStyle2);
              ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 2, 12),
                  sheet, xSSFWorkbook);

            }
            hotelConstractDet = null;
          }
          lastLine = lastLine + 1;

        }
      }
      dtRatesAll = null;
    } else {
      Row row = null;
      if (1 == lastLine) {
        row = row0;
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine - 1, lastLine - 1, 1, 9),
            sheet, xSSFWorkbook);
      } else {
        row = sheet.createRow(lastLine);
        ExcelUtil.frameMergedWithThinBorder(new CellRangeAddress(lastLine, lastLine, 1, 9), sheet,
            xSSFWorkbook);
      }

      cell = row.createCell((short) 1);
      cell.setCellValue("Data Not Available.");
    }

    if (columnSize_1 > 0) {
      if (10000 < (columnSize_1 * 300)) {
        sheet.setColumnWidth(0, (columnSize_1 * 300));
      } else {
        sheet.setColumnWidth(0, (columnSize_1 * 300));
      }
    }
    if (7000 > sheet.getColumnWidth(0)) {
      sheet.setColumnWidth(0, 7000);
    }
    sheet.setColumnWidth(1, 3500);
    sheet.setColumnWidth(2, 3500);
    sheet.setColumnWidth(3, 3500);
    sheet.setColumnWidth(4, 3500);
    sheet.setColumnWidth(5, 3500);
    sheet.setColumnWidth(6, 5000);
    sheet.setColumnWidth(7, 5000);
    sheet.setColumnWidth(8, 5000);
    sheet.setColumnWidth(9, 5000);
    sheet.setColumnWidth(10, 5000);
    sheet.setColumnWidth(11, 5000);
    sheet.setColumnWidth(12, 5000);
    sheet.setColumnWidth(13, 5000);
    sheet.setColumnWidth(14, 5000);
    sheet.setColumnWidth(15, 5000);
    sheet.setColumnWidth(16, 5000);
    sheet.setColumnWidth(17, 5000);
    sheet.setColumnWidth(18, 5000);
    sheet.setColumnWidth(19, 5000);
    sheet.setColumnWidth(20, 5000);
    sheet.setColumnWidth(21, 5000);
    sheet.setColumnWidth(22, 5000);
    sheet.setColumnWidth(23, 5000);
    sheet.setColumnWidth(24, 5000);
    sheet.setColumnWidth(25, 5000);

    sheet.protectSheet(rateSheetDto.getSheetPassword());
    System.gc();
    resultSetMap = null;
  }


  public Object[] uploadRateSheetDet(List<List<String>> insertList) throws Exception {

    Object[] result = new Object[2];
    List<List<String>> failureRecList = new ArrayList<>();
    List<List<String>> successRecList = new ArrayList<>();
    Map<String, Object[]> partyMap = rateSheetDAO.getPartyMastDet();
    RatesheetMgt rsMgt = new RatesheetMgt();
    Object[] countryObj = new Object[2];
    for (int i = 0; i < insertList.size(); i++) {
      List<String> userList = insertList.get(i);
      System.out.println("Mapping val::" + userList);
      RatesheetMapping rsMap = new RatesheetMapping();
      Object[] partyObj = null;
      if (!CommonUtil.nullRemove(userList.get(2)).isEmpty()) {
        rsMgt = new RatesheetMgt();
        rsMgt.setDivisionName(userList.get(1));
        if ("Royal Park Tourism Services LLC".equalsIgnoreCase(rsMgt.getDivisionName())) {
          rsMgt.setDivisionId("01");
        }
        if ("Royal Gulf Tourism LLC".equalsIgnoreCase(rsMgt.getDivisionName())) {
          rsMgt.setDivisionId("02");
        }
        rsMgt.setRsName(userList.get(2));
        rsMgt.setRsDesc(userList.get(3));
        rsMgt.setEmailIds(userList.get(4));
        String agentString = CommonUtil.nullRemove(userList.get(5));
        String[] agentArr = agentString.split("-");
        if (agentArr.length > 0) {
          rsMgt.setAgentName(CommonUtil.nullRemove(agentArr[0]));
          if (agentArr.length > 1) {
            rsMgt.setAgentCode(CommonUtil.nullRemove(agentArr[1]).trim());
            countryObj = rateSheetDAO.getCountryCode(rsMgt.getAgentCode());

          }
        }
        if ("YES".equalsIgnoreCase(userList.get(6))) {
          rsMgt.setSendRatesheet("YES");
        } else {
          rsMgt.setSendRatesheet("NO");
        }
        if ("YES".equalsIgnoreCase(userList.get(7))) {
          rsMgt.setSendUpdates("YES");
        } else {
          rsMgt.setSendUpdates("NO");
        }
        rsMgt.setSheetPassword(userList.get(8));
      }

      partyObj = partyMap.get(userList.get(10).trim());
      if (partyObj == null) {
        // logger.error("Hotel Name Incorrect Please Correct::" + userList);
        int index = (insertList.get(i)).size();
        (insertList.get(i)).set(0, String.valueOf(i + 1));
        (insertList.get(i)).set(1, rsMgt.getDivisionName());
        (insertList.get(i)).set(2, rsMgt.getRsName());
        (insertList.get(i)).set(index - 1, "Hotel Name not found.");
        failureRecList.add(insertList.get(i));
        continue;
      }
      rsMap.setDisplayName(userList.get(9));
      rsMap.setPartyName(userList.get(10));
      rsMap.setPartyCode(CommonUtil.nullRemove(partyObj[0]).trim());
      rsMap.setPartyName(CommonUtil.nullRemove(partyObj[1]).trim());
      rsMap.setCategoryCode(CommonUtil.nullRemove(partyObj[2]).trim());
      rsMap.setCategoryName(CommonUtil.nullRemove(partyObj[3]).trim());
      rsMap.setSectorCode(CommonUtil.nullRemove(partyObj[4]).trim());
      rsMap.setSectorName(CommonUtil.nullRemove(partyObj[5]).trim());
      rsMap.setCityCode(CommonUtil.nullRemove(partyObj[6]).trim());
      rsMap.setCityName(CommonUtil.nullRemove(partyObj[7]).trim());
      rsMap.setCountryCode(CommonUtil.nullRemove(countryObj[0]).trim());
      rsMap.setCountryName(CommonUtil.nullRemove(countryObj[1]).trim());
      try {
        if (!CommonUtil.nullRemove(userList.get(2)).isEmpty()) {
          rateSheetDAO.saveRateSheetDetNew(rsMgt);
        }
        if (rsMgt != null && rsMgt.getAutogenRsId() != null) {
          rsMap.setAutogenRsId(rsMgt.getAutogenRsId());
          rateSheetDAO.saveRateSheetMapDetNew(rsMap);
          int index = (insertList.get(i)).size();
          (insertList.get(i)).set(index - 1, "success");
          successRecList.add(insertList.get(i));
        }
      } catch (Exception e) {
        String errReason = "";
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
          errReason = "Unique Constraint Violation Exception: Mapping";
        } else {
          errReason = e.getMessage();
        }

        int index = (insertList.get(i)).size();
        (insertList.get(i)).set(index - 1, errReason);
        failureRecList.add(insertList.get(i));
      }
    }

    for (List<String> failedObj : failureRecList) {
      if (failedObj.get(9) != null && !failedObj.get(10).isEmpty())
        logger.error("Failed Mapping RateSheet With Hotel::" + failedObj.get(10));
    }


    result[0] = successRecList;
    result[1] = failureRecList;
    return result;
  }
  
  public void sendRatesheetUpdates(String schedularDate) throws Exception {
    /** Get Application Param Support Mail Details */
	  try {
    Map<String, String> supportMailMaps = rateSheetDAO.getRatesheetAppParamDet("'SUPPORT_MAIL'");
    String sptMails = "";
    if (supportMailMaps != null && supportMailMaps.entrySet().size() > 0) {
      for (Entry<String, String> mail : supportMailMaps.entrySet()) {
        if (!sptMails.isEmpty()) {
          sptMails = sptMails + ";" + mail.getValue();
        } else {
          sptMails = mail.getValue();
        }
      }
    }
    String[] sptMailsArr = sptMails.split(";");
    gulfMailSubject = gulfMailSubject+"("+DateUtil.getFormattedDate(schedularDate, "dd.MM.yyyy", "dd/MM/yyyy")+") for Royal Gulf -";
    parkMailSubject = parkMailSubject+"("+DateUtil.getFormattedDate(schedularDate, "dd.MM.yyyy", "dd/MM/yyyy")+") for Royal Park -";
    parkGulfMailContent = parkGulfMailContent+"("+DateUtil.getFormattedDate(schedularDate, "dd.MM.yyyy", "dd/MM/yyyy")+") for Royal Park and Gulf";
    String subject = "";
    String mailContent = "";
    
    List<RatesheetAgentMap> ratesheetAgentMapList = rateSheetDAO.getRateSheetAgnetMap();
    if (ratesheetAgentMapList != null && !ratesheetAgentMapList.isEmpty()) {
      Map<BigInteger, String> rsMap = (Map<BigInteger, String>) rateSheetDAO.getRateSheetsDivByMap("RATESHEET");
      List<RatesheetAgentMap> uniqueAgentCode = ratesheetAgentMapList.stream()
          .filter(CommonUtil.distinctByKey(obj -> obj.getAgentCode())).collect(Collectors.toList());

      String currentDirectory = System.getProperty("user.dir");
      for (RatesheetAgentMap ratesheetAgentMap : uniqueAgentCode) {
        HashMap<String, String> fileList = new HashMap<>();
        Set<String> divSet = new HashSet<>();
        /** Filter ratesheet */
        List<RatesheetAgentMap> mailSendRatesheetList = ratesheetAgentMapList.stream()
            .filter(obj -> ratesheetAgentMap.getAgentCode().equalsIgnoreCase(obj.getAgentCode()))
            .collect(Collectors.toList());
        List<RatesheetAgentMap> ratesheetList = mailSendRatesheetList.stream()
            .filter(CommonUtil.distinctByKey(obj -> obj.getAutogenRsId()))
            .collect(Collectors.toList());

        try {
          if(ratesheetList != null && !ratesheetList.isEmpty()) {
            for (RatesheetAgentMap obj : ratesheetList) {
              divSet.add(rsMap.get(BigInteger.valueOf(obj.getAutogenRsId())));
              List<Object[]> rsUpdateResults = rateSheetDAO
                  .getRateSheetUpdatesDet(BigInteger.valueOf(obj.getAutogenRsId()), schedularDate);
              if (rsUpdateResults != null && !rsUpdateResults.isEmpty()) {
                String rateSheetName =
                    currentDirectory + "/ratesheet/" + obj.getRsName().trim() + "_Updates" + ".xlsx";
                fileList.put(obj.getRsName().trim() + "_Updates" + ".xlsx", rateSheetName);
              }
            }
          }
        } catch (FileNotFoundException ex) {
          System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
          System.err.println("I/O error: " + ex);
        }
        
        try {
        /** Mail Sent Part */
        if(divSet != null && !divSet.isEmpty() && divSet.contains("01") && divSet.contains("02")) {
          subject = parkGulfMailSubject;
          mailContent = parkGulfMailContent;
        } else if(divSet != null && !divSet.isEmpty() && divSet.contains("01")) {
          subject = parkMailSubject+ratesheetAgentMap.getAgentCode();
          mailContent = parkMailContent;
        } else if(divSet != null && !divSet.isEmpty() && divSet.contains("02")) {
          subject = gulfMailSubject+ratesheetAgentMap.getAgentCode();
          mailContent = gulfMailContent;
        }
        logger.error("RatesheetserviceImpl-->sendRatesheetUpdates()::mailContent::" + mailContent);
        
          if(mailSendRatesheetList != null && !mailSendRatesheetList.isEmpty()) {
          List<RatesheetAgentMap> mailSendRatesheetListFinal = mailSendRatesheetList.stream()
              .filter(CommonUtil.distinctByKey(obj -> obj.getRatesheetEmailTo()))
              .collect(Collectors.toList());
          
            if(mailSendRatesheetListFinal != null && !mailSendRatesheetListFinal.isEmpty()) {
              for (RatesheetAgentMap mailsentAgent : mailSendRatesheetListFinal) {
                if(1 == mailsentAgent.getEmailActive()) {
              	  String[] cc;
              	  if(mailsentAgent.getRatesheetEmailCC()!=null && !mailsentAgent.getRatesheetEmailCC().isEmpty()){
              		  cc=mailsentAgent.getRatesheetEmailCC().split(";");
              	  }else {
              		 cc=new String[1];
              	  }
                  String errMSG = emailService.sendMailWithAttachment(mailsentAgent.getRatesheetEmailTo().split(";"),
                      cc, subject, mailContent, fileList, schedularDate);
                  if (errMSG == null || errMSG.isEmpty() || !errMSG.equalsIgnoreCase("SUCCESS")) {
                    String errSubject = schedularDate +": Ratesheet Updates Mail Not Sent - " +mailsentAgent.getAgentCode() ;
                    logger.error(
                        "RatesheetserviceImpl-->sendRatesheetUpdates()::mailContent::Mail Not Sent::", errMSG);
                    logger.error(
                        errSubject+"::", errMSG+"::Scedular Date::"+schedularDate);
                    rateSheetDAO.saveRateSheetErrorLog(new RateSheetDto(), errMSG,
                        "", null, "AG_ML_NOTSENT_EX");
                    /** Support Mail Logic */                          
                    emailService.sendMultipleMessageWithAttachment(sptMailsArr, errSubject, errMSG,
                        fileList, schedularDate);
                  }
                }
              }
            }
          }
        } catch (Exception ex) {
          logger.error("RatesheetserviceImpl-->sendRatesheetUpdates()::Send mailSendRatesheetListFinal::" + ex);
          rateSheetDAO.saveRateSheetErrorLog(new RateSheetDto(), "RatesheetserviceImpl-->sendRatesheetUpdates()::Send mailSendRatesheetListFinal::" + ex,
              "", null, "AG_ML_NOTSENT_EX");
          /** Support Mail Logic */                          
          emailService.sendMultipleMessageWithAttachment(sptMailsArr, "Mail Send List Getting Error:", "RatesheetserviceImpl-->sendRatesheetUpdates()::Send mailSendRatesheetListFinal::" + ex,
              fileList, schedularDate);
        }
      }
    }
	  }catch(Exception ex){
		  logger.error("RatesheetserviceImpl-->sendRatesheetUpdates()::" + ex);
	  }finally {
		  rateSheetDAO.updateRatesheetSchedularDate();
	  }
  }
  
  public Map<String,String> getIndexNameMap(){
    Map<String,String> _map = new HashMap<>();
    _map.put("Royal Gulf Rate Sheet - Other Sector", "Other Sector Hotels");
    _map.put("Royal Gulf Rate Sheet - Abu Dhabi Sector","Abu Dhabi Sector Hotels");
    _map.put("Royal Gulf Rate Sheet - Dubai City Hotel Apartments", "Dubai City - Hotel Apartments");
    _map.put("Royal Gulf Rate Sheet - Dubai City 3 and 2star Hotels", "Dubai City - 3 and 2 Star Hotels");
    _map.put("Royal Gulf Rate Sheet - Dubai City 4star Hotels", "Dubai City - 4 Star Hotels");
    _map.put("Royal Gulf Rate Sheet - Dubai City 5star Hotels", "Dubai City - 5 Star Hotels");
    _map.put("Royal Gulf Rate Sheet - Dubai Beach Sector", "Dubai Beach Hotels");
    _map.put("Travelbullz Rate Sheet", "Travelbullz - Hotels");
    _map.put("Czech Rate Sheet - Other Sector", "Other Sector Hotels");
    _map.put("Czech Rate Sheet - Dubai Sector", "Dubai Sector Hotels");
    _map.put("Dubtour Special Rate Sheet", "Dubtour - Special Hotels");
    _map.put("Romanian Rate Sheet", "Romanian - Special Hotels");
    return _map;
  }

@Override
public String checkRatesheetDate() throws Exception {
	return rateSheetDAO.checkRatesheetDate();
}

@Override
public boolean updateRatesheetSchedularDate() throws Exception {
	return rateSheetDAO.updateRatesheetSchedularDate();
}


}
