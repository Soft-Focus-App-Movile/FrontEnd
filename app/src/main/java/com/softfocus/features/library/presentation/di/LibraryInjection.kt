package com.softfocus.features.library.presentation.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softfocus.core.networking.ApiConstants
import com.softfocus.features.library.data.di.LibraryDataModule
import com.softfocus.features.library.domain.repositories.LibraryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper para inyección de dependencias de Library en Composables
 *
 * Proporciona funciones de extensión para obtener fácilmente
 * repositorios y ViewModels en las pantallas Composable
 */

/**
 * Crea o reutiliza una instancia de Retrofit
 */
private fun getRetrofitInstance(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

/**
 * Provee el repositorio de Library
 *
 * @param context Contexto de la aplicación
 * @return Instancia del repositorio
 */
fun provideLibraryRepository(context: Context): LibraryRepository {
    return LibraryDataModule.provideLibraryRepository(
        context = context,
        retrofit = getRetrofitInstance()
    )
}

/**
 * Helper Composable para obtener un ViewModel con el repositorio inyectado
 *
 * Ejemplo de uso:
 * ```
 * @Composable
 * fun MyScreen() {
 *     val viewModel: MyViewModel = libraryViewModel { repository ->
 *         MyViewModel(repository)
 *     }
 * }
 * ```
 */
@Composable
inline fun <reified T : ViewModel> libraryViewModel(
    crossinline creator: (LibraryRepository) -> T
): T {
    val context = LocalContext.current
    val factory = remember {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                val repository = provideLibraryRepository(context)
                return creator(repository) as VM
            }
        }
    }
    return viewModel(factory = factory)
}

/**
 * Composable para obtener el repositorio directamente (sin ViewModel)
 *
 * Ejemplo de uso:
 * ```
 * @Composable
 * fun MyScreen() {
 *     val repository = rememberLibraryRepository()
 *     // Usar repository...
 * }
 * ```
 */
@Composable
fun rememberLibraryRepository(): LibraryRepository {
    val context = LocalContext.current
    return remember {
        provideLibraryRepository(context)
    }
}
