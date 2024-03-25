import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//Classroom. Teacher teaches class in the classroom and students attend class in the classroom.
public class Classroom {
	private ArrayList<Student> students; //Students in the classroom.
	private AtomicInteger currentStudentCapacity; //The number of students in the classroom.
	private AtomicBoolean session; //True if class is in session. False if class is not in session.
	private AtomicBoolean teacherPresent; //True if the teacher is in the classroom. False if the teacher is not in the classroom.
	
	public Classroom() {
		students = new ArrayList<>(); 
		currentStudentCapacity = new AtomicInteger(0); //No students in the classroom.
		session = new AtomicBoolean(false); //Class is not in session.
		teacherPresent = new AtomicBoolean(false); //No teacher in the classroom.
	}
	
	//Student enters the classroom (increases the current capacity by 1).
	public void enter(Student student) {
		students.add(student);
		currentStudentCapacity.incrementAndGet();		
	}
	
	//Student exits the classroom (decreases the current capacity by 1).
	public void exit(Student student) {
		students.remove(student);
		currentStudentCapacity.decrementAndGet();
	}
	
	//Teacher enters the classroom.
	public void enter(Teacher teacher) {
		teacherPresent.set(true);	
	}
	
	//Teacher enters the classroom.
	public void exit(Teacher teacher) {
		teacherPresent.set(false);
	}
	
	//True if the teacher is in the classroom. False if the teacher is not in the classroom.
	public boolean isTeacherPresent() {
		return teacherPresent.get();
	}
	
	//Students in the classroom.
	public ArrayList<Student> getStudents() {
		return students;
	}

	//Class is in session.
	public void begin() {
		session.set(true);
	}
	
	//Class is not in session.
	public void end() {
		session.set(false);
	}
	
	//True if class is in session. False if class is not in session.
	public boolean inSession() {
		return session.get();
	}
	
	//The number of students in the classroom.
	public int currentStudentCapacity() {
		return currentStudentCapacity.get();
	}	
	
	//The number of students and teachers in the classroom.
	public int currentCapacity() {
		if(isTeacherPresent()) {
			return currentStudentCapacity()+1;
		}
		return currentStudentCapacity();
	}
	
	//True if neither students nor teacher are in the classroom. 
	public boolean isEmpty() {
		return currentCapacity() == 0;
	}
	
	//True if no students are in the classroom.
	public boolean hasStudents() {
		return currentStudentCapacity() == 0;
	}
}