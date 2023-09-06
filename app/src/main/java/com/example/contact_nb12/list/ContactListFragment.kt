import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentContactListBinding
import com.example.contact_nb12.detail.DetailActivity
import com.example.contact_nb12.list.ContactAdapter
import com.example.contact_nb12.list.DataManager
import com.example.contact_nb12.list.ItemTouchHelperCallback
import com.example.contact_nb12.models.Contact

class ContactListFragment : Fragment() {
    lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater)

        // DataManager에서 데이터 가져오기
        val contacts = DataManager.getContacts()

        // RecyclerView에 어댑터 설정
        val adapter = ContactAdapter(contacts as MutableList<Contact>)
        binding.contactRecycler.adapter = adapter

        // RecyclerView 레이아웃 매니저 설정
        val layoutManager = LinearLayoutManager(requireContext())
        binding.contactRecycler.layoutManager = layoutManager

        // RecyclerView 아이템 클릭 이벤트 처리
        adapter.itemClick = object : ContactAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = contacts[position]

                // 전화 걸기 전 권한 확인
                if (isCallPhonePermissionGranted()) {
                    callPhoneNumber(selectedItem.phonenumber)
                } else {
                    // 권한이 없는 경우 권한 요청
                    requestCallPhonePermission()
                }
            }
        }

        // ItemTouchHelper 설정
        val recyclerView = binding.contactRecycler
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        val linearListViewAdapter = ContactAdapter(contacts)

        val callback = ItemTouchHelperCallback(linearListViewAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = linearListViewAdapter

        linearListViewAdapter.startDrag(object : ContactAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })

        return binding.root
    }

    private fun isCallPhonePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCallPhonePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CALL_PHONE),
            CALL_PHONE_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CALL_PHONE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용되었을 때 실행할 작업
                } else {
                    // 권한이 거부된 경우 사용자에게 알림
                    Toast.makeText(
                        requireContext(),
                        "전화 걸기 권한이 필요합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // 다른 권한 요청에 대한 처리 추가
        }
    }

    private fun callPhoneNumber(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    companion object {
        private const val CALL_PHONE_PERMISSION_REQUEST = 101
    }
}
