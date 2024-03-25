import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Restroom {
	public static final int MAX_CAPACITY = 2; //The maximum number of students allowed in a restroom at a time is 2.
	private Queue<Student> line; //Students use the restroom on a First Coming First Serve (FCFS) basis implemented with a First In First Out (FIFO) queue.
	private ArrayList<Student> restroom; //Restroom. Students in a restroom.
	private AtomicInteger currentCapacity; //The number of students in a restroom.
	
	public Restroom() {
		line = new LinkedList<>(); //Students who arrive first are the first out.
		restroom = new ArrayList<>();
		currentCapacity = new AtomicInteger(0); //No students in the restroom.
	}
	
	//Student gets on the restroom line. The first one on the line is the first one off the line to enter the restroom.
	public void getOnLine(Student student) {
		line.add(student);
	}
	
	//Student gets off the restroom line. The first one off the line is the first one on the line to enter the restroom.
	public void getOffLine(Student student) {
		line.remove(student);
	}
	
//	public boolean enter(Student student) {
//		if(isFull()) {
//			return false;
//		}
//		restroom.add(student);
//		currentCapacity.incrementAndGet();
//		return true;
//	}
	
	//Student enters the restroom (increases the current capacity by 1.)
	public void enter(Student student) {
		restroom.add(student);
		currentCapacity.incrementAndGet();		
	}
	
//	public boolean exit(Student student) {
//		if(currentCapacity() == 0) {
//			return false;
//		}
//		restroom.remove(student);
//		currentCapacity.decrementAndGet();
//		return true;
//	}
	
	//Student exits the restroom (decreases the current capacity by 1.)	
	public void exit(Student student) {
		restroom.remove(student);
		currentCapacity.decrementAndGet();
	}
	
	//The restroom line.
	public Queue<Student> getLine() {
		return line;
	}
	
	//The number of students in a restroom.
	public int currentCapacity() {
		return currentCapacity.get();
	}
	
	//The restroom is full if the current capacity equals the maximum capacity. 
	public boolean isFull() {
		return currentCapacity() == MAX_CAPACITY;
	}
}