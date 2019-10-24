package com.example.demo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@RestController
public class showController {
    @RequestMapping("test/getDetailPage")
    public ModelAndView getDetailPage(){
        System.out.println("我还只会打印");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("test");
        return mv;
    }

    public void generateExcel() throws Exception {
        Workbook wb = new HSSFWorkbook();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("C:\\Users\\13543\\Desktop\\text.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        wb.createSheet("sheet1");
        wb.write(fos);
    }

    public static boolean createWorkBook(String excelType, String sheetName, List<String> headList,List<List<String>> dataList,String path){
        Workbook wb = null;
        /*创建文件*/
        if (excelType == null || excelType.endsWith("2003")) {
            /*操作Excel2003以前（包括2003）的版本，扩展名是.xls */
            wb = new HSSFWorkbook();
        }else if (excelType.endsWith("2007")){
            /*XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx */
            wb = new XSSFWorkbook();
        }else {   //默认为2003版本
            /*操作Excel2003以前（包括2003）的版本，扩展名是.xls */
            wb = new HSSFWorkbook();
        }
        /*Excel文件创建完毕*/
        CreationHelper createHelper = wb.getCreationHelper();  //创建帮助工具

        /*创建表单*/
        Sheet sheet = wb.createSheet(sheetName!=null?sheetName:"new sheet");
        // Note that sheet name is Excel must not exceed 31 characters（注意sheet的名字的长度不能超过31个字符，若是超过的话，会自动截取前31个字符）
        // and must not contain any of the any of the following characters:（不能包含下列字符）
        // 0x0000  0x0003  colon (:)  backslash (\)  asterisk (*)  question mark (?)  forward slash (/)  opening square bracket ([)  closing square bracket (])
        /*若是包含的话，会报错。但有一个解决此问题的方法，
        就是调用WorkbookUtil的createSafeSheetName(String nameProposal)方法来创建sheet name,
        若是有如上特殊字符，它会自动用空字符来替换掉，自动过滤。*/
        /*String safeName = WorkbookUtil.createSafeSheetName("[O'Brien's sales*?]"); // returns " O'Brien's sales   "过滤掉上面出现的不合法字符
        Sheet sheet3 = workbook.createSheet(safeName); //然后就创建成功了*/
        /*表单创建完毕*/

        //设置字体
        Font headFont = wb.createFont();
        headFont.setFontHeightInPoints((short)14);
        headFont.setFontName("Courier New");
        headFont.setItalic(false);
        headFont.setStrikeout(false);
        //设置头部单元格样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setBorderBottom(BorderStyle.THICK);  //设置单元格线条
        headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());   //设置单元格颜色
        headStyle.setBorderLeft(BorderStyle.THICK);
        headStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderRight(BorderStyle.THICK);
        headStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderTop(BorderStyle.THICK);
        headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setAlignment(HorizontalAlignment.CENTER);    //设置水平对齐方式
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //设置垂直对齐方式
        //headStyle.setShrinkToFit(true);  //自动伸缩
        headStyle.setFont(headFont);  //设置字体
        /*设置数据单元格格式*/
        CellStyle dataStyle = wb.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);  //设置单元格线条
        dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());   //设置单元格颜色
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        dataStyle.setAlignment(HorizontalAlignment.LEFT);    //设置水平对齐方式
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //设置垂直对齐方式
        //dataStyle.setShrinkToFit(true);  //自动伸缩
        /*创建行Rows及单元格Cells*/
        Row headRow = sheet.createRow(0); //第一行为头
        for (int i=0;i<headList.size();i++){  //遍历表头数据
            Cell cell = headRow.createCell(i);  //创建单元格
            cell.setCellValue(createHelper.createRichTextString(headList.get(i)));  //设置值
            cell.setCellStyle(headStyle);  //设置样式
        }

        int rowIndex = 1;  //当前行索引
        //创建Rows
        for (List<String> rowdata : dataList){ //遍历所有数据
            Row row = sheet.createRow(rowIndex++); //第一行为头
            for (int j = 0;j< rowdata.size();j++){  //编译每一行
                Cell cell = row.createCell(j);
                cell.setCellStyle(dataStyle);
                cell.setCellValue(createHelper.createRichTextString(rowdata.get(j)));
            }
        }
        /*创建rows和cells完毕*/

        /*设置列自动对齐*/
        for (int i =0;i<headList.size();i++){
            sheet.autoSizeColumn(i);
        }
        CellRangeAddress region2 =  new CellRangeAddress(0,0,0,1);
        sheet.addMergedRegion(region2);
        // 分别为起始行，结束行，起始列，结束列
        CellRangeAddress region =  new CellRangeAddress(1,3,0,0);
        sheet.addMergedRegion(region);

        try  (OutputStream fileOut = new FileOutputStream(path)) {    //获取文件流
            wb.write(fileOut);   //将workbook写入文件流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        showController s = new showController();
       // s.generateExcel();
        List<String> headList = new ArrayList<>();
        headList.add("岗位类别");
        headList.add("");
        headList.add("人数");
        headList.add("月薪标准合计");
        headList.add("年度目标浮薪月数(预算)");
        headList.add("公司系数");
        headList.add("部门系数");
        headList.add("可计提年度浮动系数总和");
        headList.add("季度已预发浮动系数");
        headList.add("年终奖系数");
        headList.add("个人系数平均");
        headList.add("年终奖总额");
        headList.add("备注");


        List<List<String>> dataList = new ArrayList<>();
        List<String> rowOne = new ArrayList<>();
        rowOne.add("中层员工P3-P5/M3M4");
        rowOne.add("部门1");
        rowOne.add("11");
        rowOne.add("23998");
        rowOne.add("14");
        rowOne.add("0.86");
        rowOne.add("0.92");
        rowOne.add("0.99");
        rowOne.add("0.93");
        rowOne.add("0.88");
        rowOne.add("0.81");
        rowOne.add("13234");
        rowOne.add("预发");
        dataList.add(rowOne);
        List<String> rowTwo = new ArrayList<>();
        rowTwo.add("");
        rowTwo.add("部门2");
        rowTwo.add("11");
        rowTwo.add("23998");
        rowTwo.add("14");
        rowTwo.add("0.86");
        rowTwo.add("0.92");
        rowTwo.add("0.99");
        rowTwo.add("0.93");
        rowTwo.add("0.88");
        rowTwo.add("0.81");
        rowTwo.add("13234");
        rowTwo.add("预发");
        dataList.add(rowTwo);
        List<String> rowThree = new ArrayList<>();
        rowThree.add("");
        rowThree.add("部门3");
        rowThree.add("11");
        rowThree.add("23998");
        rowThree.add("14");
        rowThree.add("0.86");
        rowThree.add("0.92");
        rowThree.add("0.99");
        rowThree.add("0.93");
        rowThree.add("0.88");
        rowThree.add("0.81");
        rowThree.add("13234");
        rowThree.add("预发");
        dataList.add(rowThree);
        createWorkBook("2007","sheet1",headList,dataList,"D:\\Mydata\\chengqi1\\Desktop\\1211.xlsx");
    }
}
