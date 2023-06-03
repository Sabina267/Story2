package com.example.forsamsung.ui.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forsamsung.R
import com.example.forsamsung.utilits.*
import com.example.telegram.models.CommonModel
import com.google.firebase.database.*

class NotificationsAdapter(val listener: Notifications): RecyclerView.Adapter<NotificationsAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameNot: TextView = view.findViewById(R.id.userNameItemNot)
        val userStatusNot:TextView = view.findViewById(R.id.userStatusItemNot)
        val prinyat: Button = view.findViewById(R.id.prinyatNot)
        val otklonit: Button = view.findViewById(R.id.otklonitNot)
        val pereiti:Button = view.findViewById(R.id.pereitiVChat)
        val otpr:TextView = view.findViewById(R.id.zaprosOtpravlen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return SingleChatHolder(view)
    }
    override fun getItemCount(): Int = mListMessagesCache.size
    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        var k1=0
        var k2=0
        val userTo = mListMessagesCache[position].to
        if (mListMessagesCache[position].from == UID) {
            holder.userNameNot.visibility = View.VISIBLE
            holder.prinyat.visibility=View.GONE
            holder.otklonit.visibility=View.GONE
            holder.pereiti.visibility=View.GONE
            holder.otpr.visibility=View.VISIBLE

            REF_DATABASE_ROOT.child("users").child(userTo).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.child("username").getValue(String::class.java)
                    holder.userNameNot.text = username
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        } else {
            holder.userNameNot.visibility = View.VISIBLE
            holder.prinyat.visibility=View.VISIBLE
            holder.otklonit.visibility=View.VISIBLE
            holder.pereiti.visibility=View.GONE
            holder.otpr.visibility=View.GONE
            val userFrom = mListMessagesCache[position].from




            REF_DATABASE_ROOT.child("users").child(userFrom).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.child("username").getValue(String::class.java)
                    holder.userNameNot.text = username
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            holder.otklonit.setOnClickListener {
                val databaseRef = FirebaseDatabase.getInstance().getReference("notification").child(userFrom)
                databaseRef.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        for (childSnapshot in dataSnapshot.children) {
                            println(dataSnapshot.key + " " + childSnapshot.key + " "+ childSnapshot.value)
                            if (childSnapshot.key=="from" && userFrom == childSnapshot.value){
                                k1=1
                            }
                            if (childSnapshot.key=="to" && userTo == childSnapshot.value){
                                k2=1
                            }
                            if (k1==1 && k2==1){
                                REF_DATABASE_ROOT.child("notification").child(userFrom).child(dataSnapshot.key.toString()).removeValue()
                                REF_DATABASE_ROOT.child("notification").child(userTo).child(dataSnapshot.key.toString()).removeValue()
                                k1=0
                                k2=0
                            }
                        }
                    }
                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
            holder.prinyat.setOnClickListener(){

                val databaseRef = FirebaseDatabase.getInstance().getReference("notification").child(userFrom)
                databaseRef.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        for (childSnapshot in dataSnapshot.children) {
                            if (childSnapshot.key=="from" && userFrom == childSnapshot.value){
                                k1=1
                            }
                            if (childSnapshot.key=="to" && userTo == childSnapshot.value){
                                k2=1
                            }
                            if (k1==1 && k2==1){
                                REF_DATABASE_ROOT.child("notification").child(userFrom).child(dataSnapshot.key.toString()).child("prinyato").setValue("yes")
                                REF_DATABASE_ROOT.child("notification").child(userTo).child(dataSnapshot.key.toString()).child("prinyato").setValue("yes")
                                k1=0
                                k2=0
                            }
                        }
                    }
                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }


        }
        var database = FirebaseDatabase.getInstance()
        val notificationsRefFrom = database.getReference("notification").child(mListMessagesCache[position].from)

        notificationsRefFrom.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val prinyato = childSnapshot.child("prinyato").getValue(String::class.java)
                    if (prinyato == "yes") {
                        holder.prinyat.visibility=View.GONE
                        holder.otklonit.visibility=View.GONE
                        holder.pereiti.visibility=View.VISIBLE
                        holder.otpr.visibility=View.GONE
                        break

                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        database = FirebaseDatabase.getInstance()
        val notificationsRefTo = database.getReference("notification").child(userTo)

        notificationsRefTo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    var prinyato = childSnapshot.child("prinyato").getValue(String::class.java)
                    if (prinyato == "yes") {
                        println("!!!@ "+childSnapshot)
                        holder.prinyat.visibility=View.GONE
                        holder.otklonit.visibility=View.GONE
                        holder.pereiti.visibility=View.VISIBLE
                        holder.otpr.visibility=View.GONE
                        break
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        holder.pereiti.setOnClickListener(){
            listener.onClick(userTo,mListMessagesCache[position].from )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CommonModel>) {
        mListMessagesCache = list
        notifyDataSetChanged()
    }
    interface Listener{
        fun onClick(idUserTo:String, idUserFrom:String)
    }
}
