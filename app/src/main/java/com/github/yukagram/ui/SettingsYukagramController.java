package com.github.yukagram.ui;

import android.content.Context;
import android.view.View;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.unsorted.Settings;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.List;

import me.vkryl.core.ArrayUtils;

public class SettingsYukagramController extends RecyclerViewController<Void> implements View.OnClickListener {
  public SettingsYukagramController(Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override
  public int getId () {
    return R.id.controller_yukagramSettings;
  }

  private SettingsAdapter adapter;

  @Override
  protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    this.adapter = new SettingsAdapter(this);
    List<ListItem> items = adapter.getItems();
    items.add(new ListItem(ListItem.TYPE_INFO, 0, 0, R.string.yu_NothingHereYet));
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onClick(View v) {
    int viewId = v.getId();
  }

  @Override
  public void destroy () {
    super.destroy();
  }

  @Override
  public CharSequence getName () {
    return Lang.getString(R.string.yu_YukagramSettings);
  }
}
