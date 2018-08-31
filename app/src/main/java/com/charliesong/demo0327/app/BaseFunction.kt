package com.charliesong.demo0327.app
import com.charliesong.demo0327.bean.BaseObjData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object BaseFunction{

    fun <T> handle():ObservableTransformer<BaseObjData<T>,T>{
        return  object :ObservableTransformer<BaseObjData<T>,T>{
            override fun apply(upstream: Observable<BaseObjData<T>>): ObservableSource<T> {

                return upstream.flatMap {
                    if(it.errorCode<0){
                       return@flatMap  Observable.error<T>(Exception(it.errorMsg))
                    }else{

                        return@flatMap createData(it.data?:(Any() as T))
                    }
                    return@flatMap Observable.empty<T>()
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
    private fun <T>  createData(t:T) :Observable<T>{
        return  Observable.create {
            it.onNext(t)
            it.onComplete()
        }
    }
}