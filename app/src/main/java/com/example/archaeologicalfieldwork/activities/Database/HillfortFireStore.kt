package com.example.archaeologicalfieldwork.activities.Database

import android.content.Context
import android.graphics.Bitmap
import com.example.archaeologicalfieldwork.helper.readImageFromPath
import com.example.archaeologicalfieldwork.models.*
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context):HillfortStore,AnkoLogger {
    val hillforts = ArrayList<HillFortModel>()
    val searchedHillforts = ArrayList<HillFortModel>()
    val hillfortswithStars = ArrayList<HillFortModel>()
    val hillfortsShared = ArrayList<HillFortModel>()
    val imagesShared = ArrayList<Images>()
    val sharedHillforts = ArrayList<Share>()
    val arrayListOfImages = ArrayList<Images>()
    val searchedUsers = ArrayList<UserModel>()
    val notes = ArrayList<Notes>()
    var user:UserModel = UserModel()
    private lateinit var userId: String
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var st: StorageReference

    override fun findAllUsers(): List<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUsers(userModel: UserModel) {
        val key:String? = db.child("users").push().key
        key?.let {
            userModel.fbId = key
            user = userModel
            db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userModel)
        }
    }

    override fun createNote(note: String, fbId: String){
        val notes = Notes()
        val key:String? = db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).push().key
        key?.let {
            notes.hillfortNotesid = fbId
            notes.note = note
            db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("notes").child(key).setValue(notes)
        }
    }

    fun findNotes(fbId: String){
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val noteModel = it.getValue<Notes>(Notes::class.java)!!
                    if (noteModel.hillfortNotesid == fbId) {
                        notes.add(noteModel)
                    }
                }
            }
        }
        db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("notes").addValueEventListener(valueEventListener)
    }

    fun getArrayListofNotes(): ArrayList<Notes>{
        return notes
    }


    override fun updateUsers(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun findSharedHillforts(user: UserModel) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val share = it.getValue<Share>(Share::class.java)!!
                    sharedHillforts.add(share)
                }
            }
        }

        val eventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapShot in dataSnapshot.children){
                    for (share in sharedHillforts) {
                        if (postSnapShot.child("fbId").value == share.sharedUser){
                            val hillModel = postSnapShot.child("hillforts").child(share.sharedHillfort).getValue(HillFortModel::class.java)!!
                            hillfortsShared.add(hillModel)
                            val sharedImages = postSnapShot.child("image").children
                            for (images in sharedImages){
                                if (images.child("hillfortFbid").value == share.sharedHillfort){
                                    imagesShared.add(images.getValue(Images::class.java)!!)
                                }
                            }
                        }
                    }
                }
            }
        }

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("sharedHillforts").child(user.fbId).addValueEventListener(valueEventListener)
        db.child("users").addValueEventListener(eventListener)
    }


    override fun getSharedHillforts(): List<HillFortModel>{
        return hillfortsShared
    }

    override fun getSharedImages(): ArrayList<Images>{
        return imagesShared
    }

    override fun sharingHillfort(email: String, hillfort: HillFortModel) {
        var userModel = UserModel()
        val share = Share()
        searchedUsers.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("users")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in dataSnapshot.children) {
                    if (i.child("email").toString().contains(email)) {
                        userModel = i.getValue<UserModel>(UserModel::class.java)!!
                        share.sharedUser = user.fbId
                        share.sharedHillfort = hillfort.fbId
                        val key = db.child("sharedHillforts").child(userModel.fbId).push().key
                        db.child("sharedHillforts").child(userModel.fbId).child(key!!).setValue(share)
                        break
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun findUserByEmail(email: String): UserModel? {
        lateinit var user: UserModel
        return user
    }

    override fun findAllHillforts(): List<HillFortModel> {
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
            foundHillfort.datevisted = hillfort.datevisted
            foundHillfort.visitCheck =hillfort.visitCheck
            foundHillfort.starCheck = hillfort.starCheck
            foundHillfort.rating = hillfort.rating
        }
        db.child("users").child(userId).child("hillforts").child(foundHillfort!!.fbId).setValue(foundHillfort)
    }

    fun starHillfort(hillfort: HillFortModel){
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).child("starCheck").setValue(hillfort.starCheck)
    }

    fun likeHillfort(hillfort: HillFortModel){
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).child("visitCheck").setValue(hillfort.visitCheck)
    }


    override fun deleteHillforts(hillfort: HillFortModel, user: UserModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    if (it.child("hillfortFbid").value.toString() == hillfort.fbId){
                        val imagekey = it.key
                        // Create a storage reference from our app
                        st = FirebaseStorage.getInstance().reference

                        val desertRef = st.child(userId)
                        val arrayList = ArrayList<String>()
                        desertRef.listAll().addOnSuccessListener(OnSuccessListener<ListResult> { result ->
                                for (fileRef in result.items) {
                                    arrayList.add(fileRef.name)
                                }
                            }).addOnFailureListener(OnFailureListener {
                                // Handle any errors
                            })

                        for (fileRef in arrayList) {
                            if (it.child("image").value.toString().contains(fileRef)) {
                                desertRef.child(fileRef).delete().addOnSuccessListener {
                                    db.child("users").child(userId).child("image").child(imagekey!!).removeValue()
                                }.addOnFailureListener {
                                    // Uh-oh, an error occurred!
                                }
                                db.child("users").child(userId).child("image").child(imagekey!!).removeValue()
                            }
                        }
                    }
                }
            }
        }

        db.child("users").child(userId).child("image").addValueEventListener(valueEventListener)
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }


    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hillforts.clear()
                dataSnapshot.children.mapNotNullTo(hillforts) { it.getValue<HillFortModel>(HillFortModel::class.java) }
            }
        }

        val imageEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayListOfImages.clear()
                dataSnapshot.children.mapNotNullTo(arrayListOfImages) { it.getValue<Images>(Images::class.java) }
                hillfortsReady()
            }
        }

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("users").child(userId).child("image").addValueEventListener(imageEventListener)
        db.child("users").child(userId).child("hillforts").addValueEventListener(valueEventListener)
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

    fun deleteImage(
        hillfortImages: ArrayList<Images>,
        currentItem: Int,
        stringList: ArrayList<String>
    ){
        val image = hillfortImages[currentItem]
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (it in dataSnapshot.children) {
                    if (image.hillfortImageid.toString() == it.child("hillfortImageid").value.toString()) {
                        db.child("users").child(userId).child("image").child(it.key!!).removeValue()
                        hillfortImages.removeAt(currentItem)
                        stringList.removeAt(currentItem)
                        break
                    }
                }
            }
        }
        db.child("users").child(userId).child("image").addValueEventListener(valueEventListener)
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

    override fun currentUser():UserModel {
        return user
    }

    override fun findCurrentUser() {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(UserModel::class.java)
                post?.let {
                    user.name = it.name
                    user.email = it.email
                    user.password = it.password
                    user.fbId = it.fbId
                }
            }
        }
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("users").child(userId).addListenerForSingleValueEvent(valueEventListener)
    }

    override fun deleteUser(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getImages(): ArrayList<Images>{
        return arrayListOfImages
    }

    override fun findLocation(locationId: Long): Location {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllImages(): List<Images> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}