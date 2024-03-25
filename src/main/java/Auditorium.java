import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

//Auditorium. Students enter the auditorium if class is not in session, while waiting for the teacher to arrive at the classroom.
public class Auditorium {
	private ArrayList<Student> students; //Students in the auditorium.
	private AtomicInteger seatsTaken; //The number of seats taken by students sitting in the auditorium.
	
	public Auditorium() {
		students = new ArrayList<>();
		seatsTaken = new AtomicInteger(0); //No seats are taken. 
	}
	
	//Student enters the auditorium and takes a seat (increases the number of seats taken  by 1).
	public void enter(Student student) { //Enter when class is not in session, while waiting for the teacher to arrive at the classroom.
		students.add(student);
		seatsTaken.incrementAndGet();
	}
		
	//Student exits the auditorium and releases a seat (decreases the number of seats taken  by 1).
	public void exit(Student student) { //Exit when the teacher arrives at the classroom. 
		students.remove(student);
		seatsTaken.decrementAndGet();
	}
	
	//The number of seats taken by students sitting in the auditorium.
	public int numberOfSeatsTaken() {
		return seatsTaken.get();
	}
	
	//True if a student is in the auditorium (entered but not exited). False if a student is not in the auditorium (did not enter or exited after entering).
	public boolean inAuditorium(Student student) {
		return students.contains(student);
	}
}