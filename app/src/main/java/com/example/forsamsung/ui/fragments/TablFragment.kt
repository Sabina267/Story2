package com.example.forsamsung.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.databinding.TablFragmentBinding
import com.example.forsamsung.models.User
import com.example.forsamsung.utilits.replaceFragment
import com.example.forsamsung.utilits.userAdapter
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class TablFragment: Fragment(), userAdapter.Listener {
    private lateinit var dbref: DatabaseReference
    private lateinit var binding: TablFragmentBinding
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TablFragmentBinding.inflate(inflater, container, false)
        with(binding){
            val view = requireActivity().findViewById<View>(com.example.forsamsung.R.id.bottomBar)
            view.isVisible = true
            userRecyclerView=userList
            userRecyclerView.layoutManager = LinearLayoutManager(activity)
            userRecyclerView.setHasFixedSize(true)
            userArrayList= arrayListOf<User>()

            getUserData()


        }
        return binding.root
    }



    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnaphot in snapshot.children){
                        val user = userSnaphot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }

                    userRecyclerView.adapter = userAdapter(userArrayList, this@TablFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    override fun onClick(userId:String) {
        replaceFragment(RandomFragment(userId))
    }

}
