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
        if (patternIndex < patternLen && pattern[patternIndex] == '*') {
            if (stringIndex < stringLen) {
                return match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex)
                        || match(string, stringLen, stringIndex, pattern, patternLen, patternIndex + 1)
                        || match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex + 1);
            } else {
                return match(string, stringLen, stringIndex, pattern, patternLen, patternIndex + 1);
            }
        }
        if (pattern[patternIndex] == '?' || string[stringIndex] == pattern[patternIndex]) {
            return match(string, stringLen, stringIndex + 1, pattern, patternLen, patternIndex + 1);
        }
        if (pattern[patternIndex] == '[') {
            int index = patternIndex;
            boolean wrong = true;
            while (index < patternLen) {
                if (index == ']') {
                    wrong = false;
                } else {
                    index++;
                }
            }
            if (wrong == true) {
                throw new IllegalArgumentException();
            }
        }

        return false;


    }

}
