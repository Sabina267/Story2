@file:Suppress("DEPRECATION")

package com.example.forsamsung.utilits

import android.content.Intent
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.forsamsung.R


fun Fragment.showToast(message:String){
    Toast.makeText(this.context,message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(this, activity::class.java)
    this.finish()
    startActivity(intent)

}

fun Fragment.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(context, activity::class.java)
    startActivity(intent)
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack:Boolean = true){
    if (addStack){
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.dataContainer,
                fragment
            ).commit()
    } else {
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer,
                fragment
            ).commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment){
    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(
            R.id.dataContainer,
            fragment
        )?.commit()
}

fun FieldEmpty(view: EditText): Boolean {
    if (view.text.toString() == "") {
        view.error = "Поле пустое"
        return true
    }
    return false

}

fun animateButton(): ScaleAnimation {
    val scaleAnimation = ScaleAnimation(
        0.8f,
        1.0f,
        1f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.0f,
        Animation.RELATIVE_TO_SELF,
        0.0f
    )
    scaleAnimation.duration
    scaleAnimation.fillAfter
    return scaleAnimation
}

