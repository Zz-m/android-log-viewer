package cn.denghanxi.android_log_viewer_lib.ui.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import cn.denghanxi.android_log_viewer_lib.R;
import cn.denghanxi.android_log_viewer_lib.ui.detail.LogViewerDetailActivity;


/**
 * Created by dhx on 2022/12/12
 */
public class LogViewerListAdapter extends RecyclerView.Adapter<LogViewerListAdapter.ViewHolder> {
    private final List<File> logFiles = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log_viewer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setText(logFiles.get(position).getName());
        holder.setFilePath(logFiles.get(position).getAbsolutePath());
    }

    @Override
    public int getItemCount() {
        return logFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Logger logger = LoggerFactory.getLogger(ViewHolder.class);
        private final TextView textView;
        private String filePath = "";

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LogViewerDetailActivity.class);
                intent.putExtra(LogViewerDetailActivity.EXTRA_FILE_PATH, filePath);
                logger.debug("file path to go:{}", filePath);
                v.getContext().startActivity(intent);
            });
            textView = view.findViewById(R.id.tv_file_name);
        }

        public void setText(String text) {
            this.textView.setText(text);
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    void updateData(File[] files) {
        logFiles.clear();
        Collections.addAll(logFiles, files);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        logFiles.remove(position);
        notifyItemRemoved(position);
    }

    public List<File> getData() {
        return logFiles;
    }
}
