package com.example.forsamsung.activities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.forsamsung.R
import com.example.forsamsung.databinding.HomeBinding
import com.example.forsamsung.ui.fragments.PlayFragment
import com.example.forsamsung.ui.fragments.ProfileFragment
import com.example.forsamsung.ui.fragments.TablFragment
import com.example.forsamsung.utilits.*

open class Home():AppCompatActivity() {
    lateinit var binding: HomeBinding
    var selectedTab = 1

    override fun onResume() {
        super.onResume()
        getStatus()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        initFirebase()
        initUser()

        setContentView(binding.root)
        with(binding) {
            bottomBar.isVisible=true
            bottomBar.clipToOutline = true
            supportFragmentManager.commit{
                setReorderingAllowed(true)
                replace<PlayFragment>(R.id.fragmentContainer)
            }
            playLayout.setOnClickListener() {
                if (selectedTab != 1) {
                    supportFragmentManager.commit{
                        setReorderingAllowed(true)
                        replace<PlayFragment>(R.id.fragmentContainer)
                    }

                    notificationText.isVisible = false
                    profileText.isVisible = false

                    notificationImage.setImageResource(R.drawable.icon_notification)
                    profileImage.setImageResource(R.drawable.icon_profile)

                    notificationLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    playText.isVisible = true
                    playImage.setImageResource(R.drawable.icon_selected_play)
                    playLayout.setBackgroundResource(R.drawable.round_back_play_100)
                    playLayout.startAnimation(animateButton())

                    selectedTab = 1

                }
            }
            notificationLayout.setOnClickListener() {
                if (selectedTab != 2) {
                    supportFragmentManager.commit{
                        setReorderingAllowed(true)
                        replace<TablFragment>(R.id.fragmentContainer)
                    }
                    profileText.isVisible = false
                    playText.isVisible = false

                    profileImage.setImageResource(R.drawable.icon_profile)
                    playImage.setImageResource(R.drawable.icon_play)

                    profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    playLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    notificationText.isVisible = true
                    notificationImage.setImageResource(R.drawable.icon_selected_notification)
                    notificationLayout.setBackgroundResource(R.drawable.round_back_notification_100)

                    notificationLayout.startAnimation(animateButton())

                    selectedTab = 2

                }
            }
            profileLayout.setOnClickListener() {
                if (selectedTab != 3) {
                    supportFragmentManager.commit{
                        setReorderingAllowed(true)
                        replace<ProfileFragment>(R.id.fragmentContainer, "frag")
                    }

                    notificationText.isVisible = false
                    playText.isVisible = false

                    notificationImage.setImageResource(R.drawable.icon_notification)
                    playImage.setImageResource(R.drawable.icon_play)

                    notificationLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    playLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    profileText.isVisible = true
                    profileImage.setImageResource(R.drawable.icon_selected_profile)
                    profileLayout.setBackgroundResource(R.drawable.round_back_profile_100)

                    profileLayout.startAnimation(animateButton())

                    selectedTab = 3

                }
            }
        }
    }
}