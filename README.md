# Course Management Automation Framework

## Project Overview

This project demonstrates an **end-to-end automated test workflow** using **Selenium WebDriver** and **Appium** to
validate a complete **course management workflow** on the
[Ndosi Test Automation platform](https://ndosisimplifiedautomation.vercel.app/).

Although the application is accessed on a **mobile device**, the automation leverages **Selenium** (for web interaction)
and **Appium** (for mobile execution).

<br/>

## Test Coverage

The framework automates the following real-world scenario:

### Admin Workflow

1. Login as an admin user
2. Navigate to Admin panel
3. Create a new course
4. Verify the course successfully created

### Extended Workflow (Enhanced Scope)

5. Create a new user
6. Assign the newly created course to the user

### User Validation

7. Login as the created user
8. Verify the assinged course is visible

### Session Handling

9. Logout

<br/>

## Project Goal

The goal of this project is to:
* Demonstrate **end-to-end test automation** across multiple user roles.
* Validate **data consistency** between admin and user views.
* Showcase **mobile web automation** using Appium + Selenium.
* Build a **scalable and maintainable test framework**.

## Tech Stack

* **Language**: Java 21
* **Build Tool**: Maven 2.x
* **Version Control**: Git
* **Automation**: Selenium WebDriver
* **Module Automation**: Appium
* **Device**: 
  * **Android** (Real Device / Emulator) - fully supported
  * **iOS** (Planned) - not yet tested due to lack of device and iOS app
* **Inspector Tool**: Appium Inspector
* **Screen Mirroring**: scrcpy

<br/>

## Project Structure

<br/>

## Setup / Installation

1. Clone the repository

```bash
git clone https://github.com/vraagakrishna/automation.testing.project.course_management.git
cd automation.testing.project.course_management
```

2. Install dependencies

```bash
mvn clean install
```

3. TBC

<br/>

## Setup in IntelliJ IDEA

1. Import the project

* Open IntelliJ IDEA.
* Select **File** -> **Open**, and choose the cloned project folder.
* Wait for IntelliJ to download all Maven dependencies.

2. TBC

<br/>

## CI/CD Pipeline

TBC

<br/>
