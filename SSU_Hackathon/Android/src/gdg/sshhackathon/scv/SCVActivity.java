package gdg.sshhackathon.scv;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;
import android.widget.AdapterView.OnItemClickListener;

public class SCVActivity extends Activity {

	private Context mContext = null;

	private TextView empty_text = null;
	private ViewAnimator setting = null;
	private ViewAnimator list = null;
	private View alpha_view = null;
	private ViewGroup mViewGroup = null;
	private EditText date_edit = null;
	private EditText item_query = null;
	private ImageView search = null;
	private ListView listview = null;
	private ListView selected_view = null;
	private SCVAdapter adapter = null;
	private SelectedAdapter selected_adapter = null;
	private Button confirm_btn = null;
	private RelativeLayout detail_layout = null;
	private TextView detail_text = null;
	private TextView detail_count = null;
	private ImageView detail_image = null;
	private Button close = null;

	private int itemCount = 0;
	private boolean isThereItems = false;

	private ArrayList<RelativeLayout> items = new ArrayList<RelativeLayout>();
	private ArrayList<Item> selected = new ArrayList<Item>();
	private TranslateAnimation anim = null;

	private static final String BASE_URL = "http://openapi.naver.com/search?";
	private static final String SEARCH_KEY = "d18ee94ffe271b30e775d7cf6cedba8d";

	private static final class Param {
		public static final String KEY = "key=";
		public static final String QUERY = "&query=";
		public static final String TARGET = "&target=shop";
		public static final String START = "&start=1";
		public static final String DISPLAY = "&display=10";
	};

	private ArrayList<Item> mItems = new ArrayList<Item>();
	private InputMethodManager imm;

