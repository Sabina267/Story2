package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.activities.Home
import com.example.forsamsung.databinding.FragmentNotificationsBinding
import com.example.forsamsung.ui.fragments.oneChat.OneChatFragment
import com.example.forsamsung.utilits.*
import com.example.telegram.models.CommonModel
import com.google.firebase.database.*

class Notifications : Fragment(),NotificationsAdapter.Listener {
        private lateinit var binding: FragmentNotificationsBinding
        private lateinit var dbref: DatabaseReference
        private lateinit var userRecyclerView: RecyclerView
        private lateinit var userArrayList: ArrayList<CommonModel>
        private lateinit var userAdapter:NotificationsAdapter
        private lateinit var userNot:DatabaseReference
        private lateinit var userListener:AppValueEventListener
        private var userNotList = emptyList<CommonModel>()
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentNotificationsBinding.inflate(inflater, container, false)
            val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
            view.isVisible = false
            with(binding) {
                userRecyclerView = userNotificationList
                userRecyclerView.layoutManager = LinearLayoutManager(activity)
                userAdapter= NotificationsAdapter(this@Notifications)
                userNot= REF_DATABASE_ROOT
                    .child(NODE_NOTIFICATION)

                userRecyclerView.adapter=userAdapter

                val databaseRef = FirebaseDatabase.getInstance().getReference("notification").child(UID)
                databaseRef.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        for (childSnapshot in dataSnapshot.children) {
                            userNot= REF_DATABASE_ROOT
                                .child(NODE_NOTIFICATION)
                                .child(UID)
                            userListener = AppValueEventListener { dataSnapshot->
                                userNotList=dataSnapshot.children.map {
                                    it.getCommonModel()
                                }
                                userAdapter.setList(userNotList)
                            }
                            userNot.addValueEventListener(userListener)
                        }
                    }
                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
                back.setOnClickListener() {
                    replaceActivity(Home())
                }
            }
            return binding.root
        }

    override fun onClick(idUserTo: String, idUserFrom: String) {
        replaceFragment(SingleChatFragment(idUserTo,idUserFrom))
    }


}
