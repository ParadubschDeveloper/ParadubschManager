package de.paradubsch.paradubschmanager.util;

import org.jetbrains.annotations.Nullable;

public class StringValidator {
    public static @Nullable Integer isInteger(@Nullable String s) {
        if (s == null) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isMultipleOf(@Nullable Integer i, @Nullable Integer multiple) {
        if (i == null || multiple == null) return false;
        if (i < multiple) return false;
        return i % multiple == 0;
    }

    public static String toTitleCase(String str) {

        if (str == null || str.isEmpty()) return "";

        if(str.length() == 1) return str.toUpperCase();

        //split the string by space
        String[] parts = str.split(" ");

        StringBuilder sb = new StringBuilder( str.length() );

        for(String part : parts){

            if(part.length() > 1 )
                sb.append( part.substring(0, 1).toUpperCase() )
                        .append( part.substring(1).toLowerCase() );
            else
                sb.append(part.toUpperCase());

            sb.append(" ");
        }

        return sb.toString().trim();
    }
}
