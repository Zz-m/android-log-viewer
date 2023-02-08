package cn.denghanxi.android_log_viewer_lib.ui.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cn.denghanxi.android_log_viewer_lib.R;

public class LogViewerDetailActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_PATH = "extra_file_path";

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
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("file:///" + filePath);
    }
}