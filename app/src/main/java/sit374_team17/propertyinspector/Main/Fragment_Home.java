package sit374_team17.propertyinspector.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sit374_team17.propertyinspector.Property.Adapter_Properties;
import sit374_team17.propertyinspector.Property.Fragment_Property;
import sit374_team17.propertyinspector.Property.Photo;
import sit374_team17.propertyinspector.Property.Property;
import sit374_team17.propertyinspector.R;


public class Fragment_Home extends Fragment implements SearchView.OnQueryTextListener,Fragment_Property.DeletePropertyListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "home";

    public static  CognitoCachingCredentialsProvider credentialsProvider ;
    protected DynamoDBMapper mapper ;
    private String IDENTITY_POOL_ID="ap-southeast-2:da48cacc-60b6-41ee-8dc6-4ae3c3abf13a";
    protected  List<Property> result;
    protected  List<Photo> results;
    private String mParam1;
    private String mParam2;

    View mView;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    Adapter_Properties mPropertyAdapter;
    FloatingActionButton mFab;
   // DB_PropertyHandler mDB_properties;
    List<Property> mPropertiesList;

    private Listener mListener;
    private RatingBar mRatingBar;


    public Fragment_Home() {
    }




    public interface HomeListener {
        void onHomeInteraction();
    }


    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mDB_properties = new DB_PropertyHandler(getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        initViews();
        return mView;
    }

    private void initViews() {
        mySwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swiperefresh);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

//        mRatingBar = (RatingBar) mView.findViewById(R.id.ratingBar_property);
//
//        Drawable progress = mRatingBar.getProgressDrawable();
//        DrawableCompat.setTint(progress, Color.WHITE);

      //  Log.d(Activity_Login.LOG_TAG,getActivity().getIntent().getStringExtra("tokens"));
        // Create a credentials provider, or use the existing provider.
        credentialsProvider = new CognitoCachingCredentialsProvider(getActivity(), IDENTITY_POOL_ID, Regions.AP_SOUTHEAST_2);
        // Set up as a credentials provider.
        Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.ap-southeast-2.amazonaws.com/ap-southeast-2_e4nCxiblG", getActivity().getIntent().getStringExtra("tokens"));
        credentialsProvider.setLogins(logins);
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
        mapper = new DynamoDBMapper(ddbClient);
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                result = mapper.scan(Property.class, scanExpression);
                results = mapper.scan(Photo.class, scanExpression);
                if (result.size() >= 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPropertyAdapter = new Adapter_Properties(mListener, getContext(),credentialsProvider,results,Fragment_Home.this);
                            mPropertyAdapter.setPropertyList(result);
                            mRecyclerView.setAdapter(mPropertyAdapter);
                        }
                    });
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        /*
 * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
 * performs a swipe-to-refresh gesture.
 */
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        Runnable runnable = new Runnable() {
                            public void run() {
                                //DynamoDB calls go here
                                AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                                ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
                                mapper = new DynamoDBMapper(ddbClient);
                                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                                result = mapper.scan(Property.class, scanExpression);
                                results = mapper.scan(Photo.class, scanExpression);
                                if (result.size() >= 0) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPropertyAdapter = new Adapter_Properties(mListener, getContext(),credentialsProvider,results,Fragment_Home.this);
                                            mPropertyAdapter.setPropertyList(result);
                                            mRecyclerView.setAdapter(mPropertyAdapter);
                                            mySwipeRefreshLayout.setRefreshing(false);
                                            Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        };
                        Thread mythread = new Thread(runnable);
                        mythread.start();

                    }
                }
        );
    }

    @Override
    public void mDelete(final Property property) {
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
                mapper = new DynamoDBMapper(ddbClient);
                mapper.delete(property);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        MenuItem propertyItem = menu.findItem(R.id.action_property);
        propertyItem.setVisible(false);
        MenuItem criteriaItem = menu.findItem(R.id.action_criteria);
        criteriaItem.setVisible(false);
        MenuItem notesItem = menu.findItem(R.id.action_notes);
        notesItem.setVisible(false);

        //  MenuItem camera = menu.findItem(R.id.action_camera);
        //  camera.setVisible(false);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toLowerCase();

        final List<Property> filteredModelList = new ArrayList<>();
        for (Property property : result) {
            final String text = property.getDescription() + property.getAddress() + property.getCity() + property.getState() +  property.getPostCode();
            if (text.toLowerCase().contains(query)) {
                filteredModelList.add(property);
            }
        }
      // mPropertyAdapter.animateTo(filteredModelList);
         mPropertyAdapter.setPropertyList(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
       initViews();
        super.onViewStateRestored(savedInstanceState);
    }
}

