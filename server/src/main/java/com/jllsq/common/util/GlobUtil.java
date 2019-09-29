package com.jllsq.common.util;

public class GlobUtil {

    public static boolean match(byte[] string, int stringLen, byte[] pattern, int patternLen) {
        return match(string, stringLen, 0, pattern, patternLen, 0);
    }

    private static boolean match(byte[] string, int stringLen, int stringIndex, byte[] pattern, int patternLen, int patternIndex) {
        if (stringIndex == stringLen && patternIndex == patternLen) {
            return true;
        }
        if (stringIndex < stringLen && patternIndex == patternLen) {
            return false;
        }

        if (pattern[patternIndex] == '[') {
            int index = patternIndex;
            boolean wrong = true;
            int start;
            int end;
            boolean range = false;
            while (index < patternLen) {
                if (pattern[index] == ']') {
                    wrong = false;
                    break;
                } else {
                    if (pattern[index] == '-') {
                        range = true;
                    }
                    index++;
                }
            }
            if (wrong) {
                throw new IllegalArgumentException();
            }
            if (range) {
                if (index - patternIndex < 4) {
                    throw new IllegalArgumentException();
                }
                start = pattern[patternIndex + 1];
                end = pattern[index - 1];
                if (string[stringIndex] >= start && string[stringIndex] <= end) {
                    return match(string, stringLen, stringIndex + 1, pattern, patternLen, index + 1);
                } else {
                    return false;
                }
            }
            boolean result = false;
            for (int i = patternIndex + 1; i < index; i++) {
                if (string[stringIndex] == pattern[i]) {
                    result = true;
                }
            }
            if (result) {
                return match(string, stringLen, stringIndex + 1, pattern, patternLen, index + 1);
            }
            return false;
        }
        if ( stringIndex < stringLen && pattern[patternIndex] == '\\') {
            if (patternIndex + 1 < patternLen && (pattern[patternIndex + 1] == '*' || pattern[patternIndex + 1] == '?' || pattern[patternIndex + 1] == '[')) {
                return string[stringIndex] == pattern[patternIndex + 1] && match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex + 2);
            } else {
                return string[stringIndex] == pattern[patternIndex] && match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex + 1);
            }
        }

        if (patternIndex < patternLen && pattern[patternIndex] == '*') {
            if (stringIndex < stringLen) {
                boolean r1 =  match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex);
                boolean r2 = match(string, stringLen, stringIndex, pattern, patternLen, patternIndex + 1);
                boolean r3 = match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex + 1);
                return r1 || r2 || r3;
            } else {
                return match(string, stringLen, stringIndex, pattern, patternLen, patternIndex + 1);
            }
        }
        if (stringIndex < stringLen && (pattern[patternIndex] == '?' || string[stringIndex] == pattern[patternIndex])) {
            return match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex + 1);
        }
        return false;


    }

}
