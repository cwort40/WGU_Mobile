# academic-planner-app
Mobile application development

This project is for the WGU C196 Mobile Development Applications course. The goal of this project is to create an Android mobile application for managing terms, courses, and assessments. The application should be compatible with Android 8.0 and higher.

## Features

1. Term management
2. Course management
3. Assessment management
4. Scheduler implementation
5. Screen layouts
6. Notifications and alerts

## Functional Requirements

### Term Management

- Create a user option to enter term titles, start dates, and end dates for each term.
- Allow the user to add as many terms as needed.
- Implement validation so that a term cannot be deleted if courses are assigned to it.

### Course Management

- Allow the user to add as many courses as needed for each term.
- Display a list of courses associated with each term.
- Display a detailed view of the term title, start date, and end date for each term.
- Include course details such as title, start date, end date, status, instructor information.
- Allow the user to add as many assessments as needed for each course.
- Add a minimum of one optional note per course.
- Enter, edit, and delete course information.
- Display optional notes.
- Display a detailed view of the course, including the end date.
- Set alerts for the start and end date, that will trigger when the application is not running.
- Share notes via a sharing feature (either e-mail or SMS) that automatically populates with the notes.

### Assessment Management

- Add performance and objective assessments for each course, including the titles and end dates of the assessments.
- Enter, edit, and delete assessment information.
- Set alerts for start and end dates, that will trigger when the application is not running.

## Screen Layouts

1. Home screen
2. List of terms
3. List of courses
4. List of assessments
5. Detailed course view
6. Detailed term view
7. Detailed assessment view

## Scheduler Implementation

- Use an ArrayList.
- Use an intent.
- Implement navigation capability between multiple screens using activity.
- Include three activities.
- Implement events (e.g., a click event).
- Enable the ability to work in portrait and landscape layout.
- Add interactive capability (e.g., the ability to accept and act upon user input).
- Use a database to store and retrieve application data.
- Add an application title and an icon.
