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

public class DialogMessage extends AppCompatDialogFragment {
    private EditText number;
    private EditText text;
    private DialogMessageListener listener;
    private Integer operation;

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_message, null);

        builder.setView(view)
                .setTitle(R.string.sendMessage)
                .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.sendButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyTextsDialogMessage(number.getText().toString(), text.getText().toString(), operation);
                    }
                });

        number = view.findViewById(R.id.number_to_send);
        text = view.findViewById(R.id.text_to_send);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface DialogMessageListener {
        void applyTextsDialogMessage(String number, String text, Integer operation);
    }
}