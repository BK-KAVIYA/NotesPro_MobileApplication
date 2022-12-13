package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

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
                }
                Note note=new Note();
                note.setTitle(noteTitle);
                note.setContent(noteContent);
                note.setTimestamp(Timestamp.now());

                saveNoteToFirebase(note);
            }
        });
    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        documentReference=Utility.getCollectionRefferenceForNote().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetails.this,"Note Added Successfully");
                    finish();
                }else{
                    //note added fail
                    Utility.showToast(NoteDetails.this,"Failed to add note");
                }
            }
        });
    }
}
