package com.creatrix.salessolution.Activity.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.creatrix.salessolution.Activity.PersonInfoDAO;
import com.creatrix.salessolution.R;

import java.util.List;

    public class PersonChoiceAdapter extends ArrayAdapter<PersonInfoDAO> {
        private int selectedPosition = -1;

        public PersonChoiceAdapter(Context context, List<PersonInfoDAO> data) {
            super(context, 0, data);
        }
        public PersonInfoDAO getSelectedPerson() {
            return (selectedPosition >= 0) ? getItem(selectedPosition) : null;
        }
        public int getSelectedPosition() { return selectedPosition; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person_choice, parent, false);

            PersonInfoDAO p = getItem(position);
            TextView tv = convertView.findViewById(R.id.tvLine);
            RadioButton rb = convertView.findViewById(R.id.rb);

            tv.setText(p != null ? p.oneLine() : "");
            rb.setChecked(position == selectedPosition);

            convertView.setOnClickListener(v -> {
                selectedPosition = position;
                notifyDataSetChanged();
            });

            return convertView;
        }
    }
