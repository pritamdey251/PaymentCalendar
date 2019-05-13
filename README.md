# PaymentCalendar
CSCIE-37 Final Project: Payment Calendar

### Build
```shell
./gradlew installDebug
```
This should build the app apk under `./app/build/outputs/apk/debug/app-debug.apk`
The same apk has been copied under `./apk/app-debug.apk`

### Emulator
Before proceeding further, we need to start the local emulator. For this assignment I have used Pixel_3_API_28
To start an emulator:
```shell
./emulator @Pixel_3_API_28
```
The emulator should be up and running after this command.

### Install
#### Install MoreLocale 2 (Not required if you are using System Language settings to switch languages)
```shell
./adb install ./apk/MoreLocale2.apk 
```

#### Install MyFirstApp apk into the emulator
```shell
./adb install ./apk/app-debug.apk 
```

This installs MoreLocale2 and MyFirstApp app in the emulator running. Once both the apps are installed, we need to give permissions to MoreLocale2 to modify system Locales

```shell
./adb shell pm grant jp.co.c_lis.ccl.morelocale android.permission.CHANGE_CONFIGURATION
```

#### Run application
```shell
./adb shell am start -n "org.harvard.edu.cscie57a.paymentcalendar/org.harvard.edu.cscie57a.paymentcalendar.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
```

Now you should be able to view the PaymentCalendar app running on your android emulator.
You can switch locales by going to system settings and changing language/region or by using MoreLocale2 application.

#### Run application in Android Studio
This application can also be run in Android Studio. You would not need to configure a emulator on a command line in that case. You can also choose to install MoreLocale2 from PlayStore if desired.

### Demo
en_US             |  hi_IN
:-------------------------:|:-------------------------:
<img src="https://github.com/pritamdey251/PaymentCalendar/raw/master/demo/en_US.gif" alt="" height="350" width="200">  |  <img src="https://github.com/pritamdey251/PaymentCalendar/raw/master/demo/hi_IN.gif" alt="" height="350" width="200">


#### ToDo:
The Add Payment functionality is not implemented yet.

#### Conclusion:
This application is localized for hi_IN, en_US and fr_FR. For all other locales it will default to using english language but all the globalized APIs (like currency format, date format, number format, weekdays, month names etc) would continue to work.
