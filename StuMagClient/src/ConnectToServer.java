import java.awt.Label;
import java.sql.*;
import java.util.Scanner;
import javax.swing.*;

public class ConnectToServer {

	public static ResultSet rs = null;
    //��ӽ�ʦ�ڿ���Ϣ
	public static void insertCourseArrange(Net net, String tno, String cno, String classname) {
		String str = null;
		net.sendString("insertCourseArrange");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(tno);//��ʦ��
		net.sendString(cno);//�γ̺�
		net.sendString(classname);//�γ�����
		str = net.recvString();
	//	JOptionPane.showMessageDialog(null, net.recvString(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}
    //ѡ�β���
	public static void chooseCourse(Net net, String id, String cno) {
		String str = null;
		net.sendString("chooseCourse");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(id);//ѧ��ѧ��
		net.sendString(cno);//�γ̺�
		str = net.recvString();//ѡ�ν��
		// JOptionPane.showMessageDialog(null, net.recvString(), "��ʾ",
		// JOptionPane.INFORMATION_MESSAGE);

	}
   //¼��ɼ�
	public static void setGrade(Net net, String sno, String cno, int grade) {
		String str = null;
		net.sendString("setGrade");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(sno);//ѧ��
		net.sendString(cno);//�γ̺�
		net.sendInt(grade);//�ɼ�
		str = net.recvString();//¼����
		JOptionPane.showMessageDialog(null, str, "��ʾ", JOptionPane.INFORMATION_MESSAGE);

	}
   //�鿴�Ƿ���ѡ�ÿγ�
	public static boolean findSChoosedCourse(Net net, String id, String cno) {
		String str = null;
		net.sendString("findSChoosedCourse");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		net.sendString(id);//ѧ��
		net.sendString(cno);//�γ̺�
		str = net.recvString();//���
		System.out.println(str);
		return Boolean.parseBoolean(str);
	}
    //ɾ����ѡ��ĳ���γ�
	public static void deleteChoosedCourse(Net net, String id, String cno) {
		String str = null;
		net.sendString("deleteChoosedCourse");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(id);//ѧ��
		net.sendString(cno);//�γ̺�
		str = net.recvString();//���
		// JOptionPane.showMessageDialog(null, net.recvString(), "��ʾ",
		// JOptionPane.INFORMATION_MESSAGE);

	}
    //���ݿγ�����γ̺�
	public static String cnameFindCno(Net net, String cname) {
		String str = null;
		net.sendString("cnameFindCno");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		net.sendString(cname);//�γ�����
		str = net.recvString();//���
		return str;
	}
    //����Ȩ��
	public static String findAccess(Net net, String tn, String r) {
		String str = null;
		net.sendString("findAccess");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		//����Ȩ�ޱ�鿴Ȩ��
		net.sendString(tn);
		net.sendString(r);
		str = net.recvString();
		return str;
	}
    //����Ȩ��
	public static void changeAccess(Net net, String tn, String r, String op) {
		String str = null;
		net.sendString("changeAccess");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(tn);//��
		net.sendString(r);//��ɫ
		net.sendString(op);//����
		str = net.recvString();//���ս��
		// JOptionPane.showMessageDialog(null, net.recvString(), "��ʾ",
		// JOptionPane.INFORMATION_MESSAGE);
	}
    //�鿴ĳѧ����ѡ���пγ̵ĳɼ�
	public static Box showGrade(Net net, String id) {
		String temp = null;
		String cno = null;
        //����Ӧ�ı���з��ʿ���
		net.sendString("showGrade");
		String str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		//���з���Ȩ������
		net.sendString(id);//����ѧ��id
		Box CourseName = Box.createVerticalBox();
		Box CourseProperty = Box.createVerticalBox();
		Box Credit = Box.createVerticalBox();
		Box Grade = Box.createVerticalBox();
		Box totalColumn = Box.createHorizontalBox();

		CourseName.add(new Label("�γ�����"));
		CourseProperty.add(new Label("�γ�����"));
		Credit.add(new Label("ѧ��"));
		Grade.add(new Label("�ɼ�"));

		while (true) {//���տγ���Ϣ
			temp = net.recvString();//���տγ�����
			if (temp.equals("end"))
				break;
			CourseName.add(new Label(temp));
			temp = net.recvString();//���տγ�����
			if (temp.equals("end"))
				break;
			CourseProperty.add(new Label(temp));
			temp = net.recvString();//����ѧ��
			if (temp.equals("end"))
				break;
			Credit.add(new Label(temp));
			temp = net.recvString();//���ճɼ�
			if (temp.equals("end"))
				break;
			Grade.add(new Label(temp));
		}

		totalColumn.add(CourseName);
		totalColumn.add(CourseProperty);
		totalColumn.add(Credit);
		totalColumn.add(Grade);

		return totalColumn;
	}
//�鿴ѡ���˸ÿγ̵�����ѧ��
	public static String[] findSUsingCourse(Net net, String cno) {
		String str = null;
		net.sendString("findSUsingCourse");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		net.sendString(cno);//���Ϳγ̱��
		String[] result = new String[10];//���ѡ��ÿγ̵�ѧ��
		String temp;
		int i = 0;
		while (true) {
			temp = net.recvString();
			if (temp.equals("end"))
				break;
			result[i++] = temp;
		}
		return result;
	}
    //�鿴�ý�ʦ���̵����пγ�
	public static String[] findSpecCourse(Net net, String id) {
		String str = null;
		net.sendString("findSpecCourse");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		net.sendString(id);//��ʦ��
		String[] result = new String[10];//��Ÿý�ʦѡ��Ŀγ�
		String temp;
		int i = 0;
		while (true) {//���շ������õ��Ĳ�ѯ���
			temp = net.recvString();
			if (temp.equals("end"))
				break;
			result[i++] = temp;
		}
		return result;
	}
//���ҿγ���Ϣ
	public static String[][] findLessons(Net net) { 
		//���ʿ��ƣ��ж��Ƿ�����Ӧ����Ȩ��
		String str = null;
		net.sendString("findLessons");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		//��Ȩ��
		String[][] result = new String[10][4];
		String temp = "start";
		for (int i = 0; i < 10; i++) {//���շ������˲��ҵĿγ���Ϣ
			for (int j = 0; j < 4; j++) {
				temp = net.recvString();
				if (temp.equals("end"))
					break;
				result[i][j] = temp;
			}
			if (temp.equals("end"))
				break;
		}
		return result;
	}
//���ҽ�ʦ
	public static void findTeacher(Net net, String username, StringBuffer name, StringBuffer sex, StringBuffer id,
			StringBuffer school, StringBuffer dept, StringBuffer pos, StringBuffer tel) {
		//���ʿ��ƣ��ж��Ƿ�����Ӧ����Ȩ��
		String str = null;
		net.sendString("findTeacher");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		//��Ȩ��
		net.sendString(username);//������������û���
		String tmp = net.recvString();//���ղ��ҽ��
		if (tmp.equals("û�в��ҵ������ʦ")) {
			JOptionPane.showMessageDialog(null, tmp);
			return;
		}
		//�����ѯ���������
		name.replace(0, name.length(), tmp);
		sex.replace(0, sex.length(), net.recvString());
		id.replace(0, id.length(), net.recvString());
		school.replace(0, school.length(), net.recvString());
		dept.replace(0, dept.length(), net.recvString());
		tel.replace(0, tel.length(), net.recvString());
		pos.replace(0, pos.length(), net.recvString());
	}
//����ѧ��
	public static void findStudent(Net net, String username, StringBuffer name, StringBuffer sex, StringBuffer id,
			StringBuffer school, StringBuffer dept, StringBuffer major, StringBuffer classname, StringBuffer cyear,
			StringBuffer pos, StringBuffer tel) {
		//���ʿ��ƣ��ж��Ƿ�����Ӧ����Ȩ��
		String str = null;
		net.sendString("findStudent");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
        //��Ȩ��
		net.sendString(username);//������������û���
		String tmp = net.recvString();//���ղ��ҽ��
		if (tmp.equals("û�в��ҵ����ѧ��")) {
			JOptionPane.showMessageDialog(null, tmp);
			return;
		}
		//�����ѯ���������
		name.replace(0, name.length(), tmp);
		sex.replace(0, sex.length(), net.recvString());
		id.replace(0, id.length(), net.recvString());
		school.replace(0, school.length(), net.recvString());
		dept.replace(0, dept.length(), net.recvString());
		major.replace(0, dept.length(), net.recvString());
		classname.replace(0, classname.length(), net.recvString());
		cyear.replace(0, cyear.length(), net.recvString());
		pos.replace(0, pos.length(), net.recvString());
		tel.replace(0, tel.length(), net.recvString());
	}
    //����������
	public static void setCode(Net net, String username, String text) {
		//���ʿ��ƣ��ж��Ƿ�����Ӧ����Ȩ��
		String str = null;
		net.sendString("setCode");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		//����Ȩ�ޣ������������
		net.sendString(username);
		String textencrypt=EncryptionByMD5.getMD5(text.getBytes());
		net.sendString (textencrypt);
		str = net.recvString();//�����Ƿ���ĳɹ�����ʾ
		JOptionPane.showMessageDialog(null, str, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}
    //�����ʦ�����ݿ�
	public static int insertTeacher(Net net, String name, String sex, String password, String id, String tel,
			String pos, String dept) {
		//���ʿ��ƣ��ж��Ƿ�����Ӧ����Ȩ��
		String str = null;
		net.sendString("insertTeacher");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return -1;
		}
		//����Ȩ�ޣ�ִ�в������
		net.sendString(name);
		net.sendString(sex);
		net.sendString(id);
		net.sendString(tel);
		net.sendString(pos);
		net.sendString(dept);
		String passwordencrypt=EncryptionByMD5.getMD5( password.getBytes());
		net.sendString(passwordencrypt);
		int username = net.recvInt();//���շ�����Ϊ���û�������û���
		return username;
	}
    //����ѧ�������ݿ�
	public static int insertStudent(Net net, String name, String sex, String password, String id, String tel,
			String pos, String classname) {
		//���ʿ��ƣ��ж��Ƿ�����Ӧ����Ȩ��
		String str = null;
		net.sendString("insertStudent");
		str = net.recvString();
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return -1;
		}
		//����Ȩ�ޣ�ִ�в������
		net.sendString(name);
		net.sendString(sex);
		net.sendString(id);
		net.sendString(tel);
		net.sendString(pos);
		net.sendString(classname);
		String passwordencrypt=EncryptionByMD5.getMD5( password.getBytes());//���������
		net.sendString(passwordencrypt);
		int username = net.recvInt();//���շ�����Ϊ���û�������û���
		return username;
	}
	//��֤�û����������Ƿ���ȷ
	public static boolean findUser(Net net, String username, String password, String role) {
		net.sendString(role);//���շ�������������role�ֶ�
		String passwordencrypt=EncryptionByMD5.getMD5(password.getBytes());//��������������
		net.sendString(username);//������������û������username
		net.sendString(passwordencrypt);//����������ͼ��ܺ��password
		boolean b = net.recvBool();//���ܷ��������͵��жϽ��
		return b;
	}
	//��������ʱ�жϾ������Ƿ���ȷ
	public static boolean PasswordIsCorrect(Net net, String username, String password) {
		String str = null;
		net.sendString("PasswordIsCorrect");//����������͸��ֶ�����Ե�½��ķ���
		str = net.recvString();//���շ��������͵ķ��ʿ��ƽ�����ж��Ƿ�����Ӧ��Ȩ��
		if (str != null && str.equals("��Ȩ��")) {
			JOptionPane.showMessageDialog(null, "��Ǹ����û�����Ȩ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		//�������Ӧ�ķ���Ȩ�����������������֤�������ȷ��
		net.sendString(username);
		String passwordencrypt=EncryptionByMD5.getMD5( password.getBytes());
		net.sendString( passwordencrypt);
		boolean b = net.recvBool();//���շ���������Ӧ
		return b;
	}

}
