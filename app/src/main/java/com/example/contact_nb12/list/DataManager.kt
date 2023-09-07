package com.example.contact_nb12.list

import com.example.contact_nb12.R
import com.example.contact_nb12.models.Contact

object DataManager {
    private val contacts = mutableListOf<Contact>()

    init {
        // 초기 더미 데이터 추가
        contacts.add(Contact(R.drawable.sample1,"John Smith", "010-1234-5678", "John@example.com", "1990-01-01", "J",false))
        contacts.add(Contact(R.drawable.sample2,"Emma Johnson", "010-9876-5432", "Emma@example.com", "1995-03-15", "E", false))
        contacts.add(Contact(R.drawable.sample3,"Michael Brown", "010-9876-5432", "Michael@example.com", "1997-02-17", "M", false))
        contacts.add(Contact(R.drawable.sample4,"Olivia Davis", "010-9876-5432", "Olivia@example.com", "1998-03-25", "O", false))
        contacts.add(Contact(R.drawable.sample5,"William Wilson", "010-9876-5432", "William@example.com", "2000-12-12", "W", false))
        contacts.add(Contact(R.drawable.sample6,"Sophia Martinez", "010-9876-5432", "Sophia@example.com", "2001-09-24", "S", false))
        contacts.add(Contact(R.drawable.sample7,"Ava Anderson", "010-9876-5432", "Ava@example.com", "1987-01-12", "A", false))
        contacts.add(Contact(R.drawable.sample8,"Benjamin Thomas", "010-9876-5432", "Benjamin@example.com", "2010-04-18", "B", false))
        contacts.add(Contact(R.drawable.sample9,"Daniel Lee", "010-9876-5432", "Daniel@example.com", "2004-03-25", "D", false))
        contacts.add(Contact(R.drawable.sample10,"Isabella Hernandez", "010-9876-5432", "Isabella@example.com", "1995-05-05", "I", false))
        contacts.add(Contact(R.drawable.sample11,"Charles Carter", "010-9876-5432", "Charles@example.com", "1995-06-11", "C", false))
        contacts.add(Contact(R.drawable.sample12," Frank Foster", "010-9876-5432", "Frank@example.com", "1995-11-19", "F", false))
        contacts.add(Contact(R.drawable.sample13,"Patrick Peterson", "010-9876-5432", "Patrick@example.com", "1995-12-29", "P", false))
        contacts.add(Contact(R.drawable.sample14,"Laura Lopez", "010-9876-5432", "Laura@example.com", "1995-06-14", "L", false))
        contacts.add(Contact(R.drawable.sample15,"Henry Harrison", "010-9876-5432", "Henry@example.com", "1995-07-14", "H", false))
    }

    fun getContacts(): List<Contact> {
        return contacts
    }

    fun addContact(contact: Contact) {
        contacts.add(contact)
    }

}
