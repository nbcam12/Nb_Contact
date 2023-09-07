import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

    private val requestContactPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            val list: MutableList<Contact>
            if (isGranted) {
                // 권한을 허용한 경우 처리
                list = getContacts()
            } else {
                // 권한을 거절한 경우 처리
                list = DataManager.getContacts().toMutableList()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_contact_denied_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
            initView(list)
        }

    // 전화권한요청
    private val requestCallPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한을 허용한 경우 처리
            } else {
                // 권한을 거절한 경우 처리
            }
        }

    companion object {
        private const val REQUEST_DETAIL = 101
        fun newInstacne() = ContactListFragment()
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

        // 연락처 권한설정
        initContactPermission()

        // 전화 걸기 전 권한 확인
        if (isCallPhonePermissionGranted()) {
        } else {
            // 권한이 없는 경우 권한 요청
            requestCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun initView(list: MutableList<Contact>) = with(binding) {

        contactRecycler.layoutManager = LinearLayoutManager(requireContext())

        // ItemTouchHelper 설정
        val adapter = ContactAdapter(list)
        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(contactRecycler)

        contactRecycler.adapter = adapter

        adapter.startDrag(object : ContactAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })


        // RecyclerView 아이템 클릭 이벤트 처리
        adapter.itemClick = object : ContactAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = list[position]
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("selectedItem", selectedItem) // 객체를 intent에 추가
                intent.putExtra("position", position)
                Log.d("tag", selectedItem.toString())
                startActivityForResult(intent, REQUEST_DETAIL) // DetailActivity 시작
            }
        }
    }

    private fun isCallPhonePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 디바이스 연락처 권한 요청하기
    private fun initContactPermission() {
        val checkPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            val list = getContacts()
            initView(list)
        } else {
            requestContactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }


    private fun getContacts(): MutableList<Contact> {
        val contacts = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val list = mutableListOf<Contact>()

        contacts?.let {
            if (contacts.count > 0) {
                while (it.moveToNext()) {
                    val id = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Data._ID))
                    val name =
                        it.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        it.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val emailCursor = requireContext().contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf("$id"),
                        null
                    )

                    emailCursor?.let {
                        if(it != null && it.moveToFirst()) {
                            val emailIndex: Int =
                                emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                            val email: String = emailCursor.getString(emailIndex)
                            emailCursor.close()

                            val model = Contact(
                                Img = R.drawable.dialog_profile,
                                name = name,
                                phonenumber = phoneNumber.toString(),
                                email = email,
                                birth = "",
                                nickname = "",
                            )
                            list.add(model)
                        }
                    }
                }
            }
        }

        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
