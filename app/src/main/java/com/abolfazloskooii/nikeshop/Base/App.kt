package com.abolfazloskooii.nikeshop.Base

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.abolfazloskooii.nikeshop.Model.Database.AppDatabase
import com.abolfazloskooii.nikeshop.Model.repositories.*
import com.abolfazloskooii.nikeshop.Servies.http.createRetrofit
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.*
import com.abolfazloskooii.nikeshop.Model.resource.*
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoader
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.abolfazloskooii.nikeshop.ViewModels.*
import com.facebook.drawee.backends.pipeline.Fresco
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import timber.log.Timber


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        val module = module {
            single { createRetrofit() }
            single { Room.databaseBuilder(this@App,AppDatabase::class.java,"db_products").build() }
            single <ImageLoadingServies> { ImageLoader() }
            factory<ProductsRepositoryManager> { ProductsRepository(ServerProductsDataSource(get()),get<AppDatabase>().getDatabase()) }
            factory<BannerRepositoryManager> { BannerRepository(ServerBannerDataSource(get())) }
            factory<CommentRepositoryManager> { CommentRepository(ServerCommentDataSource(get())) }
            factory<CartRepositoryManager> { CartRepository(ServerCartDataSource(get())) }
            single<SharedPreferences> { getSharedPreferences("shared_perf", MODE_PRIVATE) }
            single<PaymentRepositoryManager> { PaymentRepository(ServerPaymentDataSource(get())) }
            single<UserRepositoryManager> { UserRepository(ServerUserDataSource(get()),LocalUserDataSource(get())) }
            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle : Bundle) -> ProductViewModel(bundle,get(),get(),get()) }
            viewModel { (id : Int) -> CommentViewModel(id,get()) }
            viewModel { (sort : Int) -> AllProductViewModel(sort,get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { (orderId : Int) -> CheckOutViewModel(orderId,get()) }
            viewModel { PaymentViewModel(get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteViewModel(get(),get()) }
            viewModel { OrderHistoryViewModel(get()) }
            viewModel { AddCommentViewModel(get()) }
            viewModel { SearchViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(module)
        }

        val authRepository : UserRepositoryManager = get()
        authRepository.runToken()

    }
}