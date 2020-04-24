package br.com.lorenzowindmoller.pdm_atividadelive06;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogEmail extends AppCompatDialogFragment {
    private EditText address;
    private EditText subject;
    private EditText body;
    private DialogEmailListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_email, null);

        builder.setView(view)
                .setTitle(R.string.emailMessage)
                .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.sendButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyTextsDialogEmail(address.getText().toString(), subject.getText().toString(), body.getText().toString());
                    }
                });

        address = view.findViewById(R.id.address_email);
        subject = view.findViewById(R.id.subject_email);
        body = view.findViewById(R.id.body_email);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogEmailListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface DialogEmailListener {
        void applyTextsDialogEmail(String address, String subject, String body);
    }
}