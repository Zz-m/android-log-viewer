package cn.denghanxi.android_log_viewer_lib.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


import cn.denghanxi.android_log_viewer_lib.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogViewerListActivity extends AppCompatActivity {
    public static final String FILE_PATH_KEY = "cn.denghanxi.android_log_viewer_lib.filePath";

    private final Logger logger = LoggerFactory.getLogger(LogViewerListActivity.class);
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final LogViewerListAdapter adapter = new LogViewerListAdapter();

    private LogViewerListViewModel viewModel;
    private RecyclerView recyclerView;

    private String logFileDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Toast.makeText(this, "未配置log文件路径", Toast.LENGTH_SHORT).show();
            logger.warn("需要以FILE_PATH_KEY 为key 传入文件夹路径");
            finish();
            return;
        }
        logFileDir = bundle.getString(FILE_PATH_KEY);

        if (TextUtils.isEmpty(logFileDir)) {
            Toast.makeText(this, "传入文件路径为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            File file = new File(logFileDir);
            if (!file.exists()) {
                Toast.makeText(this, "传入文件不存在", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            if (!file.isDirectory()) {
                Toast.makeText(this, "需要传入日志文件夹路径", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_log_viewer_list);

        viewModel = new ViewModelProvider(this).get(LogViewerListViewModel.class);

        //setup RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        enableSwipeToDeleteAndUndo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        disposables.add(viewModel.loadLogFiles(logFileDir).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::updateData, e -> logger.error("list log file err.", e)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final File item = adapter.getData().get(position);

                adapter.removeItem(position);

                try {
                    Files.deleteIfExists(item.toPath());
                    boolean success = item.delete();
                    if (!success) {
                        logger.warn("删除文件失败: {}", item.getName());
                    }
                } catch (IOException e) {
                    logger.warn("删除文件异常: {}\n", item.getName(), e);
                }
                Snackbar snackbar = Snackbar
                        .make(recyclerView, "删除日志.", Snackbar.LENGTH_SHORT);

                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}