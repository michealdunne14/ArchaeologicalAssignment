package com.example.archaeologicalfieldwork.activities.Database

import android.content.Context
import android.graphics.Bitmap
import com.example.archaeologicalfieldwork.helper.readImageFromPath
import com.example.archaeologicalfieldwork.models.*
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context):HillfortStore,AnkoLogger {
    val hillforts = ArrayList<HillFortModel>()
    val searchedHillforts = ArrayList<HillFortModel>()
    val hillfortswithStars = ArrayList<HillFortModel>()
    val arrayListOfImages = ArrayList<Images>()
    var userModel = UserModel()
    lateinit var userId: String
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var st: StorageReference
    var key:String? = db.child("users").push().key

    override fun findAllUsers(): List<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUsers(user: UserModel) {
        key?.let {
            user.fbId = key as String
            userModel = user
            db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
        }
    }

    override fun updateUsers(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUser(id: Long): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUserByEmail(email: String): UserModel? {
        lateinit var user: UserModel
        return user
    }

    override fun findAllHillforts(user: UserModel): List<HillFortModel> {
        return hillforts
    }

    override fun findSearchedHillforts(): List<HillFortModel>{
        return searchedHillforts
    }

    override fun clearSearchResult(){
        searchedHillforts.clear()
    }

    override fun findHillfortsWithStar(): List<HillFortModel> {
        hillfortswithStars.clear()
        if(searchedHillforts.size != 0){
            for (i in searchedHillforts) {
                if (i.starCheck) {
                    hillfortswithStars.add(i)
                }
            }
        }else {
            for (i in hillforts) {
                if (i.starCheck) {
                    hillfortswithStars.add(i)
                }
            }
        }
        return hillfortswithStars
    }

    override fun findHillfort(marker: String): HillFortModel? {
        val foundHillfort: HillFortModel? = hillforts.find { p -> p.name == marker }
        return foundHillfort
    }

    override fun createHillfort(
        hillFortModel: HillFortModel,
        user: UserModel,
        listofImages: ArrayList<String>
    ) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillFortModel.fbId = key
            updateImage(listofImages,key)
            hillforts.add(hillFortModel)
        }
        db.child("users").child(userId).child("hillforts").child(key!!).setValue(hillFortModel)
    }

    override fun updateHillforts(hillfort: HillFortModel) {
        val foundHillfort: HillFortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.location = hillfort.location
        }
        db.child("users").child(userId).child("hillforts").child(foundHillfort!!.fbId).setValue(foundHillfort)
    }

    fun starHillfort(hillfort: HillFortModel){
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).child("starCheck").setValue(hillfort.starCheck)
    }

    fun likeHillfort(hillfort: HillFortModel){
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).child("starCheck").setValue(hillfort.starCheck)
    }


    override fun deleteHillforts(hillfort: HillFortModel, user: UserModel) {
        db.child("users").child(userId).child("placemarks").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    override fun fetchHills(){
        fetchHillforts {}
    }


    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(hillforts) { it.getValue<HillFortModel>(HillFortModel::class.java) }
            }
        }

        val imageEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(arrayListOfImages) { it.getValue<Images>(Images::class.java) }
                hillfortsReady()
            }
        }

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("image").addListenerForSingleValueEvent(imageEventListener)
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)

    }


    fun findHillforts(name: String){
        searchedHillforts.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("users").child(userId).child("hillforts")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    if(it.child("name").toString().contains(name)){
                        searchedHillforts.add(it.getValue<HillFortModel>(HillFortModel::class.java)!!)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun updateImage(hillfortImages: ArrayList<String>, fbId:String) {
        for (image in hillfortImages) {
            var images = Images()
            if (image != "") {
                val fileName = File(image)
                val imageName = fileName.getName()

                var imageRef = st.child(userId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, image)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            images.image = it.toString()
                            images.hillfortFbid = fbId
                            images.hillfortImageid = generateRandomId()
                            arrayListOfImages.add(images)
                            val key = db.child("users").child(userId).child("image").push().key
                            key?.let {
                                db.child("users").child(userId).child("image").child(key).setValue(images)
                            }
                        }
                    }
                }
            }
        }
    }
    val user = UserModel()

    override fun findCurrentUser(): UserModel {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(UserModel::class.java)
                post?.let {
                    user.name = it.name
                    user.email = it.email
                    user.password = it.password
                }
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("users").child(userId).addListenerForSingleValueEvent(valueEventListener)
        return user
    }

    override fun deleteUser(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findNotes(hillfort: HillFortModel): List<Notes> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getImages(): ArrayList<Images>{
        return arrayListOfImages
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