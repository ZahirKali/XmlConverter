package launcher;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "record")
public class Report {
	private int refId;
	private String name;
	private int age;
	private String dob;
	private String income;

	@XmlAttribute(name = "refId")
	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	@XmlElement(name = "age")
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@XmlElement
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	@XmlElement
	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	// for csv file only
	public String getCsvDob() {
		return this.getDob();

	}
	
	@Override
	public String toString(){
		return "Nom : " + name
				+ " age : +" + age
				+ " dob :" + dob;
	}

}