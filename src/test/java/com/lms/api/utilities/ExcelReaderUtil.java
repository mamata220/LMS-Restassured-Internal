package com.lms.api.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;

/**
 * This class is a utility class and is used to Read Excel sheet
 */
public class ExcelReaderUtil {

    String path;
    FileInputStream fis;
    Workbook workbook;
    Sheet sheet;
    Row row;

    public ExcelReaderUtil(String path) {
        super();
        this.path = path;
    }

    public Sheet getSheet(String sheetName) throws Exception {
        File file = new File(path);
        fis = new FileInputStream(file);
        //Below API can ready both xls and xlsx formats
        workbook = WorkbookFactory.create(fis);
        sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    public  Object [] getSheetData(Sheet sheet){

       int firstRowNum = sheet.getFirstRowNum();
      // int lastRow = sheet.getLastRowNum();
       int physicalNoOfRowsCount = sheet.getPhysicalNumberOfRows();

       Row firstRow = sheet.getRow(firstRowNum);
       int columnsCountNum = firstRow.getPhysicalNumberOfCells();

     //  Map<String, Integer> headerColumnMap = new HashMap<>();
     //  Map<String, Integer> dataColumnMap = new HashMap<>();
       List<ExcelRowData> excelRowDataList = new ArrayList<>();

       Object [] excelRowDataObjArr = new Object[physicalNoOfRowsCount-1];

       for(int rowIndex = 0; rowIndex < physicalNoOfRowsCount; rowIndex++) {
           if(rowIndex == 0){
               for(int colIndex=0; colIndex < columnsCountNum; colIndex++){
              //     headerColumnMap.put(firstRow.getCell(colIndex).getStringCellValue(), colIndex);
               }
           }else{
               Row rowData = sheet.getRow(rowIndex);
               ExcelRowData excelRowData = new ExcelRowData();
               excelRowData.setTest_Case_Scenario_Name(rowData.getCell(0).getStringCellValue());
               excelRowData.setEndpoint_Path(rowData.getCell(1).getStringCellValue());
               excelRowData.setProgram_Id(getCellValue(rowData.getCell(2)));
               excelRowData.setJson_Payload(rowData.getCell(3).getStringCellValue());
               excelRowData.setExpected_Status_Code((int) rowData.getCell(4).getNumericCellValue());
               excelRowData.setExpected_Message(rowData.getCell(5).getStringCellValue());
//               excelRowDataList.add(excelRowData);
               excelRowDataObjArr[rowIndex-1] = excelRowData;
           }

       }

     /* for(int i=0; i< columnsCountNum; i++){
           headerColumnMap.put(firstRow.getCell(i).getStringCellValue(), i);

           ExcelRowData excelRowData = new ExcelRowData();
           excelRowData.setTest_Case_Scenario_Name();
           dataColumnMap.
       }*/
    //    System.out.println(headerColumnMap);
        return excelRowDataObjArr;
    }

    private String getCellValue(Cell cell){
        String columnData = null;
        if(cell.getCellType().equals(CellType.BLANK)) {
            columnData = null;
        }else if(cell.getCellType().equals(CellType.NUMERIC)) {
                columnData = ""+ (int) cell.getNumericCellValue();
        }else if(cell.getCellType().equals(CellType.STRING)) {
                columnData = cell.getStringCellValue();
        }
        return columnData;
    }


     public String getDataFromExcel(String rowName, String colName) throws IOException {
        int dataRowNum = -1;
        int dataColNum = -1;
        int totalRows = sheet.getLastRowNum();
        int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0; i <= totalRows; i++) {
            if (sheet.getRow(i).getCell(0).getStringCellValue().equals(rowName)) {
                dataRowNum = i;
                break;
            }

        }
        for (int j = 0; j <= totalCols; j++) {
            if (sheet.getRow(0).getCell(j).getStringCellValue().equals(colName)) {
                dataColNum = j;
                break;
            }
        }

        String body = sheet.getRow(dataRowNum).getCell(dataColNum).getStringCellValue();
        fis.close();
        return body;
    }


}