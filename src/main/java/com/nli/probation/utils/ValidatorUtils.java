package com.nli.probation.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ValidatorUtils {
    /**
     * check not negative number
     * @param number
     * @param numberErr
     * @param fieldName
     * @return number object
     */
    public static  Integer checkNotNegativeIntNumber(String number,
                                                     Map<String, String> numberErr, String fieldName) {
        Integer checkedNum = null;
        if(!number.matches("[0-9]{1,}")) {
            numberErr.put(fieldName, "must be a number and must not be negative");
            return null;
        }
        else {
            return Integer.parseInt(number);
        }
    }

    /**
     * check positive number or not
     * @param number
     * @param numberErr
     * @param fieldName
     * @return greater than zero or not
     */
    public static boolean checkGreaterThanZero(Integer number, Map<String, String> numberErr, String fieldName) {
        if(number <= 0) {
            numberErr.put(fieldName, "must be positive");
            return false;
        }
        return true;
    }

    /**
     * check one field is exist in class or not
     * @param checkedClass
     * @param fieldName
     * @return list of error
     */
    public static boolean checkExistFieldOfClass(Class<? extends Object> checkedClass, String fieldName) {
        try {
            Field field = checkedClass.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Map<String, String> notExistField = new HashMap<>();
            notExistField.put(fieldName, "does not exist");
            return false;
        }
        return true;
    }
}
