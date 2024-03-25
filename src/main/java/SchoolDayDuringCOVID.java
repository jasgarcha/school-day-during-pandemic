/**
 * @author Jasminder Garcha (CUNY ID: 23109141)
 */
public class SchoolDayDuringCOVID {
	public static final School PS1111 = new School(); //PS1111. The school both students and teacher attend (Student and Teacher threads access) following a hybrid teaching regimen.
	public static long time; //Time the main thread begins execution.
	/**
	 * @param args Usage: -s, --students: The number of students as a positive integer. No arguments: the default number of students (13).
	 */
	public static void main(String[] args) {
		//"Initialize the time at the beginning of the main method, so that it is unique to all threads."
		time = System.currentTimeMillis();
		
		//Input validation for -s command line argument.
		if(args.length == 1 || args.length > 2){ 
			System.err.println("Usage: -s, --students: The number of students as a positive integer. No arguments: the default number of students (13).");
			System.exit(-1);
		}
		if(args.length == 2) {
			if(args[0].equals("-s") || args[0].equals("--students")) {
				try {
					int numberOfStudents = Integer.parseInt(args[1]);
					if(numberOfStudents < 0) {
						System.err.println("Usage: -s, --students: The number of students as a positive integer. No arguments: the default number of students (13).");
						System.exit(-1);
					}
					else { //Valid.
						PS1111.setNumberOfStudents(numberOfStudents);	
					}
				} catch(Exception e) {
					System.err.println("Usage: -s, --students: The number of students as a positive integer. No arguments: the default number of students (13).");
					System.exit(-1);
				}
			}
			else {
				System.err.println("Usage: -s, --students: The number of students as a positive integer. No arguments: the default number of students (13).");
				System.exit(-1);
			}
		}

		//Create students.
		Student[] students = new Student[PS1111.getNumberOfStudents()];
		for(int i = 0; i < students.length; i++) {
			students[i] = new Student(i+1); //The first Student Id is 1.
		}
		
		//Create teacher.
		Teacher teacher = new Teacher();
		
		Thread[] studentThread = new Thread[PS1111.getNumberOfStudents()]; //Student threads.
		Thread teacherThread = new Thread(); //Teacher thread.
			
		//Start the Student thread.
		for(int i = 0; i < students.length; i++) {
			students[i].start();
			studentThread[i] = students[i].getThread();
		}
	
		//Start the teacher thread.
		teacher.start();
		teacherThread = teacher.getThread();
		
		//At the end of the school day, students leave the school in decreasing order of their ID.
		for(int i = 0; i < studentThread.length; i++) {
			try {
				studentThread[i].join(); //Student N waits for Student N-1 to terminate its thread.
			} catch (InterruptedException e) {
				System.err.print(e.getMessage()); 
			}				
		}
		//Teacher leaves after the last student leaves. 
		try {
			teacherThread.join();
		} catch (InterruptedException e) {
			System.err.print(e.getMessage()); 
		}
		
		int numberOfTeachers = 1;
		for(int i = 0; i < PS1111.getNumberOfStudents()+numberOfTeachers; ) {
			for(int j = 0; j < studentThread.length; j++) {
				if(!studentThread[j].isAlive()) { //The student thread terminated. Student has left school and gone home.
					msg(studentThread[j].getName()+" has left school and gone home.");
					i++;
				}
				
			}
			if(!teacherThread.isAlive()) { //The teacher thread terminated. Teacher has left school and gone home.
				msg("Teacher has left school and gone home.");
				i++;
			}
		}
		
		//All the students and teacher have gone home. School is closed.
		System.out.println("All the students and teacher have gone home. School is closed.");
	}
	
	//Standardizes output, from the Project 1 specifications. Modified getName() to thread.getName() because implements Runnable versus extends Thread. 
	private static void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+Thread.currentThread().getName()+": "+m);
	}
}