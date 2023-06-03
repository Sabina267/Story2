package com.example.forsamsung.utilits

import com.example.forsamsung.models.User
import com.example.telegram.models.CommonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

lateinit var AUTH: FirebaseAuth
lateinit var UID:String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: User

const val TYPE_TEXT = "text"

const val NODE_MESSAGES = "messages"
const val NODE_USERS = "users"
const val NODE_NAMEID = "nameid"
const val NODE_ZAPISI = "zapisi"
const val NODE_NOTIFICATION = "notification"
const val NODE_MESSAGES_ONE = "messageOne"

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
const val CHILD_TO="to"
const val CHILD_PRINYATO="prinyato"

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

fun saveZapisId(textZapis: String, textZagolovok:String, id:String, function: () -> Unit) {
    val zapisKey = REF_DATABASE_ROOT.child(NODE_ZAPISI).child(id).push().key.toString()
    val dateMap = hashMapOf<String,Any>()
    dateMap[CHILD_TEXT] = textZapis
    dateMap[CHILD_ZAGOLOVOK] = textZagolovok
    dateMap[CHILD_TIME] = ServerValue.TIMESTAMP
    val dateMap2 = hashMapOf<String,Any>()
    dateMap2[zapisKey] = dateMap
    REF_DATABASE_ROOT.child(NODE_ZAPISI).child(id).updateChildren(dateMap2).addOnSuccessListener { function() }
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
fun sendZapros(userId: String) {
    val refDialogUser = "$NODE_NOTIFICATION/$UID"
    val refDialogReceivingUser = "$NODE_NOTIFICATION/$userId"

    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key
    val mapMessage = hashMapOf<String,Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TO] = userId
    mapMessage[CHILD_PRINYATO] = "no"

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)

}

fun sendOne(message: String, userId: String, typeText: String, function: () -> Unit){
    val refDialogUser = "$NODE_MESSAGES_ONE/$UID/$userId"
    val refDialogReceivingUser = "$NODE_MESSAGES_ONE/$userId/$UID"

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

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()

fun getStatus(){
    val database = FirebaseDatabase.getInstance()
    val zapisiRef = database.getReference("zapisi")
    val idUserRef = zapisiRef.child(UID)

    idUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val count = dataSnapshot.childrenCount
            if (count<5){
                REF_DATABASE_ROOT.child("users").child(UID).child("status").setValue("Новичок")
                USER.status="Новичок"
            }
            else if (count>=5 && count<=10){
                REF_DATABASE_ROOT.child("users").child(UID).child("status").setValue("Любитель")
                USER.status="Любитель"
            }
            else{
                REF_DATABASE_ROOT.child("users").child(UID).child("status").setValue("Прошаренный")
                USER.status="Прошаренный"
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    })
}