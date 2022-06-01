package com.example.historyvideo.base

import android.app.Application
import android.os.Looper
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val disposables = CompositeDisposable()
    private val viewEventLiveData : MutableLiveData<AppEvent<String, Objects>> = MutableLiveData()
    private val loadingLiveData : MutableLiveData<Boolean> = MutableLiveData()

    open fun getViewEventLiveData(): MutableLiveData<AppEvent<String, Objects>> {
        return viewEventLiveData
    }

    open fun getLoadingLiveData(): MutableLiveData<Boolean> {
        return loadingLiveData
    }

    fun subscribe(disposable: Disposable): Disposable {
        disposables!!.add(disposable)
        return disposable
    }

    override fun onCleared() {
        super.onCleared()
        if (disposables != null && !disposables.isDisposed) {
            disposables.clear()
        }
    }

    protected open fun handleError(throwable: Throwable?) {
        loadingLiveData.postValue(false)
        if (throwable == null || TextUtils.isEmpty(throwable.localizedMessage)) {
            return
        }
        if (throwable is RetrofitException) {
            val retrofitException = throwable
            val isMainThread = Looper.myLooper() == Looper.getMainLooper()
            if (isMainThread) {
                when (retrofitException.kind) {
                    RetrofitException.Kind.HTTP, RetrofitException.Kind.UNEXPECTED -> viewEventLiveData.setValue(
                        AppEvent(AppEvent.LOCAL_ERROR, throwable.getLocalizedMessage())
                    )
                    RetrofitException.Kind.NETWORK -> viewEventLiveData.setValue(
                        AppEvent(
                            AppEvent.INTERNET_ERROR,
                            throwable.getLocalizedMessage()
                        )
                    )
                }
            } else {
                when (retrofitException.kind) {
                    RetrofitException.Kind.HTTP, RetrofitException.Kind.UNEXPECTED -> viewEventLiveData.postValue(
                        AppEvent(AppEvent.LOCAL_ERROR, throwable.getLocalizedMessage())
                    )
                    RetrofitException.Kind.NETWORK -> viewEventLiveData.postValue(
                        AppEvent(
                            AppEvent.INTERNET_ERROR,
                            throwable.getLocalizedMessage()
                        )
                    )
                }
            }
        }
        throwable.printStackTrace()
    }
}