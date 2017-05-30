package sit374_team17.propertyinspector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by callu on 2/05/2017.
 */

public class Fragment_PrivateComments extends Fragment {
    private static final String ARG_PROPERTY = "comment";

    protected List<DB_Comments> result;
    protected DynamoDBMapper mapper ;
    private Property mComment;

    private Listener mListener;
    private DB_CommentHandler mDB_comments;
    private View mView;
    RecyclerView mRecyclerView;
    private Adapter_Comments mCommentsAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
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

    public static Fragment_Property newInstance(Property property) {
        Fragment_Property fragment = new Fragment_Property();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROPERTY, property);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB_comments = new DB_CommentHandler(getContext());
        if (getArguments() != null) {
            mComment = getArguments().getParcelable(ARG_PROPERTY);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_private_comments, container, false);

        initViews();
        return mView;
    }


    private void initViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_commentPrivate);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager;



        layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mRecyclerView.setLayoutManager(layoutManager);

        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(Fragment_Home.credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
        mapper = new DynamoDBMapper(ddbClient);
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.addFilterCondition("PropertyID",new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue().withS(Fragment_Property.PROPERTY_ID)));
                scanExpression.addFilterCondition("CommentType",
                        new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ)
                                .withAttributeValueList(new AttributeValue().withS("private")));
                result = mapper.scan(DB_Comments.class, scanExpression);
                if (result.size() >= 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCommentsAdapter = new Adapter_Comments(mListener);
                            mCommentsAdapter.setCommentList(result);
                            mRecyclerView.setAdapter(mCommentsAdapter);
                        }
                    });
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

}
