package com.glance.artbet.data.local_storage

import android.annotation.SuppressLint
import android.content.Context
import com.glance.artbet.BuildConfig
import com.glance.artbet.domain.repository.BaseRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LocalStorageRepository @Inject constructor(context: Context) : BaseRepository(context) {

    private val paperBookName = "localStorage." + BuildConfig.APPLICATION_ID

    private val storedTokenKey = "storedTokenKey"

    private val tutorialShownKey = "tutorialShownKey"
    private val tutorialShownDefaultValue: Boolean = false

    private val userProfileKey = "userProfile"
    private val localSettingsKey = "localSettings"
    private val receptionInfoKey = "receptionInfo"
    private val notificationReadMarkerKey = "notificationReadMarkerKey"
    private val welcomeNotificationShownKey = "welcomeNotificationShownKey"

    private val paperBook by lazy { RxPaperBook.with(paperBookName) }

    fun clear(): Completable = Completable.concat(
        arrayListOf(
            paperBook.delete(storedTokenKey),
            paperBook.delete(userProfileKey),
            paperBook.delete(localSettingsKey),
            paperBook.delete(receptionInfoKey),
            paperBook.delete(notificationReadMarkerKey),
            paperBook.delete(welcomeNotificationShownKey)
        )
    ).getSchedulers()

    private fun getToken(): Single<LoginResponse> {
        return paperBook.read(storedTokenKey, LoginResponse()).onErrorReturn {
            LoginResponse()
        }
    }

    fun saveToken(tokens: LoginResponse): Completable {
        return paperBook.write<LoginResponse>(storedTokenKey, tokens)
    }

    fun getBearerToken(): Single<String> {
        return getToken().map { if (it.token.isEmpty()) "" else "Bearer ${it.token}" }.getSchedulers()
            .onErrorReturn {
                ""
            }
    }

    fun isAuthorized(): Single<Boolean> {
        return getToken().map { !it.token.isBlank() }.getSchedulers().onErrorReturn { false }
    }

    fun getTutorialShown(): Single<Boolean> {
        return paperBook.read(tutorialShownKey, tutorialShownDefaultValue).getSchedulers()
    }

    @SuppressLint("CheckResult")
    fun setTutorialShown() {
        paperBook.write<Boolean>(tutorialShownKey, true).subscribe({}, {})
    }

    fun setLocalSettings(localSettings: LocalSettings): Completable {
        return paperBook.write<LocalSettings>(localSettingsKey, localSettings)
    }

    fun setLocalSettingsWithCompletable(localSettings: LocalSettings): Completable {
        return paperBook.write<LocalSettings>(localSettingsKey, localSettings)
    }

    fun getLocalSettings(): Single<LocalSettings?> {
        return paperBook.read<LocalSettings>(localSettingsKey, LocalSettings()).getSchedulers()
    }

    @SuppressLint("CheckResult")
    fun setUserProfile(profile: UserProfile) {
        paperBook.write<UserProfile>(userProfileKey, profile).subscribe({}, {})
    }

    fun setUserProfileWithImage(imageUrl: String): Completable {
        return getUserProfile().flatMapCompletable {
            it.image = imageUrl
            paperBook.write<UserProfile>(userProfileKey, it)
        }
    }

    fun isUserFirstTimeLogin(): Single<Boolean> {
        return getUserProfile().map { it.isFirstTime }.getSchedulers()
    }

    fun setOffUserFirstTimeLogin(): Completable {
        return getUserProfile().flatMapCompletable {
            it.isFirstTime = false
            paperBook.write<UserProfile>(userProfileKey, it)
        }.getSchedulers()
    }

    fun changeWelcomeNotificationVisibility(shouldShow: Boolean): Completable {
        return paperBook.write<Boolean>(welcomeNotificationShownKey, shouldShow).getSchedulers()
    }

    fun getWelcomeNotificationVisibility(): Single<Boolean> {
        return paperBook.read<Boolean>(welcomeNotificationShownKey, false).getSchedulers()
            .onErrorReturnItem(false)
    }

    fun setUserProfileWithObservable(profile: UserProfile): Completable {
        return paperBook.write<UserProfile>(userProfileKey, profile)
    }

    fun getUserProfile(): Single<UserProfile> {
        return paperBook.read<UserProfile>(userProfileKey, UserProfile.getEmptyProfile())
            .getSchedulers()
            .onErrorReturn { UserProfile.getEmptyProfile() }
    }

    fun setReceptionInfoWithObservable(info: ReceptionInfoModel): Completable {
        return paperBook.write<ReceptionInfoModel>(receptionInfoKey, info)
    }

    fun getReceptionInfo(): Single<ReceptionInfoModel> {
        return paperBook.read<ReceptionInfoModel>(
            receptionInfoKey,
            ReceptionInfoModel.getEmptyModel()
        ).getSchedulers()
    }

    fun setNotificationRead(marker: NotificationReadMarkerModel): Single<HashMap<NotificationReadMarkerModel, Boolean>> {
        return getNotificationReadMarkers().flatMap {
            it.put(marker, true)
            writeNotificationReadMarkers(it)
        }
    }

    fun writeNotificationReadMarkers(model: HashMap<NotificationReadMarkerModel, Boolean>): Single<HashMap<NotificationReadMarkerModel, Boolean>> {
        return paperBook.write(notificationReadMarkerKey, model).toSingleDefault(model).getSchedulers()
    }

    private fun getNotificationReadMarkers(): Single<HashMap<NotificationReadMarkerModel, Boolean>?> {
        return paperBook
            .read<HashMap<NotificationReadMarkerModel, Boolean>>(notificationReadMarkerKey, hashMapOf())
            .getSchedulers()
            .onErrorReturn { hashMapOf() }
    }

    fun isNotificationMarkerRead(marker: NotificationReadMarkerModel): Single<Boolean> {
        return getNotificationReadMarkers().map { markers ->
            markers.any { it.key.isEqual(marker) }
        }
    }
}
