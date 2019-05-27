# RUASPortal
  This android application is basically a student portal implemented on universities where students gets to access the all the readable 
  data that a university or a school can provide to a student like Books, Assignments and this app can receive push notifications from the faculty regarding any important notifications and can send any important files as links to the users
  The main feature of this application is Attendance marking using QR codes, which are provided by the faculty.
  
  The complete report of the completed project is provided in the link below:
  https://firebasestorage.googleapis.com/v0/b/ruasportal.appspot.com/o/UG%20Group%20Project%20Final%20Report%20Group%2025.docx?alt=media&token=7568d80c-485d-409c-b384-f08a380c1019
  
  Login Screen:
  
<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58438967-5ef27d80-80ef-11e9-9095-7b787f7fc923.jpg" width="200" height="400"><br>Figure 5.1: Login Screen</div>
<br>
In the login page (Figure 5.1) the user enters the credentials provided to him/her. The student is needed to log in to the app only once and saves the user authentication data until he/she logs out of the application. The user is redirected to dashboard if he/she is already signed in to the application.



<br><br><br>
**Dashboard:**
 
 <div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439082-15eef900-80f0-11e9-83e0-45bd4bcbb18f.jpg" width="200" height="400"><br>Figure 5.2: Dashboard Activity</div>
<br>
In Figure 5.2, the user retrieves all the basic information like his name, registration number, branch of study, attendance status, assignment submission date and buttons to other activities like Notifications (Bell icon on the top right corner), Logout button (Door icon on the top left corner), Attendance, Books and Assignments.


<br><br><br>
**Books:**

<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439210-86961580-80f0-11e9-9bc9-9dca6a03e55e.jpg" width="200" height="400"><br>Figure 5.3: Books Activity (Subject)	</div>
<br><br>
<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439233-a4637a80-80f0-11e9-85ad-6eaff8ce255c.jpg" width="200" height="400"><br>Figure 5.4: Books Activity (Notes)</div> 
<br>
In Figure 5.3, the user gets college notes and books which are separated subject wise where the user can select the subject and redirects to another screen containing the notes which can be downloaded for later use.
	In Figure 5.4, a subject is selected and shows the list of books available for download. The books can be downloaded by clicking the download button.




<br><br><br>
**Assignments**

<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439281-ef7d8d80-80f0-11e9-80c0-6327e11031ff.jpg" width="200" height="400"><br>Figure 5.5: Assignment Activity</div> 
<br>
In Figure 5.5, the user gets all the assignment questions that are needed to be answered within the given date as shown in dashboard screen. Assignments files are stored in the same way as books section by storing the files in online storage and using real-time database to store the downloadable URI.



<br><br><br>
**Notifications:**

<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439300-08863e80-80f1-11e9-9a07-a1f36fc5b7e5.jpg" width="200" height="400"><br>Figure 5.6: Notifications Activity</div>
<br>
In Figure 5.6, the user gets all the important notifications sent by the faculty or admin of the application. These notifications are sent as Push Notifications to the application and are saved on the screen in order of received time. Any important files can be sent as downloadable links.

<br><br><br>
**Attendance:**

<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439330-1dfb6880-80f1-11e9-8d7b-ec2e4fd4abf7.jpg" width="200" height="400"><br>Figure 5.7: Attendance Activity</div>
<br><br>
<div align="center"><img src="https://user-images.githubusercontent.com/38137202/58439356-353a5600-80f1-11e9-9623-575335beafea.png" width="700" height="400"><br>Figure 5.8: Website</div>
<br>
In this screen, the user gets all the subject’s attendance in a circular progress bar. The circular bar lights up red if the student’s attendance is lesser than 80% and lights up green if its greater than or equal to 80%. Clicking the QR image provided on the bottom opens options box to select the subject to mark attendance for, selecting a subject opens camera to scan the QR code and mark the attendance.
The image in the previous page is the webpage that will be provided to the faculty. The faculty opens this page when the attendance has to be taken. As soon as the subject is selected, the page starts to generate QR codes. For every 10 seconds, the QR code changes and the old QR code expires, this is to make sure that the students can never counterfeit by marking the attendance. For further additional security, the application restricts marking attendance for every 50 minutes (one complete class timing) after one successful attendance. The marked attendance is stored in excel sheet, including the time of attendance. The codes which is used to generate QR are given by real-time database.
<br>Website link: https://ruasportal.firebaseapp.com/

  <br><br>
  The webpage application's repository is provided below:
  https://github.com/Sriharikrishna06/RUAS_Attendance_Page
  <br><br>
  Any comments regarding the projects are totaly welcome
