package com.example.navigation_smd_7a;


import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
class DateValidator {

    // Method to validate and parse date
    public static String validateAndParseDate(String dateString) {
        // Try to parse in dd/MM/yyyy format
        SimpleDateFormat sdfWithSlash = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfWithoutSlash = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());  // Format without slashes

        try {
            // If the date is in dd/MM/yyyy format
            sdfWithSlash.setLenient(false);
            sdfWithSlash.parse(dateString);  // Try to parse the date
            return dateString;  // Valid date in dd/MM/yyyy format, return as is
        } catch (ParseException e) {
            // If parsing fails, try ddMMyyyy format (without slashes)
            try {
                sdfWithoutSlash.setLenient(false);
                Date date = sdfWithoutSlash.parse(dateString);
                // Format it back to dd/MM/yyyy format and add slashes
                return sdfWithSlash.format(date);  // Automatically add slashes and return
            } catch (ParseException ex) {
                return null;  // Invalid date format
            }
        }
    }

    // Method to get the current date in dd/MM/yyyy format
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }
}
