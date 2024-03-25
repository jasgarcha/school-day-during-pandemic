import java.util.concurrent.atomic.AtomicInteger;

//School. School contains the schoolyard, restrooms, classroom, auditorium, period: shared structures (Critical Sections) accessed by students and teachers (Student and Teacher threads). 
public class School {
	private int numberOfStudents; //The number of students.
	private Schoolyard schoolYard; //Schoolyard.
	private Restroom boysRestroom; //Boys' restroom.
	private Restroom girlsRestroom; //Girls' restroom.
	private Classroom classroom; //Classroom.
	private Auditorium auditorium; //Auditorium.
	public final int NUMBER_OF_PERIODS = 5; //The number of periods in a school day is 5.
	private AtomicInteger period; //Class period of the school day.

	public School() { 
		numberOfStudents = 13; //The default number of students is 13.
		schoolYard = new Schoolyard();
		boysRestroom = new Restroom();
		girlsRestroom = new Restroom();
		classroom = new Classroom();
		auditorium = new Auditorium();
		period = new AtomicInteger(1); //Class starts at period 1.
	}
	
	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(int numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public Schoolyard getSchoolYard() {
		return schoolYard;
	}
	
	public Restroom getBoysRestroom() {
		return boysRestroom;
	}

	public Restroom getGirlsRestroom() {
		return girlsRestroom;
	}

	public Classroom getClassroom() {
		return classroom;
	}
	
	public Auditorium getAuditorium() {
		return auditorium;
	}
	
	//Current class period of the school day.
	public int getPeriod() {
		return period.get();
	}
	
	//The next class period.
	public void nextPeriod() {
		period.incrementAndGet();
	}
	
	//The school day ends after 5th period. 
	public boolean endOfSchoolDay() {
		return period.get() > NUMBER_OF_PERIODS;
	}
}