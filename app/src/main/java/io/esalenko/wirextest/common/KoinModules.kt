package io.esalenko.wirextest.common

import io.esalenko.wirextest.details.di.detailsModule
import io.esalenko.wirextest.main.di.mainModule

val appModules = listOf(mainModule, detailsModule)
