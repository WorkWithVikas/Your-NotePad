# YOUR NOTEPAD
 -   JavaFX    PROJECT 
 

# JavaFX Notepad App

This is a simple Notepad application built using JavaFX. It allows users to create, edit, save, and manage notes, providing both a graphical user interface and the ability to interact with a MySQL database. Users can customize the font size, style, and text color for a personalized experience.

## Features

- **Create and Edit Notes**: A rich text editor with support for text formatting and styling.
- **Save Notes**: Save notes to a MySQL database or export them to local files.
- **Customizable Font**: Change font size, style, and type through the Edit menu.
- **Text Color Customization**: Modify the text color to fit your preferences.
- **Full-Screen Mode**: Toggle full-screen mode for a distraction-free experience.
- **Developer Information**: An About section displaying developer details.

## PROJECT STRUCTURE 
###  FRONT VIEW : 


  
![1](https://github.com/user-attachments/assets/8da7cb36-156c-4e99-b4f6-d35b3a799141)

 ### MENUBAR :

 
![2](https://github.com/user-attachments/assets/480c6758-4655-47a0-8487-ca756cfd2ca7)   

  ### DEVELOPER INFORMATION

  
![3](https://github.com/user-attachments/assets/0aebfc5b-addd-412f-aafe-0d7be8893a6e)



### Software Requirements:
- **JDK 8 or higher**: Ensure you have the Java Development Kit installed on your machine. This project was developed using JDK 8.
- **MySQL**: A MySQL database is required to store notes.
- **JavaFX**: This application uses JavaFX for the user interface.


## Developer Information:

- Lead Developer: Vikas Rathore
- Email: jarvis.90v@gmail.com
- GitHub: https://github.com/WorkWithVikas
- Version: 1.0
- Date: December 2024

### MySQL Setup:
You will need to set up a MySQL database to store your notes.

1. Install MySQL if you haven't already.
2. Create a new database:
   
  ```sql
   CREATE DATABASE NotepadApp ;
   
   CREATE TABLE notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT
   );
