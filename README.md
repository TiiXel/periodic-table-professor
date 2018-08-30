# Periodic Table Professor

An Android app that will help you memorize and associate name, atomic number and
symbol of elements in the Periodic Table.

Please note that this is a work in progress.

## How ?

John P. Pratt's [website](http://www.johnpratt.com/atomic/periodic.html) is a
[mnemonic peg system](https://en.wikipedia.org/wiki/Mnemonic_peg_system) for the
Periodic Table of the Elements.

> Each picture in this periodic table is designed to remind you of the element's
> name, atomic number, and abbreviation. There is also an explanation of how to
> use the pictures as memory pegs. At least memorize the first twenty! Each of
> those first 20 also has a unique color which can also be used for memorizing a
> list of twenty objects by associating a color with each.

With their permission, I turned this website into an Android application. This
app will show you *cards*, following a [flashcard](https://en.wikipedia.org/wiki/Flashcard)
method with the [spaced repetition](https://en.wikipedia.org/wiki/Spaced_repetition)
algorithm that is described here: [SM2+](http://www.blueraja.com/blog/477/a-better-spaced-repetition-learning-algorithm-sm2).

## Why ?

This is personal project on my side. I meant to learn how to build an Android 
application starting from nothing, applying programming best practices. I am
aiming for a testable, extendable application, following the Clean Architecture
principles.

I am a student physicist, fascinated by the Periodic Table of the Elements. I
used the memorization system described above prior to developing this app,
challenging myself to learn the table. I took me about a quarter. I then decided
to share the work of John P. Pratt with others, by the mean of an Android
application.

## Data sources

- Memory peg system: John P. Pratt's [website](http://www.johnpratt.com/atomic/periodic.html)
- Elements chemical and physical data: [mendeleev](https://bitbucket.org/lukaszmentel/mendeleev/overview)
python package database

## How do I use it ?

This application has not been released yet. It's not ready to be used, many
features are missing or not working as intended.

A pre-release is available on the [releases](https://github.com/TiiXel/periodic-table-professor/releases)
page of this Github repository, you can download and install it to your Android
device.

If you want to use and test the latest code, which might be unstable, you can
download the master branch sources and compile the apk.

## Main todo list

- [ ] Clean up gradle scripts
- [ ] Write unit tests
- [ ] Allow users to add personal notes for elements
- [x] Implement a statistics page, showing graphs of known elements over time
- [ ] Correct colors displayed in the Periodic Table activity
- [ ] Enhance the Periodic Table view, make it follow Material Design rules
- [x] Add an activity to display more details regarding a selected element
- [x] Welcome new users with a guided tour of the app
- [ ] Add an option to notify users when a card is ready to be reviewed