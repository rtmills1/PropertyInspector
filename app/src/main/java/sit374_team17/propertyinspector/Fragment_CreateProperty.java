package sit374_team17.propertyinspector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class Fragment_CreateProperty extends Fragment {

    private static final String ARG_PROPERTY = "property";

    private Property mProperty;

    View mView;
    Button mButton_save;
    DB_PropertyHandler mDB_properties;
    List<Property> mPropertyList;
    EditText mEditText_address, mEditText_bedrooms, mEditText_bathrooms, mEditText_garages, mEditText_price;

    private CreatePropertyListener mListener;

    public Fragment_CreateProperty() {
    }

    public interface CreatePropertyListener {
        void onSaveProperty();
    }

    public static Fragment_CreateProperty newInstance(Property property) {
        Fragment_CreateProperty fragment = new Fragment_CreateProperty();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROPERTY, property);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB_properties = new DB_PropertyHandler(getContext());
        if (getArguments() != null) {
            mProperty = getArguments().getParcelable(ARG_PROPERTY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_create_property, container, false);

        mEditText_address = (EditText) mView.findViewById(R.id.editText_address);
        mEditText_bedrooms = (EditText) mView.findViewById(R.id.editText_bedrooms);
        mEditText_bathrooms = (EditText) mView.findViewById(R.id.editText_bathrooms);
        mEditText_garages = (EditText) mView.findViewById(R.id.editText_garages);
        mEditText_price = (EditText) mView.findViewById(R.id.editText_price);

        mButton_save = (Button) mView.findViewById(R.id.button_save);


        mButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    saveProperty();
                }
            }
        });
        return mView;
    }

    private void saveProperty() {
        String address = mEditText_address.getText().toString();
        String bedrooms = mEditText_bedrooms.getText().toString();
        String bathrooms = mEditText_bathrooms.getText().toString();
        String garages = mEditText_garages.getText().toString();
        String price = mEditText_price.getText().toString();

        String empty = "--";

        if (mProperty != null) {
            if (address != "" && !address.isEmpty()) {
                if (mProperty.get_id() < 0) {
                    mProperty.set_address(address);

                    if (bedrooms.isEmpty()) bedrooms = empty;
                    if (bathrooms.isEmpty()) bathrooms = empty;
                    if (garages.isEmpty()) garages = empty;
                    if (price.isEmpty()) price = empty;

                    mProperty.set_bedrooms(bedrooms);
                    mProperty.set_bathrooms(bathrooms);
                    mProperty.set_garages(garages);
                    mProperty.set_price(price);
                    mDB_properties.addProperty(mProperty);
                } else {
                    mProperty.set_address(address);
                    mDB_properties.updateProperty(mProperty);
                }
                mListener.onSaveProperty();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreatePropertyListener) {
            mListener = (CreatePropertyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreatePropertyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
