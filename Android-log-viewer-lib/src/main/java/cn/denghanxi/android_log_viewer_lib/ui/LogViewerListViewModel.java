package cn.denghanxi.android_log_viewer_lib.ui;

import androidx.lifecycle.ViewModel;

import java.io.File;

import io.reactivex.rxjava3.core.Single;

/**
 * Created by dhx on 2022/12/12
 */
public class LogViewerListViewModel extends ViewModel {

    Single<File[]> loadLogFiles(String dirPath) {

        return Single.fromCallable(() -> {
            File file = new File(dirPath);
            return file.listFiles();
        });
    }
}
