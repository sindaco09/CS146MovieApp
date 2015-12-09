README for how this application works

---Main Activity---
A tabbed activity that contains the fragments:
1. Profile Fragment
2. Movies Fragment
3. Friends Fragment

---Login Activity---
Main Activity starts up and checks if user is logged in
1. if user is not logged in, navigate to Login Activity
2. if user is logged in, stay in Main Activity

Compares entered username/password credentials to User database on Parse.com
1. if login is a match, start Main Activity
2. if login does not match, notify user
If user presses "Sign up!" button, navigate to SignUp Activity

---SignUp Activity---
User enters
1. Username
2. Email
3. Password
The entered fields are then pushed to Parse.com and the user is then
automatically logged into Main Activity

---Edit Friends Activity---
Reach this activity by pressing the action bar in top right corner and selecting
option to view users/friends
Populates a listView with all the users available in the User Database
relationship between users to users is Many-to-Many
* if user is selected, user is added to CurrentUser's friend's list
* if user is deselected, user is removed from CurrentUser's friend's list
* working on search function

---Edit Movies Activity---
Reach this activity by pressing the action bar in the top right corner and selecting
option to view movies.
Same concept as friends activity but instead of a User to User relationship
it is a User to Object Many-to-Many relationship

hashKey: X9ci89Bjb+YmcPxYbQ90yGSyWqg