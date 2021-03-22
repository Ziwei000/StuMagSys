import java.sql.*;
import java.util.Scanner;
import javax.swing.*;
//所有对数据库的操作
public class JDBC {

	public static Connection conn = null;
	public static PreparedStatement stmt = null;
	public static ResultSet rs = null;

	public static int accessAdminId = 000000000;
	public static int adminId = 100000000;
	public static int teacherId = 200000000;
	public static int studentId = 400000005;
    //连接数据库
	public static void connect() {

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=InformationManagement";
			conn = DriverManager.getConnection(URL, "nan", "3150604032");//获取连接
			System.out.println("数据库已连接成功！");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
     //插入学校
	public static void insertSchool(String schoolno, String schoolname, String president) {
		try {

			stmt = conn.prepareStatement("insert into School values(?,?,?)");//查询语句
			stmt.setString(1, schoolno);//获取schoolno字段
			stmt.setString(2, schoolname);//获取schoolname字段
			stmt.setString(3, president);//获取president字段
			stmt.executeUpdate();//更新数据库
			System.out.println("学院插入成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
//插入系
	public static void insertDept(String deptno, String deptname, String schoolno, String dean) {
		try {

			stmt = conn.prepareStatement("insert into Dept(Deptno,Deptname,Schoolno,Dean) values(?,?,?,?)");
			stmt.setString(1, deptno);//获取deptno字段
			stmt.setString(2, deptname);//获取deptname段
			stmt.setString(3, schoolno);//获取schoolno字段
			stmt.setString(4, dean);//获取dean字段
			stmt.executeUpdate();//更新数据库
			System.out.println("系插入成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    //插入专业
	public static void insertMajor(String majorno, String majorname, String deptno) {
		try {

			stmt = conn.prepareStatement("insert into Major(Majorno,Majorname,Deptno) values(?,?,?)");
			stmt.setString(1, majorno);//获取majorno字段
			stmt.setString(2, majorname);//获取majorname字段
			stmt.setString(3, deptno);//获取deptno字段
			stmt.executeUpdate();//更新数据库
			System.out.println("专业插入成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    //插入班级
	public static void insertClass(String classname, String cyear, String majorno) {
		try {

			stmt = conn.prepareStatement("insert into Class(Classname,CYear,Majorno) values(?,?,?)");
			stmt.setString(1, classname);//获取classname字段
			stmt.setString(2, cyear);//获取cyear字段
			stmt.setString(3, majorno);//获取majorno字段
			stmt.executeUpdate();//更新数据库
			System.out.println("班级插入成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    //插入教师信息
	public static int insertTeacher(String tname, String sex, String id, String tel, String pos, String deptno) {
		try {

			stmt = conn.prepareStatement("insert into Teacher values(?,?,?,?,?,?,?)");
			stmt.setString(1, new Integer(++teacherId).toString());//教师编号从teacherid开始逐次+1
			stmt.setString(2, tname);//获取tname字段
			stmt.setString(3, sex);//获取sex字段
			stmt.setString(4, id);//获取id字段
			stmt.setString(5, tel);//获取tel字段
			stmt.setString(6, pos);//获取pos字段
			stmt.setString(7, deptno);//获取deptno字段

			stmt.executeUpdate();//更新数据库
			new JOptionPane("您已注册成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teacherId;//返回新申请的教师id

	}
    //插入学生信息
	public static int insertStudent(String name, String sex, String id, String tel, String pos, String classno) {
		try {

			stmt = conn.prepareStatement("insert into Student values(?,?,?,?,?,?,?)");//执行sql语句
			stmt.setString(2, name);//获取tname字段
			stmt.setString(3, sex);//获取sex字段
			stmt.setString(4, id);//获取id字段
			stmt.setString(5, tel);//获取tel字段
			stmt.setString(6, pos);//获取pos字段
			stmt.setString(7, classno);//获取classno字段

			stmt.executeUpdate();//更新数据库

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return studentId;//返回新申请的学生id

	}
//关闭对数据库的所有连接
	public static void end() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//查找用户
	public static boolean findUser(String username, String password, String role) {

		try {

			stmt = conn.prepareStatement("select * from Register where Username = ? and Code = ? and RoleName = ?");
			stmt.setString(1, username);//获取tname字段
			stmt.setString(2, password);//获取tname字段
			stmt.setString(3, role);//获取tname字段
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			if(rs.next()) return true;
			else return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	//ResultSet类是一种数据库查询结果存储类，就是当查询数据库的时候，
	//可以将查询的结果放在具体的ResultSet对象中，
	//其实我们把这种存储查询结果的ResultSet对象叫做ResultSet结果集。
   //查找教师表
	public static ResultSet findTeacher(String username) {
		try {

			String statement = "select Tname,Tsex,Tid,Deptname,Schoolname,Ttel,Tpos from Teacher,Dept,School  where Tno = ? and Dept.Deptno = Teacher.Deptno and Dept.Schoolno = School.Schoolno";
			stmt = conn.prepareStatement(statement);//执行sql语句
			stmt.setString(1, username);//获取教师姓名
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    //查找学生
	public static ResultSet findStudent(String username) {
		try {

			String statement = "select Sname,Ssex,Sid,Student.Classname,Majorname,CYear,Deptname,Schoolname,Stel,Spos from Student,Class,Dept,School,Major  where Sno = ? and Student.Classname = Class.Classname and Class.Majorno = Major.Majorno and Major.Deptno = Dept.Deptno and Dept.Schoolno = School.Schoolno";
			stmt = conn.prepareStatement(statement);//执行sql语句
			stmt.setString(1, username);
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
   //修改密码
	public static String setCode(String username, String text) {
		try {
				stmt = conn.prepareStatement("update Register set Code = ?  where Username = ?");//执行sql语句
			//	String textencrypt=EncryptionByMD5.getMD5(text.getBytes());
				stmt.setString(1, text);
			stmt.setString(2, username);
			stmt.executeUpdate();//更新数据库
			return "修改密码成功";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "修改密码失败";
	}
      //查找课程
	public static ResultSet findLessons() {
		try {

			stmt = conn.prepareStatement("select * from Course");//执行sql语句																																													
			return stmt.executeQuery();//产生单个结果集，用于查询语句

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    //插入课程
	public static void insertCourse(String cno, String cname, String cpro, int credit) {
		try {
			stmt = conn.prepareStatement("insert into Course values(?,?,?,?)");//执行sql语句
			stmt.setString(1, cno);
			stmt.setString(2, cname);
			stmt.setString(3, cpro);
			stmt.setInt(4, credit);
			stmt.executeUpdate();//更新数据库
			System.out.println("课程插入成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//添加教师授课信息
	public static String insertCourseArrange(String tno, String cno, String classname) {
		try {

			stmt = conn.prepareStatement("insert into Teacher_Course values(?, ?, ?)");
			stmt.setString(1, tno);
			stmt.setString(2, cno);
			stmt.setString(3, classname);
			stmt.executeUpdate();//更新数据库
			System.out.println("课程安排记录插入成功！");
			return "课程安排记录插入成功！";

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "课程安排记录插入不成功！";
	}
	 //查看该教师所教的所有课程
	public static ResultSet findSpecCourse(String id) {
		try {
			String temp = "";
			stmt = conn.prepareStatement(
					"select distinct Cname from Teacher_Course,Course where Tno = ? and Teacher_Course.Cno = Course.Cno");
			stmt.setString(1, id);
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//查看是否已选该课程
	public static String chooseCourse(String id, String cno) {
		try {

			stmt = conn.prepareStatement("insert into Student_Course(Sno,Cno) values(?, ?)");
			stmt.setString(1, id);
			stmt.setString(2, cno);
			stmt.executeUpdate();//更新数据库
			return "选课成功";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "选课失败";
	}
    //查看成绩
	public static ResultSet findGrade(String id) {
		try {

			stmt = conn.prepareStatement(
					"select Cname,Cpro,Ccredit,Grade from Course,Student_Course where Sno = ? and Student_Course.Cno = Course.Cno");
			stmt.setString(1, id);
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//查看选择了该课程的所有学生
	public static ResultSet findSUsingCourse(String cno) {
		try {

			stmt = conn.prepareStatement(
					"select Student.Sno,Sname,Ssex,Grade from Student,Student_Course where Cno = ? and Student.Sno = Student_Course.Sno");
			stmt.setString(1, cno);
			return stmt.executeQuery();//产生单个结果集，用于查询语句
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    //修改成绩
	public static String setGrade(String sno, String cno, int grade) {
		try {

			stmt = conn.prepareStatement("update Student_Course set Grade = ? where Sno = ? and cno = ?");
			stmt.setInt(1, grade);
			stmt.setString(2, sno);
			stmt.setString(3, cno);
			stmt.executeUpdate();//更新数据库
			return "成绩录入成功";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "成绩录入失败";
	}
    //查看已选课程
	public static boolean findSChoosedCourse(String id, String cno) {

		try {

			stmt = conn.prepareStatement("select * from Student_Course where Sno = ? and Cno = ?");
			stmt.setString(1, id);
			stmt.setString(2, cno);
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
   //删除已选课程
	public static String deleteChoosedCourse(String id, String cno) {
		try {
			stmt = conn.prepareStatement("delete from Student_Course where Sno = ? and Cno = ?");
			stmt.setString(1, id);
			stmt.setString(2, cno);
			stmt.executeUpdate();//更新数据库
			return "退选成功";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "退选失败";
	}
    //根据课程名查找课程号
	public static String cnameFindCno(String cname) {
		try {
			stmt = conn.prepareStatement("select Cno from Course where Cname = ?");
			stmt.setString(1, cname);
			rs = stmt.executeQuery();//产生单个结果集，用于查询语句
			if (rs.next()) {
				return rs.getString("Cno");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	  //插入登录信息
	public static void insertRegister(int no, String password, String string) {
		try {

			stmt = conn.prepareStatement("insert into Register values(?,?,?)");
			stmt.setString(1, "" + no);
			stmt.setString(2, password);
			stmt.setString(3, string);
			stmt.executeUpdate();//更新数据库
			System.out.println("教师注册成功！");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
