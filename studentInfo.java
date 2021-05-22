package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class studentInfo {
	
	//student
	private final StringProperty id;
	private final StringProperty lname;
	private final StringProperty fname;
	private final StringProperty extname;
	private final StringProperty mname;
	private final StringProperty gender;
	private final StringProperty bdate;
	private final StringProperty course;
	private final StringProperty yrlevel;
	
	//father
	private final StringProperty flname;
	private final StringProperty ffname;
	private final StringProperty fmname;
	
	//mother
	private final StringProperty mlname;
	private final StringProperty mfname;
	private final StringProperty mmname;
	
	//others
	private final StringProperty householdnum;
	private final StringProperty householdincome;
	private final StringProperty brgy;
	private final StringProperty town;
	private final StringProperty province;
	private final StringProperty zip;
	private final StringProperty contact;
	private final StringProperty email;
	private final StringProperty scholarship;
	private final StringProperty yrstart;
	private final StringProperty yrend;
	
	
	//Constructor
		public studentInfo(String id, String lname, String fname, String extname, String mname, String gender, String bdate, String course, 
				String yrlevel, String flname, String ffname, String fmname, String mlname, String mfname, String mmname, String householdnum,
				String householdincome, String brgy, String town, String province, String zip, String contact, String email, String scholarship,
				String yrstart, String yrend) {
			this.id = new SimpleStringProperty(id);
			this.lname = new SimpleStringProperty(lname);
			this.fname = new SimpleStringProperty(fname);
			this.extname = new SimpleStringProperty(extname);
			this.mname = new SimpleStringProperty(mname);
			this.gender = new SimpleStringProperty(gender);
			this.bdate = new SimpleStringProperty(bdate);
			this.course = new SimpleStringProperty(course);
			this.yrlevel = new SimpleStringProperty(yrlevel);
			this.flname = new SimpleStringProperty(flname);
			this.ffname = new SimpleStringProperty(ffname);
			this.fmname = new SimpleStringProperty(fmname);
			this.mlname = new SimpleStringProperty(mlname);
			this.mfname = new SimpleStringProperty(mfname);
			this.mmname = new SimpleStringProperty(mmname);
			this.householdnum = new SimpleStringProperty(householdnum);
			this.householdincome = new SimpleStringProperty(householdincome);
			this.brgy = new SimpleStringProperty(brgy);
			this.town = new SimpleStringProperty(town);
			this.province = new SimpleStringProperty(province);
			this.zip = new SimpleStringProperty(zip);
			this.contact = new SimpleStringProperty(contact);
			this.email = new SimpleStringProperty(email);
			this.scholarship = new SimpleStringProperty(scholarship);
			this.yrstart = new SimpleStringProperty(yrstart);
			this.yrend = new SimpleStringProperty(yrend);
		}
		
		
	//Getters and Setters
		public String getId() {
			return id.get();
		}
		public String getLname() {
			return lname.get();
		}
		public String getFname() {
			return fname.get();
		}
		public String getExtname() {
			return extname.get();
		}
		public String getMname() {
			return mname.get();
		}
		public String getGender() {
			return gender.get();
		}
		public String getBdate() {
			return bdate.get();
		}
		public String getCourse() {
			return course.get();
		}
		public String getYrlevel() {
			return yrlevel.get();
		}
		public String getFlname() {
			return flname.get();
		}
		public String getFfname() {
			return ffname.get();
		}
		public String getFmname() {
			return fmname.get();
		}
		public String getMlname() {
			return mlname.get();
		}
		public String getMfname() {
			return mfname.get();
		}
		public String getMmname() {
			return mmname.get();
		}
		public String getHouseholdnum() {
			return householdnum.get();
		}
		public String getHouseholdincome() {
			return householdincome.get();
		}
		public String getBrgy() {
			return brgy.get();
		}
		public String getTown() {
			return town.get();
		}
		public String getProvince() {
			return province.get();
		}
		public String getZip() {
			return zip.get();
		}
		public String getContact() {
			return contact.get();
		}
		public String getEmail() {
			return email.get();
		}
		public String getScholarship() {
			return scholarship.get();
		}
		public String getYrstart() {
			return yrstart.get();
		}
		public String getYrend() {
			return yrend.get();
		}
		

		public void setId(String value) {
			id.set(value);
		}
		public void setLname(String value) {
			lname.set(value);
		}
		public void setFname(String value) {
			fname.set(value);
		}
		public void setExtname(String value) {
			extname.set(value);
		}
		public void setMname(String value) {
			mname.set(value);
		}
		public void setGender(String value) {
			gender.set(value);
		}
		public void setBdate(String value) {
			bdate.set(value);
		}
		public void setCourse(String value) {
			course.set(value);
		}
		public void setYrlevel(String value) {
			yrlevel.set(value);
		}
		public void setFlname(String value) {
			flname.set(value);
		}
		public void setFfname(String value) {
			ffname.set(value);
		}
		public void setFmname(String value) {
			fmname.set(value);
		}
		public void setMlname(String value) {
			mlname.set(value);
		}
		public void setMfname(String value) {
			mfname.set(value);
		}
		public void setMmname(String value) {
			mmname.set(value);
		}
		public void setHouseholdnum(String value) {
			householdnum.set(value);
		}
		public void setHouseholdincome(String value) {
			householdincome.set(value);
		}
		public void setBrgy(String value) {
			brgy.set(value);
		}
		public void setTown(String value) {
			town.set(value);
		}
		public void setProvince(String value) {
			province.set(value);
		}
		public void setZip(String value) {
			zip.set(value);
		}
		public void setContact(String value) {
			contact.set(value);
		}
		public void setEmail(String value) {
			email.set(value);
		}
		public void setScholarship(String value) {
			scholarship.set(value);
		}
		public void setYrstart(String value) {
			yrstart.set(value);
		}
		public void setYrend(String value) {
			yrend.set(value);
		}
	
}






