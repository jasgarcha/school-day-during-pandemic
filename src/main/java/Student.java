import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

//Student.
public class Student implements Runnable {
	private Thread thread;
	public static long time; //Time the thread begins execution.
	private Random RNG; //Random number generator.
	private int idNumber; //Unique integer to identify a student.  
	private Boolean gender; //Student's gender. true is female, false is male. 
	private Boolean eager; //Eagerness to learn. true is eager, false is not eager. 
	private AtomicBoolean inSchoolYard; //Student busy waits in schoolyard until called by Teacher.

	public Student(int idNumber) {
		thread = new Thread(this);
		thread.setName("Student-"+idNumber);
		time = System.currentTimeMillis();
		RNG = new Random();
		this.idNumber = idNumber;
		gender = RNG.nextBoolean();
		eager = RNG.nextBoolean();
	}

	public void run() {
		School school = SchoolDayDuringCOVID.PS1111;
		Schoolyard schoolYard = school.getSchoolYard();
		Restroom boysRestroom = school.getBoysRestroom();
		Restroom girlsRestroom = school.getGirlsRestroom();
		Classroom classroom = school.getClassroom();
		Auditorium auditorium = school.getAuditorium();
		
		msg("Wakes up and gets ready."); //Students wakes up and gets ready.
		msg("Starts health questionnaire."); //Student starts health questionnaire.
		try {
			Thread.sleep((long)RNG.nextInt(5000)); //Filling out health questionnaire. Simulated by sleep random time between 0 milliseconds and 5 seconds. 
		} catch (InterruptedException e) {
			System.err.print(e.getMessage()); //Thread is interrupted during sleep. 
		} 
		msg("Completes health questionnaire."); //Student completes health questionnaire.
		
		msg("Begins commute to school."); //Student begins commute.
		try {
			Thread.sleep((long)RNG.nextInt(5000)); //Commuting. Simulated by sleep random time between 0 milliseconds and 5 seconds. 
		} catch (InterruptedException e) {
			System.err.print(e.getMessage()); //Thread is interrupted during sleep. 
		}
		msg("Ends commute to school. Arrives at school."); //Student ends commute and arrives at school.
		
		msg("Enters the schoolyard.");
		inSchoolYard = new AtomicBoolean(true); //Student is in school yard..
		schoolYard.enter(this); //Student enters the schoolyard.
		while(inSchoolYard()) { } //Student (busy) waits in schoolyard until called by Teacher.
		/*ArrayList is not thread safe. remove() would throw an IndexOutOfBounds exception because of race condition.*/ 
		//schoolYard.exit(this); //Student exits the schoolyard after being called by Teacher (exits busy wait).
		msg("Exits the schoolyard.");
	
		msg("Heads to the restroom."); //Student heads to the restroom.
		Restroom restroom = new Restroom(); 
		String genderDescriptor = new String();
		if(gender == false) { //Student is a boy, uses the boys' restroom.
			restroom = boysRestroom;			
			genderDescriptor = "boys'";
		}
		if(gender == true) { //Student is a girl, uses the girls' restroom.
			restroom = girlsRestroom;
			genderDescriptor = "girls'";
		}
		restroom.getOnLine(this); //Students gets on the line of his or her respective restroom.
		msg("Gets on the "+genderDescriptor+" restroom line.");
		if(restroom.isFull()) { //Student takes a break because the restroom is full.
			msg("Takes a break because the "+genderDescriptor+" bathroom is full.");
			Thread.yield();
			Thread.yield();
			Thread.yield();
		}
		while(restroom.isFull()) { } //Student busy waits while the restroom is full until space becomes available to enter.
		restroom.getOffLine(this); //Students gets off the restroom line. 
		restroom.enter(this); //Student enters the restroom (exits busy wait because space became available in the restroom).
		msg("Gets off the "+genderDescriptor+" restroom line.");
		msg("Enters the "+genderDescriptor+" restroom.");
		try {
			msg("Starts washing hands.");
			Thread.sleep((long)RNG.nextInt(5000)); //Student washes hands simulated by sleep random time 0 milliseconds and 5 seconds. 
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
		msg("Finishes washing hands.");
		restroom.exit(this); //Student finishes washing hands and exits the restroom.
		msg("Exits the "+genderDescriptor+" restroom.");

		//"Some students are very eager to learn new things," determined by a random boolean. true is eager, false is not eager.
		if(isEager()) {
			int normalPriority = thread.getPriority();
			thread.setPriority(normalPriority+1); //Eager students increase their priority.
			msg("Rushes to class.");
			try {
				Thread.sleep((long)RNG.nextInt(5000)); //Student goes to class simulated by random time between 0 milliseconds and 5 seconds. 
			} catch (InterruptedException e) {
				e.printStackTrace(); //Thread is interrupted during sleep. 
			}
			thread.setPriority(normalPriority); //Student wakes up, priority set back to normal.
		}
		else {
			msg("Walks to class with the average level of enthusiasm."); //Non-eager students are not in such a rush.
		}
		
		//Attend class until the school day ends.
		while(!school.endOfSchoolDay()) { 
			//Class is in session.
			if(classroom.inSession()) {
				//Students in the class while the class is in session.
				if(classroom.getStudents().contains(this)) {
					//"Once the class is in session, students will immediately get bored and fall asleep (simulate this using a sleep of a long time)."
					try {
						msg("Immediately gets bored and falls asleep.");
						Thread.sleep(600000); //Student is sleeping simulated by sleep of 10 minutes.
					
					} catch (InterruptedException e) {
						msg("Wakes up after being hit by a ruler thrown by the teacher.");
					}
					//The thread has been interrupted. Sleeping student woken up by student.
					//"Student(s) will leave the classroom and hurry to have some fun between classes (sleep of random time)."
					if(thread.isInterrupted()) {
						classroom.exit(this);
					}
					msg("Leaves the the classroom and hurries to have some fun between classes.");
					try {
						Thread.sleep((long)RNG.nextInt(500)); //Leaves the the classroom and hurries to have some fun between classes simulated by sleep of random time between 0 ms and 500 ms.
					} catch (InterruptedException e) {
						System.err.println(e.getMessage()); //Thread is interrupted during sleep. 
					}
				}
				//Students not in the class attempting to enter while class is in session.
				//"By the time a student gets to class, if the class is already in session, the student(s) will leave for a while (sleep of random time) and walk around the campus and come back later on."
				else  {
					msg("Cannot enter a class in session. Leaves and walks around the campus.");
					try {
						Thread.sleep((long)RNG.nextInt(5000)); //Walk around campus while class in session simulated by sleep of random time between 0 milliseconds and 5 seconds.
					} catch (InterruptedException e) {
						System.err.println(e.getMessage()); //Thread is interrupted during sleep. 
					}
				}
			}
			//Class is not in session.
			else {
				//"If the class is not in session yet, student(s) will (busy) wait for the teacher to arrive and enter the auditorium."
				if(!classroom.isTeacherPresent()) {
					msg("Enters auditorium."); //Student enters the auditorium.
					auditorium.enter(this);
				}
				while(!classroom.isTeacherPresent()) { } //Student (busy) waits for the teacher to arrive at the classroom.
				 //The teacher arrives at the classroom so the student exits the auditorium (exits busy wait).
				if(auditorium.inAuditorium(this)) { //Students in the auditorium while class is in session.
					msg("Exits auditorium."); 
					auditorium.exit(this); 						
				}
				
				if(!classroom.getStudents().contains(this)) { //Students not in the classroom while class is in session.
					classroom.enter(this); //Student enters the classroom.
					msg("Enters the classroom.");
				}
			}
		}
	}
				
	public void start() {
		thread.start();
	}
	
	//Standardizes output, from the Project 1 specifications. Modified getName() to thread.getName() because implements Runnable versus extends Thread. 
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+thread.getName()+": "+m);
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public int getIdNumber() {
		return idNumber;
	}
	
	public AtomicBoolean getInSchoolYard() {
		return inSchoolYard;
	}
	
	public void setInSchoolYard(Boolean newValue) {
		inSchoolYard.set(newValue);
	}
	
	public boolean inSchoolYard() {
		return inSchoolYard.get();
	}
	
	public boolean isEager() {
		return eager;
	}
}