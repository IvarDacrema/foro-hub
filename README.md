REST API Forum Hub Challenge

Description:

This project is a REST API for a forum where participants
of a platform can post their questions about certain topics.
This API was developed in Java with Spring Boot and a 
PostgreSQL database and is part of the specialization in 
backend with Java and Spring Framework of the ONE - Oracle
Next Education program.

- Create new topic:

The API offers functionality to create a topic, view forum 
topics, and update or delete an existing topic.

It features validators that check whether the data the user 
wants to insert, update, or delete complies with established
business rules.

- List of topics:

Additionally, the API includes support for data pagination,
which allows you to efficiently manage the display of the 
entire list of topics without exceeding the amount of data
displayed in a single query.

The API uses a PostgreSQL database to manage and
store all relevant information.

The main components stored in the database are:

- Users: This table contains the data of users
registered in the system, such as names, emails, encrypted
passwords, and any other relevant information.
<br><br>
- Created Topics: This is where topics or questions created
by users in the forum are stored. Each topic includes 
information such as the topic title, the content of the message,
the user ID who created it, the creation date, and categories
to facilitate organization and searching.
<br><br>
- Courses: This table stores information about the different
courses offered. Each course has a unique identifier, a title, 
and a description.
<br><br>
- Answers related to each topic: Answers to the topics are stored
in this table. Each reply contains the content of the message, 
the ID of the user who responded, the ID of the topic to which 
the reply is being replied, the date the reply was created, and 
any additional information that may be necessary for the context 
of the discussion.
<br><br>
