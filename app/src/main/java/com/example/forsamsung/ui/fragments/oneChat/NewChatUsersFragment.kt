package com.example.forsamsung.ui.fragments.oneChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.activities.Home
import com.example.forsamsung.databinding.FragmentNewUsersChatOneBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.utilits.*
import com.google.firebase.database.*

class NewChatUsersFragment: Fragment(),userAdapter3.Listener {
    private lateinit var binding: FragmentNewUsersChatOneBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewUsersChatOneBinding.inflate(inflater, container, false)
        val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
        view.isVisible = false
        with(binding){
            userRecyclerView=userNewChatList
            userRecyclerView.layoutManager = LinearLayoutManager(activity)
            userRecyclerView.setHasFixedSize(true)
            userArrayList= arrayListOf<User>()

            getUserData()


            back.setOnClickListener() {
                replaceActivity(Home())
            }
        }
        return binding.root
    }
    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnaphot in snapshot.children){
                        val user = userSnaphot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerView.adapter = userAdapter3(userArrayList, this@NewChatUsersFragment)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onClick(userId:String) {
        replaceFragment(OneChatFragment(userId))
    }
    override fun onClickBut(userId:String) {
        showToast("Запрос отправлен")
        sendZapros(userId)
    }
}