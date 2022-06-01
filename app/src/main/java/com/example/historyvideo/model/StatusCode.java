package com.example.historyvideo.model;

import static com.example.historyvideo.model.StatusCode.API_ACCESS_DENIED;
import static com.example.historyvideo.model.StatusCode.API_NOT_FOUND;
import static com.example.historyvideo.model.StatusCode.API_SUCCESS;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        API_NOT_FOUND, API_SUCCESS, API_ACCESS_DENIED
})
public @interface StatusCode {
    int API_SUCCESS = 200;
    int API_NOT_FOUND = 404;
    int API_ACCESS_DENIED = 401;
    int API_ERROR_SAVE_DATA = 400;
}
