package com.bignerdranch.android.broncopals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bignerdranch.android.broncopals.adapter.ProfileAdapter
import com.bignerdranch.android.broncopals.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var manager: CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        init()
        getData()

       return binding.root
    }

    private fun init(){
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                Log.d("CardDragging", "Direction: $direction, Ratio: $ratio")
            }

            override fun onCardSwiped(direction: Direction?) {
                if (list != null && manager?.topPosition == list!!.size) {
                    Toast.makeText(requireContext(), "No more other profiles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {
                TODO("Not yet implemented")
            }

            override fun onCardCanceled() {
                TODO("Not yet implemented")
            }

            override fun onCardAppeared(view: View?, position: Int) {
                Log.d("CardAppeared", "Card appeared at position: $position")
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                Log.d("CardDisappeared", "Position: $position")
            }

        })
        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }

    companion object{
        var list : ArrayList<UserData>? = null
    }


    private fun getData(){
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    Log.d("Anthony","onDataChange: ${snapshot.toString()}")
                    if (snapshot.exists()){
                        val list = arrayListOf<UserData>()
                        for (data in snapshot.children){
                            val model = data.getValue(UserData::class.java)
                            if (model!!.username != FirebaseAuth.getInstance().currentUser!!.email){
                                if (model!=null)
                                    list.add(model)
                            }
                        }
                        list!!.shuffle()
                        init()
                        binding.cardStackView.layoutManager = manager
                        binding.cardStackView.itemAnimator = DefaultItemAnimator()
                        binding.cardStackView.adapter = ProfileAdapter(requireContext(), list)
                    }
                    else {
                        Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError){
                    Toast.makeText(requireContext(),error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}