package de.craftery.craftinglib.messaging.parser;

import lombok.Getter;
import lombok.Setter;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Fragment {
    @Getter
    @Setter
    private String contents;
    private String testRegex = "";
    private int testLen = 0;

    public Fragment(String contents) {
        this.contents = contents;
    }

    public boolean testRegex(@Language("RegExp") String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contents);

        testRegex = regex;
        return matcher.find();
    }

    public boolean testIsRangeWithoutRegex(@Language("RegExp") String regex) {
        Fragment contentNow = new Fragment(this.contents);
        int matchLen = 0;
        while (!contentNow.testRegex(regex) && !contentNow.isEmpty()) {
            matchLen++;
            contentNow.setContents(contentNow.getContents().substring(1));
        }
        this.testLen = matchLen;
        return matchLen > 0;
    }

    public String consumeRegex() {
        Pattern pattern = Pattern.compile(testRegex);
        Matcher matcher = pattern.matcher(contents);

        boolean found = matcher.find();

        if (!found) {
            System.err.println("No match found");
            return "";
        }
        String match = matcher.group();

        contents = contents.replaceFirst(testRegex, "");
        return match;
    }

    public String consume() {
        String removed = this.contents.substring(0, this.testLen);
        this.contents = this.contents.substring(this.testLen);
        return removed;
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }
}
