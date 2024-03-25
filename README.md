# school-day-during-pandemic

Synchronize the students and teacher threads (without the use of semaphores).
This project demonstrates my familiarity with the `volatile` modifier, `Atomic` classes, and the `Threads` class--fundamental Operating System concepts.

## Usage
Arguments:

`-s, --students`: The number of students as a positive integer. 

If no arguments are provided, the default number of students is 13.

## Logic

"Students at PS1111 are following a hybrid learning regimen. During this COVID time they have to follow strict rules. After a student wakes up and gets ready for a new school day, (s)he will complete a Health Questionnaire (simulate this using a sleep of random time). Next s(he) will commute to school. (sleep of random time).

Once arrived at school, student(s) will (busy)wait in the schoolyard to be called by
the teacher. Once called by the teacher, before entering the classroom, students
must wash their hands. so they will head to the restrooms. There are two restrooms,
one for “girls” and one for “boys.” The capacity of the bathroom is equal to two. You
can decide if a student is a boy or a girl using a random number or you can consider
that students with an odd id are boys while the others are girls. If a bathroom is
already taken, the student takes a break (use `yield()` three times) and later on (s)he
will wait again (use busy waiting) for the bathroom to become available. Students
will use the bathroom in a First Come First Serve basis (you can use a Boolean
array/vectors).

Next, some students are very eager to learn new things and they will rush to get to
the classroom (simulate this by increasing their priority. Use `getPriority()`,
`setPriority()`, `sleep(random time)`. After the student has increased its priority,
(s)he will `sleep` a random time and as soon as (s)he wakes up make sure you reset
his/her priority back to the normal value). On the other hand, the other students are
not in such a rush.

By the time a student gets to class, if the class is already in session, the student(s)
will leave for a while (`sleep` of random time) and walk around the campus and
come back later on. If the class is not in session yet, student(s) will (busy) wait for
the teacher to arrive and enter the auditorium.

Once the class is in session, students will immediately get bored and fall asleep
(simulate this using a `sleep` of a long time). The teacher will teach (simulate it
using sleep for a fixed interval of time) and when done, he will let the students
know that the class ended by interrupting them (use `interrupt()` and
`isInterrupted()`. Make sure that in the student code, as part of a catch block you
have a println that will show that the student has been interrupted). Student(s) will
leave the classroom and hurry to have some fun between classes (`sleep` of random
time).

Once having arrived at the school the teacher will let students in. During the day,
he will teach four classes during 5 periods. Each class takes a fixed amount of the
time period. Between any two classes there is a break. Between the second and
third class there is an office hour (the 5th period).

The school closes after the four classes end. At the end of school day, students leave
the school. Each student will join another student; they will leave in decreasing order
of their name or their ID.

For example: student 1 joins student 2, student 2 joins student 3, … student `N-1`
joins student `N` (use `join()` and `isAlive()`). The teacher will join the last student to
leave and after that he will terminate as well.

A daily report with information about what classes and when each student attended
throughout the day must be displayed. It can be displayed by the `Teacher` before
terminating or in the main method." (Dr. Simina Fluture)