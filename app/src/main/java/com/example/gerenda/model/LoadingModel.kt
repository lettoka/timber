package com.example.gerenda.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.gerenda.BR

class LoadingModel:BaseObservable() {
    var loading=false
    @Bindable get
    set(value) {
        field=value;
        notifyPropertyChanged(BR.loading)
    }
}