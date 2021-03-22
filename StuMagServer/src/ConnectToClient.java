import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectToClient {
	//����γ�
	public static void insertCourseArrange(Net net) {
		if (net != null) {
			String tno = net.recvString();//���ս�ʦ���
			String cno = net.recvString();//���տγ̱��
			String classname = net.recvString();//���հ༶����
			net.sendString(JDBC.insertCourseArrange(tno, cno, classname));//���ö����ݿ���в�������Ӧ����
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//ѡ��γ�
	public static void chooseCourse(Net net) {
		if (net != null) {
			String id = net.recvString();//����ѧ��
			String cno = net.recvString();//�γ̴���
			net.sendString(JDBC.chooseCourse(id, cno));//�����ݽ�����Ӧ����
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//�޸ĳɼ�
	public static void setGrade(Net net) {
		if (net != null) {
			String sno = net.recvString();
			String cno = net.recvString();
			int grade = net.recvInt();

			net.sendString(JDBC.setGrade(sno, cno, grade));//���ö����ݿ���в�������Ӧ����
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	 //�鿴�Ƿ���ѡ�ÿγ�
	public static void findSChoosedCourse(Net net) {//������ѡ�γ�
		if (net != null) {
			String sno = net.recvString();
			String cno = net.recvString();

			net.sendString("" + JDBC.findSChoosedCourse(sno, cno));//���ö����ݿ���в�������Ӧ����
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	 //ɾ����ѡ��ĳ���γ�
	public static void deleteChoosedCourse(Net net) {//ɾ����ѡ�γ�
		if (net != null) {
			String id = net.recvString();
			String cno = net.recvString();
			net.sendString(JDBC.deleteChoosedCourse(id, cno));//���ö����ݿ���в�������Ӧ����
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	 //���ݿγ�����γ̺�
	public static void cnameFindCno(Net net) {
		if (net != null) {
			String cname = net.recvString();
			net.sendString(JDBC.cnameFindCno(cname));//���ö����ݿ���в�������Ӧ����
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//�鿴ĳѧ����ѡ���пγ̵ĳɼ�
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
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//�鿴ѡ���˸ÿγ̵�����ѧ��
	public static void findSUsingCourse(Net net) {
		String s;
		int i = 0;
		if (net != null) {
			String cno = net.recvString();
			ResultSet rs = JDBC.findSUsingCourse(cno);//���ö����ݿ���в�������Ӧ����
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
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//�鿴�ý�ʦ���̵����пγ�
	public static void findSpecCourse(Net net) {
		if (net != null) {
			String id = net.recvString();
			ResultSet rs = JDBC.findSpecCourse(id);//���ö����ݿ���в�������Ӧ����
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
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//���ҿγ���Ϣ
	public static void findLessons(Net net) {
		if (net != null) {
			ResultSet rs = JDBC.findLessons();//���ö����ݿ���в�������Ӧ����
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
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//���ҽ�ʦ
	public static void findTeacher(Net net) { //δ����
		if (net != null) {
			String username = net.recvString();//���ö����ݿ���в�������Ӧ����
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
					net.sendString("û�в��ҵ������ʦ");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//����ѧ��
	public static void findStudent(Net net) {
		if (net != null) {
			String username = net.recvString();
			ResultSet rs = JDBC.findStudent(username);//���ö����ݿ���в�������Ӧ����
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
					net.sendString("û�в��ҵ����ѧ��");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "�������������Ч");
		}
	}
	//�����ʦ�����ݿ�
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
	 //����ѧ�������ݿ�
	public static void insertStudent(Net net) {
		String name = net.recvString();
		String sex = net.recvString();
		String id = net.recvString();
		String tel = net.recvString();
		String pos = net.recvString();
		String classname = net.recvString();
		int no = JDBC.insertStudent(name,sex,id,tel,pos,classname);//���ö����ݿ���в�������Ӧ����
		String password = net.recvString();
		JDBC.insertRegister(no, password, "Student");
		net.sendInt(no);
	}
	//��֤�û����������Ƿ���ȷ
	public static boolean findUser(Net net, String role) {
		String username= net.recvString();
		String password= net.recvString();
		boolean b = JDBC.findUser(username, password, role);//���ö����ݿ���в�������Ӧ����
		net.sendBoolean(b);
		return b;
		 
	}
	//��������
	public static void setCode(Net net) {
		String username = net.recvString();
		String password = net.recvString();
		net.sendString(JDBC.setCode(username, password));//���ö����ݿ���в�������Ӧ����
	}
	//��������ʱ�жϾ������Ƿ���ȷ
	public static void PasswordIsCorrect(Net net, String role) {
		String username = net.recvString();
		String password = net.recvString();
		boolean b = JDBC.findUser(username, password, role);//���ö����ݿ���в�������Ӧ����
		net.sendBoolean(b);
	}

}
