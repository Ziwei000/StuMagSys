import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class AccessControl {

	private static ArrayList<String> role = null;
	private static ArrayList<String> tableName = null;
	private static String[][] ACL = null;
	private static Sheet sheet;
	private static FileInputStream is;
	private static Workbook wb;
	private static File file;
	private static String excelPath = "E:/jxljar/access.xls";

	public static void init() {
		file = new File(excelPath);
		AccessControl.readExcel();
	//	AccessControl.printArrays();
	}
    //判断某个角色是否对某个表有读或写权限
	public static boolean isPermited(String tn, String role2, String op) {
		int i = 0, j = 0;
		if (role != null)
			i = role.indexOf(role2);
		if (tableName != null)
			j = tableName.indexOf(tn);
		if (i < 0 || j < 0) {
			System.out.println("没找到角色名或表名");
			return false;
		}
		String str = ACL[i][j];
//		if (str.equals(op))
//			return true;
		if (str.contains(op))//若acl表中有该权限，返回真
			return true;
		return false;
	}
    //回应客户端请求，执行查找权限操作
	public static void findAccess(Net net) {
		if(net != null) {
			String tn = net.recvString();//接收客户端传输的表名
			String r = net.recvString();//接收客户端传输的角色名
			int i = 0, j = 0;
			if (role != null)
				i = role.indexOf(r);
			if (tableName != null)
				j = tableName.indexOf(tn);
			if (i < 0 || j < 0) {
				System.out.println("没找到角色名或表名");
				net.sendString("没找到角色名或表名");
				return;
			} else {
				net.sendString(ACL[i][j]);//向客户端回应ACL中记录的权限
				return;
			}
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//修改权限操作
	private static String changeAccess(String tn, String r, String op) {
		// 修改ACL表
		int i = 0, j = 0;
		if (role != null)
			i = role.indexOf(r);
		if (tableName != null)
			j = tableName.indexOf(tn);
		if (i < 0 || j < 0) {
			System.out.println("没找到角色名或表名");
			return "修改权限失败";
		}
		ACL[i][j] = op;
		System.out.println("i=" + i + ",j=" + j + ",op=" + op);

		modifyExcel(i + 1, j + 1, op);
//		modifyExcel(0, 0, op);
		return "修改权限成功";
	}
	 //回应客户端请求，执行修改权限操作
	public static void changeAccess(Net net) {
		if(net != null) {
			String tn = net.recvString();
			String r = net.recvString();
			String op = net.recvString();
			net.sendString(changeAccess(tn, r, op));
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	
//把对权限的修改保存到execel表中
	private static void modifyExcel(int i, int j, String op) {
		/*
		 * try { // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件
		 * WritableWorkbook wbe = Workbook.createWorkbook(new File(excelPath), wb);//
		 * 创建workbook的副本 WritableSheet sheet = wbe.getSheet(0); // 获取第一个sheet
		 * WritableCell cell = sheet.getWritableCell(j, i);// 获取第一个单元格 CellFormat cf =
		 * cell.getCellFormat();// 获取第一个单元格的格式 jxl.write.Label lbl = new
		 * jxl.write.Label(i, j, op);// 将第一个单元格的值改为“修改後的值” lbl.setCellFormat(cf);//
		 * 将修改后的单元格的格式设定成跟原来一样 sheet.addCell(lbl);// 将改过的单元格保存到sheet wbe.write();//
		 * 将修改保存到workbook --》一定要保存 wbe.close();// 关闭workbook，释放内存 ---》一定要释放内存 } catch
		 * (IOException e) { e.printStackTrace(); } catch (WriteException e) {
		 * e.printStackTrace(); }
		 */

		WritableWorkbook book = null;
		try {
			// Excel获得文件
			Workbook wb = Workbook.getWorkbook(new File(excelPath));
			// 打开一个文件的副本，并且指定数据写回到原文件
			book = Workbook.createWorkbook(new File(excelPath), wb);
			Sheet sheet = book.getSheet(0);
			WritableSheet wsheet = book.getSheet(0);
			WritableCell cell = wsheet.getWritableCell(j, i);
			CellFormat cf = cell.getCellFormat();
			Label label = new Label(j, i, op);
			label.setCellFormat(cf);
			wsheet.addCell(label);
			book.write();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				book.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}

	// 去读Excel的方法readExcel，该方法的入口参数为一个File对象
	private static void readExcel() {
		try {
			// 创建输入流，读取Excel
			is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			wb = Workbook.getWorkbook(is);
			// 获得页签
			sheet = wb.getSheet(0);

			role = new ArrayList<String>(sheet.getRows() - 1);
			tableName = new ArrayList<String>(sheet.getColumns() - 1);
			ACL = new String[sheet.getRows() - 1][sheet.getColumns() - 1];

			// role表初始化
			for (int i = 1; i < sheet.getRows(); i++) {
				role.add(sheet.getCell(0, i).getContents());
			}
			// tableName表初始化
			for (int i = 1; i < sheet.getColumns(); i++) {
				tableName.add(sheet.getCell(i, 0).getContents());
			}
			// ACL表初始化
			for (int i = 1; i < sheet.getRows(); i++) {
				for (int j = 1; j < sheet.getColumns(); j++) {
					ACL[i - 1][j - 1] = sheet.getCell(j, i).getContents();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    //在控制台输出权限表
	private static void printArrays() {
		for (int i = 0; i < role.size(); i++) {
			System.out.print(role.get(i));
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i < tableName.size(); i++) {
			System.out.print(tableName.get(i));
		}
		System.out.println();
		System.out.println();
		System.out.println();
		for (int i = 0; i < ACL.length; i++) {
			for (int j = 0; j < ACL[i].length; j++) {
				System.out.print(ACL[i][j]);
			}
			System.out.println();
		}
	}

}
