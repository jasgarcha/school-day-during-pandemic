import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

//Teacher.
public class Teacher implements Runnable {
	private Thread thread;
	public static long time; //Time the thread begins executed.
	private Random RNG; //Random number generator.
	private int breakPeriod; //A random break between any 2 of the 5 periods during the school day.
	private Map<Student, ArrayList<Integer>> attendance; //The attendance of a student in a class period.

	public Teacher() {
		thread = new Thread(this);
		thread.setName("Teacher");
		time = System.currentTimeMillis();
		RNG = new Random();
		breakPeriod = RNG.nextInt(6)+1; //Either 1, 2, 3, 4, or 5.
		attendance = new HashMap<>();
	}
	
	public void run() {
		School school = SchoolDayDuringCOVID.PS1111;
		Schoolyard schoolYard = school.getSchoolYard();
		Classroom classroom = school.getClassroom();

		msg("Arrives at school"); //Teacher arrives at school.
		
		//Teacher calls students from the schoolyard.
		AtomicInteger i = new AtomicInteger(0);
		AtomicInteger j = new AtomicInteger(0);		
		while(i.get() < school.getNumberOfStudents()) {
			while(j.get() < schoolYard.currentCapacity() && schoolYard.currentCapacity() > 0) {
				if(schoolYard.getStudents().get(j.get()).inSchoolYard()) {
					msg("Calls Student-"+schoolYard.getStudents().get(j.get()).getIdNumber()+" from the schoolyard.");
					callFromSchoolYard(schoolYard.getStudents().get(j.get())); //Call student from the schoolyard.				
					i.incrementAndGet();
				}
				j.incrementAndGet();
			}
			j.set(0);
		}
		msg("Called all students from the schoolyard."); //Teacher has called all students from the schoolyard.
		
		try {
			Thread.sleep((long)RNG.nextInt(5000)); //Teach walks to the classroom simulated by random time between 0 milliseconds and 5 seconds. 
		} catch (InterruptedException e) {
			System.err.println(e.getMessage()); //Thread is interrupted during sleep. 
		}		
		
		msg("Arrives at the classroom."); 
		classroom.enter(this); //Teacher enters the classroom.
		
		try {
			Thread.sleep(500); //Prepares to teach classes. 
		} catch (InterruptedException e) {
			System.err.println(e.getMessage()); //Thread is interrupted during sleep. 
		} 
		
		//Teach class periods 1-5, until the school day ends.
		while(!school.endOfSchoolDay()) { 
			 //Break period.
			if(school.getPeriod() == breakPeriod) {
				msg("On break.");
				try {
					Thread.sleep(5000); //Break is a fixed length of 5 seconds. 
				} catch (InterruptedException e) {
					System.err.println(e.getMessage()); //Thread is interrupted during sleep. 
				}
			}
			//Office hour between 2nd and 3rd period.
			else if(school.getPeriod() == 2) {
				teach(classroom, school.getPeriod()); //Teach 2nd period.
				msg("Office hour.");
				try {
					Thread.sleep(5000); //Office hour is fixed length of 5 seconds. 
				} catch (InterruptedException e) {
					System.err.println(e.getMessage()); //Thread is interrupted during sleep. 
				}
			}
			//Teach. period != break && period != office hour.
			else {
				teach(classroom, school.getPeriod());
			}
			school.nextPeriod(); //Move on to the next class period in the school day.
		}
		//Teacher has taught all 4 classes.
		msg("Taught all 4 classes.");
		//Teacher gives a daily report with information about what classes and when each student attended.
		dailyReport();
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
	
	//Call student from the schoolyard.
	private void callFromSchoolYard(Student student) {
		student.setInSchoolYard(false);
	}
	
	//Teach a class period.
	private void teach(Classroom classroom, int period) {
		msg("Teaches period "+period+". Class period "+period+" starts.");
		classroom.begin(); //Begin class.
		takeAttendance(classroom, period); //Take attendance of students in class.
		try {
			Thread.sleep(15000); //Each class is a fixed length of 15 seconds. 
		} catch (InterruptedException e) {
			System.err.println(e.getMessage()); //Thread is interrupted during sleep.
		}
		throwRuler(classroom); //Wake up sleeping students when class is over by throwing rulers at them (interrupt the sleeping thread).
		classroom.end(); //End class. 
		msg("Class period "+period+" ends.");
		try {
			Thread.sleep(1000); //Bell between classes to allow waiting students to come in and students already in class to leave. 
		} catch (InterruptedException e) {
			System.err.println(e.getMessage()); //Thread is interrupted during sleep.
		}
	}
	
	//Wake up sleeping students when class is over by throwing rulers at them (interrupt the sleeping thread).
	private void throwRuler(Classroom classroom) {
		AtomicInteger i = new AtomicInteger(0);
		for(i.get(); i.get() < classroom.currentStudentCapacity(); i.incrementAndGet()) { 
			Student sleepingStudent = classroom.getStudents().get(i.get());
			msg("Throws a ruler at Student-"+sleepingStudent.getIdNumber()+".");
			classroom.getStudents().get(i.get()).getThread().interrupt(); //Interrupt.
		}
	}
	
	//Take attendance of the students in a class period.
	private void takeAttendance(Classroom classroom, int period) {
		AtomicInteger i = new AtomicInteger(0);
		for(i.get(); i.get() < classroom.getStudents().size(); i.incrementAndGet()) { 
			ArrayList<Integer> periodsAttended;
			Student student = classroom.getStudents().get(i.get());
			if(attendance.containsKey(student)) {
				periodsAttended = attendance.get(student);
			}
			else {
				periodsAttended = new ArrayList<>();
			}
			periodsAttended.add(period);
			attendance.put(student, periodsAttended);
		}
	}
	
	//"A daily report with information about what classes and when each student attended".
	private void dailyReport() {
		System.out.println();
		msg("Daily Report:");
		System.out.println("Student Id | Total Number of Attended Classes | Period Number");
		Set<Student> presentStudents = attendance.keySet();
		for(Student presentStudent: presentStudents) {
			ArrayList<Integer> periodsAttended = attendance.get(presentStudent);
			System.out.print("Student-"+presentStudent.getIdNumber()+" | " + periodsAttended.size() + " | ");
			for(int i = 0; i < periodsAttended.size(); i++) {
				if(i == periodsAttended.size()-1) {
					System.out.print(periodsAttended.get(i));
				}
				else {
					System.out.print(periodsAttended.get(i)+",");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}