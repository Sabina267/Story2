package com.example.forsamsung.utilits

import com.example.forsamsung.models.User
import com.example.telegram.models.CommonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

lateinit var AUTH: FirebaseAuth
lateinit var UID:String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: User

const val TYPE_TEXT = "text"

const val NODE_MESSAGES = "messages"
const val NODE_USERS = "users"
const val NODE_NAMEID = "nameid"
const val NODE_ZAPISI = "zapisi"

const val CHILD_ID = "id"
const val CHILD_EMAIL = "email"
const val CHILD_USERNAME = "username"
const val CHILD_NAMEID = "nameid"
const val CHILD_BIO = "bio"
const val CHILD_STATUS = "status"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "zapis"
const val CHILD_TIME = "time"
const val CHILD_ZAGOLOVOK = "zagolovok"
const val CHILD_TEXT_CHAT="text"
const val CHILD_FROM="from"

fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

fun saveZapis(textZapis: String, textZagolovok:String, function: () -> Unit) {
    val zapisKey = REF_DATABASE_ROOT.child(NODE_ZAPISI).child(UID).push().key.toString()
    val dateMap = hashMapOf<String,Any>()
    dateMap[CHILD_TEXT] = textZapis
    dateMap[CHILD_ZAGOLOVOK] = textZagolovok
    dateMap[CHILD_TIME] = ServerValue.TIMESTAMP
    val dateMap2 = hashMapOf<String,Any>()
    dateMap2[zapisKey] = dateMap
    REF_DATABASE_ROOT.child(NODE_ZAPISI).child(UID).updateChildren(dateMap2).addOnSuccessListener { function() }
}

fun initUser(){
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener{
            USER = it.getValue(User::class.java)?:User()
        })
}

fun send(message: String, userId: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$userId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$userId/$UID"

    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key
    val mapMessage = hashMapOf<String,Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TEXT_CHAT] = message

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }

}
fun sendZapros(message: String, userId: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$userId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$userId/$UID"

    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key
    val mapMessage = hashMapOf<String,Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TEXT_CHAT] = message

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }

}
fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()