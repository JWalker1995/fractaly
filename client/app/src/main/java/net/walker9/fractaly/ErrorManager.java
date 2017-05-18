package net.walker9.fractaly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

/**
 * Created by joel on 3/22/17.
 */

public class ErrorManager {
    private TextView output;

    public ErrorManager(TextView output) {
        this.output = output;
    }

    public void error(Exception e) {
        e.printStackTrace();
        output.append(e.toString() + "\n");
    }
}