	private String date = null;
	private boolean isDate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scv);

		createUI();
	}

	private void createUI() {
		mContext = this;
		setting = (ViewAnimator) findViewById(R.id.setting_animator);
		list = (ViewAnimator) findViewById(R.id.list_animator);
		empty_text = (TextView) findViewById(R.id.empty);
		alpha_view = (View) findViewById(R.id.alpha);
		date_edit = (EditText) findViewById(R.id.date_edit);
		item_query = (EditText) findViewById(R.id.item_query);
		search = (ImageView) findViewById(R.id.search_go);
		listview = (ListView) findViewById(R.id.listview);
		selected_view = (ListView) findViewById(R.id.selected_list);
		confirm_btn = (Button) findViewById(R.id.confirm);

		detail_layout = (RelativeLayout) findViewById(R.id.detail_layout);
		detail_text = (TextView) findViewById(R.id.detail_title);
		detail_count = (TextView) findViewById(R.id.detail_count);
		detail_image = (ImageView) findViewById(R.id.detail_image);
		close = (Button) findViewById(R.id.close);

		item_query.setOnClickListener(click);
		search.setOnClickListener(click);
		alpha_view.setOnClickListener(click);
		confirm_btn.setOnClickListener(click);
		close.setOnClickListener(click);
		list.setVisibility(View.INVISIBLE);
		setting.setVisibility(View.INVISIBLE);
		alpha_view.setVisibility(View.INVISIBLE);
		detail_layout.setVisibility(View.INVISIBLE);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				hideResult();
				addItem(mItems.get(arg2));
				empty_text.setVisibility(View.GONE);
				serverCom com= new serverCom(mContext);
				com.init(1, 1, arg2);
				com.execute(null, null);
//				sendServer(1, stringToJson(1, arg2));
			}
		});

		selected_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LoadImage async = new LoadImage(mContext);
				async.init(selected.get(arg2).image_src, arg2);
				async.execute(null, null);
			}
		});
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void showDetail(int pos, Bitmap btm) {
		detail_layout.setVisibility(View.VISIBLE);
		detail_text.setText(selected.get(pos).title);
		detail_image.setImageBitmap(btm);
	}

	public class LoadImage extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = null;
		Context mContext;
		String str;
		Bitmap bit;
		int pos;

		LoadImage(Context context) {
			mContext = context;
		}

		public void init(String str, int pos) {
			this.str = str;
			this.pos = pos;
		}

		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setMessage(getResources().getString(R.string.searching));
			dialog.setCancelable(false);
			dialog.show();
		}

		protected Void doInBackground(Void... param) {
			try {
				bit = BitmapFactory.decodeStream((new URL(str))
						.openConnection().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			showDetail(pos, bit);
		}
	}

	private void hideKeyboard() {

		// imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), 0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_item:
			if (setting.getVisibility() == View.VISIBLE) {
				hideSetting();
			} else {

				if (list.getVisibility() == View.VISIBLE) {
					hideResult();

					setting.bringToFront();

					anim = new TranslateAnimation(0, 0, -setting.getHeight(), 0);
					anim.setDuration(200);
					anim.setAnimationListener(new Animation.AnimationListener() {
						public void onAnimationStart(Animation animation) {
							empty_text.setVisibility(View.GONE);
							setting.setVisibility(View.VISIBLE);
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							alpha_view.setVisibility(View.VISIBLE);
						}
					});
					setting.startAnimation(anim);
				} else if (isThereItems) {
					list.bringToFront();

					anim = new TranslateAnimation(0, 0, -list.getHeight(), 0);
					anim.setDuration(200);
					anim.setAnimationListener(new Animation.AnimationListener() {
						public void onAnimationStart(Animation animation) {
							list.setVisibility(View.VISIBLE);
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
						}
					});
					list.startAnimation(anim);
				} else {
					setting.bringToFront();
					alpha_view.bringToFront();

					anim = new TranslateAnimation(0, 0, -setting.getHeight(), 0);
					anim.setDuration(200);
					anim.setAnimationListener(new Animation.AnimationListener() {
						public void onAnimationStart(Animation animation) {
							empty_text.setVisibility(View.GONE);
							setting.setVisibility(View.VISIBLE);
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							alpha_view.setVisibility(View.VISIBLE);
						}
					});
					setting.startAnimation(anim);
				}
			}
			//
			// if(itemCount < 5)
			// addItem();
			// else
			// Toast.makeText(this,
			// getResources().getString(R.string.too_many_items),
			// Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void hideSetting() {
		anim = new TranslateAnimation(0, 0, 0, -setting.getHeight());
		anim.setDuration(200);
		anim.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
				alpha_view.setVisibility(View.INVISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				setting.setVisibility(View.INVISIBLE);
			}
		});
		setting.startAnimation(anim);

		if (itemCount <= 0)
			empty_text.setVisibility(View.VISIBLE);
	}

	private void addItem(Item data) {
		// System.out.println(data.title+" "+data.link);

		selected.add(data);
		if (selected_adapter == null) {
			selected_adapter = new SelectedAdapter(this, R.layout.list_item,
					selected);
			selected_view.setAdapter(selected_adapter);
		} else {
			selected_adapter.notifyDataSetChanged();
		}
	}

	View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alpha:
				hideSetting();
				break;
			case R.id.item_query:
				break;
			case R.id.search_go:
				if (!isDate && date_edit.getText().toString().matches("")) {
					Toast.makeText(mContext,
							getResources().getString(R.string.no_date),
							Toast.LENGTH_SHORT).show();
				} else {
					if (!isDate)
						date = date_edit.getText().toString();
					String str = item_query.getText().toString();
					// item_query.setText("");
					str = str.replaceAll(" ", "");
					if (!str.equalsIgnoreCase("")) {
						// search query
						try {
							URL url = new URL(BASE_URL + Param.KEY + SEARCH_KEY
									+ Param.QUERY
									+ URLEncoder.encode(str, "UTF-8")
									+ Param.DISPLAY + Param.START
									+ Param.TARGET);
							search(url);
						} catch (MalformedURLException e) {
						} catch (UnsupportedEncodingException e) {
						}

						// go search

					} else {
						Toast.makeText(mContext,
								getResources().getString(R.string.empty_query),
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case R.id.confirm:
				isThereItems = false;
				hideResult();
				break;
			case R.id.close:
				// if(detail_layout.getVisibility() == View.VISIBLE)
				detail_layout.setVisibility(View.INVISIBLE);
				break;
			}
		}
	};

	private void search(URL url) {
		Item data = null;

		Search_thread searching = new Search_thread();
		searching.init(mContext, url);
		searching.execute(null, null);
	}

	private class Search_thread extends AsyncTask<Void, Void, Void> {
		Context mContext = null;
		ProgressDialog dialog = null;
		Item data = null;
		URL url;

		public void init(Context context, URL url) {
			mContext = context;
			data = new Item();
			this.url = url;
			mItems.clear();
		}

		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setMessage(getResources().getString(R.string.searching));
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				final XmlPullParserFactory parserCreator = XmlPullParserFactory
						.newInstance();
				final XmlPullParser parser = parserCreator.newPullParser();
				parser.setInput(url.openStream(), null);
				int parseEvent = parser.getEventType();
				data = new Item();
				while (parseEvent != XmlPullParser.END_DOCUMENT) {
					switch (parseEvent) {
					case XmlPullParser.START_TAG:
						String tag = parser.getName();
						if (tag.compareTo("title") == 0) {
							String titleSrc = parser.nextText();
							data.title = titleSrc;
						}
						if (tag.compareTo("image") == 0) {
							String imageSrc = parser.nextText();
							data.image_src = imageSrc;
						}
						if (tag.compareTo("lprice") == 0) {
							String lpriceSrc = parser.nextText();
							data.lprice = lpriceSrc;
						}
						if (tag.compareTo("hprice") == 0) {
							String hpriceSrc = parser.nextText();
							data.hprice = hpriceSrc;
						}
						if (tag.compareTo("mallName") == 0) {
							String mallnameSrc = parser.nextText();
						}
						if (tag.compareTo("link") == 0) {
							String linkSrc = parser.nextText();
							data.link = linkSrc;
							mItems.add(data);
						}
						break;
					}
					parseEvent = parser.next();
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();

			hideSetting();
			isThereItems = true;

			list.bringToFront();

			anim = new TranslateAnimation(0, 0, -list.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					list.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					showResult();
				}
			});
			list.startAnimation(anim);
			
			serverCom com= new serverCom(mContext);
			com.init(2, 2, 0);
			com.execute(null, null);
