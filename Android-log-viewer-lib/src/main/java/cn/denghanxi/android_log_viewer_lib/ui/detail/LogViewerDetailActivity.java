package cn.denghanxi.android_log_viewer_lib.ui.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import cn.denghanxi.android_log_viewer_lib.R;

public class LogViewerDetailActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_PATH = "extra_file_path";

    private final Logger logger = LoggerFactory.getLogger(LogViewerDetailActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_viewer);

        String filePath = getIntent().getStringExtra(EXTRA_FILE_PATH);
        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(this, "File path is empty.", Toast.LENGTH_SHORT).show();
            finish();
        }
        File f = new File(filePath);
        if (!f.exists()) {
            Toast.makeText(this, "File not exist.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // setup the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            logger.warn("action bar is null, so disable default navigate up.");
        }

        //use webView to view the log file
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("file:///" + filePath);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}