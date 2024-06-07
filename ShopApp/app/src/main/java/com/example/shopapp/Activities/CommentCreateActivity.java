package com.example.shopapp.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.Models.Comments;
import com.example.shopapp.Helpers.FileHelper;
import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.R;
import com.example.shopapp.databinding.ActivityCommentCreateBinding;

public class CommentCreateActivity extends AppCompatActivity {
    private ActivityCommentCreateBinding binding;
    private int selectedRating = 0;
    private String userName;
    private int productIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_create);

        binding = ActivityCommentCreateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = FileHelper.readFromFile(CommentCreateActivity.this, "account");
        productIndex = getIntent().getIntExtra("productIndex", 0);

        binding.ratingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                selectedRating = group.indexOfChild(checkedRadioButton) + 1;
                Log.d("Selected Rating", "Selected rating is: " + selectedRating);
            }
        });

        binding.commentSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRating <= 0) {
                    binding.commentInfoText.setText("Lütfen bir puan seçiniz.");
                    return;
                }

                if (binding.commentText.getText().length() <= 0) {
                    binding.commentInfoText.setText("Yorum kısmı boş geçilemez.");
                    return;
                }

                binding.commentInfoText.setText("Yorum kaydediliyor...");

                FirebaseDatabaseHelper<Comments> commentsFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Comments.class, "comments");
                String commentText = binding.commentText.getText().toString();
                Comments newComment = new Comments(userName, commentText, selectedRating, productIndex);
                commentsFirebaseDatabaseHelper.addDataWithListener(new FirebaseDatabaseHelper.DataListener<Void>() {
                    @Override
                    public void onDataReceived(Void data) {
                        binding.commentInfoText.setText("Yorum Kaydedildi.");
                        setResult(RESULT_OK);
                        finish();
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: ", e);
                        binding.commentInfoText.setText("Bir hata oluştu.");
                    }
                }, newComment);
            }
        });

        binding.commentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.commentInfoText.setText("Yorum iptal edildi.");
                finish();
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}