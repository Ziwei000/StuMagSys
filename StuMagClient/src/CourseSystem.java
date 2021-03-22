import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
//�γ�ϵͳ����
public class CourseSystem extends JFrame {

	String id = null;
	private Net net;

	public CourseSystem(Net net, String identity) {
		this.id = identity;
		this.net = net;
	}

	public void showTCourseWindow() {//��ʦ������ѡ�Ρ�����
		new TCourseWindow();
	}

	public void showSCourseWindow() {//ѧ��������ѡ�Ρ�����
		new SCourseWindow();
	}
//��ʦ��ѧ���ɼ�����¼��Ĵ���
	private class TCourseWindow extends JFrame {

		JList showCourse = new JList();
		JButton input = new JButton("�ɼ�¼�루�޸ģ�");
		String temp = null;

		TCourseWindow() {//������ʾ����
			super("����ѡ��");
			
			Box box = Box.createVerticalBox();
			Box box2 = Box.createHorizontalBox();

			showCourse.setForeground(Color.GRAY);

			input.addActionListener(new ActionListener() {//�����ɼ�¼�����
				public void actionPerformed(ActionEvent arg0) {
					if (showCourse.getSelectedIndex() >= 0) {
						temp = (String) showCourse.getSelectedValue();
					} else {
						JOptionPane.showMessageDialog(null, "����ѡ��γ�");
						return;
					}
					String temp2 = ConnectToServer.cnameFindCno(net, temp);//���ݿγ�����γ̺�
					if (temp2 != null) {
						new SetGradeWindow(temp2);
					}
				}
			});

			String[] result = ConnectToServer.findSpecCourse(net, id);//������ҽ�ʦ�����ڿγ�
			showCourse.setListData(result);

			box2.add(input);
			box.add(box2);

			box.add(new Label("�ҵĿγ�"));
			box.add(showCourse);
			box.add(new Label(""));

			this.add(box, BorderLayout.NORTH);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			this.setVisible(true);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

	}
//ѧ���γ̽���
	private class SCourseWindow extends JFrame {

		String cno = "1";
		JButton quickCheck = new JButton("����ѡ��");
		JButton commit = new JButton("ѡ���ύ");
		JButton delete = new JButton("��ѡ");
		JButton myGrade = new JButton("�ҵĳɼ�");
		JCheckBox[] cb = new JCheckBox[10];
		int xiaBiao = 0;
		String tmpCno = null;
		String tmpProperty = null;

		String checked = "��ѡ";
		String nochecked = "δѡ";

