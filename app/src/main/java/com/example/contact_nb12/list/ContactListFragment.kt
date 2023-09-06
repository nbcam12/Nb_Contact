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
import androidx.activity.result.contract.ActivityResultContracts
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
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsList : MutableList<Contact>
    private val recyclerAdapter : ContactAdapter by lazy {
        ContactAdapter(contactsList)
    }
    // 전화권한요청
    private val requestCallPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if(isGranted) {
                // 권한을 허용한 경우 처리
            } else {
                // 권한을 거절한 경우 처리
            }
        }

    companion object {
        private const val REQUEST_DETAIL = 101
        private const val CALL_PHONE_PERMISSION_REQUEST = 101
        fun newDummyDataInstance() = ContactListFragment().apply {
            contactsList = DataManager.getContacts().toMutableList()

        }
        fun newDeviceContactsInstacne(list : MutableList<Contact>) = ContactListFragment().apply {
            contactsList = list
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        // 전화 걸기 전 권한 확인
        if (isCallPhonePermissionGranted()) {
        } else {
            // 권한이 없는 경우 권한 요청
            requestCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun initView() = with(binding){
        // RecyclerView 아이템 클릭 이벤트 처리
        recyclerAdapter.itemClick = object : ContactAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = contactsList[position]
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("selectedItem", selectedItem) // 객체를 intent에 추가
                intent.putExtra("position", position)
                Log.d("tag", selectedItem.toString())
                startActivityForResult(intent, REQUEST_DETAIL) // DetailActivity 시작
            }
        }

        contactRecycler.layoutManager = LinearLayoutManager(requireContext())

        // ItemTouchHelper 설정
        val callback = ItemTouchHelperCallback(recyclerAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(contactRecycler)

        contactRecycler.adapter = recyclerAdapter

        recyclerAdapter.startDrag(object : ContactAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })
    }

    private fun isCallPhonePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
