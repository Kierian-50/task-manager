The task manager application from the Task Manegement Corp.
==

**Version 4.0.0**


## Contributors

- Kierian Tirlemont ; university mail : <tirlemont.e1900238@etud.univ-ubs.fr> ; personal mail : <tirlemontkierian@gmail.com>
- If you want to contribute to this project you're welcome.

## Date

 - January 2021

## Language used

 - Java
 - XML
 - SQL

#### Library/Framework/techno

- SQLite
- JSON
- AppCompat

## In which context did I this project?

I did this project as part of my studies in IT. This is a graded project for my fourth and final semester.

## The goal of the project

The goal of this project is to create a task manager application for android devices. The goal is that a user can display, add, delete and update tasks. This was the big goal but I add some goals such as create a connection activities so many users can uses the application. I also add a loading page. My goal was to try to create a corporate application which can be used in company. Next to this, I had constraints from my teachers such as the creation of a WebView, and many things like this, but I tried to keep some freedom such as using json to store the tasks and sqlite to store the users account.

## The progress of the project

The project is nearly finish, because every main activities were developed. All the part on the display, the creation, the deletion and the update of tasks are completely functional. Also, the connexion page using sqlite is over and work well. The language can be change, you can enable a dark mode or a light mode.
But there is some functionnality which could be better. First, I must talk about the bug with AppCompat when changing the language and then charging the light or dark mode it changes the language to the previous selected language (https://github.com/Kierian-50/task-manager/issues/1). But I haven't found solution to this bug coming from AppCompat. Then, we could implement the deletion or update on a swipe on a task in the list of tasks. It could be very pretty. By speaking of pretty, the application could be improve in term of design in particular the description of a task which I don't find very beautiful but I don't know what can I do to be more pretty. And we could improve the connection part adding "forgot password" and "forgot id". It's a good idea but it wasn't the goal of the project. With the same problem, it could be a great idea to encrypted password of the user because now it's in clear in the sqlite database. Finally, today I implement a JSON files and sqlite to store data, to show to my teacher that yes I can do both but in the real world this is not very good so it could be better to use json or sqlite but not both.

## The required file to execute this software

You mustn't have a special file to launch this project. At the first launch, you will to create an account and that it, you can add, remove and update task. Quite easy ?

## Installation

The application is not now on the play store, and if you haven't the apk I advice you to clone the project on Android Studio: 

```git
git clone https://github.com/Kierian-50/task-manager
```

Then the project will be on your computer and you will be able to change the code, to compile and build the APK. On android studio you have to click on Build > Build APK.
