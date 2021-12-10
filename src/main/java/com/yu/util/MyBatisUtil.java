package com.yu.util;

import com.yu.exception.RecordNotFoundException;

public class MyBatisUtil {

    public static <T> T valueOrRecordNotFoundErr(T value) throws RecordNotFoundException {
        if (value == null) {
            throw new RecordNotFoundException();
        } else {
            return value;
        }
    }

}
