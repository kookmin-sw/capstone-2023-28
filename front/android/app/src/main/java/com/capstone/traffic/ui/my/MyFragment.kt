package com.capstone.traffic.ui.my

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.traffic.databinding.FragmentMyBinding
import com.capstone.traffic.ui.feed.writefeed.WriteFeedActivity
import com.capstone.traffic.ui.feed.writefeed.addImage.Image
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.NonCancellable.start

class MyFragment : Fragment() {
    private val binding by lazy { FragmentMyBinding.inflate(layoutInflater) }
    private val MyViewModel: MyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            profileIV.setOnClickListener{
                openGallery()
            }

        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        imageResult.launch(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            CropImage.activity(result.data?.data).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true).start(requireContext(), this)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)
            if(resultCode == RESULT_OK){
                val resultUri = result.uri
                binding.profileIV.let {
                    Glide.with(this)
                        .asBitmap()
                        .load(resultUri)
                        .into(binding.profileIV)
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyViewModel.follower.observe(viewLifecycleOwner){
            binding.followerTv.text = it.toString()
        }
        MyViewModel.post.observe(viewLifecycleOwner){
            binding.postTv.text = it.toString()
        }
        MyViewModel.following.observe(viewLifecycleOwner){
            binding.followingTv.text = it.toString()
        }
        MyViewModel.nickname.observe(viewLifecycleOwner){
            binding.nicknameTv1.text = it
        }
    }
    companion object {
        fun newInstance(title: String) = MyFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}
