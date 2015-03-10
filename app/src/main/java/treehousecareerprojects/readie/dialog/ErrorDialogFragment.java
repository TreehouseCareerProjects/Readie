package treehousecareerprojects.readie.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import treehousecareerprojects.readie.R;

/**
 * Created by Dan on 2/26/2015.
 */
public class ErrorDialogFragment extends DialogFragment {
    public static final String TITLE_ID = "title";          //Dialog title string resource.
    public static final String MESSAGE_ID = "message";      //Dialog message string resource.
    public static final String TERMINATE_ID = "terminate";  //Does this finish the current activity?

    public static void displayTerminatingErrorDialog(Activity context, int title, int message, String dialogId) {
        Bundle dialogArgs = new Bundle();
        dialogArgs.putInt(ErrorDialogFragment.TITLE_ID, title);
        dialogArgs.putInt(ErrorDialogFragment.MESSAGE_ID, message);
        dialogArgs.putBoolean(ErrorDialogFragment.TERMINATE_ID, true);

        ErrorDialogFragment errorDialog = new ErrorDialogFragment();
        errorDialog.setArguments(dialogArgs);
        errorDialog.show(context.getFragmentManager(), dialogId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle messages = getArguments();

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(messages != null) {
            boolean willTerminate = messages.getBoolean(ErrorDialogFragment.TERMINATE_ID, false);

            builder.setTitle(messages.getInt(TITLE_ID, R.string.generic_error_title));
            builder.setMessage(messages.getInt(MESSAGE_ID, R.string.generic_error_message));
            builder.setPositiveButton(
                    context.getString(R.string.generic_error_dismiss),
                    willTerminate ? new OnDismiss() : null );
        }
        else {
            builder.setTitle(R.string.generic_error_title);
            builder.setMessage(R.string.generic_error_message);
            builder.setPositiveButton(context.getString(R.string.generic_error_dismiss), null);
        }

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class OnDismiss implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            getActivity().finish();
        }
    }

}
