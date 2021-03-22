import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectToClient {
	//插入课程
	public static void insertCourseArrange(Net net) {
		if (net != null) {
			String tno = net.recvString();//接收教师编号
			String cno = net.recvString();//接收课程编号
			String classname = net.recvString();//接收班级名称
			net.sendString(JDBC.insertCourseArrange(tno, cno, classname));//调用对数据库进行操作的相应函数
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//选择课程
	public static void chooseCourse(Net net) {
		if (net != null) {
			String id = net.recvString();//接收学号
			String cno = net.recvString();//课程代码
			net.sendString(JDBC.chooseCourse(id, cno));//对数据进行相应操作
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//修改成绩
	public static void setGrade(Net net) {
		if (net != null) {
			String sno = net.recvString();
			String cno = net.recvString();
			int grade = net.recvInt();

			net.sendString(JDBC.setGrade(sno, cno, grade));//调用对数据库进行操作的相应函数
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	 //查看是否已选该课程
	public static void findSChoosedCourse(Net net) {//查找已选课程
		if (net != null) {
			String sno = net.recvString();
			String cno = net.recvString();

			net.sendString("" + JDBC.findSChoosedCourse(sno, cno));//调用对数据库进行操作的相应函数
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	 //删除已选的某个课程
	public static void deleteChoosedCourse(Net net) {//删除已选课程
		if (net != null) {
			String id = net.recvString();
			String cno = net.recvString();
			net.sendString(JDBC.deleteChoosedCourse(id, cno));//调用对数据库进行操作的相应函数
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	 //根据课程名查课程号
	public static void cnameFindCno(Net net) {
		if (net != null) {
			String cname = net.recvString();
			net.sendString(JDBC.cnameFindCno(cname));//调用对数据库进行操作的相应函数
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//查看某学生所选所有课程的成绩
	public static void showGrade(Net net) {
		if (net != null) {
			String sno = net.recvString();
			ResultSet rs = JDBC.findGrade(sno);
			try {
				while (rs.next()) {
					net.sendString(rs.getString("Cname"));
					net.sendString(rs.getString("Cpro"));
					net.sendString(rs.getString("Ccredit"));
					net.sendString("" + rs.getInt("Grade"));
				}
				net.sendString("end");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//查看选择了该课程的所有学生
	public static void findSUsingCourse(Net net) {
		String s;
		int i = 0;
		if (net != null) {
			String cno = net.recvString();
			ResultSet rs = JDBC.findSUsingCourse(cno);//调用对数据库进行操作的相应函数
			try {
				while (rs.next()) {
					s = rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4);
					net.sendString(s);
				}
				net.sendString("end");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//查看该教师所教的所有课程
	public static void findSpecCourse(Net net) {
		if (net != null) {
			String id = net.recvString();
			ResultSet rs = JDBC.findSpecCourse(id);//调用对数据库进行操作的相应函数
			try {
				int i = 0;
				while (rs.next()) {
					net.sendString(rs.getString("Cname"));
				}
				net.sendString("end");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//查找课程信息
	public static void findLessons(Net net) {
		if (net != null) {
			ResultSet rs = JDBC.findLessons();//调用对数据库进行操作的相应函数
			try {
				while (rs.next()) {
					net.sendString(rs.getString("Cno"));
					net.sendString(rs.getString("Cname"));
					net.sendString(rs.getString("Cpro"));
					net.sendString(rs.getString("Ccredit"));
				}
				net.sendString("end");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//查找教师
	public static void findTeacher(Net net) { //未测试
		if (net != null) {
			String username = net.recvString();//调用对数据库进行操作的相应函数
			ResultSet rs = JDBC.findTeacher(username);
			try {
				if (rs.next()) {
					net.sendString(rs.getString("Tname"));
					net.sendString(rs.getString("Tsex"));
					net.sendString(rs.getString("Tid"));
					net.sendString(rs.getString("Schoolname"));
					net.sendString(rs.getString("Deptname"));
					net.sendString(rs.getString("Ttel"));
					net.sendString(rs.getString("Tpos"));
				} else {
					net.sendString("没有查找到这个教师");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//查找学生
	public static void findStudent(Net net) {
		if (net != null) {
			String username = net.recvString();
			ResultSet rs = JDBC.findStudent(username);//调用对数据库进行操作的相应函数
			try {
				if (rs.next()) {
					net.sendString(rs.getString("Sname"));
					net.sendString(rs.getString("Ssex"));
					net.sendString(rs.getString("Sid"));
					net.sendString(rs.getString("Schoolname"));
					net.sendString(rs.getString("Deptname"));
					net.sendString(rs.getString("Majorname"));
					net.sendString(rs.getString("Classname"));
					net.sendString(rs.getString("Cyear"));
					net.sendString(rs.getString("Spos"));
					net.sendString(rs.getString("Stel"));
				} else {
					net.sendString("没有查找到这个学生");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "传入网络参数无效");
		}
	}
	//插入教师到数据库
	public static void insertTeacher(Net net) {
		String name = net.recvString();
		String sex = net.recvString();
		String id = net.recvString();
		String tel = net.recvString();
		String pos = net.recvString();
		String dept = net.recvString();
		int no = JDBC.insertTeacher(name,sex,id,tel,pos,dept);
		String password = net.recvString();
		JDBC.insertRegister(no, password, "Teacher");
		net.sendInt(no);
	}
	 //插入学生到数据库
	public static void insertStudent(Net net) {
		String name = net.recvString();
		String sex = net.recvString();
		String id = net.recvString();
		String tel = net.recvString();
		String pos = net.recvString();
		String classname = net.recvString();
		int no = JDBC.insertStudent(name,sex,id,tel,pos,classname);//调用对数据库进行操作的相应函数
		String password = net.recvString();
		JDBC.insertRegister(no, password, "Student");
		net.sendInt(no);
	}
	//验证用户名与密码是否正确
	public static boolean findUser(Net net, String role) {
		String username= net.recvString();
		String password= net.recvString();
		boolean b = JDBC.findUser(username, password, role);//调用对数据库进行操作的相应函数
		net.sendBoolean(b);
		return b;
		 
	}
	//更改密码
	public static void setCode(Net net) {
		String username = net.recvString();
		String password = net.recvString();
		net.sendString(JDBC.setCode(username, password));//调用对数据库进行操作的相应函数
	}
	//更改密码时判断旧密码是否正确
	public static void PasswordIsCorrect(Net net, String role) {
		String username = net.recvString();
		String password = net.recvString();
		boolean b = JDBC.findUser(username, password, role);//调用对数据库进行操作的相应函数
		net.sendBoolean(b);
	}

}
