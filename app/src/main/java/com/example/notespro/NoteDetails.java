package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetails extends AppCompatActivity {

    EditText titleEditeText,contentEditeText;
    ImageButton savenotebutton;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditeMode=false;
    Button deletButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditeText=findViewById(R.id.notes_titles_text);
        contentEditeText=findViewById(R.id.notes_content_text);
        savenotebutton=findViewById(R.id.save_note_button);
        pageTitleTextView=findViewById(R.id.Pagetitle);
        deletButton=findViewById(R.id.delete_button);

        //receive data
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditeMode=true;
        }

        titleEditeText.setText(title);
        contentEditeText.setText(content);

        if(isEditeMode){
            pageTitleTextView.setText("Edite your note");
            deletButton.setVisibility(View.VISIBLE);
        }

        deletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference;
                documentReference=Utility.getCollectionRefferenceForNote().document(docId);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //note is deleted
                            Utility.showToast(NoteDetails.this,"Note Delete Successfully");
                            finish();
                        }else{
                            //note added fail
                            Utility.showToast(NoteDetails.this,"Failed to delete note");
                        }
                    }
                });
            }
        });



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
        if(isEditeMode){
            //update the note
            documentReference=Utility.getCollectionRefferenceForNote().document(docId);
        }else{
            //create note
            documentReference=Utility.getCollectionRefferenceForNote().document();
        }

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
