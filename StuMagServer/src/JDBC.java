import java.sql.*;
import java.util.Scanner;
import javax.swing.*;
//���ж����ݿ�Ĳ���
public class JDBC {

	public static Connection conn = null;
	public static PreparedStatement stmt = null;
	public static ResultSet rs = null;

	public static int accessAdminId = 000000000;
	public static int adminId = 100000000;
	public static int teacherId = 200000000;
	public static int studentId = 400000005;
    //�������ݿ�
	public static void connect() {

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=InformationManagement";
			conn = DriverManager.getConnection(URL, "nan", "3150604032");//��ȡ����
			System.out.println("���ݿ������ӳɹ���");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
     //����ѧУ
	public static void insertSchool(String schoolno, String schoolname, String president) {
		try {

			stmt = conn.prepareStatement("insert into School values(?,?,?)");//��ѯ���
			stmt.setString(1, schoolno);//��ȡschoolno�ֶ�
			stmt.setString(2, schoolname);//��ȡschoolname�ֶ�
			stmt.setString(3, president);//��ȡpresident�ֶ�
			stmt.executeUpdate();//�������ݿ�
			System.out.println("ѧԺ����ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
//����ϵ
	public static void insertDept(String deptno, String deptname, String schoolno, String dean) {
		try {

			stmt = conn.prepareStatement("insert into Dept(Deptno,Deptname,Schoolno,Dean) values(?,?,?,?)");
			stmt.setString(1, deptno);//��ȡdeptno�ֶ�
			stmt.setString(2, deptname);//��ȡdeptname��
			stmt.setString(3, schoolno);//��ȡschoolno�ֶ�
			stmt.setString(4, dean);//��ȡdean�ֶ�
			stmt.executeUpdate();//�������ݿ�
			System.out.println("ϵ����ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    //����רҵ
	public static void insertMajor(String majorno, String majorname, String deptno) {
		try {

			stmt = conn.prepareStatement("insert into Major(Majorno,Majorname,Deptno) values(?,?,?)");
			stmt.setString(1, majorno);//��ȡmajorno�ֶ�
			stmt.setString(2, majorname);//��ȡmajorname�ֶ�
			stmt.setString(3, deptno);//��ȡdeptno�ֶ�
			stmt.executeUpdate();//�������ݿ�
			System.out.println("רҵ����ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    //����༶
	public static void insertClass(String classname, String cyear, String majorno) {
		try {

			stmt = conn.prepareStatement("insert into Class(Classname,CYear,Majorno) values(?,?,?)");
			stmt.setString(1, classname);//��ȡclassname�ֶ�
			stmt.setString(2, cyear);//��ȡcyear�ֶ�
			stmt.setString(3, majorno);//��ȡmajorno�ֶ�
			stmt.executeUpdate();//�������ݿ�
			System.out.println("�༶����ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    //�����ʦ��Ϣ
	public static int insertTeacher(String tname, String sex, String id, String tel, String pos, String deptno) {
		try {

			stmt = conn.prepareStatement("insert into Teacher values(?,?,?,?,?,?,?)");
			stmt.setString(1, new Integer(++teacherId).toString());//��ʦ��Ŵ�teacherid��ʼ���+1
			stmt.setString(2, tname);//��ȡtname�ֶ�
			stmt.setString(3, sex);//��ȡsex�ֶ�
			stmt.setString(4, id);//��ȡid�ֶ�
			stmt.setString(5, tel);//��ȡtel�ֶ�
			stmt.setString(6, pos);//��ȡpos�ֶ�
			stmt.setString(7, deptno);//��ȡdeptno�ֶ�

			stmt.executeUpdate();//�������ݿ�
			new JOptionPane("����ע��ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teacherId;//����������Ľ�ʦid

	}
    //����ѧ����Ϣ
	public static int insertStudent(String name, String sex, String id, String tel, String pos, String classno) {
		try {

			stmt = conn.prepareStatement("insert into Student values(?,?,?,?,?,?,?)");//ִ��sql���
			stmt.setString(2, name);//��ȡtname�ֶ�
			stmt.setString(3, sex);//��ȡsex�ֶ�
			stmt.setString(4, id);//��ȡid�ֶ�
			stmt.setString(5, tel);//��ȡtel�ֶ�
			stmt.setString(6, pos);//��ȡpos�ֶ�
			stmt.setString(7, classno);//��ȡclassno�ֶ�

			stmt.executeUpdate();//�������ݿ�

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return studentId;//�����������ѧ��id

	}
//�رն����ݿ����������
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
//�����û�
	public static boolean findUser(String username, String password, String role) {

		try {

			stmt = conn.prepareStatement("select * from Register where Username = ? and Code = ? and RoleName = ?");
			stmt.setString(1, username);//��ȡtname�ֶ�
			stmt.setString(2, password);//��ȡtname�ֶ�
			stmt.setString(3, role);//��ȡtname�ֶ�
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			if(rs.next()) return true;
			else return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	//ResultSet����һ�����ݿ��ѯ����洢�࣬���ǵ���ѯ���ݿ��ʱ��
	//���Խ���ѯ�Ľ�����ھ����ResultSet�����У�
	//��ʵ���ǰ����ִ洢��ѯ�����ResultSet�������ResultSet�������
   //���ҽ�ʦ��
	public static ResultSet findTeacher(String username) {
		try {

			String statement = "select Tname,Tsex,Tid,Deptname,Schoolname,Ttel,Tpos from Teacher,Dept,School  where Tno = ? and Dept.Deptno = Teacher.Deptno and Dept.Schoolno = School.Schoolno";
			stmt = conn.prepareStatement(statement);//ִ��sql���
			stmt.setString(1, username);//��ȡ��ʦ����
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    //����ѧ��
	public static ResultSet findStudent(String username) {
		try {

			String statement = "select Sname,Ssex,Sid,Student.Classname,Majorname,CYear,Deptname,Schoolname,Stel,Spos from Student,Class,Dept,School,Major  where Sno = ? and Student.Classname = Class.Classname and Class.Majorno = Major.Majorno and Major.Deptno = Dept.Deptno and Dept.Schoolno = School.Schoolno";
			stmt = conn.prepareStatement(statement);//ִ��sql���
			stmt.setString(1, username);
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
   //�޸�����
	public static String setCode(String username, String text) {
		try {
				stmt = conn.prepareStatement("update Register set Code = ?  where Username = ?");//ִ��sql���
			//	String textencrypt=EncryptionByMD5.getMD5(text.getBytes());
				stmt.setString(1, text);
			stmt.setString(2, username);
			stmt.executeUpdate();//�������ݿ�
			return "�޸�����ɹ�";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "�޸�����ʧ��";
	}
      //���ҿγ�
	public static ResultSet findLessons() {
		try {

			stmt = conn.prepareStatement("select * from Course");//ִ��sql���																																													
			return stmt.executeQuery();//������������������ڲ�ѯ���

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    //����γ�
	public static void insertCourse(String cno, String cname, String cpro, int credit) {
		try {
			stmt = conn.prepareStatement("insert into Course values(?,?,?,?)");//ִ��sql���
			stmt.setString(1, cno);
			stmt.setString(2, cname);
			stmt.setString(3, cpro);
			stmt.setInt(4, credit);
			stmt.executeUpdate();//�������ݿ�
			System.out.println("�γ̲���ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//��ӽ�ʦ�ڿ���Ϣ
	public static String insertCourseArrange(String tno, String cno, String classname) {
		try {

			stmt = conn.prepareStatement("insert into Teacher_Course values(?, ?, ?)");
			stmt.setString(1, tno);
			stmt.setString(2, cno);
			stmt.setString(3, classname);
			stmt.executeUpdate();//�������ݿ�
			System.out.println("�γ̰��ż�¼����ɹ���");
			return "�γ̰��ż�¼����ɹ���";

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "�γ̰��ż�¼���벻�ɹ���";
	}
	 //�鿴�ý�ʦ���̵����пγ�
	public static ResultSet findSpecCourse(String id) {
		try {
			String temp = "";
			stmt = conn.prepareStatement(
					"select distinct Cname from Teacher_Course,Course where Tno = ? and Teacher_Course.Cno = Course.Cno");
			stmt.setString(1, id);
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//�鿴�Ƿ���ѡ�ÿγ�
	public static String chooseCourse(String id, String cno) {
		try {

			stmt = conn.prepareStatement("insert into Student_Course(Sno,Cno) values(?, ?)");
			stmt.setString(1, id);
			stmt.setString(2, cno);
			stmt.executeUpdate();//�������ݿ�
			return "ѡ�γɹ�";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ѡ��ʧ��";
	}
    //�鿴�ɼ�
	public static ResultSet findGrade(String id) {
		try {

			stmt = conn.prepareStatement(
					"select Cname,Cpro,Ccredit,Grade from Course,Student_Course where Sno = ? and Student_Course.Cno = Course.Cno");
			stmt.setString(1, id);
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//�鿴ѡ���˸ÿγ̵�����ѧ��
	public static ResultSet findSUsingCourse(String cno) {
		try {

			stmt = conn.prepareStatement(
					"select Student.Sno,Sname,Ssex,Grade from Student,Student_Course where Cno = ? and Student.Sno = Student_Course.Sno");
			stmt.setString(1, cno);
			return stmt.executeQuery();//������������������ڲ�ѯ���
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    //�޸ĳɼ�
	public static String setGrade(String sno, String cno, int grade) {
		try {

			stmt = conn.prepareStatement("update Student_Course set Grade = ? where Sno = ? and cno = ?");
			stmt.setInt(1, grade);
			stmt.setString(2, sno);
			stmt.setString(3, cno);
			stmt.executeUpdate();//�������ݿ�
			return "�ɼ�¼��ɹ�";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "�ɼ�¼��ʧ��";
	}
    //�鿴��ѡ�γ�
	public static boolean findSChoosedCourse(String id, String cno) {

		try {

			stmt = conn.prepareStatement("select * from Student_Course where Sno = ? and Cno = ?");
			stmt.setString(1, id);
			stmt.setString(2, cno);
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
   //ɾ����ѡ�γ�
	public static String deleteChoosedCourse(String id, String cno) {
		try {
			stmt = conn.prepareStatement("delete from Student_Course where Sno = ? and Cno = ?");
			stmt.setString(1, id);
			stmt.setString(2, cno);
			stmt.executeUpdate();//�������ݿ�
			return "��ѡ�ɹ�";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "��ѡʧ��";
	}
    //���ݿγ������ҿγ̺�
	public static String cnameFindCno(String cname) {
		try {
			stmt = conn.prepareStatement("select Cno from Course where Cname = ?");
			stmt.setString(1, cname);
			rs = stmt.executeQuery();//������������������ڲ�ѯ���
			if (rs.next()) {
				return rs.getString("Cno");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	  //�����¼��Ϣ
	public static void insertRegister(int no, String password, String string) {
		try {

			stmt = conn.prepareStatement("insert into Register values(?,?,?)");
			stmt.setString(1, "" + no);
			stmt.setString(2, password);
			stmt.setString(3, string);
			stmt.executeUpdate();//�������ݿ�
			System.out.println("��ʦע��ɹ���");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
