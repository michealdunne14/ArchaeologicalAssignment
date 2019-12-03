package com.example.archaeologicalfieldwork.activities.Database

import android.content.Context
import com.example.archaeologicalfieldwork.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger

class HillfortFireStore(val context: Context):HillfortStore,AnkoLogger {
    val hillforts = ArrayList<HillFortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAllUsers(): List<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUsers(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUsers(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUser(id: Long): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUserByEmail(email: String): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllHillforts(user: UserModel): List<HillFortModel> {
        return hillforts
    }

    override fun findHillfort(user: UserModel, hillfortid: Long): HillFortModel? {
        val foundHillfort: HillFortModel? = hillforts.find { p -> p.id == hillfortid }
        return foundHillfort
    }

    override fun createHillfort(hillFortModel: HillFortModel, user: UserModel) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillFortModel.fbId = key
            hillforts.add(hillFortModel)
        }
        db.child("users").child(userId).child("hillforts").child(key!!).setValue(hillFortModel)
    }

    override fun updateHillforts(hillfort: HillFortModel, user: UserModel) {
        var foundHillfort: HillFortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.name = foundHillfort.name
            foundHillfort.description = foundHillfort.description
            foundHillfort.image = foundHillfort.image
            foundHillfort.location = foundHillfort.location
            db.child("users").child(userId).child("placemarks").child(foundHillfort.fbId).setValue(foundHillfort)

        }
    }

    override fun deleteHillforts(hillfort: HillFortModel, user: UserModel) {
        db.child("users").child(userId).child("placemarks").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }


    fun fetchHillforts(placemarksReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillFortModel>(HillFortModel::class.java) }
                placemarksReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }

    override fun findCurrentUser(): UserModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUser(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findNotes(hillfort: HillFortModel): List<Notes> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findImages(id: Long): List<Images> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createImages(images: Images) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNote(notes: Notes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findLocation(locationId: Long): Location {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllImages(): List<Images> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}