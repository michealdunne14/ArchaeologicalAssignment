package com.example.archaeologicalfieldwork.activities.Database

import android.content.Context
import android.graphics.Bitmap
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.helper.readImageFromPath
import com.example.archaeologicalfieldwork.models.*
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context):HillfortStore,AnkoLogger {
    val hillforts = ArrayList<HillFortModel>()
    val hillfortswithStars = ArrayList<HillFortModel?>()
    val images = ArrayList<Images>()
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

    override fun findHillfortsWithStar(user: UserModel): List<HillFortModel?> {
        return hillfortswithStars
    }

    override fun findHillfort(user: UserModel, hillfortid: Long): HillFortModel? {
        val foundHillfort: HillFortModel? = hillforts.find { p -> p.id == hillfortid }
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
            for (i in listofImages){
                hillFortModel.image.image = i
                hillFortModel.image.imageid = generateRandomId()
                hillFortModel.image.hillfortImageid = hillFortModel.id
                updateImage(hillFortModel.image,key)
            }
            hillforts.add(hillFortModel)
        }
        db.child("users").child(userId).child("hillforts").child(key!!).setValue(hillFortModel)
    }

    override fun updateHillforts(hillfort: HillFortModel, user: UserModel) {
        var foundHillfort: HillFortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.name = foundHillfort.name
            foundHillfort.description = foundHillfort.description
            foundHillfort.location = foundHillfort.location
        }
        db.child("users").child(userId).child("hillforts").child(foundHillfort!!.fbId).setValue(foundHillfort)
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
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)


        val myTopPostsQuery = db.child("users").child(userId).child("hillforts")
        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val starcheck = postSnapshot.child("starCheck").value
                    if (starcheck == true){
                        val hillfort: HillFortModel? = postSnapshot.getValue(HillFortModel::class.java)
                        hillfortswithStars.add(hillfort)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun updateImage(hillfortImages: Images, fbId:String) {
        if (hillfortImages.image != "") {
            val fileName = File(hillfortImages.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, hillfortImages.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        hillfortImages.image = it.toString()
                        val key = db.child("users").child(userId).child("hillforts").child(fbId).child("image").push().key
                        key?.let {
                            db.child("users").child(userId).child("hillforts").child(fbId).child("image").child(key).setValue(hillfortImages)
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

    override fun findImages(id: Long): List<Images> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createImages(images: Images) {
        updateImage(images,images.hillfortFbid)
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