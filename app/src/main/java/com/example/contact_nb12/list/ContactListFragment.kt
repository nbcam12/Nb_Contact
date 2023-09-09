import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
import com.example.contact_nb12.list.ContactAddDialog
import com.example.contact_nb12.list.DataManager
import com.example.contact_nb12.list.ItemTouchHelperCallback
import com.example.contact_nb12.models.Contact

class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private var contactAddDialog: ContactAddDialog? = null
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(DataManager.setDummyData())
    }

    private val binding get() = _binding!!

    private val requestContactPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한을 허용한 경우 처리
                contactAdapter.changeItems(getDeviceContacts())
            } else {
                // 권한을 거절한 경우 처리
//                Toast.makeText(
//                    requireContext(),
//                    getString(R.string.permission_contact_denied_msg),
//                    Toast.LENGTH_LONG
//                ).show()
            }
            initView()
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

    private val registerDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        }

    companion object {
        const val EXTRA_CONTACT_MODEL = "extra_contact_model"
        const val EXTRA_POSITION = "extra_position"

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
    }

    private fun initView() = with(binding) {
        // 전화 걸기 전 권한 확인
        if (isCallPhonePermissionGranted()) {
        } else {
            // 권한이 없는 경우 권한 요청
            requestCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }

        contactRecycler.layoutManager = LinearLayoutManager(requireContext())
        // ItemTouchHelper 설정
        val callback = ItemTouchHelperCallback(contactAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(contactRecycler)

        contactAdapter.startDrag(object : ContactAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })

        // RecyclerView 아이템 클릭 이벤트 처리
        contactAdapter.itemClick = object : ContactAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val item = contactAdapter.getItem(position)
                registerDetailLauncher.launch(
                    DetailActivity.editIntent(
                        requireContext(),
                        item,
                        position
                    )
                )
            }
        }

        contactRecycler.adapter = contactAdapter
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
            initView()
        } else {
            requestContactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun getDeviceContacts(): MutableList<Contact> {
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
                    val image =
                        it.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                    val emailCursor = requireContext().contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf("$id"),
                        null
                    )

                    emailCursor?.let {
                        if (it != null && it.moveToFirst()) {
                            val emailIndex: Int =
                                emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                            val email: String = emailCursor.getString(emailIndex)
                            emailCursor.close()

                            val model = Contact(
                                Img = Uri.parse(image),
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

    fun addContactItem(item: Contact) {
        contactAdapter.addItem(item)
    }

    fun modifyContactItem(position: Int, item: Contact) {
        contactAdapter.modifyItem(position, item)
    }
}
