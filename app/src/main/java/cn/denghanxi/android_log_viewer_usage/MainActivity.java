package cn.denghanxi.android_log_viewer_usage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.denghanxi.android_log_viewer_lib.ui.LogViewerActivity;

public class MainActivity extends AppCompatActivity {

    private final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("onCreate");
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_to_log_viewer);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogViewerActivity.class);
            String logDirPath = getFilesDir().getAbsolutePath() + "/log";
            intent.putExtra(LogViewerActivity.FILE_PATH_KEY, logDirPath);
            startActivity(intent);
        });
    }
}