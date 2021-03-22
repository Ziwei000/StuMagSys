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
    //�ж�ĳ����ɫ�Ƿ��ĳ�����ж���дȨ��
	public static boolean isPermited(String tn, String role2, String op) {
		int i = 0, j = 0;
		if (role != null)
			i = role.indexOf(role2);
		if (tableName != null)
			j = tableName.indexOf(tn);
		if (i < 0 || j < 0) {
			System.out.println("û�ҵ���ɫ�������");
			return false;
		}
		String str = ACL[i][j];
//		if (str.equals(op))
//			return true;
		if (str.contains(op))//��acl�����и�Ȩ�ޣ�������
			return true;
		return false;
	}
    //��Ӧ�ͻ�������ִ�в���Ȩ�޲���
	public static void findAccess(Net net) {
		if(net != null) {
			String tn = net.recvString();//���տͻ��˴���ı���
			String r = net.recvString();//���տͻ��˴���Ľ�ɫ��
			int i = 0, j = 0;
			if (role != null)
				i = role.indexOf(r);
			if (tableName != null)
				j = tableName.indexOf(tn);
			if (i < 0 || j < 0) {
				System.out.println("û�ҵ���ɫ�������");
				net.sendString("û�ҵ���ɫ�������");
				return;
			} else {
				net.sendString(ACL[i][j]);//��ͻ��˻�ӦACL�м�¼��Ȩ��
				return;
			}
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//�޸�Ȩ�޲���
	private static String changeAccess(String tn, String r, String op) {
		// �޸�ACL��
		int i = 0, j = 0;
		if (role != null)
			i = role.indexOf(r);
		if (tableName != null)
			j = tableName.indexOf(tn);
		if (i < 0 || j < 0) {
			System.out.println("û�ҵ���ɫ�������");
			return "�޸�Ȩ��ʧ��";
		}
		ACL[i][j] = op;
		System.out.println("i=" + i + ",j=" + j + ",op=" + op);

		modifyExcel(i + 1, j + 1, op);
//		modifyExcel(0, 0, op);
		return "�޸�Ȩ�޳ɹ�";
	}
	 //��Ӧ�ͻ�������ִ���޸�Ȩ�޲���
	public static void changeAccess(Net net) {
		if(net != null) {
			String tn = net.recvString();
			String r = net.recvString();
			String op = net.recvString();
			net.sendString(changeAccess(tn, r, op));
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	
//�Ѷ�Ȩ�޵��޸ı��浽execel����
	private static void modifyExcel(int i, int j, String op) {
		/*
		 * try { // jxl.Workbook ������ֻ���ģ��������Ҫ�޸�Excel����Ҫ����һ���ɶ��ĸ���������ָ��ԭExcel�ļ�
		 * WritableWorkbook wbe = Workbook.createWorkbook(new File(excelPath), wb);//
		 * ����workbook�ĸ��� WritableSheet sheet = wbe.getSheet(0); // ��ȡ��һ��sheet
		 * WritableCell cell = sheet.getWritableCell(j, i);// ��ȡ��һ����Ԫ�� CellFormat cf =
		 * cell.getCellFormat();// ��ȡ��һ����Ԫ��ĸ�ʽ jxl.write.Label lbl = new
		 * jxl.write.Label(i, j, op);// ����һ����Ԫ���ֵ��Ϊ���޸����ֵ�� lbl.setCellFormat(cf);//
		 * ���޸ĺ�ĵ�Ԫ��ĸ�ʽ�趨�ɸ�ԭ��һ�� sheet.addCell(lbl);// ���Ĺ��ĵ�Ԫ�񱣴浽sheet wbe.write();//
		 * ���޸ı��浽workbook --��һ��Ҫ���� wbe.close();// �ر�workbook���ͷ��ڴ� ---��һ��Ҫ�ͷ��ڴ� } catch
		 * (IOException e) { e.printStackTrace(); } catch (WriteException e) {
		 * e.printStackTrace(); }
		 */

		WritableWorkbook book = null;
		try {
			// Excel����ļ�
			Workbook wb = Workbook.getWorkbook(new File(excelPath));
			// ��һ���ļ��ĸ���������ָ������д�ص�ԭ�ļ�
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

	// ȥ��Excel�ķ���readExcel���÷�������ڲ���Ϊһ��File����
	private static void readExcel() {
		try {
			// ��������������ȡExcel
			is = new FileInputStream(file.getAbsolutePath());
			// jxl�ṩ��Workbook��
			wb = Workbook.getWorkbook(is);
			// ���ҳǩ
			sheet = wb.getSheet(0);

			role = new ArrayList<String>(sheet.getRows() - 1);
			tableName = new ArrayList<String>(sheet.getColumns() - 1);
			ACL = new String[sheet.getRows() - 1][sheet.getColumns() - 1];

			// role���ʼ��
			for (int i = 1; i < sheet.getRows(); i++) {
				role.add(sheet.getCell(0, i).getContents());
			}
			// tableName���ʼ��
			for (int i = 1; i < sheet.getColumns(); i++) {
				tableName.add(sheet.getCell(i, 0).getContents());
			}
			// ACL���ʼ��
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
    //�ڿ���̨���Ȩ�ޱ�
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
