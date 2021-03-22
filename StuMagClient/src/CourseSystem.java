import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
//课程系统界面
public class CourseSystem extends JFrame {

	String id = null;
	private Net net;

	public CourseSystem(Net net, String identity) {
		this.id = identity;
		this.net = net;
	}

	public void showTCourseWindow() {//教师“江大选课”窗口
		new TCourseWindow();
	}

	public void showSCourseWindow() {//学生“江大选课”窗口
		new SCourseWindow();
	}
//教师对学生成绩进行录入的窗口
	private class TCourseWindow extends JFrame {

		JList showCourse = new JList();
		JButton input = new JButton("成绩录入（修改）");
		String temp = null;

		TCourseWindow() {//窗口显示设置
			super("江大选课");
			
			Box box = Box.createVerticalBox();
			Box box2 = Box.createHorizontalBox();

			showCourse.setForeground(Color.GRAY);

			input.addActionListener(new ActionListener() {//建立成绩录入监听
				public void actionPerformed(ActionEvent arg0) {
					if (showCourse.getSelectedIndex() >= 0) {
						temp = (String) showCourse.getSelectedValue();
					} else {
						JOptionPane.showMessageDialog(null, "请先选择课程");
						return;
					}
					String temp2 = ConnectToServer.cnameFindCno(net, temp);//根据课程名查课程号
					if (temp2 != null) {
						new SetGradeWindow(temp2);
					}
				}
			});

			String[] result = ConnectToServer.findSpecCourse(net, id);//请求查找教师的所授课程
			showCourse.setListData(result);

			box2.add(input);
			box.add(box2);

			box.add(new Label("我的课程"));
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
//学生课程界面
	private class SCourseWindow extends JFrame {

		String cno = "1";
		JButton quickCheck = new JButton("快速选课");
		JButton commit = new JButton("选课提交");
		JButton delete = new JButton("退选");
		JButton myGrade = new JButton("我的成绩");
		JCheckBox[] cb = new JCheckBox[10];
		int xiaBiao = 0;
		String tmpCno = null;
		String tmpProperty = null;

		String checked = "已选";
		String nochecked = "未选";

		Box CourseCode = Box.createVerticalBox();
		Box CourseProperty = Box.createVerticalBox();
       //学生的江大选课界面
		SCourseWindow() {
			super("江大选课");

			JPanel content = (JPanel) this.getContentPane();
			content.setLayout(new FlowLayout());

			Box CourseName = Box.createVerticalBox();
			Box Credit = Box.createVerticalBox();
			Box isChecked = Box.createVerticalBox();
			Box totalColumn = Box.createHorizontalBox();
			Box buttons = Box.createHorizontalBox();

			CourseCode.add(new Label("课程代码"));
			CourseName.add(new Label("课程名称"));
			CourseProperty.add(new Label("课程性质"));
			Credit.add(new Label("学分"));
			isChecked.add(new Label("选否"));

			String[][] courses = ConnectToServer.findLessons(net);//向服务器端请求查看课程信息

			int i = 0;
			while (true) {
				tmpCno = courses[i][0];
				if(tmpCno == null || tmpCno.equals("")) break;
				cb[xiaBiao] = new JCheckBox(tmpCno);
				cb[xiaBiao].setFont(new Font("宋体", Font.BOLD, 40));

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
		//删除选课信息
		protected void delete() {
			boolean flag = false;
			for (int i = 0; i < xiaBiao; i++) {
				if (cb[i].isSelected()) {
					JCheckBox cb =  (JCheckBox) CourseCode.getComponents()[i + 1];
					cno = cb.getText();
					System.out.println(cno);
					Boolean b = ConnectToServer.findSChoosedCourse(net, id, cno);
					if (b) {
						ConnectToServer.deleteChoosedCourse(net, id, cno);//向服务器端发送请求
						flag = true;
					}
				}
			}
			if (flag) {
				this.setVisible(false);//原窗口隐藏
				JOptionPane.showMessageDialog(null, "退选成功", "提示", JOptionPane.INFORMATION_MESSAGE);
			}

		}
       
		private void commit() {
			for (int i = 0; i < xiaBiao; i++) {
				if (cb[i].isSelected()) {
					JCheckBox label = (JCheckBox) CourseCode.getComponents()[i + 1];
					cno = label.getText();
					boolean b = ConnectToServer.findSChoosedCourse(net, id, cno);//查找该学生已选课程
					if (!b) {
						ConnectToServer.chooseCourse(net, id, cno);
					}
				}
			}
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "选课成功", "提示", JOptionPane.INFORMATION_MESSAGE);
		}

		private void quickCommit() {
			for (int i = 0; i < xiaBiao; i++) {
				JCheckBox lcno =  (JCheckBox) CourseCode.getComponents()[i + 1];
				Label lproperty = (Label) CourseProperty.getComponents()[i + 1];
				cno = lcno.getText();
				tmpProperty = lproperty.getText();
				boolean b = ConnectToServer.findSChoosedCourse(net, id, cno);
				if (!b && tmpProperty.equals("必修")) {
					ConnectToServer.chooseCourse(net, id, cno);
				}
			}
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "选课成功", "提示", JOptionPane.INFORMATION_MESSAGE);
		}

		private void getGrade() {
			new ShowGradeWindow(id).launchFrame();

		}
	}
//成绩录入界面
	private class SetGradeWindow extends JFrame {

		String cno = null;
		String sno = null;

		JList infoStudent = new JList();
		JTextField gradeInput = new JTextField(5);
		String[] result = new String[10];

		public SetGradeWindow(String cno) {
			super("成绩录入界面");
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
			container.add(new Label("请在先从表中选中一个学生，再在下面的输入框中输入成绩："));
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
				JOptionPane.showMessageDialog(null, "请先选择学生");
				return;
			}
			String[] infor = result[i].split("  ");
			sno = infor[0];
			int grade = Integer.parseInt(gradeInput.getText());
			if (grade < 0 || grade > 100) {
				JOptionPane.showMessageDialog(null, "成绩不能小于0或大于100！", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ConnectToServer.setGrade(net, sno, cno, Integer.parseInt(gradeInput.getText()));

			this.repaint();
		}

	}

	private class ShowGradeWindow extends JFrame {

		String sno = null;

		ShowGradeWindow(String sno) {
			super("我的成绩");
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
