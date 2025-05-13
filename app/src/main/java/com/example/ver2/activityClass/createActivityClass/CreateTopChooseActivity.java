/*
    目的を設定するか、目標を設定するかを決めるActivity
 */

package com.example.ver2.activityClass.createActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ver2.R;
import com.example.ver2.activityClass.MainActivity;
import com.example.ver2.activityClass.createActivityClass.createGoal.CreateGoalChooseFrameworkActivity;
import com.example.ver2.activityClass.createActivityClass.createPurpose.CreatePurposeChooseFrameworkActivity;

public class CreateTopChooseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.create_top_choose);

        Button purposeButton = findViewById(R.id.choose_purpose);
        purposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTopChooseActivity.this, CreatePurposeChooseFrameworkActivity.class);
                startActivity(intent);
            }
        });

        //目標を設定する場合
        Button goalButton = findViewById(R.id.choose_goal);
        goalButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateTopChooseActivity.this, CreateGoalChooseFrameworkActivity.class);
                startActivity(intent);
        });

        //戻るボタン。アプリのホームに戻る
        Button backButton = findViewById(R.id.create_top_choose_backButton);
        backButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateTopChooseActivity.this, MainActivity.class);
                startActivity(intent);
        });
    }
}