//			sendServer(2, stringToJson(2, 0));
		}
	}
	
	public class serverCom extends AsyncTask<Void, Void, Void>{
		ProgressDialog dialog= null;
		Context mContext= null;
		int type1, type2, pos;
		serverCom(Context context){
			mContext= context;
		}
		public void init(int type1, int type2, int pos){
			this.type1= type1;
			this.type2= type2;
			this.pos= pos;
		}
		protected void onPreExecute(){
			dialog= new ProgressDialog(mContext);
			dialog.setCancelable(false);
			dialog.show();
		}
		protected Void doInBackground(Void... params){
			sendServer(type1, stringToJson(type2, pos));
			return null;
		}
		protected void onPostExecute(Void param){
			super.onPostExecute(param);
			dialog.dismiss();
			Log.d("SCV", "http complete");
		}
	}

	private JSONObject stringToJson(int type, int pos) {
		String json= null;
		if(type == 1)
			json= "{'num':"+Integer.toString(pos)+",'imgURL':"+selected.get(pos).image_src+",'title':"+selected.get(pos).title+",'price':"+selected.get(pos).lprice+"}";
		else
			json = "{'date':"+date+"}";
		JSONObject obj = null;
		try {
			obj = new JSONObject(json);
			Log.d("My App", obj.toString());

		} catch (Throwable t) {
			Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
		}
		return obj;
	}

	private void sendServer(int type, JSONObject obj) {
		try {
			String url;
			if(type == 1)
				// add
				url = "http://total-vold-681.appspot.com/add";
			else
				// senddate
				url = "http://total-vold-681.appspot.com/sendDate";
			int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient client = new DefaultHttpClient(httpParams);

			HttpPost request = new HttpPost(url);
			request.setEntity(new ByteArrayEntity(obj.toString().getBytes("UTF8")));
			HttpResponse response = client.execute(request);
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void hideResult() {
		anim = new TranslateAnimation(0, 0, 0, -list.getHeight());
		anim.setDuration(200);
		anim.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				list.setVisibility(View.INVISIBLE);
			}
		});
		list.startAnimation(anim);
	}

	private void showResult() {
		adapter = new SCVAdapter(this, R.layout.search_item_result, mItems);
		listview.setAdapter(adapter);
	}

	// int parserEvent= parser.getEventType();
	// boolean inItem= false;
	// while(parserEvent != XmlPullParser.END_DOCUMENT){
	// switch(parserEvent){
	// case XmlPullParser.START_TAG:
	// String tag= parser.getName();
	// data= new Item();
	// if(tag.compareTo("item") == 0)
	// inItem= true;
	// if(inItem && tag.compareTo("title") == 0)
	// data.title= parser.nextText();
	// if(inItem && tag.compareTo("link") == 0)
	// data.link= parser.nextText();
	// if(inItem && tag.compareTo("image") == 0)
	// data.image_src= parser.nextText();
	// if(inItem && tag.compareTo("lprice") == 0)
	// data.lprice= parser.nextText();
	// if(inItem && tag.compareTo("hprice") == 0){
	// data.hprice= parser.nextText();
	// mItems.add(data);
	// // inItem= false;
	// }
	// break;
	// case XmlPullParser.END_TAG:
	// inItem= false;
	// break;
	// }
	//
	// //
	// System.out.println("title: "+data.title+"\nlink: "+data.link+"\nimage: "+data.image_src+"\nlprice: "+data.lprice+"\nhprice: "+data.hprice);
	//
	// parserEvent= parser.next();

	/**
	 * A static list of country names.
	 */
	private static final String[] COUNTRIES = new String[] { "Belgium",
			"France", "Italy", "Germany", "Spain", "Austria", "Russia",
			"Poland", "Croatia", "Greece", "Ukraine", };

	public void onNothing(View v) {
		switch (v.getId()) {
		case R.id.setting_animator:
			System.out.println("clicked");
			Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
