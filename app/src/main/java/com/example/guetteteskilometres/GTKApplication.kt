package com.example.guetteteskilometres

import android.app.Application
import com.example.guetteteskilometres.di.AppDependencies

class GTKApplication: Application() {
    val dependencies by lazy { AppDependencies(this) }
}