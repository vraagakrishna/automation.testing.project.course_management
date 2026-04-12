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
8. Verify the assigned course is visible

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
* **Inspector Tool**: [Appium Inspector](https://github.com/appium/appium-inspector/releases)
* **Screen Mirroring**: [scrcpy](https://github.com/Genymobile/scrcpy/releases)

<br/>

## Project Structure

```md
src/test/java
├── common # Shared constants and global configurations
├── factory # Driver, capability, and page object creation logic
├── listener # Test listeners for logging, reporting, and hooks
├── models # Data models used across tests
├── pages
│ ├── android # Android-specific page implementations
│ ├── web # Web-specific page implementations
│ ├── interfaces # Platform-agnostic page contracts
│ └── BasePage.java # Shared base functionality for all pages
├── services # External service management (e.g., Appium lifecycle)
├── tests # Test classes and test setup logic
├── utils # Reusable helper utilities
│
pom.xml # Project dependencies and build configuration
testng.xml # Test suite configuration
```

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

3. Verify Android Device (for mobile tests)

```bash
adb devices
```

You should see at least one connected device or emulator listed.

<br/>

## Running Tests

There are multiple ways to execute tests depending on:

* How the Appium server is started
* Which platform you want to run (Web or Android)

> **Default Behaviour**: <br/>
> If no execution type is specified, tests will run in Web (mobileWeb) mode.

### Required Parameters

All tests run require the following:

```bash
-DADMIN_EMAIL=ADMIN_EMAIL \ 
-DADMIN_PASSWORD=ADMIN_PASSWORD \ 
-DUSER_EMAIL=USER_EMAIL \
-DUSER_PASSWORD=USER_PASSWORD
```

### Execution Modes

| Mode          | Parameter Value | Description               | 
|:--------------|:----------------|:--------------------------|
| Web (Default) | `mobileWeb`     | Runs tests in browser     | 
| Android App   | `nativeApp`     | Runs tests on Android app | 

### Appium Server Options

#### Option 1: Start Appium Manually

```bash
appium
```

Then run

```bash
mvn test \
-DAPPIUM_SERVER_URL=APPIUM_SERVER_URL \
<REQUIRED_PARAMS> 
```

#### Option 2: Start Appium in Code (Recommended)

No manual setup required - the framework manages Appium.

```bash
mvn test <REQUIRED_PARAMS> 
```

### Run Web Tests (Default)

```bash
mvn test \
-DEXECUTION_TYPE=mobileWeb \
<REQUIRED_PARAMS> 
```

Or explicitly:

```bash
mvn test <REQUIRED_PARAMS> 
```

### Run Android Tests

```bash
mvn test \
-DEXECUTION_TYPE=nativeApp \
<REQUIRED_PARAMS> 
```

### Notes

* Ensure your environment variables (e.g., `ANDROID_HOME`) are properly configured.
* Make sure an emulator or real device is running before executing Android tests.
* Platform selection is controlled via runtime parameters.

<br/>
