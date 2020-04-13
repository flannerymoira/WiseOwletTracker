package com.example.wiseowlettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;



import static com.example.wiseowlettracker.DatabaseHelper.StudentEmail;
import static com.example.wiseowlettracker.DatabaseHelper.StudentId;
import static com.example.wiseowlettracker.DatabaseHelper.StudentName;
import static com.example.wiseowlettracker.MainActivity.DATABASE_NAME;

public class StudentActivity extends AppCompatActivity {
    public static String TimeCompleted, FromDate;
    TextView txtName, txtMins;
    ImageButton btn_log, btn_result, btn_rep;
    SQLiteDatabase sumDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Access to database
        DatabaseOpenHelper firstConn = new DatabaseOpenHelper(this, DATABASE_NAME, null, 1);
        sumDb = firstConn.getReadableDatabase();

        txtName = findViewById(R.id.txtName);
        txtName.setText(StudentName + ".");

        txtMins = findViewById(R.id.txtViewMins);
        int tmpVal =  sumStudy();
        TimeCompleted = Integer.toString(tmpVal);
        txtMins.setText(TimeCompleted);

        btn_log = findViewById(R.id.btn_log);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, AddStudyLog.class));
            }
        });

        btn_result = findViewById(R.id.btn_result);

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, StudentResults.class));
            }
        });

        btn_rep = findViewById(R.id.btn_report);
        btn_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, ReportActivity.class));
            }
        });

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 5) {
            // Send Weekly email on Friday (day 5)
            sendMail();
        }

        sumDb.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //onclick listener for items (
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(StudentActivity.this, StudentAccount.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(StudentActivity.this, StopWatch.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Get total of time studied in the last week
    public int sumStudy() {
        String sid = Long.toString(StudentId);
        int totalStudy = 0;

        SimpleDateFormat dl = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date lastweek = cal.getTime();
        FromDate = dl.format(lastweek);

        Cursor sumStudyCursor =  sumDb.rawQuery("select sum(time_spent) from study_log sl, student_subject ss where" +
                " ss.ssy_id = sl.ssy_id and ss.student_id = ? and sl.entry_date > ?", new String[]{sid, FromDate});

        if (sumStudyCursor.moveToNext())
        { totalStudy = sumStudyCursor.getInt(0); }

        sumStudyCursor.close();
        return totalStudy;
    }

    //Get details of study from last week
    public String findDailyStudy() {
        String sid = Long.toString(StudentId);
        StringBuffer buffer = new StringBuffer();
        int tmpTime;
        String stTopic, stSubject, stTime, stDate;

        Cursor studyDetailCursor =  sumDb.rawQuery("select st.study_type, s.subject_name, sl.time_spent, sl.entry_date from study_log sl, student_subject ss, subject s, study_type st where" +
                " ss.ssy_id = sl.ssy_id and st.study_id = sl.study_id and ss.subject_id = s.subject_id and ss.student_id = ? and sl.entry_date > ? order by sl.entry_date asc", new String[]{sid, FromDate});

        while (studyDetailCursor.moveToNext())
        { stTopic = studyDetailCursor.getString(0);
        stTopic = stTopic.toLowerCase();
            stSubject = studyDetailCursor.getString(1);
            tmpTime = studyDetailCursor.getInt(2);
            stTime = String.valueOf(tmpTime);
            stDate = studyDetailCursor.getString(3);
            stDate= stDate.substring(0, stDate.indexOf(" "));
            buffer.append("Did " + stTopic + " in " + stSubject + " for " + stTime + " minutes on " + stDate + "." + System.getProperty("line.separator")); }

        studyDetailCursor.close();
        return buffer.toString();
    }

    public void sendMail() {
        try
        {
            LongOperation l=new LongOperation();
            l.execute();  //sends the email in background
            Toast.makeText(this, l.get(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);

        }
    }

    public class GMailSender extends javax.mail.Authenticator
    {
        private String mailhost = "smtp.gmail.com";
        private String user;
        private String password;
        private Session session;
        private Multipart _multipart;

        {
            Security.addProvider(new JSSEProvider());
        }

        public GMailSender(String user, String password)
        {
            this.user = user;
            this.password = password;

            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.quitwait", "false");

            session = Session.getDefaultInstance(props, this);
            _multipart = new MimeMultipart();
        }

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(user, password);
        }

        public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception
        {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);

            message.setDataHandler(handler);
//            message.setContent(_multipart);

            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);

        }

        public void addAttachment(String filename) throws Exception
        {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            _multipart.addBodyPart(messageBodyPart);
        }

        public class ByteArrayDataSource implements DataSource {
            private byte[] data;
            private String type;

            public ByteArrayDataSource(byte[] data, String type) {
                super();
                this.data = data;
                this.type = type;
            }

            public ByteArrayDataSource(byte[] data) {
                super();
                this.data = data;
            }

            public void setType(String type)
            {
                this.type = type;
            }

            public String getContentType() {
                if (type == null)
                    return "application/octet-stream";
                else
                    return type;
            }

            public InputStream getInputStream() throws IOException
            {
                return new ByteArrayInputStream(data);
            }

            public String getName()
            {
                return "ByteArrayDataSource";
            }

            public OutputStream getOutputStream() throws IOException
            {
                throw new IOException("Not Supported");
            }
        }
    }

    public final class JSSEProvider extends Provider
    {
        private static final long serialVersionUID = 1L;

        public JSSEProvider()
        {
            super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
            AccessController.doPrivileged(new java.security.PrivilegedAction<Void>()
            {
                public Void run()
                {
                    put("SSLContext.TLS",
                            "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                    put("Alg.Alias.SSLContext.TLSv1", "TLS");
                    put("KeyManagerFactory.X509",
                            "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                    put("TrustManagerFactory.X509",
                            "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                    return null;
                }
            });
        }
    }

    public class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {

                GMailSender sender = new GMailSender("WiseOwletTracker@gmail.com", "FinalProject");
                sender.addAttachment("c:/images/owlet.png");
                sender.sendMail("Wise Owlet Tracker Weekly Report",
                        toString(),
                        "WiseOwletTracker@gmail.com",
                        StudentEmail)                   ;
            } catch (Exception e) {

                Log.e("error", e.getMessage(), e);
                return "Weekly Email Not Sent";
            }
            return "Weekly Email Sent";
        }

        @Override
        public String toString() {
            String buffer = findDailyStudy();
            return "Hi " + StudentName + "," + System.getProperty("line.separator") + System.getProperty("line.separator")
                    + "congrats you have completed " + TimeCompleted + " minutes this week." + System.getProperty("line.separator")
                    + "The breakdown is  : " + System.getProperty("line.separator") + System.getProperty("line.separator") + buffer +  System.getProperty("line.separator")
                    + "Keep up the good work.";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("LongOperation",result+"");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

