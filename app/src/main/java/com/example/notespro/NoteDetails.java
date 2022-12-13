package com.example.notespro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NoteDetails extends AppCompatActivity {

    EditText titleEditeText,contentEditeText;
    ImageButton savenotebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditeText=findViewById(R.id.notes_titles_text);
        contentEditeText=findViewById(R.id.notes_content_text);
        savenotebutton=findViewById(R.id.save_note_button);

        savenotebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle=titleEditeText.getText().toString();
                String noteContent=contentEditeText.getText().toString();

                if(noteTitle==null || noteTitle.isEmpty()){
                    titleEditeText.setError("Title is required");
                    return;
                }else{

                }
            }
        });
    }
}
