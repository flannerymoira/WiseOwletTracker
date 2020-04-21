package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wiseowlettracker.Entities.Exam;
import com.example.wiseowlettracker.Entities.Student_Subject;

import java.util.ArrayList;

import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;
import static com.example.wiseowlettracker.StudentAccount.ssyId;

// Add a Student’s exam results.
// Student must enter exam, subject and result.
// Insert onto exam_result table.
public class StudentResults extends AppCompatActivity {
    public static int ExamId;
    Spinner subList, examNameList;
    ArrayList<Student_Subject> StudentSubjectList;
    SQLiteDatabase srDb;
    ArrayList<String> subNames, examType;
    ArrayList<Exam> examList;
    EditText editResult;
    Button btn_add_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_results);
        subList = findViewById(R.id.subSpinner);
        examNameList = findViewById(R.id.examSpinner);

        //Access to database
        DatabaseOpenHelper srConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        srDb = srConn.getWritableDatabase();

        getStudentSubjectList();
        getExamList();

        ArrayAdapter<CharSequence> adapterSR = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subNames);
        subList.setAdapter(adapterSR);

        //method to select an option from subject list
        subList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ssyId = StudentSubjectList.get(position - 1).getSsy_Id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapterEx = new ArrayAdapter(this, android.R.layout.simple_spinner_item, examType);
        examNameList.setAdapter(adapterEx);

        //method to select an option from type of study list
        examNameList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ExamId = examList.get(position -1).getExamId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_add_result = findViewById(R.id.btn_add_result);

        btn_add_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editResult = findViewById(R.id.editResult);
                String exam_res = editResult.getText().toString();
                Boolean addResult = createResult(exam_res);
                if (addResult)
                    Toast.makeText(getApplicationContext(), "Added result successfully.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Did not add, please retry.", Toast.LENGTH_SHORT).show();
            }});
    }

    //method to get array of subjects for a student from student_subject table
    private void getStudentSubjectList() {
        Student_Subject student_subject;
        String sid = Long.toString(StudentId);
        StudentSubjectList = new ArrayList<Student_Subject>();

        Cursor studentSubject = srDb.rawQuery("SELECT S.SUBJECT_NAME, SS.SSY_ID, SS.STUDENT_ID FROM STUDENT_SUBJECT SS, SUBJECT S" +
                " WHERE SS.SUBJECT_ID = S.SUBJECT_ID AND SS.STUDENT_ID = ?", new String[]{sid});

        subNames = new ArrayList<String>();
        subNames.add("(Select Subject )");

        while (studentSubject.moveToNext()) {
            student_subject = new Student_Subject();
            student_subject.setSsy_Id(studentSubject.getInt(1));
            student_subject.setStudentId(studentSubject.getInt(2));
            StudentSubjectList.add(student_subject);

            subNames.add(studentSubject.getString(0));

        } // end while

        studentSubject.close();

    }

    private void getExamList() {
        Exam exam;
        examList = new ArrayList<Exam>();

        examType = new ArrayList<String>();
        examType.add("(Select Exam )");

        Cursor examCursor = srDb.rawQuery("select * from exam ",null);

        while (examCursor.moveToNext()) {
            exam = new Exam();
            exam.setExamId(examCursor.getInt(0));
            exam.setExamName(examCursor.getString(1));
            exam.setYear(examCursor.getString(2));
            examList.add(exam);

            examType.add(exam.getExamName());
        } // end while

        examCursor.close();
    }
    //add exam result
    public boolean createResult(String exam_res) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("ssy_id",ssyId);
        contentValues.put("exam_id", ExamId);
        contentValues.put("achieved_mark",exam_res);

        long ins = srDb.insert("exam_result", null, contentValues);

        if(ins==-1)
        {return false;}
        else
            return true;
    }
}
