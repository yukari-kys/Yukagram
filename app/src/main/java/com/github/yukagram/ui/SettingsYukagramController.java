package com.github.yukagram.ui;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.github.yukagram.YuSettings;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.tool.UI;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.List;

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
    this.adapter = new SettingsAdapter(this) {
      @Override
      protected void setValuedSetting(ListItem item, SettingView view, boolean isUpdate) {
        int itemId = item.getId();

        if (itemId == R.id.btn_hideConnectionStatusBar) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isConnectionStatusBarHidden(), isUpdate);
        }
      }
    };
    List<ListItem> items = adapter.getItems();

    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideConnectionStatusBar, 0, R.string.yu_HideConnectionStatusBar));
//    items.add(new ListItem(ListItem.TYPE_INFO, 0, 0, R.string.yu_NothingHereYet));

    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onClick(View v) {
    int viewId = v.getId();

    boolean needToRestart = false;

    if (viewId == R.id.btn_hideConnectionStatusBar) {
      boolean value = adapter.toggleView(v);
      YuSettings.getInstance().setConnectionStatusBarHidden(value);
      needToRestart = true;
    }

    if (needToRestart) {
      UI.showToast(R.string.yu_RestartIsNeeded, Toast.LENGTH_SHORT);
    }
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
