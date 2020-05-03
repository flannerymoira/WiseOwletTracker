package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wiseowlettracker.Entities.Student_Subject;

import java.util.ArrayList;

import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentPhone;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

// See the Student's account. Can change phone, daily and weekly targets and subject targets.
// Update student, study_target and student_subject tables.
public class StudentAccount extends AppCompatActivity {
    public static String sid = Long.toString(StudentId);
    public static int ssyId;
    Spinner subList;
    ArrayList<String> subNames;
    ArrayList<Student_Subject> StudentSubjectList;
    ImageView imgPhone, imgEmail;
    EditText txtPhone, txtEmail, txtDailyTarget, txtWeeklyTarget, txtSubjectTarget;
    SQLiteDatabase saDb;
    int StudentDailyTarget, StudentWeeklyTarget;
    String tmpDailyVal, tmpWeeklyVal;
    Button btn_upd_phone, btn_upd_target, btn_upd_subject, btn_add_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        //Access to database
        DatabaseOpenHelper nextConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        saDb = nextConn.getWritableDatabase();

        imgPhone = findViewById(R.id.img_phone);
        imgEmail = findViewById(R.id.img_email);
        subList = findViewById(R.id.subSpinner);

        txtPhone = findViewById(R.id.txtPhone);
        txtPhone.setText(StudentPhone);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(StudentEmail);

        getStudentDetails();

        txtDailyTarget = findViewById(R.id.editDailyTarget);
        tmpDailyVal = Integer.toString(StudentDailyTarget);
        txtDailyTarget.setText(tmpDailyVal);

        txtWeeklyTarget = findViewById(R.id.editWeeklyTarget);
        tmpWeeklyVal = Integer.toString(StudentWeeklyTarget);
        txtWeeklyTarget.setText(tmpWeeklyVal);

        txtSubjectTarget = findViewById(R.id.editSubjectTarget);

        getStudentSubjectList();

        ArrayAdapter<CharSequence> adapterSA = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subNames);
        subList.setAdapter(adapterSA);

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

        String sid = Long.toString(StudentId);

        btn_upd_phone = findViewById(R.id.btnUpdatePhone);

        btn_upd_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_S = txtPhone.getText().toString();
                boolean updatedPhone = updatePhone(phone_S);
                if (updatedPhone)
                    Toast.makeText(getApplicationContext(), "Updated phone successfully.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Did not update phone, please retry.", Toast.LENGTH_SHORT).show();
            }});

        btn_upd_target = findViewById(R.id.btnUpdateTarget);

        btn_upd_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dailyVal = txtDailyTarget.getText().toString();
                String weeklyVal = txtWeeklyTarget.getText().toString();
                boolean updatedTarget = updateTargets(dailyVal, weeklyVal);
                if (updatedTarget) {
                    Toast.makeText(getApplicationContext(), "Updated targets successfully.", Toast.LENGTH_SHORT).show();

                    StudentDailyTarget = Integer.parseInt(dailyVal);
                    StudentWeeklyTarget = Integer.parseInt(weeklyVal);
                }
                else
                    Toast.makeText(getApplicationContext(), "Did not update targets, please retry.", Toast.LENGTH_SHORT).show();
            }});

        btn_upd_subject = findViewById(R.id.btnUpdateSubject);

        btn_upd_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subTargetVal = txtSubjectTarget.getText().toString();
                int target = Integer.parseInt(subTargetVal);
                if ((target == 0) || (target > 100))
                    Toast.makeText(StudentAccount.this, "Subject target must be between 0 and 100.", Toast.LENGTH_LONG).show();
                else {

                    boolean updatedSubTarget = updateSubTarget(subTargetVal);
                    if (updatedSubTarget) {
                        Toast.makeText(getApplicationContext(), "Updated subject target successfully.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Did not update subject target, please retry.", Toast.LENGTH_SHORT).show();
                }
            }});

        btn_add_subject = findViewById(R.id.btnAddSubject);

        btn_add_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentAccount.this, SetupSubjects.class));
            }});
    }

    //get targets for the Student
    public void getStudentDetails() {
        String sid = Long.toString(StudentId);

        Cursor targetCursor = saDb.rawQuery("select daily_target, weekly_target from study_target where student_id =?", new String[]{sid});

        if (targetCursor.moveToFirst()) {
            StudentDailyTarget = targetCursor.getInt(0);
            StudentWeeklyTarget  = targetCursor.getInt(1);
        }

        targetCursor.close();
    }

    //method to get array of subjects for a student from student_subject table
    public void getStudentSubjectList() {
        String sid = Long.toString(StudentId);
        Student_Subject  student_subject;
        StudentSubjectList = new ArrayList<>();

        subNames = new ArrayList<>();
        subNames.add("(Subject / Target)");

        Cursor stuSubCursor = saDb.rawQuery("SELECT S.SUBJECT_NAME, SS.SUBJECT_TARGET, SS.SSY_ID, SS.Student_id FROM STUDENT_SUBJECT SS, SUBJECT S" +
                " WHERE SS.SUBJECT_ID = S.SUBJECT_ID AND SS.STUDENT_ID = ?", new String[]{sid});

        while (stuSubCursor.moveToNext()) {
                student_subject = new Student_Subject();
                student_subject.setSsy_Id(stuSubCursor.getInt(2));
                student_subject.setStudentId(stuSubCursor.getInt(3));
                StudentSubjectList.add(student_subject);

                subNames.add(stuSubCursor.getString(0) + " / " + stuSubCursor.getString(1));
        } // end while

        stuSubCursor.close();
    }

    //update phone on student table
    public boolean updatePhone(String phone) {

        Cursor updPhCursor =  saDb.rawQuery("update student set phone = ?  where student_id=?", new String[]{phone, sid});

        StudentPhone = phone;
        if(updPhCursor.getCount()>0)
        {updPhCursor.close();
            return false;}
        else
        {updPhCursor.close();
            return true;}
    }

    //update targets on study_target table
    public boolean updateTargets(String daily_target, String weekly_target) {

        Cursor updTargCursor =  saDb.rawQuery("update study_target set daily_target = ?, weekly_target = ? where student_id = ?", new String[]{daily_target, weekly_target, sid});

        if(updTargCursor.getCount()>0)
        {updTargCursor.close();
            return false;}
        else
        {updTargCursor.close();
            return true;}
    }

    //update subject target on student_subject table
    public boolean updateSubTarget(String subject_target) {
        String ssyid = Long.toString(ssyId);

        Cursor updSubCursor =  saDb.rawQuery("update student_subject set subject_target = ? where ssy_id = ?", new String[]{subject_target, ssyid});

        if(updSubCursor.getCount()>0)
        {updSubCursor.close();
            return false;}
        else
        {updSubCursor.close();
            return true;}
    }
}
