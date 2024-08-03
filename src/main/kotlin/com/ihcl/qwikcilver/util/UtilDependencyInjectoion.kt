package com.ihcl.qwikcilver.util

import com.ihcl.qwikcilver.repository.DatabaseRepository
import org.koin.dsl.module

val utilModule = module {
    single {
        Signature()
    }
    single{
        DatabaseRepository()
    }
}