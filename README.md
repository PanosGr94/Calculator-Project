# Calculator-Project
A simple calculator and a currency convertor to 5 international big currencies 


### Android Libraries Used:

[RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView), [ButterKnife](https://jakewharton.github.io/butterknife/)
, [CardView](https://developer.android.com/reference/android/support/v7/widget/CardView), [Retrofit](https://square.github.io/retrofit/),
[Viewmodel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel) and [Livedata](https://developer.android.com/reference/android/arch/lifecycle/LiveData)

### App's main features:

- Retrieving currency exchange json data through http://data.fixer.io/api/latest API endpoint
- Retrofit for the call and caching result
- Broadcast Receiver for network status updates
- ViewModel to share data between Fragments and Livedata to update values as they change from both the user and the network call.
- ViewPager for swiping seamlessly through Fragments and FragmentStatePagerAdapter to handle more efficiently their configuration changes and better their lifecycle manipulation.
- Calculator buttons made through a recyclerview implementation and operations update result on the spot
- Cool SplashScreen
- Landscape UI
- Error message shown when offline.
- Versioning through Git.
