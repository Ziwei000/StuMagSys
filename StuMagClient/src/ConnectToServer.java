import java.awt.Label;
import java.sql.*;
import java.util.Scanner;
import javax.swing.*;

public class ConnectToServer {

	public static ResultSet rs = null;
    //添加教师授课信息
	public static void insertCourseArrange(Net net, String tno, String cno, String classname) {
		String str = null;
		net.sendString("insertCourseArrange");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(tno);//教师号
		net.sendString(cno);//课程号
		net.sendString(classname);//课程名称
		str = net.recvString();
	//	JOptionPane.showMessageDialog(null, net.recvString(), "提示", JOptionPane.INFORMATION_MESSAGE);
	}
    //选课操作
	public static void chooseCourse(Net net, String id, String cno) {
		String str = null;
		net.sendString("chooseCourse");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(id);//学生学号
		net.sendString(cno);//课程号
		str = net.recvString();//选课结果
		// JOptionPane.showMessageDialog(null, net.recvString(), "提示",
		// JOptionPane.INFORMATION_MESSAGE);

	}
   //录入成绩
	public static void setGrade(Net net, String sno, String cno, int grade) {
		String str = null;
		net.sendString("setGrade");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(sno);//学号
		net.sendString(cno);//课程号
		net.sendInt(grade);//成绩
		str = net.recvString();//录入结果
		JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);

	}
   //查看是否已选该课程
	public static boolean findSChoosedCourse(Net net, String id, String cno) {
		String str = null;
		net.sendString("findSChoosedCourse");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		net.sendString(id);//学号
		net.sendString(cno);//课程号
		str = net.recvString();//结果
		System.out.println(str);
		return Boolean.parseBoolean(str);
	}
    //删除已选的某个课程
	public static void deleteChoosedCourse(Net net, String id, String cno) {
		String str = null;
		net.sendString("deleteChoosedCourse");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(id);//学号
		net.sendString(cno);//课程号
		str = net.recvString();//结果
		// JOptionPane.showMessageDialog(null, net.recvString(), "提示",
		// JOptionPane.INFORMATION_MESSAGE);

	}
    //根据课程名查课程号
	public static String cnameFindCno(Net net, String cname) {
		String str = null;
		net.sendString("cnameFindCno");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		net.sendString(cname);//课程名称
		str = net.recvString();//结果
		return str;
	}
    //查找权限
	public static String findAccess(Net net, String tn, String r) {
		String str = null;
		net.sendString("findAccess");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		//访问权限表查看权限
		net.sendString(tn);
		net.sendString(r);
		str = net.recvString();
		return str;
	}
    //更改权限
	public static void changeAccess(Net net, String tn, String r, String op) {
		String str = null;
		net.sendString("changeAccess");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		net.sendString(tn);//表
		net.sendString(r);//角色
		net.sendString(op);//操作
		str = net.recvString();//接收结果
		// JOptionPane.showMessageDialog(null, net.recvString(), "提示",
		// JOptionPane.INFORMATION_MESSAGE);
	}
    //查看某学生所选所有课程的成绩
	public static Box showGrade(Net net, String id) {
		String temp = null;
		String cno = null;
        //对相应的表进行访问控制
		net.sendString("showGrade");
		String str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		//若有访问权限则查表
		net.sendString(id);//发送学生id
		Box CourseName = Box.createVerticalBox();
		Box CourseProperty = Box.createVerticalBox();
		Box Credit = Box.createVerticalBox();
		Box Grade = Box.createVerticalBox();
		Box totalColumn = Box.createHorizontalBox();

		CourseName.add(new Label("课程名称"));
		CourseProperty.add(new Label("课程性质"));
		Credit.add(new Label("学分"));
		Grade.add(new Label("成绩"));

		while (true) {//接收课程信息
			temp = net.recvString();//接收课程名称
			if (temp.equals("end"))
				break;
			CourseName.add(new Label(temp));
			temp = net.recvString();//接收课程性质
			if (temp.equals("end"))
				break;
			CourseProperty.add(new Label(temp));
			temp = net.recvString();//接收学分
			if (temp.equals("end"))
				break;
			Credit.add(new Label(temp));
			temp = net.recvString();//接收成绩
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
//查看选择了该课程的所有学生
	public static String[] findSUsingCourse(Net net, String cno) {
		String str = null;
		net.sendString("findSUsingCourse");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		net.sendString(cno);//发送课程编号
		String[] result = new String[10];//存放选择该课程的学生
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
    //查看该教师所教的所有课程
	public static String[] findSpecCourse(Net net, String id) {
		String str = null;
		net.sendString("findSpecCourse");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		net.sendString(id);//教师号
		String[] result = new String[10];//存放该教师选择的课程
		String temp;
		int i = 0;
		while (true) {//接收服务器得到的查询结果
			temp = net.recvString();
			if (temp.equals("end"))
				break;
			result[i++] = temp;
		}
		return result;
	}
//查找课程信息
	public static String[][] findLessons(Net net) { 
		//访问控制，判断是否有相应访问权限
		String str = null;
		net.sendString("findLessons");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		//有权限
		String[][] result = new String[10][4];
		String temp = "start";
		for (int i = 0; i < 10; i++) {//接收服务器端查找的课程信息
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
//查找教师
	public static void findTeacher(Net net, String username, StringBuffer name, StringBuffer sex, StringBuffer id,
			StringBuffer school, StringBuffer dept, StringBuffer pos, StringBuffer tel) {
		//访问控制，判断是否有相应访问权限
		String str = null;
		net.sendString("findTeacher");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		//有权限
		net.sendString(username);//向服务器发送用户名
		String tmp = net.recvString();//接收查找结果
		if (tmp.equals("没有查找到这个教师")) {
			JOptionPane.showMessageDialog(null, tmp);
			return;
		}
		//输出查询结果到窗口
		name.replace(0, name.length(), tmp);
		sex.replace(0, sex.length(), net.recvString());
		id.replace(0, id.length(), net.recvString());
		school.replace(0, school.length(), net.recvString());
		dept.replace(0, dept.length(), net.recvString());
		tel.replace(0, tel.length(), net.recvString());
		pos.replace(0, pos.length(), net.recvString());
	}
//查找学生
	public static void findStudent(Net net, String username, StringBuffer name, StringBuffer sex, StringBuffer id,
			StringBuffer school, StringBuffer dept, StringBuffer major, StringBuffer classname, StringBuffer cyear,
			StringBuffer pos, StringBuffer tel) {
		//访问控制，判断是否有相应访问权限
		String str = null;
		net.sendString("findStudent");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
        //有权限
		net.sendString(username);//向服务器发送用户名
		String tmp = net.recvString();//接收查找结果
		if (tmp.equals("没有查找到这个学生")) {
			JOptionPane.showMessageDialog(null, tmp);
			return;
		}
		//输出查询结果到窗口
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
    //更改新密码
	public static void setCode(Net net, String username, String text) {
		//访问控制，判断是否有相应访问权限
		String str = null;
		net.sendString("setCode");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		//若有权限，更新密码操作
		net.sendString(username);
		String textencrypt=EncryptionByMD5.getMD5(text.getBytes());
		net.sendString (textencrypt);
		str = net.recvString();//接收是否更改成功的提示
		JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
	}
    //插入教师到数据库
	public static int insertTeacher(Net net, String name, String sex, String password, String id, String tel,
			String pos, String dept) {
		//访问控制，判断是否有相应访问权限
		String str = null;
		net.sendString("insertTeacher");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return -1;
		}
		//若有权限，执行插入操作
		net.sendString(name);
		net.sendString(sex);
		net.sendString(id);
		net.sendString(tel);
		net.sendString(pos);
		net.sendString(dept);
		String passwordencrypt=EncryptionByMD5.getMD5( password.getBytes());
		net.sendString(passwordencrypt);
		int username = net.recvInt();//接收服务器为该用户分配的用户名
		return username;
	}
    //插入学生到数据库
	public static int insertStudent(Net net, String name, String sex, String password, String id, String tel,
			String pos, String classname) {
		//访问控制，判断是否有相应访问权限
		String str = null;
		net.sendString("insertStudent");
		str = net.recvString();
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return -1;
		}
		//若有权限，执行插入操作
		net.sendString(name);
		net.sendString(sex);
		net.sendString(id);
		net.sendString(tel);
		net.sendString(pos);
		net.sendString(classname);
		String passwordencrypt=EncryptionByMD5.getMD5( password.getBytes());//对密码加密
		net.sendString(passwordencrypt);
		int username = net.recvInt();//接收服务器为该用户分配的用户名
		return username;
	}
	//验证用户名与密码是否正确
	public static boolean findUser(Net net, String username, String password, String role) {
		net.sendString(role);//接收服务器传过来的role字段
		String passwordencrypt=EncryptionByMD5.getMD5(password.getBytes());//对输入的密码加密
		net.sendString(username);//向服务器发送用户键入的username
		net.sendString(passwordencrypt);//向服务器发送加密后的password
		boolean b = net.recvBool();//接受服务器发送的判断结果
		return b;
	}
	//更改密码时判断旧密码是否正确
	public static boolean PasswordIsCorrect(Net net, String username, String password) {
		String str = null;
		net.sendString("PasswordIsCorrect");//向服务器发送该字段请求对登陆表的访问
		str = net.recvString();//接收服务器发送的访问控制结果，判断是否有相应的权限
		if (str != null && str.equals("无权限")) {
			JOptionPane.showMessageDialog(null, "抱歉，您没有这个权限", "提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		//如果有相应的访问权限则向服务器请求验证密码的正确性
		net.sendString(username);
		String passwordencrypt=EncryptionByMD5.getMD5( password.getBytes());
		net.sendString( passwordencrypt);
		boolean b = net.recvBool();//接收服务器的响应
		return b;
	}

}
