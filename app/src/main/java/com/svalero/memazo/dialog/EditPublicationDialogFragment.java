package com.svalero.memazo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.svalero.memazo.R;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;

public class EditPublicationDialogFragment extends DialogFragment {

    private Publication publication;

    public interface EditPublicationListener {
        void onPublicationEdited(long publicationId, PublicationInDto publicationDto);
    }

    private EditPublicationListener listener;

    public static EditPublicationDialogFragment newInstance(Publication publication) {
        EditPublicationDialogFragment fragment = new EditPublicationDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("publication", publication);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditPublicationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditPublicationListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Recuperamos la publicación del Bundle
        if (getArguments() != null) {
            publication = (Publication) getArguments().getSerializable("publication");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_publication, null);

        // Referencias a las vistas del diálogo
        EditText etContent = view.findViewById(R.id.et_edit_content);
        EditText etImageUrl = view.findViewById(R.id.et_edit_image_url);
        Spinner spinnerPrivacy = view.findViewById(R.id.spinner_edit_privacy);

        // Rellenamos los Spinners
        ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.privacy_options, android.R.layout.simple_spinner_item);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrivacy.setAdapter(privacyAdapter);

        // Rellenamos el formulario con los datos actuales de la publicación
        etContent.setText(publication.getContent());
        etImageUrl.setText(publication.getImageUrl());
        // Seleccionamos la opción correcta en el Spinner
        if (publication.getPrivacy() != null) {
            int spinnerPosition = privacyAdapter.getPosition(publication.getPrivacy());
            spinnerPrivacy.setSelection(spinnerPosition);
        }

        builder.setView(view)
                .setTitle(R.string.edit_publication_title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> {
                    // Recogemos los nuevos datos del formulario
                    String newContent = etContent.getText().toString();
                    String newImageUrl = etImageUrl.getText().toString();
                    String newPrivacy = spinnerPrivacy.getSelectedItem().toString();

                    // Creamos el DTO con los datos actualizados y los originales que no cambian
                    PublicationInDto dto = new PublicationInDto(
                            1L, // TODO: Usar el ID del usuario real
                            newContent,
                            newImageUrl.isEmpty() ? null : newImageUrl,
                            publication.getTypeContent(), // Mantenemos el original
                            newPrivacy,
                            publication.getLatitude(),  // Mantenemos las originales
                            publication.getLongitude()
                    );

                    // Devolvemos el DTO a la Activity
                    listener.onPublicationEdited(publication.getId(), dto);
                })
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel());

        return builder.create();
    }
}