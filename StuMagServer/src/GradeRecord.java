import java.io.Serializable;
//³É¼¨¼ÇÂ¼Àà
public class GradeRecord implements Serializable{

	public String sno;
	public String cno;
	public int grade;
	
	public GradeRecord() {}
	
	public GradeRecord(String sno, String cno, int grade) {
		this.sno = sno;
		this.cno = cno;
		this.grade = grade;
	}

	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}

}
