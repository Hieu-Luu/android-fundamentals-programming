package com.android.persistentdata.ui.room;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.persistentdata.R;

import static com.android.persistentdata.ui.room.WordRoomFragment.EXTRA_DATA_ID;
import static com.android.persistentdata.ui.room.WordRoomFragment.EXTRA_DATA_UPDATE_WORD;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.android.persistentdata.ui.room.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.persistentdata.ui.room.REPLY_ID";

    private EditText mEditWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);
        int id = -1;
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String word = extras.getString(EXTRA_DATA_UPDATE_WORD);
            if (!word.isEmpty()) {
                mEditWordView.setText(word);
                mEditWordView.setSelection(word.length());
                mEditWordView.requestFocus();
            }
        }

        final Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                        int id = extras.getInt(EXTRA_DATA_ID, -1);
                        if (id != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, id);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
