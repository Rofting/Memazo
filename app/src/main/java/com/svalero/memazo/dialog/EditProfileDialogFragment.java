package com.svalero.memazo.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.svalero.memazo.R;
import com.svalero.memazo.domain.User;
import java.util.Calendar;

public class EditProfileDialogFragment extends DialogFragment {

    private User user;

    public interface EditProfileListener {
        void onProfileEdited(User updatedUser);
    }

    private EditProfileListener listener;

    public static EditProfileDialogFragment newInstance(User user) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditProfileListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditProfileListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_profile, null); // Asegúrate que el layout se llama así

        // Referencias a todas las vistas del formulario
        EditText etName = view.findViewById(R.id.et_edit_name);
        EditText etEmail = view.findViewById(R.id.et_edit_email);
        EditText etPassword = view.findViewById(R.id.et_edit_password);
        EditText etBirthDate = view.findViewById(R.id.et_edit_birth_date);
        EditText etPhone = view.findViewById(R.id.et_edit_phone);
        EditText etAvatar = view.findViewById(R.id.et_edit_avatar);
        RadioGroup rgSex = view.findViewById(R.id.rg_edit_sex);
        RadioButton rbMale = view.findViewById(R.id.rb_edit_male);
        RadioButton rbFemale = view.findViewById(R.id.rb_edit_female);

        // Rellenamos el formulario con los datos actuales del usuario
        if (user != null) {
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etBirthDate.setText(user.getBirthDate());
            etPhone.setText(user.getPhone());
            etAvatar.setText(user.getAvatar());
            if (user.getSex() != null && user.getSex().equalsIgnoreCase("Male")) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
        }

        // Configurar el DatePickerDialog para la fecha de nacimiento
        setupDatePicker(etBirthDate);

        builder.setView(view)
                .setPositiveButton(R.string.save_button, (dialog, which) -> {
                    // Recogemos todos los nuevos datos del formulario
                    String newName = etName.getText().toString();
                    String newEmail = etEmail.getText().toString();
                    String newPassword = etPassword.getText().toString();
                    String newBirthDate = etBirthDate.getText().toString();
                    String newPhone = etPhone.getText().toString();
                    String newAvatar = etAvatar.getText().toString();
                    String newSex = rbMale.isChecked() ? "Male" : "Female";

                    // Actualizamos el objeto User original
                    if (user != null) {
                        user.setName(newName);
                        user.setEmail(newEmail);
                        // Solo actualizamos la contraseña si el usuario ha escrito una nueva
                        if (!newPassword.isEmpty()) {
                            user.setPassword(newPassword);
                        }
                        user.setBirthDate(newBirthDate);
                        user.setPhone(newPhone);
                        user.setAvatar(newAvatar);
                        user.setSex(newSex);

                        // Devolvemos el objeto User completo y actualizado a la Activity
                        listener.onProfileEdited(user);
                    }
                })
                .setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.cancel());

        return builder.create();
    }

    private void setupDatePicker(EditText etBirthDate) {
        etBirthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etBirthDate.setText(selectedDate);
                    }, year, month, day);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }
}