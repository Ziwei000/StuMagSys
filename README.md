# StuMagSys
A student information management system with implementation of data encryption, identity authentication, and access control functions.
Environment: Windows, eclipse, SQL Server need to adjust the eclipse encoding mode to GBK.

1. Create a user in SQL Server with the character "sup" and the password "012345678", and set the database login mode as a hybrid login.

2. Build a database in which new queries are created using the sql statement.txt in the compressed package to create a table:

These include:

  1) AccessAdmin (store permission administrator information);

  2) Administrator (store administrator information);

  3) Register (store the user name and password of the registrant, where the password is stored in ciphertext form);

  4) Course (all course information);

  5) School (college information);

  6) Dept (departmental information and definition of the college);

  7) Major (professional information and defined department);

  8) Class (store class information and define the class's major);

  9) Student (student information, including the student's class);

  10) Student_Course (student selection information and corresponding results);

  11) Teacher (teacher information includes the department where the teacher is located);

  12) Teacher_Course (teacher's teaching information)

3. Put the folder called jxljar in the compressed package on disk E.

4. Run the Welcome.java connection database in StuMagServer.

5. After success, run Willcome.java in StuMagClient to start entering the login interface;

  1) Select the administrator role, log in to the corresponding user name password, and enter the registration role interface. Choose a registered student or teacher, fill in the relevant information, successfully register, get a system given id as the user name, and remember the password used at the time of registration; close the administrator window and return to the login interface.

  2) Log in according to the matching student user name and the corresponding password to enter the student interface: click to view personal information; click to enter the course selection interface, quickly select courses whose attributes are compulsory, or click on relevant courses by yourself. You can withdraw from selected courses. After entering the results, teachers can check the corresponding course results. Enter to modify the password, enter the old password and the new password, and change the user password to a new password if the original password matches the input of the old password; click to log out and return to the user login interface.

  3) Log in to the teacher role, you can view personal information; click to enter the Jiang election course, click to enter the results of students who have selected the course, return to submit, and you can't modify it after submitting; you can modify the password; click to log out to return to the user login interface.

  4) Login permission administrators can view the relevant permissions of the corresponding table of relevant roles. You will need to modify the read-write permissions (such as reading and writing; reading; writing) to modify their corresponding permissions by clicking to modify them in the input box, and close the permission administrator window and return to the login interface.

  5) You can shut down the system by clicking out.