		Box CourseCode = Box.createVerticalBox();
		Box CourseProperty = Box.createVerticalBox();
       //ѧ���Ľ���ѡ�ν���
		SCourseWindow() {
			super("����ѡ��");

			JPanel content = (JPanel) this.getContentPane();
			content.setLayout(new FlowLayout());

			Box CourseName = Box.createVerticalBox();
			Box Credit = Box.createVerticalBox();
			Box isChecked = Box.createVerticalBox();
			Box totalColumn = Box.createHorizontalBox();
			Box buttons = Box.createHorizontalBox();

			CourseCode.add(new Label("�γ̴���"));
			CourseName.add(new Label("�γ�����"));
			CourseProperty.add(new Label("�γ�����"));
			Credit.add(new Label("ѧ��"));
			isChecked.add(new Label("ѡ��"));

			String[][] courses = ConnectToServer.findLessons(net);//�������������鿴�γ���Ϣ

			int i = 0;
			while (true) {
				tmpCno = courses[i][0];
				if(tmpCno == null || tmpCno.equals("")) break;
				cb[xiaBiao] = new JCheckBox(tmpCno);
				cb[xiaBiao].setFont(new Font("����", Font.BOLD, 40));

				CourseCode.add(cb[xiaBiao++]);
				CourseName.add(new Label(courses[i][1]));
				CourseProperty.add(new Label(courses[i][2]));
				Credit.add(new Label(courses[i][3]));
				if (ConnectToServer.findSChoosedCourse(net, id, tmpCno)) {
					isChecked.add(new Label(checked));
				} else {
					isChecked.add(new Label(nochecked));
				}
				i++;
			}
    
			totalColumn.add(CourseCode);
			totalColumn.add(CourseName);
			totalColumn.add(CourseProperty);
			totalColumn.add(Credit);
			totalColumn.add(isChecked);

			quickCheck.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					quickCommit();
				}
			});
			commit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					commit();
				}
			});
			delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					delete();
				}
			});
			myGrade.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getGrade();
				}
			});

			buttons.add(quickCheck);
			buttons.add(commit);
			buttons.add(delete);
			buttons.add(myGrade);

			content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
			content.add(totalColumn);
			content.add(buttons);

			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			this.setVisible(true);
			this.setResizable(false);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
		//ɾ��ѡ����Ϣ
		protected void delete() {
			boolean flag = false;
			for (int i = 0; i < xiaBiao; i++) {
				if (cb[i].isSelected()) {
					JCheckBox cb =  (JCheckBox) CourseCode.getComponents()[i + 1];
					cno = cb.getText();
					System.out.println(cno);
					Boolean b = ConnectToServer.findSChoosedCourse(net, id, cno);
					if (b) {
						ConnectToServer.deleteChoosedCourse(net, id, cno);//��������˷�������
						flag = true;
					}
				}
			}
			if (flag) {
				this.setVisible(false);//ԭ��������
				JOptionPane.showMessageDialog(null, "��ѡ�ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}

		}
       
		private void commit() {
			for (int i = 0; i < xiaBiao; i++) {
				if (cb[i].isSelected()) {
					JCheckBox label = (JCheckBox) CourseCode.getComponents()[i + 1];
					cno = label.getText();
					boolean b = ConnectToServer.findSChoosedCourse(net, id, cno);//���Ҹ�ѧ����ѡ�γ�
					if (!b) {
						ConnectToServer.chooseCourse(net, id, cno);
					}
				}
			}
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "ѡ�γɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		}

		private void quickCommit() {
			for (int i = 0; i < xiaBiao; i++) {
				JCheckBox lcno =  (JCheckBox) CourseCode.getComponents()[i + 1];
				Label lproperty = (Label) CourseProperty.getComponents()[i + 1];
				cno = lcno.getText();
				tmpProperty = lproperty.getText();
				boolean b = ConnectToServer.findSChoosedCourse(net, id, cno);
				if (!b && tmpProperty.equals("����")) {
					ConnectToServer.chooseCourse(net, id, cno);
				}
			}
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "ѡ�γɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		}

		private void getGrade() {
			new ShowGradeWindow(id).launchFrame();

		}
	}
//�ɼ�¼�����
	private class SetGradeWindow extends JFrame {

		String cno = null;
		String sno = null;

		JList infoStudent = new JList();
		JTextField gradeInput = new JTextField(5);
		String[] result = new String[10];

		public SetGradeWindow(String cno) {
			super("�ɼ�¼�����");
			this.cno = cno;

			gradeInput.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setGrade();
				}
			});

			infoStudent.setForeground(Color.GRAY);

			gradeInput.setText("");
			result = ConnectToServer.findSUsingCourse(net, cno);
			infoStudent.setListData(result);

			Container container = this.getContentPane();
			container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
			container.add(infoStudent);
			container.add(new Label("�����ȴӱ���ѡ��һ��ѧ������������������������ɼ���"));
			container.add(gradeInput);

			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			this.setBackground(Color.GRAY);
			this.setVisible(true);

		}

		@Override
		public void paint(Graphics g) {
			gradeInput.setText("");
			gradeInput.setText("");
			result = ConnectToServer.findSUsingCourse(net, cno);
			infoStudent.setListData(result);
		}

		private void setGrade() {
			int i = infoStudent.getSelectedIndex();
			if (i < 0) {
				JOptionPane.showMessageDialog(null, "����ѡ��ѧ��");
				return;
			}
			String[] infor = result[i].split("  ");
			sno = infor[0];
			int grade = Integer.parseInt(gradeInput.getText());
			if (grade < 0 || grade > 100) {
				JOptionPane.showMessageDialog(null, "�ɼ�����С��0�����100��", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ConnectToServer.setGrade(net, sno, cno, Integer.parseInt(gradeInput.getText()));

			this.repaint();
		}

	}

	private class ShowGradeWindow extends JFrame {

		String sno = null;

		ShowGradeWindow(String sno) {
			super("�ҵĳɼ�");
			this.sno = sno;
		}

		void launchFrame() {
			Box totalColumn = ConnectToServer.showGrade(net, sno);
			this.add(totalColumn);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			this.setVisible(true);
		}
	}
}
