import net.sourceforge.tess4j.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OCR {
	private String a0="无法识别",c0="无法识别";

	/**
	 * 从识别出的字符串中定位要求的信息位置
	 * @param s	识别出的字符串
	 */
	public void PickUp(String s) {
		int c1=-1;
		c1=s.lastIndexOf("号 :");
		if(c1!=-1) {
			c0=s.substring(c1+3, c1+21);
		}
		int a1=-1;
		a1=s.indexOf("称 :");
		if(a1!=-1) {
			a0=s.substring(a1+3);
		}
	}

	/**
	 *  将识别的数据导出到电子表格
	 * @param i			当前行数
	 * @param wb		表格文件对象
	 * @param sheet		表格工作表对象
	 */
	public void toExcel(int i,XSSFWorkbook wb,XSSFSheet sheet) {
		//表头
		if(i==0) {
			XSSFRow row=sheet.createRow(0);
			XSSFCellStyle style=wb.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			XSSFCell cell=row.createCell(0);
			cell.setCellValue("企业名称");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("企业注册号");
			cell.setCellStyle(style);
		} else {
			XSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(this.a0);
			row.createCell(1).setCellValue(this.c0);
		}
	}

	public static void main(String[] args) throws IOException {
		OCR ocr =new OCR();
		int num=1;
		Rectangle ret=new Rectangle(0,0,550,80);
		File root=new File(System.getProperty("user.dir"));
		File fixed=new File(System.getProperty("user.dir") + "/resources/res");
		File source=new File(System.getProperty("user.dir") + "/resources/imgs");
		
		ITesseract instance = new Tesseract();
		instance.setDatapath(root + "/resources/tessdata");
		instance.setLanguage("songti"); 							//使用训练好中文字库识别
		instance.setTessVariable("user_defined_dpi", "70");

		XSSFWorkbook wb=new XSSFWorkbook();
		XSSFSheet sheet=wb.createSheet("信息汇总");
		ocr.toExcel(0,wb,sheet);

		try {
			File[] ress = source.listFiles();
			int i=0;
			for(File file : ress){
				i++;
				WaterMarkRemove.Clean(file.getAbsolutePath(),fixed.getAbsolutePath()+"\\"+i+".png");
			}
			
			
			File[] files = fixed.listFiles();
			for (File file : files) {                        	   //识别去除了水印的图片
				String result = instance.doOCR(file,ret);
				System.out.print(result);
				ocr.a0="无法识别";
				ocr.c0="无法识别";
				ocr.PickUp(result);
				ocr.toExcel(num,wb,sheet);
				num++;
			}
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
		
		
		 try {  
	            FileOutputStream fileOutputStream = new FileOutputStream(root.getAbsoluteFile() + "\\company.xlsx");
	            wb.write(fileOutputStream);
	            fileOutputStream.close();
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }
	}
	
}