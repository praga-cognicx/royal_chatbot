package com.royal.app.util;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.core.io.ClassPathResource;

public  class ExcelUtil {
	public static CellStyle getCellStyleForHeader(Workbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THICK);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THICK);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THICK);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THICK);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;

	}
	
	public static CellStyle getCellStyleForBackButton(Workbook workbook) {
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontName("Arial");
      headerFont.setColor(IndexedColors.BLACK.getIndex());
      headerFont.setUnderline(Font.U_NONE);
      headerFont.setFontHeightInPoints((short)12);
      CellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(headerFont);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      return style;
  }
	
  public static CellStyle getCellStyleForHotelName(Workbook workbook) {
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontName("Arial");
      headerFont.setColor(IndexedColors.WHITE.getIndex());
      headerFont.setUnderline(Font.U_NONE);
      headerFont.setFontHeightInPoints((short)18);
      CellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor(new XSSFColor(new Color(51,63,79)).getIndex());
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFont(headerFont);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      style.setBorderBottom(BorderStyle.THIN);
      style.setBorderRight(BorderStyle.THIN);
      style.setBorderTop(BorderStyle.THIN);
      style.setBorderLeft(BorderStyle.THIN);
      return style;
  }	
  public static CellStyle getCellStyleForContentLightBlue(Workbook workbook) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Arial");
    headerFont.setColor(IndexedColors.BLACK.getIndex());
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
} 
  
  public static CellStyle getCellStyleForContentLightGrey(Workbook workbook) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Arial");
    headerFont.setColor(IndexedColors.BLACK.getIndex());
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
}   
  
  public static CellStyle getCellStyleForContentLightWhite(Workbook workbook) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Arial");
    headerFont.setColor(IndexedColors.WHITE.getIndex());
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    return style;
}
  
  public static CellStyle getCellStyleForContentLightWhite(Workbook workbook, CellStyle cellStyle, short color) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Arial");
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    return style;
}   
  
  public static CellStyle getCellStyleForContentGrayWithBlue(Workbook workbook) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Arial");
    headerFont.setColor(IndexedColors.BLUE1.getIndex());
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
   // style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderRight(BorderStyle.THIN);
    //style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
   // style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
   // style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    return style;
}  
  
  public static CellStyle getCellStyleForContentGrayWithBlue(Workbook workbook, CellStyle cellStyle, short color, HorizontalAlignment halign) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Arial");
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
} 
  
  public static XSSFCellStyle getCellStyleForContent(Workbook workbook, CellStyle cellStyle, short color, XSSFColor bgColor, HorizontalAlignment halign, boolean bold) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(bold);
    headerFont.setFontName("Arial");
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
    style.setFillForegroundColor(bgColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
}
  
  public static XSSFCellStyle getCellStyleForContent(Workbook workbook, CellStyle cellStyle, short color, XSSFColor bgColor, HorizontalAlignment halign, boolean bold, boolean wrapText, short fontSize) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(bold);
    headerFont.setFontName("Arial");
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints(fontSize);
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();;
    style.setFillForegroundColor(bgColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setWrapText(wrapText);
    return style;
}
  
  public static XSSFCellStyle getCellStyleForContent(Workbook workbook, CellStyle cellStyle, short color, XSSFColor bgColor, HorizontalAlignment halign, boolean bold, boolean wrapText, short fontSize, String fontName) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(bold);
    headerFont.setFontName(fontName);
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints(fontSize);
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();;
    style.setFillForegroundColor(bgColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setWrapText(wrapText);
    return style;
}
  
  public static XSSFCellStyle getCellStyleForContent(Workbook workbook, CellStyle cellStyle, short color, XSSFColor bgColor, HorizontalAlignment halign, boolean bold, boolean wrapText, short fontSize, String fontName, boolean border) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(bold);
    headerFont.setFontName(fontName);
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints(fontSize);
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();;
    style.setFillForegroundColor(bgColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    if(border) {
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    }
    style.setWrapText(wrapText);
    return style;
}
  
  public static XSSFCellStyle getCellStyleForContent(Workbook workbook, CellStyle cellStyle, short color, XSSFColor bgColor, HorizontalAlignment halign, boolean bold, boolean wrapText, short fontSize, String fontName, boolean border, VerticalAlignment valign) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(bold);
    headerFont.setFontName(fontName);
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints(fontSize);
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();;
    style.setFillForegroundColor(bgColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(valign);
    if(border) {
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    }
    style.setWrapText(wrapText);
    return style;
}
  
  public static CellStyle getCellStyleForContentBlueWithWhite(Workbook workbook, CellStyle cellStyle, short color, short bgColor, HorizontalAlignment halign, boolean bold) {
    Font headerFont = workbook.createFont();
    headerFont.setBold(bold);
    headerFont.setFontName("Arial");
    headerFont.setColor(color);
    headerFont.setUnderline(Font.U_NONE);
    headerFont.setFontHeightInPoints((short)10);
    CellStyle style = workbook.createCellStyle();;
    style.setFillForegroundColor(bgColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(headerFont);
    style.setAlignment(halign);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
}  
    
	
	@SuppressWarnings("deprecation")
	public static void frameMerged(CellRangeAddress region, Sheet sheet, Workbook wb) {
		sheet.addMergedRegion(region);
		final short borderMediumDashed = BorderStyle.THICK.getCode();
		RegionUtil.setBorderBottom(borderMediumDashed, region, sheet);
		RegionUtil.setBorderTop(borderMediumDashed, region, sheet);
		RegionUtil.setBorderLeft(borderMediumDashed, region, sheet);
		RegionUtil.setBorderRight(borderMediumDashed, region, sheet);
	}
	
    @SuppressWarnings("deprecation")
    public static void frameMergedWithThinBorder(CellRangeAddress region, Sheet sheet, Workbook wb) {
        sheet.addMergedRegion(region);
        final short borderMediumDashed = BorderStyle.THIN.getCode();
        RegionUtil.setBorderBottom(borderMediumDashed, region, sheet);
        RegionUtil.setBorderTop(borderMediumDashed, region, sheet);
        RegionUtil.setBorderLeft(borderMediumDashed, region, sheet);
        RegionUtil.setBorderRight(borderMediumDashed, region, sheet);
    }
    
    @SuppressWarnings("deprecation")
    public static void setTableBorder(CellRangeAddress region, Sheet sheet) {
        final short borderMediumDashed = BorderStyle.THIN.getCode();
        RegionUtil.setBorderBottom(borderMediumDashed, region, sheet);
        RegionUtil.setBorderTop(borderMediumDashed, region, sheet);
        //RegionUtil.setBorderLeft(borderMediumDashed, region, sheet);
        RegionUtil.setBorderRight(borderMediumDashed, region, sheet);
    }   
    
    @SuppressWarnings("deprecation")
    public static void setCellBorder(CellRangeAddress region, Sheet sheet) {
        final short borderMediumDashed = BorderStyle.THIN.getCode();
        RegionUtil.setBorderBottom(borderMediumDashed, region, sheet);
        RegionUtil.setBorderTop(borderMediumDashed, region, sheet);
        RegionUtil.setBorderLeft(borderMediumDashed, region, sheet);
        RegionUtil.setBorderRight(borderMediumDashed, region, sheet);
    }   
	 
	public static void setCellValue(CellStyle style, Row row, int cellPosition, Double value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCellValue(CellStyle style, Row row, int cellPosition, int value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCellValue(CellStyle style, Row row, int cellPosition, String value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCellValue(CellStyle style, Row row, int cellPosition, long value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static int getLogo(Workbook workbook, Sheet sheet, String picName, int picType) throws IOException {
		ClassPathResource headerLogo = new ClassPathResource(picName);
		byte[] logoBytes = IOUtils.toByteArray(headerLogo.getInputStream());
		return workbook.addPicture(logoBytes, picType);
	}

	public static void deleteRow(Sheet sheet) {
	        if(sheet != null) {
	          for (int rowNo = sheet.getLastRowNum(); rowNo >= 0; rowNo--) {
	           // if(sheet.getRow(rowNo) != null) {
	              int lastRowNum = sheet.getLastRowNum();
	              if (rowNo >= 0 && rowNo < lastRowNum) {
	                sheet.shiftRows(rowNo + 1, lastRowNum, -1);
	              }
	              if (rowNo == lastRowNum) {
	                XSSFRow removingRow = (XSSFRow) sheet.getRow(rowNo);
	                if (removingRow != null) {
	                  sheet.removeRow(removingRow);
	                }
	              }
	            //}
	          }
	        }
	}
	
	public static int getColumnHeight(String val) throws IOException {
	  int sEHeight = 0;
	  BufferedReader bufReader =
          new BufferedReader(new StringReader(val));
      int genPolHeight = 0;
      while (bufReader.readLine() != null) {
        genPolHeight++;
      }
      if (sEHeight < genPolHeight) {
        sEHeight = genPolHeight;
      }
      return sEHeight;
	}
}
