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
* **Build Tool**: Maven 3.x
* **Automation**: Selenium 4 & Appium 3
* **Device**:
    * **Android** (Web & Native App)
    * **iOS** (Web supported; Native App in development)
* **Inspector Tool**: [Appium Inspector](https://github.com/appium/appium-inspector/releases)
* **Screen Mirroring**: [scrcpy](https://github.com/Genymobile/scrcpy/releases)

<br/>

## Project Structure

```md
src/test/java
├── common              # Shared constants and global configurations
├── factory             # Driver, capability, and page object creation logic
├── listener            # Test listeners for logging, reporting, and hooks
├── models              # Data models used across tests
├── pages
│ ├── android           # Android-specific page implementations
│ ├── web               # Web-specific page implementations
│ ├── interfaces        # Platform-agnostic page contracts
│ └── BasePage.java     # Shared base functionality for all pages
├── services            # External service management (e.g., Appium lifecycle)
├── tests               # Test classes and test setup logic
├── utils               # Reusable helper utilities
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

3. Verify Connected Devices

To run tests locally, ensure your device is recognised by your machine.

You should see at least one connected device or emulator listed.

**Android**:

```bash
adb devices
```

**iOS**:

```bash
xcrun simctl list devices | grep "(Booted)"
```

<br/>

## Running Tests

### Required Parameters

To maintain security, credentials and environment configurations must be passed dueing execution:

#### Security Credentials

* `-DADMIN_EMAIL`
* `-DADMIN_PASSWORD`
* `-DUSER_EMAIL`
* `-DUSER_PASSWORD`

#### Driver Configuration

* `-DPLATFORM_NAME`: `Android` or `iOS`
* `-DEXECUTION_TYPE`: `mobileWeb` or `nativeApp`
* `-DAUTOMATOR_NAME`: `UiAutomator2` or `XCUITest`
* `-DBROWSER_NAME`: `chrome` (Android) or `safari` (iOS)

### Execution Commands

1. Android Web

```bash
mvn clean test \
  -DPLATFORM_NAME=Android \
  -DEXECUTION_TYPE=mobileWeb \
  -DAUTOMATOR_NAME=UiAutomator2 \
  -DBROWSER_NAME=chrome \
  <REQUIRED_PARAMS>
```

2. Android Native App

```bash
mvn clean test \
  -DPLATFORM_NAME=Android \
  -DEXECUTION_TYPE=nativeApp \
  -DAUTOMATOR_NAME=UiAutomator2 \
  -DBROWSER_NAME=chrome \
  <REQUIRED_PARAMS>
```

3. iOS Web

```bash
mvn clean test \
  -DPLATFORM_NAME=iOS \
  -DEXECUTION_TYPE=mobileWeb \
  -DAUTOMATOR_NAME=XCUITest \
  -DBROWSER_NAME=safari \
  <REQUIRED_PARAMS>
```

### Appium Server Management

The framework provides flexibility in how the Appium server is handled during test execution:

#### Option 1: Automatic Setup (Recommended)

The framework is designed to be "plug-and-play". If no external server URL is provided, the framework will
automatically:

* Locate a free port on your machine.
* Start a local Appium server instance.
* Automatically shut down the server once the test suite completes.

**How to run**: Simply execute your `mvn clean test <OTHER_REQUIRED_PARAMS>` command with the required platform and
credential parameters. No manual server setup is required.

#### Option 2: Manual/Remote Server

If you prefer to use a running Appium server, you can bypass the internal manager by providing the server URL.

**How to run**:

1. Start your server manually (e.g., by typing `appium` in your terminal).
2. Add the `APPIUM_SERVER_URL` property to your Maven command:

```bash
mvn clean test \
  -DAPPIUM_SERVER_URL=APPIUM_SERVER_URL \ 
  <OTHER_REQUIRED_PARAMS>
```

**Note**: When running manually, ensure your Appium server has the necessary drivers installed (`uiautomator2` for
Android or `xcuitest` for iOS) to match your `-DAUTOMATOR_NAME` parameter.


<br/>
