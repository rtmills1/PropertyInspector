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

public class Fragment_Walkthrough_7_Moval_Details extends Fragment {

    Listener_Walkthrough mListener;
    private Moval mMoval;

    public Fragment_Walkthrough_7_Moval_Details() {
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

    public static Fragment_Walkthrough_7_Moval_Details newInstance(Moval moval) {
        Fragment_Walkthrough_7_Moval_Details fragment = new Fragment_Walkthrough_7_Moval_Details();
        Bundle args = new Bundle();
        args.putParcelable("moval", moval);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMoval = getArguments().getParcelable("moval");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_walkthrough_7_moval_details, container, false);

        TextView name = (TextView) mView.findViewById(R.id.textView_name);
        TextView pitch = (TextView) mView.findViewById(R.id.textView_pitch);
        TextView info = (TextView) mView.findViewById(R.id.textView_info);

        name.setText(mMoval.getName());
        pitch.setText(Html.fromHtml(mMoval.getPitch()));
        info.setText(Html.fromHtml(mMoval.getInfo()));

        Button button_select = (Button) mView.findViewById(R.id.button_select);
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goTo_8_finish();
                //getActivity().finish();
            }
        });

        return mView;
    }
}
