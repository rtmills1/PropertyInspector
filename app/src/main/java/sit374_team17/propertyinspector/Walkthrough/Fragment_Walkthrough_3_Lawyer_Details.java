package sit374_team17.propertyinspector.Walkthrough;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import sit374_team17.propertyinspector.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment_Walkthrough_3_Lawyer_Details extends Fragment {

    private View mView;
    Listener_Walkthrough mListener;
    private Lawyer mLawyer;

    public Fragment_Walkthrough_3_Lawyer_Details() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener_Walkthrough) {
            mListener = (Listener_Walkthrough) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static Fragment_Walkthrough_3_Lawyer_Details newInstance(Lawyer lawyer) {
        Fragment_Walkthrough_3_Lawyer_Details fragment = new Fragment_Walkthrough_3_Lawyer_Details();
        Bundle args = new Bundle();
        args.putParcelable("lawyer", lawyer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLawyer = getArguments().getParcelable("lawyer");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_walkthrough_3_lawyer_details, container, false);

        TextView name = (TextView) mView.findViewById(R.id.textView_name);
        TextView pitch = (TextView) mView.findViewById(R.id.textView_pitch);
        TextView info = (TextView) mView.findViewById(R.id.textView_info);

        name.setText(mLawyer.getName());
        pitch.setText(Html.fromHtml(mLawyer.getPitch()));
        info.setText(Html.fromHtml(mLawyer.getInfo()));

        Button button_select = (Button) mView.findViewById(R.id.button_select);
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goTo_4_electrical();
            }
        });

        return  mView;


    }
}
