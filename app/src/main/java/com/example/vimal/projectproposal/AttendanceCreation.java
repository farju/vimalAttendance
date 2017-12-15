package com.example.vimal.projectproposal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Mera on 2017-12-12.
 */

public class AttendanceCreation extends DialogFragment {

    public interface SetLimitDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String code);
    }

    SetLimitDialogListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SetLimitDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.attendancescreen, null);
        Bundle b = this.getArguments();
        Class c = (Class) b.getSerializable("class");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        TextView text = (TextView) view.findViewById(R.id.startTime);
        TextView text2 = (TextView) view.findViewById(R.id.uniqueCode);
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("hh:mm a");
        String time = df.format(currentTime);

        text.setText("Started at " + time + " For " + c.getClass_name());

        final String value = generateCode();
        text2.setText(value);
        builder.setView(view);
        //c.setCode(value);
        builder.setPositiveButton("Take Attendance", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("Dialog", "Button clicked");
                mListener.onDialogPositiveClick(AttendanceCreation.this, value);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private String generateCode() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
