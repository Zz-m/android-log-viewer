# android-log-viewer
This is a simple library to view the Android application's log.

Example:
### Gradle

`implementation 'cn.denghanxi:android-log-viewer:0.0.1'`


Then in your activity:
```
String logDirPath = [YOUR APP LOG DIR PATH];
Intent intent = new Intent(this, LogViewerActivity.class);
intent.putExtra(LogViewerActivity.FILE_PATH_KEY, logDirPath);
startActivity(intent);
```
