import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

//Schoolyard. Student waits in the schoolyard until called by the teacher.
public class Schoolyard {
	private ArrayList<Student> students; //Students in the schoolyard.
	private AtomicInteger currentCapacity; //The number of students in the schoolyard. 
	
	public Schoolyard() {
		students = new ArrayList<>();
		currentCapacity = new AtomicInteger(0); //The schoolyard is empty.
	}
	
	//Student enters the schoolyard and takes space (increases the current capacity by 1).
	public void enter(Student student) { //Enter the schoolyard after arriving to school.
		students.add(student);
		currentCapacity.incrementAndGet();
	}
	
//	public boolean enter(Student student) {
//		if(students.contains(student)) { //Student is already in the schoolyard.
//			return false; 
//		} 
//		students.add(student);
//		currentCapacity.incrementAndGet();
//		return true;
//	}
		
	//Student exits the schoolyard and releases space (decreases the current capacity by 1).
	public void exit(Student student) { //Exit after the teacher has called student from the schoolyard.
		students.remove(student);
		currentCapacity.decrementAndGet();
	}
	
//	public boolean exit(Student student) {
//		if(!students.contains(student)) { //Teacher has already called the student.
//			return false; 
//		} 
//		students.remove(student);
//		currentCapacity.decrementAndGet();
//		return true;
//	}
	
	//The number of students in the schoolyard. 
	public int currentCapacity() {
		return currentCapacity.get();
	}
	
	//Students in the schoolyard.
	public ArrayList<Student> getStudents() {
		return students;
	}
}